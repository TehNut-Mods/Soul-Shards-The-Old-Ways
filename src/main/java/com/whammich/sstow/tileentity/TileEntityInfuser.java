package com.whammich.sstow.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityInfuser extends TileEntity implements IInventory {
	public ItemStack[] invSize;
	
	public TileEntityInfuser() {
		this.invSize = new ItemStack[1];
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
	public ItemStack decrStackSize(int slot, int amount) {
		if (this.invSize[slot] != null) {
			ItemStack itemstack;

			if (this.invSize[slot].stackSize <= amount) {
				itemstack = this.invSize[slot];
				this.invSize[slot] = null;
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.invSize[slot].splitStack(amount);

				if (this.invSize[slot].stackSize == 0) {
					this.invSize[slot] = null;
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
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
			this.markDirty();
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
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		this.invSize = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound tabCompound1 = tagList.getCompoundTagAt(i);
			byte byte0 = tabCompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < this.invSize.length) {
				this.invSize[byte0] = ItemStack.loadItemStackFromNBT(tabCompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagList tagList = new NBTTagList();
		for (int i = 0; i < this.invSize.length; ++i) {
			if (this.invSize[i] != null) {
				NBTTagCompound tagCompound1 = new NBTTagCompound();
				tagCompound1.setByte("Slot", (byte) i);
				this.invSize[i].writeToNBT(tagCompound1);
				tagList.appendTag(tagCompound1);
			}
		}
		tagCompound.setTag("Items", tagList);

	}
}
