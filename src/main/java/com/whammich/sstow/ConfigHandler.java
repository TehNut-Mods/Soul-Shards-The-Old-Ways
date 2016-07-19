package com.whammich.sstow;

import com.whammich.sstow.compat.CompatibilityType;
import com.whammich.sstow.util.EntityMapper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tehnut.lib.annot.Handler;
import tehnut.lib.annot.Used;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Handler
public class ConfigHandler {

    public static Configuration config;
    public static List<String> categories = new ArrayList<String>();

    public static List<String> entityList = new ArrayList<String>();

    public static int spawnCap;
    public static boolean displayDurabilityBar;

    public static boolean addShardsForAllMobs;
    public static boolean ignoreBlacklistForTab;

    public static int soulStealerID;
    public static int soulStealerWeight;
    public static int soulStealerBonus;

    public static boolean enableBosses;
    public static boolean allowSpawnerAbsorption;
    public static int spawnerAbsorptionBonus;
    public static boolean enableExperienceDrop;
    public static boolean requireOwnerOnline;
    public static boolean cooldownUsesSeconds;
    public static boolean countCageBornForShard;
    public static boolean forceRedstoneRequirement;
    public static boolean convenienceReset;

    public static boolean enableBlacklistedSpawning;

    public static CompatibilityType compatibilityType = CompatibilityType.VANILLA;
    public static int lpPerMob;
    public static boolean enableSoulStealerModifier;

    @SubscribeEvent
    @Used
    public void configChanged(ConfigChangedEvent event) {
        if (event.getModID().equals(SoulShardsTOW.MODID)) {
            syncConfig();
            handleEntityList("Entity List");
        }
    }

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        categories.clear();

        String category;

        category = "Balancing";
        categories.add(category);
        enableBosses = config.getBoolean("enableBosses", category, false, "Allows bosses to be spawned. This is probably the worst thing you can do to your instance.");
        allowSpawnerAbsorption = config.getBoolean("allowSpawnerAbsorption", category, true, "Allows Shards to absorb spawners of the same entity type.");
        spawnerAbsorptionBonus = config.getInt("spawnerAbsorptionBonus", category, 200, 0, 1024, "Amount of souls to add when absorbing a spawner.");
        enableExperienceDrop = config.getBoolean("enableExperienceDrop", category, true, "Mobs spawned via a Soul Cage will drop experience upon death.");
        requireOwnerOnline = config.getBoolean("requireOwnerOnline", category, false, "Requires the player who put the shard into the Soul Cage to be online for it to be active.");
        cooldownUsesSeconds = config.getBoolean("cooldownUsesSeconds", category, true, "The cooldown time set in \"ShardTiers.json\" should use seconds instead of ticks. Allows finer control over tiers.\n1 second = 20 ticks\nI suggest not changing this unless you have edited the configs.");
        countCageBornForShard = config.getBoolean("countCageBornForShard", category, true, "Count mobs spawned by a Soul Cage towards Shard kills.");
        forceRedstoneRequirement = config.getBoolean("forceRedstoneRequirement", category, false, "Forces Soul Cages to require a Redstone signal in order to spawn entities.");
        convenienceReset = config.getBoolean("convenienceReset", category, true, "Enables a convenience recipe that allows you to reset the stats of a Soul Shard.");

        category = "Client";
        categories.add(category);
        addShardsForAllMobs = config.getBoolean("addShardsForAllMobs", category, false, "Adds a Shard for each enabled mob to the creative tab.");
        ignoreBlacklistForTab = config.getBoolean("ignoreBlacklistForTab", category, false, "Adds A shards for mobs even if they are blacklisted");

        category = "General";
        categories.add(category);
        spawnCap = config.getInt("spawnCap", category, 30, 0, 256, "Max amount of mobs spawned by a given spawner in a 16 block radius.");
        displayDurabilityBar = config.getBoolean("displayDurabilityBar", category, true, "Displays a durability bar on the shard as an indicator on how close to maxed it is");

        category = "Enchantments";
        categories.add(category);
        soulStealerID = config.getInt("soulStealerID", category, 70, 63, 256, "ID for the Soul Stealer enchantment. If you have Enchantment ID conflicts, change this.");
        soulStealerWeight = config.getInt("soulStealerWeight", category, 3, 1, 10, "Weight of the Soul Stealer enchantment. Higher values make it more common.");
        soulStealerBonus = config.getInt("soulStealerBonus", category, 1, 1, 5, "Amount of bonus kills to provide per enchantment level.");

        category = "Debug";
        categories.add(category);
        enableBlacklistedSpawning = config.getBoolean("enableBlacklistedSpawning", category, false, "Allows disabled entities to still be spawned by the cage. They are, however, still not obtainable on a shard.");

        config.setCategoryComment("Entity List", "Set an entity to false to disable it's ability to be spawned.");
        categories.add("Entity List");

        category = "Compatibility";
        categories.add(category);
        String compatTypeString = config.get(category, "compatibilityType", "VANILLA", "The type of spawning mechanic to use for the Soul Cage.\nValid values:\nVANILLA - The standard spawning mechanic.\nBLOODMAGIC - Requires 100 LP per mob spawned.\nHARDMODE - Reduces the contained souls of the shard by 1 for each mob spawned. Does not reduce tier. Recommended to disable \"countCageBornForShard\".").setRequiresWorldRestart(true).getString();
        try {
            compatibilityType = CompatibilityType.valueOf(compatTypeString.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            SoulShardsTOW.instance.getLogHelper().error("{} is not a valid CompatibilityType. Falling back to VANILLA.", compatTypeString.toUpperCase(Locale.ENGLISH));
        }
        lpPerMob = config.getInt("lpPerMob", category, 250, 0, Integer.MAX_VALUE, "Amount of LP required for each mob spawned.\nIf this amount is not contained in the LP network, a nausea effect will be applied to the player and the Soul Cage will stop functioning.");
        enableSoulStealerModifier = config.getBoolean("enableSoulStealerModifier", category, true, "Adds a Tinkers Construct modifier for Soul Stealer.");

        if (config.hasChanged())
            config.save();
    }

    public static void handleEntityList(String category) {
        entityList.clear();

        for (String name : EntityMapper.entityList)
            if (config.get(category, name, true).getBoolean(true))
                entityList.add(name);

        config.save();
    }
}
