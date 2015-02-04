package sstow.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import sstow.Main;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class Config {

	// Enchant Section
	public static int ENCHANT_ID;
	public static int ENCHANT_WEIGHT;
	public static int ENCHANT_KILL_BONUS;
	// general Section
	public static int SPAWNER_ABSORB_BONUS;
	public static int MAX_NUM_ENTITIES;
	public static boolean ALLOW_SPAWNER_ABSORB;
	public static boolean INVERT_REDSTONE;
	public static boolean ENABLE_FLOOD_PREVENTION;
	public static boolean ENABLE_DEBUG;
	public static boolean EASYMODE;
	// recipes Section
	public static int COOK_TIME;
	public static int SHARDS;
	public static int NUGGETS;
	public static int INGOTS;

	public static final short[] DEFAULT_MIN_KILLS = { 64, 128, 256, 512, 1024 };
	private static final byte[] DEFAULT_NUM_SPAWNS = { 2, 4, 4, 4, 6 };
	private static final byte[] DEFAULT_SPAWN_DELAY = { 20, 10, 5, 5, 2 };
	private static final boolean[] DEFAULT_NEEDS_PLAYER = { true, true, false,
			false, false };
	private static final boolean[] DEFAULT_CHECKS_LIGHT = { true, true, true,
			true, false };
	private static final boolean[] DEFAULT_CHECKS_WORLD = { true, true, true,
			false, false };
	private static final boolean[] DEFAULT_CHECKS_REDSTONE = { false, false,
			false, false, true };

	public static Configuration config;

	public static File configDirectory;

	public static class Section {
		public final String name;

		public Section(String name, String lang) {
			this.name = name;
			register();
		}

		private void register() {
			sections.add(this);
		}

		public String lc() {
			return name.toLowerCase();
		}
	}

	public static final List<Section> sections;
	static {
		sections = new ArrayList<Section>();
	}

	public static final Section enchantment = new Section("enchantment",
			"enchantment");
	public static final Section general = new Section("general", "general");
	public static final Section recipes = new Section("recipes", "recipes");
	public static final Section tier1 = new Section("tier 1 settings",
			"tier 1 settings");
	public static final Section tier2 = new Section("tier 2 settings",
			"tier 2 settings");
	public static final Section tier3 = new Section("tier 3 settings",
			"tier 3 settings");
	public static final Section tier4 = new Section("tier 4 settings",
			"tier 4 settings");
	public static final Section tier5 = new Section("tier 5 settings",
			"tier 5 settings");

	public static void load(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new Config());
		configDirectory = new File(event.getModConfigurationDirectory()
				+ "/SSTOW/");
		if (!configDirectory.exists()) {
			configDirectory.mkdir();
		}
		File configFile = new File(configDirectory, "config.cfg");
		config = new Configuration(configFile);
		syncConfig();
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.modID.equals(Main.MODID)) {
			TOWLogger.logInfo("Updating config...");
			syncConfig();
		}
	}

	public static void syncConfig() {
		try {
			// Soul Stealer Section
			ENCHANT_ID = config.getInt("ID", "enchantment", 52, 1, 128,
					"Soul-Stealer enchant id");
			ENCHANT_WEIGHT = config.getInt("Weight", "enchantment", 8, 1, 10,
					"Soul-Stealer enchant probability");
			ENCHANT_KILL_BONUS = config.getInt("Kill Bonus", "enchantment", 1,
					1, 10, "Soul-Stealer kill bonus");
			// general Section
			SPAWNER_ABSORB_BONUS = config
					.getInt("Vanilla Spawner Bonus", "general", 64, 1, 400,
							"Amount of kills added to the shard when right-clicking a spawner");
			MAX_NUM_ENTITIES = config.getInt("Max Entities Spawned", "general",
					80, 1, 200,
					"Max number of Entities soul cages can spawn in an area");
			ALLOW_SPAWNER_ABSORB = config.getBoolean(
					"Vanilla Spawner Absorbing", "general", true,
					"Allow absorbing of vanilla spawners for a kill bonus");
			INVERT_REDSTONE = config.getBoolean("Invert Redstone", "general",
					false, "Active redstone stops a soul cage");
			ENABLE_FLOOD_PREVENTION = config
					.getBoolean("Flood Prevention", "general", true,
							"Soul cages will stop when too many entities have been spawned");
			ENABLE_DEBUG = config
					.getBoolean(
							"Enable Debug",
							"general",
							false,
							"This will enable debug mode, where the console will inform you when a mob is spawned");
			EASYMODE = config
					.getBoolean("Enable Easy mode", "general", false,
							"RESTART REQUIRED: This will revert the shard creation to the structure method");
			// recipes Section
			COOK_TIME = config
					.getInt("Cooking Time", "recipes", 12800, 0, 999999,
							"Time (In Ticks) it takes to create Soulium and Soul Shards");
			SHARDS = config
					.getInt("Shard Amount",
							"recipes",
							3,
							1,
							8,
							"RESTART REQUIRED: How many Soul Shards do you want to get by smelting 1 diamond");
			NUGGETS = config
					.getInt("Nugget Amount",
							"recipes",
							8,
							1,
							9,
							"RESTART REQUIRED: How many Soulium Nuggets do you want to get by smelting 1 iron ingot");
			INGOTS = config
					.getInt("Ingot Amount",
							"recipes",
							7,
							1,
							9,
							"RESTART REQUIRED: How many Soulium Ingots do you want to get by smelting 1 iron block");

			short[] minKills = new short[5];

			for (int i = 0; i < 5; i++) {
				minKills[i] = (short) config.getInt("Min kills", "tier "
						+ (i + 1) + " settings", DEFAULT_MIN_KILLS[i], 1, 2048,
						"Minimum kills for the tier");

				TierHandler.setNumSpawns(i, (byte) config.getInt("Num Spawns",
						"tier " + (i + 1) + " settings", DEFAULT_NUM_SPAWNS[i],
						1, 10, "Number of spawns per operation"));
				TierHandler.setSpawnDelay(i, (byte) config.getInt("Cooldown",
						"tier " + (i + 1) + " settings",
						DEFAULT_SPAWN_DELAY[i], 1, 60,
						"Cooldown time for soul cages (in seconds)"));
				TierHandler.setPlayerChecks(i, config.getBoolean(
						"Check Player", "tier " + (i + 1) + " settings",
						DEFAULT_NEEDS_PLAYER[i],
						"Needs a player nearby to spawn entities"));
				TierHandler.setLightChecks(i, config.getBoolean("Check Light",
						"tier " + (i + 1) + " settings",
						DEFAULT_CHECKS_LIGHT[i],
						"Needs appropriate light to spawn entities"));
				TierHandler.setWorldChecks(i, config.getBoolean("Checks World",
						"tier " + (i + 1) + " settings",
						DEFAULT_CHECKS_WORLD[i],
						"Needs appropriate world to spawn entities"));
				TierHandler.setRedstoneChecks(i, config.getBoolean(
						"Redstone control", "tier " + (i + 1) + " settings",
						DEFAULT_CHECKS_REDSTONE[i],
						"Reacts to a redstone signal"));
			}

			TierHandler.setTierReqKills(minKills);

			TOWLogger.logInfo("Loaded configuration file!");
		} catch (Exception e) {
			TOWLogger.logFatal("Failed to load configuration file!");
			e.printStackTrace();
		} finally {
			if (config.hasChanged()) {
				config.save();
			}

		}
	}
}
