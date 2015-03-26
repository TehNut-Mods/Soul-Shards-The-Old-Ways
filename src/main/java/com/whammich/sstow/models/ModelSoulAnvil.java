package com.whammich.sstow.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

/**
 * SoulAnvil - TeamWhammich Created using Tabula 4.1.1
 */
public class ModelSoulAnvil extends ModelBase {
	public ModelRenderer SAnvilDec;
	public ModelRenderer SAnvilCol;
	public ModelRenderer SAnvilHead;
	public ModelRenderer Scale;
	public ModelRenderer ShardPT1;
	public ModelRenderer ShardPT2;
	public ModelRenderer ShardPT3;
	public ModelRenderer SAnvilBase;
	public ModelRenderer Claw21;
	public ModelRenderer Claw22;
	public ModelRenderer Claw11;
	public ModelRenderer Claw12;
	public ModelRenderer Claw31;
	public ModelRenderer Claw32;
	public ModelRenderer Claw41;
	public ModelRenderer Claw42;

	public ModelSoulAnvil() {
		this.textureWidth = 32;
		this.textureHeight = 32;
		this.SAnvilDec = new ModelRenderer(this, 0, 0);
		this.SAnvilDec.setRotationPoint(0.0F, 21.0F, 0.0F);
		this.SAnvilDec.addBox(-5.0F, -1.0F, -5.0F, 10, 1, 10, 0.0F);
		this.ShardPT1 = new ModelRenderer(this, 16, 16);
		this.ShardPT1.setRotationPoint(-1.0F, 10.9F, 0.0F);
		this.ShardPT1.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.setRotateAngle(ShardPT1, 0.0F, 0.7853981633974483F, 0.0F);
		this.ShardPT3 = new ModelRenderer(this, 16, 16);
		this.ShardPT3.setRotationPoint(0.0F, 10.9F, 0.0F);
		this.ShardPT3.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.SAnvilCol = new ModelRenderer(this, 0, 0);
		this.SAnvilCol.setRotationPoint(0.0F, 20.0F, 0.0F);
		this.SAnvilCol.addBox(-4.0F, -4.0F, -2.0F, 8, 4, 4, 0.0F);
		this.Scale = new ModelRenderer(this, 0, 0);
		this.Scale.setRotationPoint(16.0F, 16.0F, 0.0F);
		this.Scale.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
		this.Claw42 = new ModelRenderer(this, 0, 0);
		this.Claw42.setRotationPoint(-7.0F, 24.0F, 7.0F);
		this.Claw42.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.Claw12 = new ModelRenderer(this, 0, 0);
		this.Claw12.setRotationPoint(-7.0F, 24.0F, -7.0F);
		this.Claw12.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.Claw31 = new ModelRenderer(this, 0, 0);
		this.Claw31.setRotationPoint(6.0F, 24.0F, 6.0F);
		this.Claw31.addBox(-1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F);
		this.Claw11 = new ModelRenderer(this, 0, 0);
		this.Claw11.setRotationPoint(-6.0F, 24.0F, -6.0F);
		this.Claw11.addBox(-1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F);
		this.Claw41 = new ModelRenderer(this, 0, 0);
		this.Claw41.setRotationPoint(-6.0F, 24.0F, 6.0F);
		this.Claw41.addBox(-1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F);
		this.Claw22 = new ModelRenderer(this, 0, 0);
		this.Claw22.setRotationPoint(7.0F, 24.0F, -7.0F);
		this.Claw22.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.SAnvilBase = new ModelRenderer(this, 0, 0);
		this.SAnvilBase.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.SAnvilBase.addBox(-6.0F, -3.0F, -6.0F, 12, 3, 12, 0.0F);
		this.Claw32 = new ModelRenderer(this, 0, 0);
		this.Claw32.setRotationPoint(7.0F, 24.0F, 7.0F);
		this.Claw32.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.Claw21 = new ModelRenderer(this, 0, 0);
		this.Claw21.setRotationPoint(6.0F, 24.0F, -6.0F);
		this.Claw21.addBox(-1.0F, -2.0F, -1.0F, 2, 2, 2, 0.0F);
		this.SAnvilHead = new ModelRenderer(this, 0, 0);
		this.SAnvilHead.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.SAnvilHead.addBox(-7.0F, -6.0F, -5.0F, 14, 6, 10, 0.0F);
		this.ShardPT2 = new ModelRenderer(this, 16, 16);
		this.ShardPT2.setRotationPoint(1.0F, 10.9F, 0.0F);
		this.ShardPT2.addBox(-1.0F, -1.0F, -1.0F, 2, 1, 2, 0.0F);
		this.setRotateAngle(ShardPT2, 0.0F, 0.7853981633974483F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		this.SAnvilDec.render(f5);
		this.ShardPT1.render(f5);
		GL11.glPushMatrix();
		GL11.glTranslatef(this.ShardPT3.offsetX, this.ShardPT3.offsetY,
				this.ShardPT3.offsetZ);
		GL11.glTranslatef(this.ShardPT3.rotationPointX * f5,
				this.ShardPT3.rotationPointY * f5, this.ShardPT3.rotationPointZ
						* f5);
		GL11.glScaled(1.0D, 1.0D, 1.4D);
		GL11.glTranslatef(-this.ShardPT3.offsetX, -this.ShardPT3.offsetY,
				-this.ShardPT3.offsetZ);
		GL11.glTranslatef(-this.ShardPT3.rotationPointX * f5,
				-this.ShardPT3.rotationPointY * f5,
				-this.ShardPT3.rotationPointZ * f5);
		this.ShardPT3.render(f5);
		GL11.glPopMatrix();
		this.SAnvilCol.render(f5);
		this.Scale.render(f5);
		this.Claw42.render(f5);
		this.Claw12.render(f5);
		this.Claw31.render(f5);
		this.Claw11.render(f5);
		this.Claw41.render(f5);
		this.Claw22.render(f5);
		this.SAnvilBase.render(f5);
		this.Claw32.render(f5);
		this.Claw21.render(f5);
		this.SAnvilHead.render(f5);
		this.ShardPT2.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y,
			float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
