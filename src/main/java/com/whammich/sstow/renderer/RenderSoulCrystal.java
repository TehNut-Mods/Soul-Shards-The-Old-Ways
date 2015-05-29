package com.whammich.sstow.renderer;

import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.models.ModelSoulCrystal;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.client.FMLClientHandler;

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

		glPushMatrix();

		glEnable(2977);
		glEnable(3042);
		glEnable(32826);
		glBlendFunc(770, 771);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(220);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		for (int a = 0; a < 4; a++) {
			float pulse = MathHelper.sin(p.ticksExisted / (10.0F + a)) * 0.1F;
			glPushMatrix();
			glTranslatef((float) x + 0.5F, (float) y + bob, (float) z + 0.5F);
			glRotatef((rand.nextInt(5) + 45) * a + f + p.ticksExisted % 360, 0.0F, 1.0F, 0.0F);
			glColor4f(0.9F + pulse, 0.2F + pulse, 0.9F + pulse, 1.0F);
			glScalef(0.35F + rand.nextFloat() * 0.05F, 0.9F + rand.nextFloat() * 0.1F, 0.35F + rand.nextFloat() * 0.05F);
			this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
			glScalef(1.0F, 1.0F, 1.0F);
			glPopMatrix();
		}

		glDisable(32826);
		glDisable(3042);
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		glPopMatrix();
	}


	public void renderModel(TileEntity tile, double x, double y, double z) {
        float scale = 1F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glScalef(scale, scale, scale);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
        GL11.glRotatef(180F, 90.0F, 0.0F, 90.0F);
        model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
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
		switch (type) {
		case ENTITY: { //item entity
			if (item.getItem() == new ItemStack(Register.BlockSoulCrystal).getItem())
				render(0.5F, 15F, -0.5F, 0.1F);
			return;
		}
		case EQUIPPED: { //third person in hand
			if (item.getItem() == new ItemStack(Register.BlockSoulCrystal).getItem())
				render(2F, 15F, 5F, 0.1F);
			return;
		}
		case EQUIPPED_FIRST_PERSON: { //first person in hand
			if (item.getItem() == new ItemStack(Register.BlockSoulCrystal).getItem())
				render(1F, 19F, 7F, 0.1F);
			return;
		}
		case INVENTORY: { //the item in inventories
			if (item.getItem() == new ItemStack(Register.BlockSoulCrystal).getItem())
				render(-0.01F, 10F, 0.0F, 0.1F);
			return;
		}
		default:
			return;
		}
	}
	
    private void render(float x, float y, float z, float size) {
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
