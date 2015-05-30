package com.whammich.sstow.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.item.ItemShardSoul;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Utils;

public class RenderSoulCage extends TileEntitySpecialRenderer {//implements IItemRenderer {
	
	private static final ResourceLocation texture = new ResourceLocation(Reference.modID + ":textures/blocks/soulCage.png");
	private Minecraft mc;
	private TileEntityCage TECage;

	public RenderSoulCage() {
		this.mc = Minecraft.getMinecraft();
	}

	@SuppressWarnings("static-access")
	public void getEntityForRender(ItemStack stack, double x, double y, double z, float f) {
		Tessellator tessellator = Tessellator.instance;

		if(stack != null) {
			if(stack.getItem() instanceof ItemShardSoul) {
				Entity mob = EntityList.createEntityByName(Utils.getShardBoundEnt(stack), mc.theWorld);
				GL11.glPushMatrix();
				GL11.glScalef(0.4375F, 0.4375F, 0.4375F);
				GL11.glTranslatef((float) x + 0.5F, (float) y - 2F, (float) z + 0.5F);
				GL11.glRotatef(20F, 0F, 0F, 1F);
				if(TECage.active) {
					GL11.glRotatef(mc.getSystemTime() / -10, 0F, 1F, 0F);
				}
				GL11.glRotatef(-20F, 1F, 0F, 0F);
				RenderManager.instance.renderEntityWithPosYaw(mob, 0, 0, 0, 0F, 1F);
				GL11.glPopMatrix();
			} 
		}
		{
			
			// FOLLOWING CODE BORROWED FROM DRACONIC EVOLUTION
			// https://github.com/brandon3055/Draconic-Evolution/
			
			GL11.glPushMatrix();

			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			
			tessellator.setColorRGBA(255, 255, 255, 255);
			tessellator.setBrightness(200);

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-0.5F, 0F, -0.5F);

			tessellator.addVertexWithUV(0, 0.99D, 0, 0, 0);
			tessellator.addVertexWithUV(1, 0.99D, 0, 1, 0);
			tessellator.addVertexWithUV(1, 0.99D, 1, 1, 1);
			tessellator.addVertexWithUV(0, 0.99D, 1, 0, 1);

			tessellator.addVertexWithUV(0, 0.99D, 0, 0, 0);
			tessellator.addVertexWithUV(0, 0.99D, 1, 1, 0);
			tessellator.addVertexWithUV(1, 0.99D, 1, 1, 1);
			tessellator.addVertexWithUV(1, 0.99D, 0, 0, 1);

			tessellator.addVertexWithUV(0, 0.01D, 0, 0, 0);
			tessellator.addVertexWithUV(1, 0.01D, 0, 1, 0);
			tessellator.addVertexWithUV(1, 0.01D, 1, 1, 1);
			tessellator.addVertexWithUV(0, 0.01D, 1, 0, 1);

			tessellator.addVertexWithUV(0, 0.01D, 0, 0, 0);
			tessellator.addVertexWithUV(0, 0.01D, 1, 1, 0);
			tessellator.addVertexWithUV(1, 0.01D, 1, 1, 1);
			tessellator.addVertexWithUV(1, 0.01D, 0, 0, 1);

			tessellator.draw();

			GL11.glPopMatrix();
		}

	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

		getEntityForRender(((IInventory)entity).getStackInSlot(0), x, y, z, f);

		GL11.glPopMatrix();
	}
}
