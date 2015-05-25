package com.whammich.sstow.guihandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.whammich.sstow.tileentity.ContainerCage;
import com.whammich.sstow.tileentity.ContainerDiffuser;
import com.whammich.sstow.tileentity.ContainerForge;
import com.whammich.sstow.tileentity.ContainerInfuser;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.tileentity.TileEntityDiffuser;
import com.whammich.sstow.tileentity.TileEntityForge;
import com.whammich.sstow.tileentity.TileEntityInfuser;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {

	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		TileEntity tileEnt = world.getTileEntity(x, y, z);
		if(tileEnt instanceof TileEntityForge) {
			return new ContainerForge(player.inventory, (TileEntityForge)tileEnt);
		}
		if(tileEnt instanceof TileEntityCage) {
			return new ContainerCage(player.inventory, (TileEntityCage)tileEnt);
		}
		if(tileEnt instanceof TileEntityDiffuser) {
			return new ContainerDiffuser(player.inventory, (TileEntityDiffuser)tileEnt);
		}
		if(tileEnt instanceof TileEntityInfuser) {
			return new ContainerInfuser(player.inventory, (TileEntityInfuser)tileEnt);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEnt = world.getTileEntity(x, y, z);
		if(tileEnt instanceof TileEntityForge) {
			return new GuiSoulForge(player.inventory, (TileEntityForge)tileEnt);
		}
		if(tileEnt instanceof TileEntityCage) {
			return new GuiSoulCage(player.inventory, (TileEntityCage)tileEnt);
		}
		if(tileEnt instanceof TileEntityDiffuser) {
			return new GuiDiffuser(player.inventory, (TileEntityDiffuser)tileEnt);
		}
		if(tileEnt instanceof TileEntityInfuser) {
			return new GuiInfuser(player.inventory, (TileEntityInfuser)tileEnt);
		}
		
		return null;
	}

}