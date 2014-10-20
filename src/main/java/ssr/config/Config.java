package ssr.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import ssr.SSRCore;

public class Config {
	// public static boolean autoID;
	public static boolean disallowMobs;
	public static boolean canAbsorbSpawners;
	public static boolean easyVanillaAbsorb;
	public static boolean requireOwnerOnline;
	public static boolean exceedMaxNumSpawns;
	public static boolean enableEndStoneRecipe;
	public static boolean enableDebug;
	public static int vanillaBonus;
	public static int soulStealerID;
	public static int soulStealerWeight;
	public static int maxNumSpawns;
	public static int coolDown[] = new int[5];
	public static int numMobs[] = new int[5];
	public static int killReq[] = new int[5];
	public static boolean[] enableRS = new boolean[5];
	public static boolean[] needPlayer = new boolean[5];
	public static boolean[] checkLight = new boolean[5];
	public static boolean[] otherWorlds = new boolean[5];

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

	public static final Section sectionEnchant = new Section("Enchantments",
			"Enchantment IDs");
	public static final Section sectionMisc = new Section("General Settings",
			"General Settings");
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
		FMLCommonHandler.instance().bus().register(new Config());
		configDirectory = new File(event.getModConfigurationDirectory()
				+ "/Soul Shards Reborn/");
		if (!configDirectory.exists()) {
			configDirectory.mkdir();
		}
		File configFile = new File(configDirectory, SSRCore.MOD_NAME.replace(":", "") + ".cfg");
		config = new Configuration(configFile);
		syncConfig();
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.modID.equals(SSRCore.MODID)) {
			SSRCore.SoulLog.info("Updating config...");
			syncConfig();
		}
	}

	public static void syncConfig() {
		try {

			soulStealerID = config.get("Enchantments",
					"Soul Stealer enchant ID", 95).getInt();
			soulStealerWeight = config.get("Enchantments",
					"The probability of the Soul Stealer enchant", 5).getInt(5);

			disallowMobs = config.get("General Settings",
					"Peaceful Mobs only?", false).getBoolean(false);
			canAbsorbSpawners = config.get("General Settings",
					"Absorb Spawners?", true).getBoolean(true);
			easyVanillaAbsorb = config.get("General Settings",
					"Absorb any spawner?", false).getBoolean(false);
			requireOwnerOnline = config.get("General Settings",
					"Player online?", false).getBoolean(false);
			vanillaBonus = config.get("General Settings", "Spawner Bonus", 200)
					.getInt(200);
			exceedMaxNumSpawns = config.get("General Settings",
					"Spawn over maximum?", false).getBoolean(false);
			maxNumSpawns = config.get("General Settings",
					"Maximum Alive Spawns?", 80).getInt(80);
			enableEndStoneRecipe = config.get("General Settings",
					"Enable End Stone Recipe", false).getBoolean(false);
			enableDebug = config.get("General Settings",
					"Enable Debug Logging", false).getBoolean(false);

			coolDown[0] = config.get("Tier 1 Settings",
					"Cool-down (in seconds)", 20).getInt(20);
			numMobs[0] = config.get("Tier 1 Settings",
					"Number of mobs to spawn", 2).getInt(2);
			enableRS[0] = config.get("Tier 1 Settings", "Redstone?", false)
					.getBoolean(false);
			needPlayer[0] = config.get("Tier 1 Settings",
					"Require Player in range?", true).getBoolean(true);
			checkLight[0] = config.get("Tier 1 Settings",
					"Correct Light Levels?", true).getBoolean(true);
			otherWorlds[0] = config.get("Tier 1 Settings",
					"Correct dimension?", true).getBoolean(true);
			killReq[0] = config.get("Tier 1 Settings", "Klls required", 64)
					.getInt(64);

			coolDown[1] = config.get("Tier 2 Settings",
					"Cool-down (in seconds)", 10).getInt(10);
			numMobs[1] = config.get("Tier 2 Settings",
					"Number of mobs to spawn", 4).getInt(4);
			enableRS[1] = config.get("Tier 2 Settings", "Redstone?", false)
					.getBoolean(false);
			needPlayer[1] = config.get("Tier 2 Settings",
					"Require Player in range?", true).getBoolean(true);
			checkLight[1] = config.get("Tier 2 Settings",
					"Correct Light Levels?", true).getBoolean(true);
			otherWorlds[1] = config.get("Tier 2 Settings",
					"Correct dimension?", true).getBoolean(true);
			killReq[1] = config.get("Tier 2 Settings",
					"Klls required (Greater than Tier 1)", 128).getInt(128);

			coolDown[2] = config.get("Tier 3 Settings",
					"Cool-down (in seconds)", 5).getInt(5);
			numMobs[2] = config.get("Tier 3 Settings",
					"Number of mobs to spawn", 4).getInt(4);
			enableRS[2] = config.get("Tier 3 Settings", "Redstone?", false)
					.getBoolean(false);
			needPlayer[2] = config.get("Tier 3 Settings",
					"Require Player in range?", false).getBoolean(false);
			checkLight[2] = config.get("Tier 3 Settings",
					"Correct Light Levels?", false).getBoolean(false);
			otherWorlds[2] = config.get("Tier 3 Settings",
					"Correct dimension?", true).getBoolean(true);
			killReq[2] = config.get("Tier 3 Settings",
					"Klls required (Greater than Tier 2)", 256).getInt(256);

			coolDown[3] = config.get("Tier 4 Settings",
					"Cool-down (in seconds)", 5).getInt(5);
			numMobs[3] = config.get("Tier 4 Settings",
					"Number of mobs to spawn", 4).getInt(4);
			enableRS[3] = config.get("Tier 4 Settings", "Redstone?", false)
					.getBoolean(false);
			needPlayer[3] = config.get("Tier 4 Settings",
					"Require Player in range?", false).getBoolean(false);
			checkLight[3] = config.get("Tier 4 Settings",
					"Correct Light Levels?", true).getBoolean(true);
			otherWorlds[3] = config.get("Tier 4 Settings",
					"Correct dimension?", false).getBoolean(false);
			killReq[3] = config.get("Tier 4 Settings",
					"Klls required (Greater than Tier 3)", 512).getInt(512);

			coolDown[4] = config.get("Tier 5 Settings",
					"Cool-down (in seconds)", 2).getInt(2);
			numMobs[4] = config.get("Tier 5 Settings",
					"Number of mobs to spawn", 6).getInt(6);
			enableRS[4] = config.get("Tier 5 Settings", "Redstone?", true)
					.getBoolean(true);
			needPlayer[4] = config.get("Tier 5 Settings",
					"Require Player in range?", false).getBoolean(false);
			checkLight[4] = config.get("Tier 5 Settings",
					"Correct Light Levels?", false).getBoolean(false);
			otherWorlds[4] = config.get("Tier 5 Settings",
					"Correct dimension?", false).getBoolean(false);
			killReq[4] = config.get("Tier 5 Settings",
					"Klls required (Greater than Tier 4)", 1024).getInt(1024);

			SSRCore.SoulLog.info("SSR: Loaded Main configuration file.");

		} catch (Exception e) {
			SSRCore.SoulLog
					.warn("Soul-Shards Reborn had a problem loading it's configuration files.");
			e.printStackTrace();
		} finally {
			if (config.hasChanged()) {
				config.save();
			}
		}
	}
}
