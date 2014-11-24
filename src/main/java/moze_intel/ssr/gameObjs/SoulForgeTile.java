package moze_intel.ssr.gameObjs;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFurnace;

public class SoulForgeTile extends TileEntityFurnace {
	private ItemStack[] furnaceItemStacks = new ItemStack[3];
	private String customName;

	@Override
	public void updateEntity() {
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;
		if (this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}

		if (!this.worldObj.isRemote) {

			if (this.furnaceBurnTime != 0 || this.furnaceItemStacks[1] != null
					&& this.furnaceItemStacks[0] != null) {
				if (this.furnaceBurnTime == 0 && canSmelt()) {
					this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

					if (this.furnaceBurnTime > 0) {
						flag1 = true;

						if (this.furnaceItemStacks[1] != null) {
							--this.furnaceItemStacks[1].stackSize;

							if (this.furnaceItemStacks[1].stackSize == 0) {
								this.furnaceItemStacks[1] = furnaceItemStacks[1]
										.getItem().getContainerItem(
												furnaceItemStacks[1]);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.furnaceCookTime;

					if (this.furnaceCookTime == 200) {
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
				int meta = 0;
				if (this.furnaceBurnTime > 0) {
					meta = 1;
				}
				this.worldObj.setBlockMetadataWithNotify(this.xCoord,
						this.yCoord, this.zCoord, meta, 2);
			}
		}

		if (flag1) {
			this.markDirty();
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.furnaceItemStacks[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.furnaceItemStacks[slot] != null) {
			ItemStack itemstack;

			if (this.furnaceItemStacks[slot].stackSize <= amount) {
				itemstack = this.furnaceItemStacks[slot];
				this.furnaceItemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = this.furnaceItemStacks[slot].splitStack(amount);

				if (this.furnaceItemStacks[slot].stackSize == 0) {
					this.furnaceItemStacks[slot] = null;
				}

				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.furnaceItemStacks[slot] != null) {
			ItemStack itemstack = this.furnaceItemStacks[slot];
			this.furnaceItemStacks[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		this.furnaceItemStacks[slot] = itemStack;

		if (itemStack != null
				&& itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack itemstack = getSmeltingResult(furnaceItemStacks[0]);

			if (this.furnaceItemStacks[2] == null) {
				this.furnaceItemStacks[2] = itemstack.copy();
			} else if (this.furnaceItemStacks[2].getItem() == itemstack
					.getItem()) {
				this.furnaceItemStacks[2].stackSize += itemstack.stackSize;
			}

			--this.furnaceItemStacks[0].stackSize;

			if (this.furnaceItemStacks[0].stackSize <= 0) {
				this.furnaceItemStacks[0] = null;
			}
		}
	}

	private boolean canSmelt() {
		if (this.furnaceItemStacks[0] == null) {
			return false;
		} else {
			ItemStack itemstack = getSmeltingResult(this.furnaceItemStacks[0]);
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

	private ItemStack getSmeltingResult(ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (item == Items.iron_ingot) {
			return new ItemStack(ObjHandler.SOULIUM_INGOT, 1);
		} else if (item == Items.diamond) {
			return new ItemStack(ObjHandler.SOUL_SHARD, 1);
		} else {
			return null;
		}
	}

	public static int getItemBurnTime(ItemStack itemStack) {
		if (itemStack == null) {
			return 0;
		} else {
			Item item = itemStack.getItem();

			if (item instanceof ItemBlock
					&& Block.getBlockFromItem(item) != Blocks.air) {
				Block block = Block.getBlockFromItem(item);

			}

			if (item == ObjHandler.CORRUPTED_ESSENCE) {
				return 1600;
			} else {
				return 0;
			}

		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "Soul Forge";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}

	@Override
	public void func_145951_a(String newName) {
		this.customName = newName;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag_) {
		super.readFromNBT(nbtTag_);
		NBTTagList nbttaglist = nbtTag_.getTagList("Items", 10);
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[b0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.furnaceBurnTime = nbtTag_.getShort("BurnTime");
		this.furnaceCookTime = nbtTag_.getShort("CookTime");
		this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

		if (nbtTag_.hasKey("Soul Forge", 8)) {
			this.customName = nbtTag_.getString("Soul Forge");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag_) {
		super.writeToNBT(nbtTag_);
		nbtTag_.setShort("BurnTime", (short) this.furnaceBurnTime);
		nbtTag_.setShort("CookTime", (short) this.furnaceCookTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
			if (this.furnaceItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbtTag_.setTag("Items", nbttaglist);

		if (this.hasCustomInventoryName()) {
			nbtTag_.setString("Soul Forge", this.customName);
		}
	}
}
