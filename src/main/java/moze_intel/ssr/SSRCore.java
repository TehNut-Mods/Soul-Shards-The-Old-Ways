package moze_intel.ssr;

import moze_intel.ssr.commands.SSRCMD;
import moze_intel.ssr.events.AchievementEvents;
import moze_intel.ssr.events.CreateShardEvent;
import moze_intel.ssr.events.PlayerKillEntityEvent;
import moze_intel.ssr.events.SSRAchievement;
import moze_intel.ssr.gameObjs.ObjHandler;
import moze_intel.ssr.utils.EntityMapper;
import moze_intel.ssr.utils.SSRConfig;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SSRCore.ID, name = SSRCore.NAME, version = SSRCore.VERSION, guiFactory = "moze_intel.ssr.utils.guiFactory")
public class SSRCore {

	public static final String ID = "SSR";
	public static final String NAME = "Soul Shards Reborn";
	public static final String VERSION = "RC3.1-C";

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
				"moze_intel.ssr.utils.SSRWailaProvider.callbackRegister");
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
