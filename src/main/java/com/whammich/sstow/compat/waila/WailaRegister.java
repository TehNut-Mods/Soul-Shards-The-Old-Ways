package com.whammich.sstow.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;

import com.whammich.sstow.block.BlockCage;

public class WailaRegister {
	public static void wailaCallback (IWailaRegistrar registrar) {

		
		registrar.registerBodyProvider(new SoulCageWailaProvider(), BlockCage.class);
		registrar.registerNBTProvider(new SoulCageWailaProvider(), BlockCage.class);
	
    }
}
