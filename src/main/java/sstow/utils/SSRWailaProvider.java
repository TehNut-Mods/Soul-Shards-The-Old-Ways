package sstow.utils;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import sstow.gameObjs.block.Cage_Block;
import sstow.gameObjs.tile.CageTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class SSRWailaProvider implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {

		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		CageTile teSoulCage = (CageTile) accessor.getTileEntity();
		if (teSoulCage != null) {
			if (teSoulCage.getStackInSlot(0) != null) {
				// currenttip.add(StatCollector.translateToLocal("sstow.waila.soulcage.filled"));
				currenttip.add(StatCollector
						.translateToLocal("sstow.waila.soulcage.mob")
						+ teSoulCage.getEntityName());
				currenttip.add(StatCollector
						.translateToLocal("sstow.waila.soulcage.tier")
						+ teSoulCage.getTier());
				NBTTagCompound tag = (NBTTagCompound) accessor.getNBTData();

				if (tag.getBoolean("active")) {
					currenttip.add(StatCollector
							.translateToLocal("sstow.waila.soulcage.activated"));
				} else {
					currenttip
							.add(StatCollector
									.translateToLocal("sstow.waila.soulcage.deactivated"));
				}
			} else {
				currenttip.add(StatCollector
						.translateToLocal("sstow.waila.soulcage.empty"));
			}
		}

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new SSRWailaProvider(),
				Cage_Block.class);
	}

}
