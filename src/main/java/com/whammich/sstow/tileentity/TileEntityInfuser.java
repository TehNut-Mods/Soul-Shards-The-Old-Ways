package com.whammich.sstow.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import com.whammich.sstow.utils.ModLogger;

public class TileEntityInfuser extends TileEntity implements IInventory {
	public ItemStack[] invSize;
	
	public TileEntityInfuser() {
		this.invSize = new ItemStack[36];
	}
	
	@Override
	public int getSizeInventory() {
		return invSize.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.invSize[slot];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.invSize != null) {
			ItemStack itemstack = this.invSize[slot];
			this.invSize[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.invSize[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
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
			if (slot >= 0 && slot < this.invSize.length) {
				ModLogger.logDebug("If-Slot: " + i + " :" + item.toString());
				this.invSize[slot] = ItemStack.loadItemStackFromNBT(item);
				ModLogger.logDebug("Done-Slot: " + i + " :" + item.toString());
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
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
		nbt.setTag("Items", items);
	}
}
