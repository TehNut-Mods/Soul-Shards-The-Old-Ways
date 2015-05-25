package com.whammich.sstow.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSoulCrystal extends ModelBase {
	public ModelRenderer Crystal;

	public ModelSoulCrystal() {
		textureWidth = 64;
		textureHeight = 32;

		Crystal = new ModelRenderer(this, 0, 0);
		Crystal.addBox(-16.0F, -16.0F, 0.0F, 16, 16, 16);
		Crystal.setRotationPoint(0.0F, 32.0F, 0.0F);
		Crystal.setTextureSize(textureWidth, textureHeight);
		Crystal.mirror = true;
		setRotation(Crystal, 0.7071F, 0.0F, 0.7071F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
		super.render(entity, f, f1, f2, f3, f4, f5);
		Crystal.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void renderAll(){
		Crystal.render(0.625F);
	}

}
