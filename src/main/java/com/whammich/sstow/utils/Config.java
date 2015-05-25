package com.whammich.sstow.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class Config {

	// Config Wall
	public static boolean oldWaysOption;

	// Experience Points
	public static float fakePlayerXP;
	public static float playerXP;

	// Animus Section
	public static int crystalHeal;
	public static boolean crystalRegenEnable;
	public static int crystalRegenTimer;
	public static int crystalRegenLevel;
	public static boolean crystalResistEnable;
	public static int crystalResistTimer;
	public static int immundusReturn;
	public static boolean hurtOnBind;

	// Enchant Section
	public static int enchantID;
	public static int enchantWeight;
	public static int enchantBonus;

	// General Section
	public static int spawnerBonus;
	public static boolean allowAbsorb;
	public static boolean bindingAbsorb;
	public static boolean debug;
	public static boolean ritual;
	public static boolean personalShard;

	// Soul Cage
	public static int maxEntities;
	public static boolean invertRedstone;
	public static boolean floodPrevention;
	
	// Cage Module Section
	public static boolean redstoneModule;
	public static boolean lightModule;
	public static boolean dimensionModule;
	public static boolean playerModule;

	// Recipes Section
	public static int cookingModifier;
	public static int nuggetsReturn;
	public static int ingotsReturn;

	// World Generation
	public static boolean petrifiedForest;
	
	public static final short[] defaultMinKills = { 64, 128, 256, 512, 1024 };
	private static final byte[] defaultSpawns = { 2, 4, 4, 4, 6 };
	private static final byte[] defaultDelay = { 20, 10, 5, 5, 2 };
	private static final boolean[] defaultPlayer = { true, true, false, false, false };
	private static final boolean[] defaultLight = { true, true, true, true, false };
	private static final boolean[] defaultWorld = { true, true, true, false, false };
	private static final boolean[] defaultRedstone = { false, false, false, false, true };

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

	public static final Section enchantment = new Section("enchantment", "enchantment");
	public static final Section general = new Section("general", "general");
	public static final Section cages = new Section("soul cages", "soul cages");
	public static final Section modules = new Section("cage modules", "cage modules");
	public static final Section animus = new Section("animus crystal","animus crystal");
	public static final Section recipes = new Section("recipes", "recipes");
	public static final Section tier1 = new Section("tier 1 settings", "tier 1 settings");
	public static final Section tier2 = new Section("tier 2 settings", "tier 2 settings");
	public static final Section tier3 = new Section("tier 3 settings", "tier 3 settings");
	public static final Section tier4 = new Section("tier 4 settings", "tier 4 settings");
	public static final Section tier5 = new Section("tier 5 settings", "tier 5 settings");

	public static int enchantmentSoulStealingId;

	public static void load(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new Config());
		configDirectory = new File(event.getModConfigurationDirectory() + "/Whammich/");
		if (!configDirectory.exists()) {
			configDirectory.mkdir();
		}
		File configFile = new File(configDirectory, "Soul-Shards-TOW.cfg");
		config = new Configuration(configFile);
		syncConfig();
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if (event.modID.equals(Reference.modID)) {
			ModLogger.logInfo(Utils.localize("chat.sstow.util.configupdate"));
			syncConfig();
		}
	}


	public static void syncConfig() {
		try {

			// Config Wall
			oldWaysOption = config.getBoolean("Disable new content?", "general", false, "RESTART REQUIRED: Setting to true will disable all new content and returns functionality to the original old ways");

			if(!Config.oldWaysOption) {
				// Insta-Heal
				crystalHeal = config.getInt("Animus Healing", "animus crystal", 10, 1, 20, "How much should the animus crystal heal you?");
				
				// Regen Effects
				crystalRegenEnable = config.getBoolean("Enable Animus Regen", "animus crystal", true, "Will the animus crystal regen you on death?");
				crystalRegenTimer = config.getInt("Animus Regen Timer", "animus crystal", 600, 1, 2400, "How long should the regen effect last?");
				crystalRegenLevel = config.getInt("Animus Regen Level", "animus crystal", 0, 0, 2, "What level should the regen effect be?");

				// Resistence Effects
				crystalResistEnable = config.getBoolean("Enable Animus Resistence", "animus crystal", true, "Will the animus crystal briefly make you immortal?");
				crystalResistTimer = config.getInt("Animus Resistence Timer", "animus crystal", 100, 20, 400, "How long should the resistence effect last?");

				// Amulet Binding
				hurtOnBind = config.getBoolean("Hurt player when equpping the Animus", "animus crystal", true, "If True, take damage upon equipping the Animus");
			}
			
			// XP Section
			fakePlayerXP = config.getFloat("Mob Farm XP", "general", 1.0F, 0F, 3.0F, "Set the XP amount for xp farms");
			playerXP = config.getFloat("Player Kill XP", "general", 1.0F, 0F, 3.0F, "Set the XP amount for player kills");

			// Soul Stealer Section
			enchantID = config.getInt("ID", "enchantment", 52, 1, 128, "Soul-Stealer enchant id");
			enchantWeight = config.getInt("Weight", "enchantment", 8, 1, 10, "Soul-Stealer enchant probability");
			enchantBonus = config.getInt("Kill Bonus", "enchantment", 1, 1, 10, "Soul-Stealer kill bonus");

			// Soul Cage Section
			maxEntities = config.getInt("Max Entities Spawned", "soul cages", 80, 1, 200, "Max number of Entities soul cages can spawn in an area");
			invertRedstone = config.getBoolean("Invert Redstone", "soul cages", false, "Active redstone stops a soul cage");
			floodPrevention = config.getBoolean("Flood Prevention", "soul cages", true, "Soul cages will stop when too many entities have been spawned");
			
			if(!Config.oldWaysOption) {
				// Soul Cage Module Section
				redstoneModule = config.getBoolean("Enable Redstone Module", "cage modules", true, "Enable the Redstone Module?");
				lightModule = config.getBoolean("Enable Light Module", "cage modules", true, "Enable the Light Module?");
				dimensionModule = config.getBoolean("Enable Dimension Module", "cage modules", true, "Enable the Dimension Module?");
				playerModule = config.getBoolean("Enable Player Module","cage modules", false, "Enable the Player Module?");
			}
			
			// General Section
			spawnerBonus = config.getInt("Vanilla Spawner Bonus", "general", 64, 1, 400, "Amount of kills added to the shard when right-clicking a spawner");
			bindingAbsorb = config.getBoolean("Bind Shard", "general", false, "Bind an unbound shard when right-clicking a mob spawner?");
			allowAbsorb = config.getBoolean("Vanilla Spawner Absorbing", "general", true, "Allow absorbing of vanilla spawners for a kill bonus");
			debug = config.getBoolean("Enable Debug", "general", false, "This will enable debug mode, where the console will inform you when a mob is spawned");
			personalShard = config.getBoolean("Personal shards", "general", false, "The soulcage will only function if the original shard creator is nearby, not just anyone.");

			// Recipes Section
			cookingModifier = config.getInt("Cooking Time Modifier", "recipes", 10, 0, 30, "Modify the speed of the soul forge, higher the number the faster it smelts");
			immundusReturn = config.getInt("Immundus Crystal Amount", "recipes", 1, 1, 8, "RESTART REQUIRED: How many Immundus Crystals do you want to get by smelting 1 diamond");
			nuggetsReturn = config.getInt("Nugget Amount", "recipes", 8, 1, 9, "RESTART REQUIRED: How many Soulium Nuggets do you want to get by smelting 1 iron ingot");
			ingotsReturn = config.getInt("Ingot Amount", "recipes", 7, 1, 9, "RESTART REQUIRED: How many Soulium Ingots do you want to get by smelting 1 iron block");

			if(!Config.oldWaysOption) {
				petrifiedForest = config.getBoolean("Enable Petrified Forest Biome", "general", true, "Do you want to generate the petrified forest biome?");
			}
			
			short[] minKills = new short[5];

			for (int i = 0; i < 5; i++) {
				minKills[i] = (short) config.getInt("Min kills", "tier " + (i + 1) + " settings", defaultMinKills[i], 1, 8096, "Minimum kills for the tier");

				TierHandler.setNumSpawns(i, (byte) config.getInt("Num Spawns", "tier " + (i + 1) + " settings", defaultSpawns[i], 1, 10, "Number of spawns per operation"));
				TierHandler.setSpawnDelay(i, (byte) config.getInt("Cooldown", "tier " + (i + 1) + " settings", defaultDelay[i], 1, 60, "Cooldown time for soul cages (in seconds)"));
				TierHandler.setPlayerChecks(i, config.getBoolean("Check Player", "tier " + (i + 1) + " settings", defaultPlayer[i], "Needs a player nearby to spawn entities"));
				TierHandler.setLightChecks(i, config.getBoolean("Check Light", "tier " + (i + 1) + " settings", defaultLight[i], "Needs appropriate light to spawn entities"));
				TierHandler.setWorldChecks(i, config.getBoolean("Checks World", "tier " + (i + 1) + " settings", defaultWorld[i], "Needs appropriate world to spawn entities"));
				TierHandler.setRedstoneChecks(i, config.getBoolean("Redstone control", "tier " + (i + 1) + " settings", defaultRedstone[i], "Reacts to a redstone signal"));
			}

			TierHandler.setTierReqKills(minKills);

			ModLogger.logInfo(Utils.localize("chat.sstow.util.configload"));
		} catch (Exception e) {
			ModLogger.logFatal(Utils.localize("chat.sstow.util.configloadfail"));
			e.printStackTrace();
		} finally {
			if (config.hasChanged()) {
				config.save();
			}

		}
	}
}
