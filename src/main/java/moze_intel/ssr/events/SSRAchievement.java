package moze_intel.ssr.events;

import moze_intel.ssr.gameObjs.ObjHandler;
import moze_intel.ssr.utils.SSRConfig;
import moze_intel.ssr.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class SSRAchievement {
	public static Achievement achievementCage;
	public static Achievement theoldWays;
	public static Achievement corruption;
	public static Achievement soulForge;
	public static Achievement achievementShard;
	public static Achievement achievementTier1;
	public static Achievement achievementTier2;
	public static Achievement achievementTier3;
	public static Achievement achievementTier4;
	public static Achievement achievementTier5;

	public static void Get() {

		ItemStack shard1 = new ItemStack(ObjHandler.SOUL_SHARD);
		Utils.setShardTier(shard1, (byte) 1);
		Utils.setShardBoundEnt(shard1, "NULL");
		ItemStack shard2 = new ItemStack(ObjHandler.SOUL_SHARD);
		Utils.setShardTier(shard2, (byte) 2);
		Utils.setShardBoundEnt(shard2, "NULL");
		ItemStack shard3 = new ItemStack(ObjHandler.SOUL_SHARD);
		Utils.setShardTier(shard3, (byte) 3);
		Utils.setShardBoundEnt(shard3, "NULL");
		ItemStack shard4 = new ItemStack(ObjHandler.SOUL_SHARD);
		Utils.setShardTier(shard4, (byte) 4);
		Utils.setShardBoundEnt(shard4, "NULL");
		ItemStack shard5 = new ItemStack(ObjHandler.SOUL_SHARD);
		Utils.setShardTier(shard5, (byte) 5);
		Utils.setShardBoundEnt(shard5, "NULL");

		if (!SSRConfig.THEOLDWAYS) {

			achievementCage = new Achievement("achievement.createCage",
					"createCage", -2, 0, ObjHandler.SOUL_CAGE,
					(Achievement) null).initIndependentStat().registerStat();

			achievementShard = new Achievement("achievement.createShard",
					"createShard", 0, 0,
					new ItemStack(ObjHandler.SOUL_SHARD, 0), (Achievement) null)
					.initIndependentStat().registerStat();

			achievementTier1 = new Achievement("achievement.tier1Shard",
					"tier1Shard", 2, 0, shard1, achievementShard)
					.registerStat();

			achievementTier2 = new Achievement("achievement.tier2Shard",
					"tier2Shard", 4, 0, shard2, achievementTier1)
					.registerStat();

			achievementTier3 = new Achievement("achievement.tier3Shard",
					"tier3Shard", 6, 0, shard3, achievementTier2)
					.registerStat();

			achievementTier4 = new Achievement("achievement.tier4Shard",
					"tier4Shard", 8, 0, shard4, achievementTier3)
					.registerStat();

			achievementTier5 = new Achievement("achievement.tier5Shard",
					"tier5Shard", 10, 0, shard5, achievementTier4).setSpecial()
					.registerStat();

			AchievementPage.registerAchievementPage(new AchievementPage(
					"Soul Shards: Reborn", new Achievement[] { achievementCage,
							achievementShard, achievementTier1,
							achievementTier2, achievementTier3,
							achievementTier4, achievementTier5 }));

		} else if (SSRConfig.THEOLDWAYS) {

			achievementCage = new Achievement("achievement.createCage",
					"createCage", -2, 0, ObjHandler.SOUL_CAGE,
					(Achievement) null).initIndependentStat().registerStat();

			theoldWays = new Achievement("achievement.vile_dust", "vile_dust",
					0, 0, ObjHandler.VILE_DUST, (Achievement) null)
					.initIndependentStat().registerStat();

			corruption = new Achievement("achievement.corrupted_essence",
					"corrupted_essence", 2, 1, ObjHandler.CORRUPTED_ESSENCE,
					SSRAchievement.theoldWays).registerStat();

			soulForge = new Achievement("achievement.soulForge", "soulForge",
					4, -1, ObjHandler.SOUL_FORGE, SSRAchievement.corruption)
					.registerStat();

			achievementShard = new Achievement("achievement.createShard",
					"createShard", 4, 2,
					new ItemStack(ObjHandler.SOUL_SHARD, 0),
					SSRAchievement.soulForge).registerStat();

			achievementTier1 = new Achievement("achievement.tier1Shard",
					"tier1Shard", 3, 4, shard1, achievementShard)
					.registerStat();

			achievementTier2 = new Achievement("achievement.tier2Shard",
					"tier2Shard", 1, 4, shard2, achievementTier1)
					.registerStat();

			achievementTier3 = new Achievement("achievement.tier3Shard",
					"tier3Shard", -1, 4, shard3, achievementTier2)
					.registerStat();

			achievementTier4 = new Achievement("achievement.tier4Shard",
					"tier4Shard", -3, 4, shard4, achievementTier3)
					.registerStat();

			achievementTier5 = new Achievement("achievement.tier5Shard",
					"tier5Shard", -5, 4, shard5, achievementTier4).setSpecial()
					.registerStat();

			AchievementPage.registerAchievementPage(new AchievementPage(
					"Soul Shards: Reborn", new Achievement[] { achievementCage,
							achievementShard, achievementTier1,
							achievementTier2, achievementTier3,
							achievementTier4, achievementTier5, theoldWays,
							soulForge, corruption }));
		}

	}
}
