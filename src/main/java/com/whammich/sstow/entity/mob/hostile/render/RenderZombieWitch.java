package com.whammich.sstow.entity.mob.hostile.render;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.entity.mob.hostile.EntityZombieWitch;
import com.whammich.sstow.utils.Reference;

public class RenderZombieWitch extends RenderLiving {

	private final ModelWitch witchModel;

	public RenderZombieWitch() {
		super(new ModelWitch(0.0F), 0.5F);
		this.witchModel = (ModelWitch)this.mainModel;
	}

	private static final ResourceLocation witchTextures = new ResourceLocation(Reference.modID + ":textures/entity/zombieWitch.png");

	public void doRender(EntityZombieWitch entity, double d0, double d1, double d2, float f1, float f2) {
		ItemStack itemstack = entity.getHeldItem();
		this.witchModel.field_82900_g = itemstack != null;
		super.doRender((EntityLiving)entity, d0, d1, d2, f1, f2);
	}

	protected ResourceLocation getEntityTexture(EntityZombieWitch entity) {
		return witchTextures;
	}

	protected void renderEquippedItems(EntityZombieWitch entity, float f) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		super.renderEquippedItems(entity, f);
		ItemStack itemstack = entity.getHeldItem();

		if (itemstack != null) {
			GL11.glPushMatrix();
			float f1;

			if (this.mainModel.isChild) {
				f1 = 0.5F;
				GL11.glTranslatef(0.0F, 0.625F, 0.0F);
				GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
				GL11.glScalef(f1, f1, f1);
			}

			this.witchModel.villagerNose.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.53125F, 0.21875F);

			if (itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
				f1 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f1 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f1, -f1, f1);
			} else if (itemstack.getItem() == Items.bow) {
				f1 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f1, -f1, f1);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (itemstack.getItem().isFull3D()) {
				f1 = 0.625F;

				if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				this.func_82410_b();
				GL11.glScalef(f1, -f1, f1);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				f1 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f1, f1, f1);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			GL11.glRotatef(-15.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
			this.renderManager.itemRenderer.renderItem(entity, itemstack, 0);

			if (itemstack.getItem().requiresMultipleRenderPasses()) {
				this.renderManager.itemRenderer.renderItem(entity, itemstack, 1);
			}

			GL11.glPopMatrix();
		}
	}

	protected void func_82410_b() {
		GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityZombieWitch entity, float f3) {
		float f1 = 0.9375F;
		GL11.glScalef(f1, f1, f1);
	}

	public void doRender(EntityLiving entity, double d0, double d1, double d2, float f1, float f2) {
		this.doRender((EntityZombieWitch)entity, d0, d1, d2, f1, f2);
	}

	protected void preRenderCallback(EntityLivingBase entity, float f3) {
		this.preRenderCallback((EntityZombieWitch)entity, f3);
	}

	protected void renderEquippedItems(EntityLivingBase entity, float f) {
		this.renderEquippedItems((EntityZombieWitch)entity, f);
	}

	public void doRender(EntityLivingBase entity, double d0, double d1, double d2, float f1, float f2) {
		this.doRender((EntityZombieWitch)entity, d0, d1, d2, f1, f2);
	}

	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityZombieWitch)p_110775_1_);
	}

	public void doRender(Entity entity, double d0, double d1, double d2, float f1, float f2) {
		this.doRender((EntityZombieWitch)entity, d0, d1, d2, f1, f2);
	}
}
