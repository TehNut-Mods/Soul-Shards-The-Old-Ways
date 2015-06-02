package com.whammich.sstow.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.item.ItemShardSoul;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.utils.Utils;

public class RenderSoulCage extends TileEntitySpecialRenderer {//implements IItemRenderer {
	
	private Minecraft mc;
	private TileEntityCage TECage;

	public RenderSoulCage() {
		this.mc = Minecraft.getMinecraft();
	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
		getEntityForRender(((IInventory)entity).getStackInSlot(0), x, y, z, f);
		GL11.glPopMatrix();
	}

	@SuppressWarnings("static-access")
	public void getEntityForRender(ItemStack stack, double x, double y, double z, float f) {
		
		if(stack != null) {
			if(stack.getItem() instanceof ItemShardSoul) {
				
				Entity mob = EntityList.createEntityByName(Utils.getShardBoundEnt(stack), mc.theWorld);
				float f1 = 0.4375F;
				GL11.glTranslatef(0.0F, 0.4F, 0.0F);
				GL11.glRotatef(20F, 0F, 0F, 1F);
				if(TECage.active) {
					GL11.glRotatef(mc.getSystemTime() / -10, 0F, 1F, 0F);
				}
				GL11.glRotatef(-20F, 1F, 0F, 0F);
				GL11.glTranslatef(0.0F, -0.4F, 0.0F);
				GL11.glScalef(f1, f1, f1);
				mob.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
				RenderManager.instance.renderEntityWithPosYaw(mob, 0, 0, 0, 0F, 1F);
			} 
		}
	}

}
