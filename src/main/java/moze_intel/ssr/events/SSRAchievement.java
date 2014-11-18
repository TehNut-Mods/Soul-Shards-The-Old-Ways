package moze_intel.ssr.events;

import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class SSRAchievement {
	public static Achievement achievementCage;
	public static Achievement achievementShard;
	public static Achievement achievementTier1;
	public static Achievement achievementTier2;
	public static Achievement achievementTier3;
	public static Achievement achievementTier4;
	public static Achievement achievementTier5;

	public static void Get() {
		achievementCage = new Achievement("achievement.createCage",
				"createCage", -2, 0, ObjHandler.SOUL_CAGE, (Achievement) null)
				.initIndependentStat().registerStat();

		achievementShard = new Achievement("achievement.createShard",
				"createShard", 0, 0, ObjHandler.SOUL_SHARD, (Achievement) null)
				.initIndependentStat().registerStat();

		achievementTier1 = new Achievement("achievement.tier1Shard",
				"tier1Shard", 2, 0, new ItemStack(ObjHandler.SOUL_SHARD, 1),
				achievementShard).registerStat();

		achievementTier2 = new Achievement("achievement.tier2Shard",
				"tier2Shard", 4, 0, ObjHandler.SOUL_SHARD, achievementTier1)
				.registerStat();

		achievementTier3 = new Achievement("achievement.tier3Shard",
				"tier3Shard", 6, 0, ObjHandler.SOUL_SHARD, achievementTier2)
				.registerStat();

		achievementTier4 = new Achievement("achievement.tier4Shard",
				"tier4Shard", 8, 0, ObjHandler.SOUL_SHARD, achievementTier3)
				.registerStat();

		achievementTier5 = new Achievement("achievement.tier5Shard",
				"tier5Shard", 10, 0, ObjHandler.SOUL_SHARD, achievementTier4)
				.registerStat();

		AchievementPage
				.registerAchievementPage(new AchievementPage(
						"Soul Shards: Reborn", new Achievement[] {
								achievementCage, achievementShard,
								achievementTier1, achievementTier2,
								achievementTier3, achievementTier4,
								achievementTier5 }));
	}
}
