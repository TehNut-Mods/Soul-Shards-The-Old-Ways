package com.whammich.sstow.compat.waila;

import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.block.BlockSoulCrystal;

import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegister {
	public static void wailaCallback (IWailaRegistrar registrar) {
		
		registrar.registerBodyProvider(new SoulCrystalWailaProvider(), BlockSoulCrystal.class);
		registrar.registerNBTProvider(new SoulCrystalWailaProvider(), BlockSoulCrystal.class);
		
		registrar.registerBodyProvider(new SoulCageWailaProvider(), BlockCage.class);
		registrar.registerNBTProvider(new SoulCageWailaProvider(), BlockCage.class);
	
    }
}
