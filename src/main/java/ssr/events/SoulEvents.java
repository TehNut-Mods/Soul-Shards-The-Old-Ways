package ssr.events;

import net.minecraftforge.common.MinecraftForge;

public class SoulEvents {
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new PlayerKill());
		MinecraftForge.EVENT_BUS.register(new AbsorbSpawner());
		// MinecraftForge.EVENT_BUS.register(new CreateShard());
	}
}
