package com.whammich.sstow.compat.waila;

import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.compat.waila.provider.DataProviderCage;
import mcp.mobius.waila.api.IWailaRegistrar;
import com.whammich.repack.tehnut.lib.annot.Used;

@Used
public class WailaCallbackHandler {

    @Used
    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new DataProviderCage(), BlockCage.class);
    }
}
