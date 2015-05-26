package com.whammich.sstow.compat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.whammich.sstow.tileentity.TileEntitySoulCrystal;
import com.whammich.sstow.utils.Utils;

public class SoulCrystalWailaProvider implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntitySoulCrystal teSoulCrystal = (TileEntitySoulCrystal) accessor.getTileEntity();
		if(teSoulCrystal != null) {
			if(teSoulCrystal.getEntityName() == null) {
				currenttip.add(Utils.localize("sstow.waila.soulcage.empty"));
			} else {
				currenttip.add(Utils.localize("") + teSoulCrystal.getEntityName());
				currenttip.add(Utils.localize("") + teSoulCrystal.getSoulCount());
			}
		}

		return currenttip;
	}

	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound nbt, World world, int arg4, int arg5, int arg6) {
		if (tile != null) {
			tile.writeToNBT(nbt);
		}

		return nbt;
	}

}