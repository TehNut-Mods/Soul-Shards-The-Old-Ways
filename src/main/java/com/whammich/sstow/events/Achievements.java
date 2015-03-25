package com.whammich.sstow.events;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import cpw.mods.fml.common.Loader;

public class Achievements {
	public static Achievement soulcage;
	public static Achievement viledust;
	public static Achievement corruption;
	public static Achievement soulforge;
	public static Achievement unboundshard;
	public static Achievement shardt1;
	public static Achievement shardt2;
	public static Achievement shardt3;
	public static Achievement shardt4;
	public static Achievement shardt5;

	public static Achievement fixed;

	public static void defaultAchievements() {

		ItemStack shard1 = new ItemStack(Register.ItemShardSoul);
		Utils.setShardTier(shard1, (byte) 1);
		Utils.setShardBoundEnt(shard1, "NULL");

		ItemStack shard2 = new ItemStack(Register.ItemShardSoul);
		Utils.setShardTier(shard2, (byte) 2);
		Utils.setShardBoundEnt(shard2, "NULL");

		ItemStack shard3 = new ItemStack(Register.ItemShardSoul);
		Utils.setShardTier(shard3, (byte) 3);
		Utils.setShardBoundEnt(shard3, "NULL");

		ItemStack shard4 = new ItemStack(Register.ItemShardSoul);
		Utils.setShardTier(shard4, (byte) 4);
		Utils.setShardBoundEnt(shard4, "NULL");

		ItemStack shard5 = new ItemStack(Register.ItemShardSoul);
		Utils.setShardTier(shard5, (byte) 5);
		Utils.setShardBoundEnt(shard5, "NULL");

		viledust = new Achievement("achievement.vile_dust", "vile_dust", -1,
				-3, new ItemStack(Register.ItemMaterials, 1, 3), (Achievement) null)
				.initIndependentStat().registerStat();

		corruption = new Achievement("achievement.corrupted_essence",
				"corrupted_essence", -1, -1, new ItemStack(Register.ItemMaterials, 1, 4),
				Achievements.viledust).registerStat();

		soulforge = new Achievement("achievement.soulForge", "soulForge", -1,
				1, Register.BlockForge, Achievements.corruption)
				.registerStat();

		soulcage = new Achievement("achievement.createCage", "createCage", 0,
				0, Register.BlockCage, Achievements.soulforge).registerStat();
		if (!Config.EASYMODE) {
			unboundshard = new Achievement("achievement.createShard",
					"createShard", 4, 1,
					new ItemStack(Register.ItemShardSoul, 0),
					Achievements.soulforge).registerStat();
		} else {
			unboundshard = new Achievement("achievement.createShard",
					"createShard", 4, 1,
					new ItemStack(Register.ItemShardSoul, 0), (Achievement) null)
					.initIndependentStat().registerStat();
		}
		shardt1 = new Achievement("achievement.tier1Shard", "tier1Shard", 2,
				-2, shard1, unboundshard).registerStat();

		shardt2 = new Achievement("achievement.tier2Shard", "tier2Shard", 6,
				-2, shard2, shardt1).registerStat();

		shardt3 = new Achievement("achievement.tier3Shard", "tier3Shard", 7, 2,
				shard3, shardt2).registerStat();

		shardt4 = new Achievement("achievement.tier4Shard", "tier4Shard", 4, 4,
				shard4, shardt3).registerStat();

		shardt5 = new Achievement("achievement.tier5Shard", "tier5Shard", 1, 2,
				shard5, shardt4).setSpecial().registerStat();

	}

	public static void MFRAchievements() {
		// System.out.println("Registering MFR Achievement");

		fixed = new Achievement("achievement.fixed", "fixed", 10, 10,
				Register.ItemFixedDummy, (Achievement) null).initIndependentStat()
				.registerStat();

		// System.out.println("Registering MFR Achievement Pages");

		AchievementPage.registerAchievementPage(new AchievementPage(
				"Soul Shards: The Old Ways", new Achievement[] { soulcage,
						unboundshard, shardt1, shardt2, shardt3, shardt4,
						shardt5, viledust, soulforge, corruption, fixed }));
	}

	public static void defaultAchievementPages() {
		// System.out.println("Registering Achievement Page");

		AchievementPage.registerAchievementPage(new AchievementPage(
				"Soul Shards TOW", new Achievement[] { soulcage, unboundshard,
						shardt1, shardt2, shardt3, shardt4, shardt5, viledust,
						corruption, soulforge }));
	}

	public static void Get() {

		defaultAchievements();

		if (Loader.isModLoaded("MineFactoryReloaded")) {

			MFRAchievements();

		} else {

			defaultAchievementPages();

		}
	}
}
