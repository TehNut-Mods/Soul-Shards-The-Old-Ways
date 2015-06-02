package com.whammich.sstow.entity.particle;

import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityPurpleFlameFX extends EntityFlameFX {

	public EntityPurpleFlameFX(World world, double x, double y, double z, double movX, double movY, double movZ) {
        super(world, x, y, z, movX, movY, movZ);
        this.particleRed = 68 / 255.0F;
        this.particleGreen = 0 / 255.0F;
        this.particleBlue = 152 / 255.0F;
		this.motionX = this.motionX * 0.009999999776482582D + 0;
		this.motionY = this.motionY * 0.009999999776482582D + 0;
		this.motionZ = this.motionZ * 0.009999999776482582D + 0;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
		this.noClip = true;
	}

	@Override
	public void onUpdate() {
		 this.prevPosX = this.posX;
		 this.prevPosY = this.posY;
		 this.prevPosZ = this.posZ;

		 if (this.particleAge++ >= this.particleMaxAge) {
			 this.setDead();
		 }

		 this.moveEntity(this.motionX, this.motionY, this.motionZ);
		 this.motionX *= 0.9599999785423279D;
		 this.motionY *= 0.9599999785423279D;
		 this.motionZ *= 0.9599999785423279D;

		 if (this.onGround) {
			 this.motionX *= 0.699999988079071D;
			 this.motionZ *= 0.699999988079071D;
		 }
	 }
}
