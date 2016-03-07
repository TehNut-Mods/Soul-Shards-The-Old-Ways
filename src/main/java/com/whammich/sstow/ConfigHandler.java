package com.whammich.sstow;

import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.sstow.util.EntityMapper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Handler
public class ConfigHandler {

    public static Configuration config;
    public static List<String> categories = new ArrayList<String>();

    public static List<String> entityList = new ArrayList<String>();

    public static int spawnCap;
    public static boolean displayDurabilityBar;

    public static int soulStealerID;
    public static int soulStealerWeight;
    public static int soulStealerBonus;

    public static boolean enableBosses;
    public static boolean allowSpawnerAbsorption;
    public static int spawnerAbsorptionBonus;
    public static boolean enableExperienceDrop;
    public static boolean requireOwnerOnline;
    public static boolean cooldownUsesSeconds;

    public static boolean enableBlacklistedSpawning;

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent event) {
        if (event.modID.equals(SoulShardsTOW.MODID)) {
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
