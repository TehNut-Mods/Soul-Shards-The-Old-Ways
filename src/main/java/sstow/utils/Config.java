package sstow.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sstow.Main;
import sstow.gameObjs.ObjHandler;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class Config {

	//Enchant Section
	public static int ENCHANT_ID;
	public static int ENCHANT_WEIGHT;
	public static int ENCHANT_KILL_BONUS;
	//General Section
	public static int SPAWNER_ABSORB_BONUS;
	public static int MAX_NUM_ENTITIES;
	public static boolean ALLOW_SPAWNER_ABSORB;
	public static boolean INVERT_REDSTONE;
	public static boolean ENABLE_FLOOD_PREVENTION;
	public static boolean ENABLE_DEBUG;
	public static boolean EASYMODE;
	//Recipes Section
	public static int COOK_TIME;
	public static int SHARDS;
	public static int NUGGETS;

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

	public static final Section sectionEnchant = new Section("Soul Stealer Enchant", "Soul Stealer Enchant");
	public static final Section sectionGeneral = new Section("General", "General");
	public static final Section sectionRecipes = new Section("Recipes","Recipes");
	public static final Section sectionTier1 = new Section("Tier 1 Settings", "Tier 1 Settings");
	public static final Section sectionTier2 = new Section("Tier 2 Settings", "Tier 2 Settings");
	public static final Section sectionTier3 = new Section("Tier 3 Settings", "Tier 3 Settings");
	public static final Section sectionTier4 = new Section("Tier 4 Settings", "Tier 4 Settings");
	public static final Section sectionTier5 = new Section("Tier 5 Settings", "Tier 5 Settings");

	public static void load(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new Config());
		configDirectory = new File(event.getModConfigurationDirectory()
				+ "/Soul Shards The Old Ways/");
		if (!configDirectory.exists()) {
			configDirectory.mkdir();
		}
		File configFile = new File(configDirectory, Main.NAME.replace(":",
				"") + ".cfg");
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
			//Soul Stealer Section
			ENCHANT_ID = config.getInt("ID", "Soul Stealer Enchant", 52, 1, 128, "Soul-Stealer enchant id");
			ENCHANT_WEIGHT = config.getInt("Weight", "Soul Stealer Enchant", 8, 1, 10, "Soul-Stealer enchant probability");
			ENCHANT_KILL_BONUS = config.getInt("Kill Bonus", "Soul Stealer Enchant", 1, 1, 10, "Soul-Stealer kill bonus");
			//General Section
			SPAWNER_ABSORB_BONUS = config.getInt("Vanilla Spawner Bonus", "General", 64, 1, 400, "Amount of kills added to the shard when right-clicking a spawner");
			MAX_NUM_ENTITIES = config.getInt("Max Entities Spawned", "General", 80, 1, 200, "Max number of Entities soul cages can spawn in an area");
			ALLOW_SPAWNER_ABSORB = config.getBoolean("Vanilla Spawner Absorbing", "General", true, "Allow absorbing of vanilla spawners for a kill bonus");
			INVERT_REDSTONE = config.getBoolean("Invert Redstone", "General", false, "Active redstone stops a soul cage");
			ENABLE_FLOOD_PREVENTION = config.getBoolean("Flood Prevention", "General", true, "Soul cages will stop when too many entities have been spawned");
			ENABLE_DEBUG = config.getBoolean("Enable Debug", "General", false, "This will enable debug mode, where the console will inform you when a mob is spawned");
			EASYMODE = config.getBoolean("Enable Easy mode", "General", false, "RESTART REQUIRED: This will revert the shard creation to the structure method");
			//Recipes Section
			COOK_TIME = config.getInt("Cooking Time", "Recipes", 12800, 0, 999999, "Time (In Ticks) it takes to create Soulium and Soul Shards");
			SHARDS = config.getInt("Shard Amount", "Recipes", 3, 1, 8, "RESTART REQUIRED: How many Soul Shards do you want to get by smelting 1 diamond");
			NUGGETS = config.getInt("Nugget Amount", "Recipes", 8, 1, 9, "RESTART REQUIRED: How many Soulium Nuggets do you want to get by smelting 1 iron ingot");

			short[] minKills = new short[5];

			for (int i = 0; i < 5; i++) {
				minKills[i] = (short) config.getInt("Min kills", "Tier "
						+ (i + 1) + " settings", DEFAULT_MIN_KILLS[i], 1, 2048,
						"Minimum kills for the tier");

				TierHandler.setNumSpawns(i, (byte) config.getInt("Num Spawns",
						"Tier " + (i + 1) + " settings", DEFAULT_NUM_SPAWNS[i],
						1, 10, "Number of spawns per operation"));
				TierHandler.setSpawnDelay(i, (byte) config.getInt("Cooldown",
						"Tier " + (i + 1) + " settings",
						DEFAULT_SPAWN_DELAY[i], 1, 60,
						"Cooldown time for soul cages (in seconds)"));
				TierHandler.setPlayerChecks(i, config.getBoolean(
						"Check Player", "Tier " + (i + 1) + " settings",
						DEFAULT_NEEDS_PLAYER[i],
						"Needs a player nearby to spawn entities"));
				TierHandler.setLightChecks(i, config.getBoolean("Check Light",
						"Tier " + (i + 1) + " settings",
						DEFAULT_CHECKS_LIGHT[i],
						"Needs appropriate light to spawn entities"));
				TierHandler.setWorldChecks(i, config.getBoolean("Checks World",
						"Tier " + (i + 1) + " settings",
						DEFAULT_CHECKS_WORLD[i],
						"Needs appropriate world to spawn entities"));
				TierHandler.setRedstoneChecks(i, config.getBoolean(
						"Redstone control", "Tier " + (i + 1) + " settings",
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