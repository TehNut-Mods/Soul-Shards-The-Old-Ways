package com.whammich.sstow;

import com.whammich.sstow.events.AchievementEvents;
import com.whammich.sstow.events.Achievements;
import com.whammich.sstow.events.CreateShardEvent;
import com.whammich.sstow.events.PlayerKillEntityEvent;
import com.whammich.sstow.gameObjs.ObjHandler;
import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.EntityMapper;
import com.whammich.sstow.commands.SSTOWCMD;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import codechicken.nei.api.API;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SSTheOldWays.MODID, name = SSTheOldWays.NAME, version = SSTheOldWays.VERSION, guiFactory = "sstow.utils.guiFactory")
public class SSTheOldWays {

	public static final String MODID = "SSTOW";
	public static final String NAME = "Soul Shards: The Old Ways";
	public static final String VERSION = "@VERSION@";

	@Instance(MODID)
	public static SSTheOldWays modInstance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ObjHandler.registerObjs();
		// System.out.println("Achievements Loading");
		Achievements.Get();
		// System.out.println("Achievements Loaded");
		MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());
		MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
		// System.out.println("Registering Achievement Events");
		FMLCommonHandler.instance().bus().register(new AchievementEvents());
		// System.out.println("Achievement Events Registed");
		FMLInterModComms.sendMessage("Waila", "register",
				"sstow.utils.WailaProvider.callbackRegister");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		EntityMapper.init();
		if (Loader.isModLoaded("NotEnoughItems")) {
			API.hideItem(new ItemStack(ObjHandler.SOUL_FORGE_ACTIVE));
			API.hideItem(new ItemStack(ObjHandler.FIXED));
			API.hideItem(new ItemStack(ObjHandler.IRON_NUGGET));
		}
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new SSTOWCMD());
	}
}
