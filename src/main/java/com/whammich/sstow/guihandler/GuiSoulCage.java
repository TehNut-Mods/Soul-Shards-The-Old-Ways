package com.whammich.sstow.guihandler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.tileentity.ContainerCage;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSoulCage extends GuiContainer {

	private static final ResourceLocation CageGuiTexture = new ResourceLocation(Reference.modID + ":textures/gui/container/cage.png");
	private TileEntityCage tileCage;

	public GuiSoulCage(InventoryPlayer invPlayer, TileEntityCage tile) {
		super(new ContainerCage(invPlayer, tile));
		this.tileCage = tile;

	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String string = this.tileCage.hasCustomInventoryName() ? this.tileCage.getInventoryName() : I18n.format(this.tileCage.getInventoryName(), new Object[0]);
		this.fontRendererObj.drawString(string, this.xSize / 2 - this.fontRendererObj.getStringWidth(string), 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CageGuiTexture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		this.drawTexturedModalRect(k+35, l+16, 176, 0,
				18, 18);
//		if (!Config.Module_CONTROL){
			this.drawTexturedModalRect(k+152, l+17, 176, 18,
					16, 16);
//		}
		if (!Config.redstoneModule) {
			this.drawTexturedModalRect(k+70, l+17, 176, 18,
					16, 16);
		}
		if (!Config.lightModule) {
			this.drawTexturedModalRect(k+88, l+17, 176, 18,
					16, 16);
		}
		if (!Config.dimensionModule) {
			this.drawTexturedModalRect(k+106, l+17, 176, 18,
					16, 16);
		}
		if (!Config.playerModule) {
			this.drawTexturedModalRect(k+124, l+17, 176, 18,
					16, 16);
		}
	}
}
