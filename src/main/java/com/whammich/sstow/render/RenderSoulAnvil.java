package com.whammich.sstow.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.whammich.sstow.models.ModelSoulAnvil;
import com.whammich.sstow.tileentity.TileEntitySoulAnvil;
import com.whammich.sstow.utils.Reference;

public class RenderSoulAnvil extends TileEntitySpecialRenderer {

	ResourceLocation texture = new ResourceLocation(Reference.MOD_ID + ":"
			+ "textures/blocks/SoulAnvil-texture.png");

	private ModelSoulAnvil model;

	public RenderSoulAnvil() {
		this.model = new ModelSoulAnvil();
	}

	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {

		TileEntitySoulAnvil SoulAnvil = (TileEntitySoulAnvil) entity;
		 GL11.glEnable(GL11.GL_LIGHTING);
		 GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		 model.render(SoulAnvil, x, y, z);
//		GL11.glPushMatrix();
//		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
//		GL11.glRotatef(180, 0F, 0F, 1F);
//
//		this.bindTexture(texture);
//
//		GL11.glPushMatrix();
//		this.model.render(entity);
//		GL11.glPopMatrix();
//		GL11.glPopMatrix();
	}

}
