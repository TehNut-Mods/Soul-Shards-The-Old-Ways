package com.whammich.sstow.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.whammich.sstow.block.BlockForge;
import com.whammich.sstow.utils.SFRecipeHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityForge extends TileEntity implements ISidedInventory {

	private static final int[] slotsTop = new int[] { 0 };
	private static final int[] slotsBottom = new int[] { 2, 1 };
	private static final int[] slotsSides = new int[] { 1 };

	@SuppressWarnings("unused")
	private static final int[] slotsBy = new int[] { 3 };

	private ItemStack[] furnaceItemStacks = new ItemStack[4];
	public int furnaceBurnTime;
	public int currentBurnTime;
	public int furnaceCookTime;

	private String furnaceName;

	public void furnaceName(String string) {
		this.furnaceName = string;
	}

	@Override
	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.furnaceItemStacks[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (this.furnaceItemStacks[var1] != null) {
			ItemStack itemstack;
			if (this.furnaceItemStacks[var1].stackSize <= var2) {
				itemstack = this.furnaceItemStacks[var1];
				this.furnaceItemStacks[var1] = null;
				return itemstack;
			} else {
				itemstack = this.furnaceItemStacks[var1].splitStack(var2);
				if (this.furnaceItemStacks[var1].stackSize == 0) {
					this.furnaceItemStacks[var1] = null;
				}
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.furnaceItemStacks != null) {
			ItemStack itemstack = this.furnaceItemStacks[slot];
			this.furnaceItemStacks[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.furnaceItemStacks[slot] = itemstack;
		if (itemstack != null
				&& itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.furnaceName
				: "container.soulforge";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.furnaceName != null && this.furnaceName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound tabCompound1 = tagList.getCompoundTagAt(i);
			byte byte0 = tabCompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[byte0] = ItemStack
						.loadItemStackFromNBT(tabCompound1);
			}
		}
		this.furnaceBurnTime = tagCompound.getShort("BurnTime");
		this.furnaceCookTime = tagCompound.getShort("CookTime");
		/* burn time here */
		this.currentBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
		if (tagCompound.hasKey("CustomName", 8)) {
			this.furnaceName = tagCompound.getString("CustomName");
		}
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setShort("BurnTime", (short) this.furnaceBurnTime);
		tagCompound.setShort("CookTime", (short) this.furnaceBurnTime);
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
			if (this.furnaceItemStacks[i] != null) {
				NBTTagCompound tagCompound1 = new NBTTagCompound();
				tagCompound1.setByte("Slot", (byte) i);
				this.furnaceItemStacks[i].writeToNBT(tagCompound1);
				tagList.appendTag(tagCompound1);
			}
		}
		tagCompound.setTag("Items", tagList);
		if (this.hasCustomInventoryName()) {
			tagCompound.setString("CustomName", this.furnaceName);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int val1) {
		return this.furnaceCookTime * val1 / this.smeltTime();
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int val1) {
		if (this.currentBurnTime == 0) {
			this.currentBurnTime = this.smeltTime();
		}
		return this.furnaceBurnTime * val1 / this.currentBurnTime;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	public void updateEntity() {
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;
		if (this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}
		if (!this.worldObj.isRemote) {
			if (this.furnaceBurnTime == 0 && this.canSmelt()) {
				this.currentBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
				if (this.furnaceBurnTime > 0) {
					flag1 = true;
					if (this.furnaceItemStacks[1] != null) {
						--this.furnaceItemStacks[1].stackSize;
						if (this.furnaceItemStacks[1].stackSize == 0) {
							this.furnaceItemStacks[1] = furnaceItemStacks[1]
									.getItem().getContainerItem(
											this.furnaceItemStacks[1]);
						}
					}
				}
			}
			if (this.isBurning() && this.canSmelt()) {
				++this.furnaceCookTime;
				if (this.furnaceCookTime == this.smeltTime()) {
					this.furnaceCookTime = 0;
					this.smeltItem();
					flag1 = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}
		}
		if (flag != this.furnaceBurnTime > 0) {
			flag1 = true;
			BlockForge.updateBlockstate(this.furnaceBurnTime > 0,
					this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		if (flag1) {
			this.markDirty();
		}
	}

	private boolean canSmelt() {
		if (this.furnaceItemStacks[0] == null) {
			return false;
		} else {
			ItemStack itemstack = SFRecipeHandler.smelting().getSmeltingResult(
					this.furnaceItemStacks[0]);
			if (itemstack == null)
				return false;
			if (this.furnaceItemStacks[2] == null)
				return true;
			if (!this.furnaceItemStacks[2].isItemEqual(itemstack))
				return false;
			int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit()
					&& result <= this.furnaceItemStacks[2].getMaxStackSize();
		}
	}

	public int smeltTime() {
		if (this.furnaceItemStacks[0] == null) {
			return 200;
		} else {
			int smeltTime = (int) (SFRecipeHandler.smelting().getSmeltingTime(
					this.furnaceItemStacks[0]) * 0.1);// * Config.COOK_TIME);
			if (smeltTime == 0) {
				return 200;
			}
			return smeltTime;
		}
	}

	public void smeltItem() {
		
		if (this.canSmelt()) {
			
			ItemStack itemstack = SFRecipeHandler.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
			ItemStack itemstack1 = SFRecipeHandler.smelting().getSecondaryResult(this.furnaceItemStacks[0]);
			
			if (this.furnaceItemStacks[2] == null) {

				this.furnaceItemStacks[2] = itemstack.copy();
				
			} else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
				
				this.furnaceItemStacks[2].stackSize += itemstack.stackSize;
			}
			
			if (itemstack1 != null) {
				
				if (this.furnaceItemStacks[3] == null) {
					
					this.furnaceItemStacks[3] = itemstack1.copy();
					
				} else if (this.furnaceItemStacks[3].getItem() == itemstack1.getItem()) {
					
					this.furnaceItemStacks[3].stackSize += itemstack1.stackSize;
					
				}
				
			}
			
			--this.furnaceItemStacks[0].stackSize;
			
			if (this.furnaceItemStacks[0].stackSize <= 0) {
				
				this.furnaceItemStacks[0] = null;
				
			}
		}
	}

	@SuppressWarnings("unused")
	public int getItemBurnTime(ItemStack itemstack) {
		if (itemstack == null) {
			return 0;
		} else {
			Item item = itemstack.getItem();
			int fuelCost = (int) SFRecipeHandler.smelting().getFuelCost(
					this.furnaceItemStacks[0]);
			int fuelMod = (int) SFRecipeHandler.smelting().getFuelMod(
					this.furnaceItemStacks[1]);
			int BurnTime = (int) this.smeltTime() / fuelCost * fuelMod;
			return BurnTime;
		}
	}

	public static boolean isItemFuel(ItemStack itemstack) {
		return SFRecipeHandler.smelting().getFuelMod(itemstack) > 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : player.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack itemstack) {
		return var1 == 2 ? false : (var1 == 1 ? isItemFuel(itemstack) : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return var1 == 0 ? slotsBottom : (var1 == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack itemstack, int var3) {
		return this.isItemValidForSlot(var1, itemstack);
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack itemstack, int var3) {
		return var3 != 0 || var1 != 1 || itemstack.getItem() == Items.bucket;
	}

}