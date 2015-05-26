package com.whammich.sstow.guihandler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.whammich.sstow.guihandler.containers.ContainerInfuser;
import com.whammich.sstow.tileentity.TileEntityInfuser;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Utils;

public class GuiInfuser extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation(Reference.modID + ":textures/gui/container/fusers.png");
	
	TileEntityInfuser tileInfuser;

	public GuiInfuser(InventoryPlayer par1InventoryPlayer, TileEntityInfuser tileInfuser) {
		super(new ContainerInfuser(par1InventoryPlayer, tileInfuser));
		this.tileInfuser = tileInfuser;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(Utils.localize("container.soulinfuser"), 60, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}
