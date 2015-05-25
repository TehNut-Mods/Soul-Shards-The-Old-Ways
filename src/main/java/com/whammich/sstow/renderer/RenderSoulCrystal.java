package com.whammich.sstow.renderer;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.models.ModelSoulCrystal;
import com.whammich.sstow.utils.Reference;

public class RenderSoulCrystal extends TileEntitySpecialRenderer implements IItemRenderer {

	private static final ResourceLocation texture = new ResourceLocation(Reference.modID + ":textures/models/soulCrystalTexture.png");

	private ModelSoulCrystal model = new ModelSoulCrystal();

	private final RenderItem customRenderItem = new RenderItem();
	
	public RenderSoulCrystal() {
		customRenderItem.setRenderManager(RenderManager.instance);
	}


	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
		EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		float bob = MathHelper.sin(p.ticksExisted / 14.0F) * 0.066F;
		
		Random rand = new Random(te.xCoord * te.yCoord + te.zCoord);

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
			GL11.glTranslatef((float) x + 0.5F, (float) y + bob, (float) z + 0.5F);
			GL11.glRotatef((rand.nextInt(5) + 45) * a + f + p.ticksExisted % 360, 0.0F, 1.0F, 0.0F);
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


	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}


	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}


	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		GL11.glScalef(-1F, -1F, 1F);
		switch (type) {
		case ENTITY:
		case INVENTORY:
			GL11.glTranslatef(0, -0.5F, 0);
			break;
		case EQUIPPED:
			GL11.glTranslatef(-0.5F,-1F, 0.5F);
			break;
		case EQUIPPED_FIRST_PERSON:
			GL11.glTranslatef(-0.3F, -1.1F, 0.5F);
			break;
		default:
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		model.render(null, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
	}
}
