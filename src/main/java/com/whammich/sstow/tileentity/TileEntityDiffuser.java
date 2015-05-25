package com.whammich.sstow.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import com.whammich.sstow.utils.ModLogger;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

public class TileEntityDiffuser extends TileEntity implements IInventory {

	public ItemStack[] inv = new ItemStack[1];

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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	@Override
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

	@Override
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

	@Override
	public void updateEntity() {
		TileEntity t = this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
		if ((t == null) || (!(t instanceof TileEntitySoulCrystal)) || (this.inv[0] == null) 
				|| (!this.inv[0].getItem().equals(Register.ItemShardSoul))) {
			return;
		}
		TileEntitySoulCrystal ts = (TileEntitySoulCrystal)t;

		if (Utils.getShardBoundEnt(this.inv[0]).isEmpty()){
			return;
		}
		if (!Utils.getShardBoundEnt(this.inv[0]).equals(ts.type)) {
			if (ts.charge > 0){
				return;
			}
			ts.type = Utils.getShardBoundEnt(this.inv[0]);
		}

		((TileEntitySoulCrystal)t).charge += 1;
		this.inv[0].setItemDamage(this.inv[0].getItemDamage() + 1);
	}

}
