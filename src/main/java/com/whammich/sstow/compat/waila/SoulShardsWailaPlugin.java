package com.whammich.sstow.compat.waila;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.tile.TileEntityCage;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class SoulShardsWailaPlugin implements IWailaPlugin {

    public static final String CONFIG_OWNER = SoulShardsTOW.MODID + ".displayOwner";

    @Override
    public void register(IWailaRegistrar registrar) {
        final DataProviderCage providerCage = new DataProviderCage();

        registrar.registerBodyProvider(providerCage, TileEntityCage.class);
        registrar.registerNBTProvider(providerCage, TileEntityCage.class);

        registrar.addConfig(SoulShardsTOW.MODID, CONFIG_OWNER, false);
    }
}
