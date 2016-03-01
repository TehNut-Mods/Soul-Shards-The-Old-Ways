package com.whammich.sstow.compat.waila;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.compat.waila.provider.DataProviderCage;
import mcp.mobius.waila.api.IWailaRegistrar;
import com.whammich.repack.tehnut.lib.annot.Used;

@Used
public class WailaCallbackHandler {

    public static final String CONFIG_OWNER = SoulShardsTOW.MODID + ".displayOwner";

    @Used
    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new DataProviderCage(), BlockCage.class);

        registrar.addConfig(SoulShardsTOW.MODID, CONFIG_OWNER, false);
    }
}
