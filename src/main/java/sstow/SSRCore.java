package sstow;

import sstow.commands.SSRCMD;
import sstow.events.AchievementEvents;
import sstow.events.CreateShardEvent;
import sstow.events.PlayerKillEntityEvent;
import sstow.events.SSRAchievement;
import sstow.gameObjs.ObjHandler;
import sstow.utils.EntityMapper;
import sstow.utils.SSRConfig;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SSRCore.MODID, name = SSRCore.NAME, version = SSRCore.VERSION, guiFactory = "sstow.utils.guiFactory")
public class SSRCore {

	public static final String MODID = "SSTOW";
	public static final String NAME = "Soul Shards: The Old Ways";
	public static final String VERSION = "RC-4B";
	
	@Instance(MODID)
	public static SSRCore modInstance;
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SSRConfig.load(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ObjHandler.registerObjs();

		SSRAchievement.Get();

		MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());
		MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
		FMLCommonHandler.instance().bus().register(new AchievementEvents());
		FMLInterModComms.sendMessage("Waila", "register",
				"sstow.utils.SSRWailaProvider.callbackRegister");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		EntityMapper.init();
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent eventt) {
		eventt.registerServerCommand(new SSRCMD());
	}
}
