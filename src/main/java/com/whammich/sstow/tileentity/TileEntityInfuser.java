package com.whammich.sstow.tileentity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import com.whammich.sstow.utils.ModLogger;

public class TileEntityInfuser extends TileEntity implements IInventory {

	public ItemStack[] inv = new ItemStack[1];
	public Random rand;
	List<Item> infusable = Arrays.asList(new Item[] { Items.egg});//, SoulShards.itemCorruptedPickaxe });
	//List chargable = Arrays.asList(new Item[] { SoulShards.itemShadowPickaxe });
	int infusedSouls = 0;
	int tick = 1;
	TileEntitySoulCrystal t = null;

	@Override
	public int getSizeInventory() {
		return this.inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inv[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amount) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inv[slot] = stack;
		if ((stack != null) && (stack.stackSize > getInventoryStackLimit()))
			stack.stackSize = getInventoryStackLimit();
	}

	@Override
	public String getInventoryName() {
		return "invdiffuser";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return (player.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this) && (player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) < 64.0D);
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList items = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound item = items.getCompoundTagAt(i);
			ModLogger.logDebug("Pre-Slot: " + i + " :" + item.toString());
			int slot = item.getInteger("Slot");
			ModLogger.logDebug("Post-Slot: " + i + " :" + item.toString());			
			if (slot >= 0 && slot < this.inv.length) {
				ModLogger.logDebug("If-Slot: " + i + " :" + item.toString());
				this.inv[slot] = ItemStack.loadItemStackFromNBT(item);
				ModLogger.logDebug("Done-Slot: " + i + " :" + item.toString());
			}
		}
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		NBTTagList items = new NBTTagList();

		for (int i = 0; i < getSizeInventory(); ++i) {
			ModLogger.logDebug("Null-Slot: " + i + " :" + items.toString());
			if (getStackInSlot(i) != null) {
				ModLogger.logDebug("Tag-Slot: " + i + " :" + items.toString());
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				ModLogger.logDebug("Pre-Slot: " + i + " :" + items.toString());
				getStackInSlot(i).writeToNBT(item);
				items.appendTag(item);
				ModLogger.logDebug("Post-Slot: " + i + " :" + items.toString());
			}
		}
		ModLogger.logDebug("Saving Item: " + items.toString());
		tagCompound.setTag("Items", items);
	}

	public void updateEntity() {
		if ((this.t == null) || (this.t.isInvalid())) {
			int x = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord).xCoord;
			int y = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord).yCoord;
			int z = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord).zCoord;
			for (int l = x - 2; l <= x + 2; ++l) {
				for (int i1 = z - 2; i1 <= z + 2; ++i1) {
					if (l > x - 2 && l < x + 2 && i1 == z - 1) {
						i1 = z + 2;
					}

					if (rand.nextInt(16) == 0) {
						for (int j1 = y; j1 <= y + 1; ++j1) {
							if ((this.worldObj.getTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z) instanceof TileEntitySoulCrystal)) {
								System.out.println("Found");
								this.t = ((TileEntitySoulCrystal)this.worldObj.getTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z));
							}
						}
					}
				}
			}
			//			for (x = -2; x <= 2; x++) {
			//				for (z = -2; z <= 2; z++) {
			//					if ((this.worldObj.getTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z) instanceof TileEntitySoulCrystal)) {
			//						System.out.println("Found");
			//						this.t = ((TileEntitySoulCrystal)this.worldObj.getTileEntity(this.xCoord + x, this.yCoord, this.zCoord + z));
			//					}
			//				}
			//			}
		}

		if ((this.t == null) || (this.inv[0] == null) || (!this.infusable.contains(this.inv[0].getItem()))) {
			this.infusedSouls = 0;
			return;
		}
		if (++this.tick == 20) process(this.inv[0]); 
	}

	void process(ItemStack is) { 
		System.out.println("processing: " + this.infusedSouls);
		this.tick = 1;
		if (this.infusedSouls >= getCost(is)) this.inv[0] = getResult(is).copy();
		if ((this.t.charge <= 0) || (this.t.type.isEmpty())) {
			return;
		}
		this.t.charge -= 1;
		if (this.t.charge == 0){
			this.t.type = null;
		}
		this.infusedSouls += 1;
	}

	int getCost(ItemStack is) {
		if (is.getItem().equals(Items.egg)) {
			return 10;
		}
		//		if (is.getItem().equals(SoulShards.itemCorruptedPickaxe)) {
		//			return 10;
		//		}
		return 0;
	}

	ItemStack getResult(ItemStack is) {
		if (is.getItem().equals(Items.egg)) {
			return new ItemStack(Items.spawn_egg, 1, EntityList.getEntityID(EntityList.createEntityByName(this.t.type, this.worldObj)));
		}
		//		if (is.getItem().equals(SoulShards.itemCorruptedPickaxe)){ 
		//			return new ItemStack(SoulShards.itemShadowPickaxe, 1, 0);
		//		}
		return null;
	}

}
