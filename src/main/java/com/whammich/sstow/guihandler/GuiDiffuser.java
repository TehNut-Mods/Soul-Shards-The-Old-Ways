package com.whammich.sstow.guihandler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.guihandler.containers.ContainerDiffuser;
import com.whammich.sstow.tileentity.TileEntityDiffuser;
import com.whammich.sstow.utils.Utils;

public class GuiDiffuser extends GuiContainer {
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/fusers.png");
	
	TileEntityDiffuser tileDiffuser;

	public GuiDiffuser(InventoryPlayer par1InventoryPlayer, TileEntityDiffuser tileDiffuser) {
		super(new ContainerDiffuser(par1InventoryPlayer, tileDiffuser));
		this.tileDiffuser = tileDiffuser;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(Utils.localize("container.souldiffuser"), 60, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}
}
