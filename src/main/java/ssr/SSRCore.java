package ssr;

import static ssr.SSRCore.MODID;
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

@Mod(modid = SSRCore.MODID, name = SSRCore.MOD_NAME, version = SSRCore.MOD_VERSION, guiFactory = "ssr.guiFactory")
public class SSRCore {
	@Instance("SSR")
	public static SSRCore instance;
	public static final String MODID = "SSR";
	public static final String MOD_NAME = "Soul Shards: Reborn";
	public static final String MOD_VERSION = "Alpha 0.9e";
	public static Logger SoulLog = FMLLog.getLogger();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SoulConfig.load(event);
		TierHandling.init();
		ObjHandler.init();
		SoulEvents.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		SoulLog.info("SSR: Mod Loaded!");
	}

	@EventHandler
	public void loadWorld(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandKillMobs());
		DynamicMobMapping.init(event.getServer().getEntityWorld());
		SSRCore.SoulLog.info("SSR: Mapped a total of "
				+ DynamicMobMapping.entityList.size() + " entities.");
		MobBlackList.init(new File(SoulConfig.configDirectory
				+ "/Entity Blacklist.cfg"));
		ssr.utils.Utils.hideItems();
	}
}