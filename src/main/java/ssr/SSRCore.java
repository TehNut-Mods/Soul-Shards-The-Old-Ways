package ssr;

import java.io.File;

import org.apache.logging.log4j.Logger;

import ssr.config.MobBlackList;
import ssr.config.SoulConfig;
import ssr.events.SoulEvents;
import ssr.gameObjs.ObjHandler;
import ssr.utils.CommandKillMobs;
import ssr.utils.DynamicMobMapping;
import ssr.utils.TierHandling;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SSRCore.ID, name = SSRCore.Name, version = SSRCore.Version)

public class SSRCore 
{
	@Instance("SSR")
	public static SSRCore instance;
	
	public static final String ID = "SSR";
	public static final String Name = "Soul Shards: Reborn";
	public static final String Version = "Alpha 0.9e";
	
	public static Logger SoulLog = FMLLog.getLogger();
	
	String configDir;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configDir = event.getModConfigurationDirectory() + "/Soul Shards Reborn/";
		SoulConfig.init(new File(configDir + "Main.cfg"));
		TierHandling.init();
		ObjHandler.init();
		SoulEvents.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{	
		SoulLog.info("SSR: Mod Loaded!");
	}
	
	@EventHandler
	public void loadWorld(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandKillMobs());
		DynamicMobMapping.init(event.getServer().getEntityWorld());
		SSRCore.SoulLog.info("SSR: Mapped a total of " + DynamicMobMapping.entityList.size() + " entities.");
		MobBlackList.init(new File(configDir + "Entity Blacklist.cfg"));
		ssr.utils.Utils.hideItems();
	}
}
