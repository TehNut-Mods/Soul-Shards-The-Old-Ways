package com.whammich.sstow.guihandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.whammich.sstow.tileentity.ContainerCage;
import com.whammich.sstow.tileentity.ContainerForge;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.tileentity.TileEntityForge;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			TileEntityForge TileEntityForge = (TileEntityForge) world.getTileEntity(x, y, z);
			return new ContainerForge(player.inventory, TileEntityForge);
		}
		if (ID == 1) {
			TileEntityCage tileEntityCage = (TileEntityCage) world.getTileEntity(x, y, z);
			return new ContainerCage(player.inventory, tileEntityCage);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			TileEntityForge TileEntityForge = (TileEntityForge) world.getTileEntity(x, y, z);
			return new GuiSoulForge(player.inventory, TileEntityForge);
		}
		if (ID == 1) {
			TileEntityCage TileEntityCage = (TileEntityCage) world.getTileEntity(x, y, z);
			return new GuiSoulCage(player.inventory, TileEntityCage);
		}
		return null;
	}

}