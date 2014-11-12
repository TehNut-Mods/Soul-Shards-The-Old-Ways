package moze_intel.ssr;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import moze_intel.ssr.commands.KillCMD;
import moze_intel.ssr.events.CreateShardEvent;
import moze_intel.ssr.events.PlayerKillEntityEvent;
import moze_intel.ssr.events.ShardPickup;
import moze_intel.ssr.gameObjs.ObjHandler;
import moze_intel.ssr.utils.EntityMapper;
import moze_intel.ssr.utils.SSRConfig;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

@Mod(modid = SSRCore.ID, name = SSRCore.NAME, version = SSRCore.VERSION, guiFactory = "moze_intel.ssr.utils.guiFactory")
public class SSRCore {
	public static final String ID = "SSR";
	public static final String NAME = "Soul Shards Reborn";
	public static final String VERSION = "RC2.1";

	/*
	 * public static Achievement achievementCage;
	 * public static Achievement achievementShard;
	 * public static Achievement achievementTier1;
	 * public static Achievement achievementTier2;
	 * public static Achievement achievementTier3;
	 * public static Achievement achievementTier4;
	 * public static Achievement achievementTier5;
	 */
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		SSRConfig.load(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ObjHandler.registerObjs();
		/*
		 * achievementCage = new Achievement("achievement.createCage",
		 * "createCage", -2, 0, ObjHandler.SOUL_CAGE, (Achievement) null)
		 * .initIndependentStat().registerStat();
		 * 
		 * achievementShard = new Achievement("achievement.createShard",
		 * "createShard", 0, 0, ObjHandler.SOUL_SHARD, (Achievement) null)
		 * .initIndependentStat().registerStat();
		 * 
		 * achievementTier1 = new Achievement("achievement.tier1Shard",
		 * "tier1Shard", 2, 0, new ItemStack(ObjHandler.SOUL_SHARD),
		 * achievementShard).initIndependentStat().registerStat();
		 * 
		 * achievementTier2 = new Achievement("achievement.tier2Shard",
		 * "tier2Shard", 4, 0, ObjHandler.SOUL_SHARD, achievementTier1)
		 * .initIndependentStat().registerStat();
		 * 
		 * achievementTier3 = new Achievement("achievement.tier3Shard",
		 * "tier3Shard", 6, 0, ObjHandler.SOUL_SHARD, achievementTier2)
		 * .initIndependentStat().registerStat();
		 * 
		 * achievementTier4 = new Achievement("achievement.tier4Shard",
		 * "tier4Shard", 8, 0, ObjHandler.SOUL_SHARD, achievementTier3)
		 * .initIndependentStat().registerStat();
		 * 
		 * achievementTier5 = new Achievement("achievement.tier5Shard",
		 * "tier5Shard", 10, 0, ObjHandler.SOUL_SHARD, achievementTier4)
		 * .initIndependentStat().registerStat();
		 * 
		 * AchievementPage .registerAchievementPage(new AchievementPage(
		 * "Soul Shards: Reborn", new Achievement[] { achievementCage,
		 * achievementShard, achievementTier1, achievementTier2,
		 * achievementTier3, achievementTier4, achievementTier5 }));
		 * 
		 * MinecraftForge.EVENT_BUS.register(new ShardPickup());
		 */

		MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());
		MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		EntityMapper.init();
	}

	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent eventt) {
		eventt.registerServerCommand(new KillCMD());
	}
}
