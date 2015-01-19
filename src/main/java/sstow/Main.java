package sstow;

import net.minecraftforge.common.MinecraftForge;
import sstow.commands.SSTOWCMD;
import sstow.events.AchievementEvents;
import sstow.events.Achievements;
import sstow.events.CreateShardEvent;
import sstow.events.EntityDeathEvent;
import sstow.events.PlayerKillEntityEvent;
import sstow.gameObjs.ObjHandler;
import sstow.utils.Config;
import sstow.utils.EntityMapper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, guiFactory = "sstow.utils.guiFactory")
public class Main {

	public static final String MODID = "SSTOW";
	public static final String NAME = "Soul Shards: The Old Ways";
	public static final String VERSION = "RC6-B";

	@Instance(MODID)
	public static Main modInstance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ObjHandler.registerObjs();

		Achievements.Get();

		MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());
		MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
		//MinecraftForge.EVENT_BUS.register(new EntityDeathEvent());
		FMLCommonHandler.instance().bus().register(new AchievementEvents());
		FMLInterModComms.sendMessage("Waila", "register",
				"sstow.utils.WailaProvider.callbackRegister");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		EntityMapper.init();
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent eventt) {
		eventt.registerServerCommand(new SSTOWCMD());
	}
}
