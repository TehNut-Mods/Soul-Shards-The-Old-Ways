package com.whammich.sstow.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySoulCrystal extends TileEntity {
	public int oldCharge = 0;
	public int charge = 0;
	public int count = 0;
	public String type = null;

	@Override
	public void updateEntity() {
		if (!this.worldObj.isRemote){
			return;
		}
		
		this.count += 1;
		if ((this.count % 20 == 0) && (this.oldCharge != this.charge)){
			System.out.println("Soul Crystal Charge: " + this.charge);
		}
		this.oldCharge = this.charge;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {}
}
