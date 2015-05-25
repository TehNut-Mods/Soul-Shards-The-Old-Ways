package com.whammich.sstow.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;

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

	@SuppressWarnings("static-access")
	public void getEntityForRender(ItemStack stack, double x, double y, double z, float f) {
		try {
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
				} else {
					return;
				}
			} else {

			}
		} catch (ReportedException RE) {
			RE.printStackTrace();
		}

	}

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		getEntityForRender(((IInventory)entity).getStackInSlot(0), x, y, z, f);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
