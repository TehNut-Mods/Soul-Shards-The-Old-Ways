package moze_intel.ssr.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import moze_intel.ssr.SSRCore;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class SSRConfig {

	public static int ENCHANT_ID;
	public static int ENCHANT_WEIGHT;
	public static int ENCHANT_KILL_BONUS;
	public static int SPAWNER_ABSORB_BONUS;
	public static int MAX_NUM_ENTITIES;
	public static boolean ALLOW_SPAWNER_ABSORB;
	public static boolean INVERT_REDSTONE;
	public static boolean ENABLE_FLOOD_PREVENTION;
	public static boolean ENABLE_ENDSTONE_RECIPE;
	public static boolean ENABLE_DEBUG;
	public static boolean THEOLDWAYS;

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

	public static final Section sectionEnchant = new Section(
			"Soul Stealer Enchant", "Soul Stealer Enchant");
	public static final Section sectionMisc = new Section("Misc", "Misc");
	public static final Section sectionTier1 = new Section("Tier 1 Settings",
			"Tier 1 Settings");
	public static final Section sectionTier2 = new Section("Tier 2 Settings",
			"Tier 2 Settings");
	public static final Section sectionTier3 = new Section("Tier 3 Settings",
			"Tier 3 Settings");
	public static final Section sectionTier4 = new Section("Tier 4 Settings",
			"Tier 4 Settings");
	public static final Section sectionTier5 = new Section("Tier 5 Settings",
			"Tier 5 Settings");

	public static void load(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new SSRConfig());
		configDirectory = new File(event.getModConfigurationDirectory()
				+ "/Soul Shards Reborn/");
		if (!configDirectory.exists()) {
			configDirectory.mkdir();
		}
		File configFile = new File(configDirectory, SSRCore.NAME.replace(":",
				"") + ".cfg");
		config = new Configuration(configFile);
		syncConfig();
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.modID.equals(SSRCore.ID)) {
			SSRLogger.logInfo("Updating config...");
			syncConfig();
		}
	}

	public static void syncConfig() {
		try {
			ENCHANT_ID = config.getInt("ID", "Soul Stealer Enchant", 86, 1,
					128, "Soul-Stealer enchant id");
			ENCHANT_WEIGHT = config.getInt("Weight", "Soul Stealer Enchant", 8,
					1, 10, "Soul-Stealer enchant probability");
			ENCHANT_KILL_BONUS = config
					.getInt("Kill Bonus", "Soul Stealer Enchant", 1, 1, 10,
							"Soul-Stealer kill bonus");
			SPAWNER_ABSORB_BONUS = config
					.getInt("Vanilla Spawner Bonus", "Misc", 200, 1, 400,
							"Amount of kills added to the shard when right-clicking a spawner");
			ALLOW_SPAWNER_ABSORB = config.getBoolean(
					"Vanilla Spawner Absorbing", "Misc", true,
					"Allow absorbing of vanilla spawners for a kill bonus");
			INVERT_REDSTONE = config.getBoolean("Invert Redstone", "Misc",
					false, "Active redstone stops a soul cage");
			ENABLE_FLOOD_PREVENTION = config
					.getBoolean("Flood Prevention", "Misc", true,
							"Soul cages will stop when too many entities have been spawned");
			MAX_NUM_ENTITIES = config.getInt("Max Entities Spawned", "Misc",
					80, 1, 200,
					"Max number of Entities soul cages can spawn in an area");
			ENABLE_ENDSTONE_RECIPE = config.getBoolean("Enable Endstone Recipe", "Misc", false, "This will make Endstone craftable");
			ENABLE_DEBUG = config
					.getBoolean(
							"Enable Debug",
							"Misc",
							false,
							"This will enable debug mode, where the console will inform you when a mob is spawned");
			THEOLDWAYS = config
					.getBoolean("Enable The Old Ways", "Misc", false,
							"RESTART REQUIRED, This will enable the old ways of creating a soul shard");

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

			SSRLogger.logInfo("Loaded configuration file!");
		} catch (Exception e) {
			SSRLogger.logFatal("Failed to load configuration file!");
			e.printStackTrace();
		} finally {
			if (config.hasChanged()) {
				config.save();
			}

		}
	}
}