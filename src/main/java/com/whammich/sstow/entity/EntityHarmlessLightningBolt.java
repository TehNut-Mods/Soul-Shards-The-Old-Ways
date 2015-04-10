package com.whammich.sstow.entity;

import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityHarmlessLightningBolt extends EntityLightningBolt {
	private int lightningState;
	public long boltVertex;
	private int boltLivingTime;

	public EntityHarmlessLightningBolt(World par1World, double par2,
			double par4, double par6) {
		super(par1World, 0, Double.MAX_VALUE, 0);
		this.setLocationAndAngles(par2, par4, par6, 0.0F, 0.0F);
		this.lightningState = 2;
		this.boltVertex = this.rand.nextLong();
		this.boltLivingTime = this.rand.nextInt(3) + 1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {

		if (this.lightningState == 2) {
			this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ,
					"ambient.weather.thunder", 10000.0F,
					0.8F + this.rand.nextFloat() * 0.2F);
			this.worldObj
					.playSoundEffect(this.posX, this.posY, this.posZ,
							"random.explode", 2.0F,
							0.5F + this.rand.nextFloat() * 0.2F);
		}

		--this.lightningState;

		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.setDead();
			} else if (this.lightningState < -this.rand.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = this.rand.nextLong();
			}
		}

		if (this.lightningState >= 0) {
			if (this.worldObj.isRemote) {
				this.worldObj.lastLightningBolt = 2;
			}
		}
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
	}
}
