package com.whammich.sstow.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.models.ModelSoulAnvil;
import com.whammich.sstow.utils.Reference;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderSoulAnvil extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(Reference.modID + ":textures/models/soulAnvilTexture.png");

	private ModelSoulAnvil model;

	public RenderSoulAnvil() {
		this.model = new ModelSoulAnvil();
	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glPushMatrix();
		this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();

		GL11.glPopMatrix();
	}

	public void render(float x, float y, float z, float size) {
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		GL11.glPushMatrix(); // start
		GL11.glScalef(size, size, size);
		GL11.glTranslatef(x, y, z); // size
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(-90, 0, 1, 0);
		model.renderAll();
		GL11.glPopMatrix(); // end
	}
}
