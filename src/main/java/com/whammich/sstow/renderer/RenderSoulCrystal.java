package com.whammich.sstow.renderer;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.models.ModelSoulCrystal;
import com.whammich.sstow.utils.Reference;

public class RenderSoulCrystal extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(Reference.modID + ":textures/models/soulCrystalTexture.png");

	private ModelSoulCrystal model;

	public RenderSoulCrystal() {
		this.model = new ModelSoulCrystal();
	}


	private void drawCrystal(float x, float y, float z, float partialTick, Random rand) {
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		float bob = MathHelper.sin(p.ticksExisted / 14.0F) * 0.066F;

		GL11.glPushMatrix();

		GL11.glEnable(2977);
		GL11.glEnable(3042);
		GL11.glEnable(32826);
		GL11.glBlendFunc(770, 771);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(220);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		for (int a = 0; a < 4; a++) {
			float pulse = MathHelper.sin(p.ticksExisted / (10.0F + a)) * 0.1F;
			GL11.glPushMatrix();
			GL11.glTranslatef(x + 0.5F, y + bob, z + 0.5F);
			GL11.glRotatef((rand.nextInt(5) + 45) * a + partialTick + p.ticksExisted % 360, 0.0F, 1.0F, 0.0F);
			GL11.glColor4f(0.9F + pulse, 0.2F + pulse, 0.9F + pulse, 1.0F);
			GL11.glScalef(0.35F + rand.nextFloat() * 0.05F, 0.9F + rand.nextFloat() * 0.1F, 0.35F + rand.nextFloat() * 0.05F);
			this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}

		GL11.glDisable(32826);
		GL11.glDisable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		drawCrystal((float)x, (float)y, (float)z, f, new Random(te.xCoord * te.yCoord + te.zCoord));
	}
}
