package com.whammich.sstow.compat.waila;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.block.BlockCage;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class SoulShardsWailaPlugin implements IWailaPlugin {

    public static final String CONFIG_OWNER = SoulShardsTOW.MODID + ".displayOwner";

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new DataProviderCage(), BlockCage.class);
        registrar.registerNBTProvider(new DataProviderCage(), BlockCage.class);

        registrar.addConfig(SoulShardsTOW.MODID, CONFIG_OWNER, false);
    }
}
