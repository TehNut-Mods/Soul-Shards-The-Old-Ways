package com.whammich.sstow.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySoulCrystal extends TileEntity {
	public int charge = 0;
	public String type = "";

	
	public String getEntityName() {
		return type;
	}
	
	public int getSoulCount() {
		return charge;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {}
}
