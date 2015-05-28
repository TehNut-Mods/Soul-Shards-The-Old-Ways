package com.whammich.sstow.compat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.utils.Reference;

public class SoulCageWailaProvider implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntityCage teSoulCage = (TileEntityCage) accessor.getTileEntity();
		if (teSoulCage != null) {
			if (teSoulCage.getStackInSlot(0) != null) {
				// currenttip.add(StatCollector.translateToLocal(Reference.modID + ".waila.soulcage.filled"));
				currenttip.add(StatCollector.translateToLocal(Reference.modID + ".waila.soulcage.mob") + teSoulCage.getEntityName());
				currenttip.add(StatCollector.translateToLocal(Reference.modID + ".waila.soulcage.tier") + teSoulCage.getTier());
				NBTTagCompound tag = (NBTTagCompound) accessor.getNBTData();

				if (tag.getBoolean("active")) {
					currenttip.add(StatCollector.translateToLocal(Reference.modID + ".waila.soulcage.activated"));
				} else {
					currenttip.add(StatCollector.translateToLocal(Reference.modID + ".waila.soulcage.deactivated"));
				}
			} else {
				currenttip.add(StatCollector.translateToLocal(Reference.modID + ".waila.soulcage.empty"));
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