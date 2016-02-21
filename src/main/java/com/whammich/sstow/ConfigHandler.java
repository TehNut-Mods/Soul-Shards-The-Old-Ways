package com.whammich.sstow;

import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.TierHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tehnut.lib.annot.Handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Handler
public class ConfigHandler {

    public static Configuration config;
    public static List<String> categories = new ArrayList<String>();

    public static List<String> entityList = new ArrayList<String>();

    public static int spawnCap;

    public static int soulStealerID;
    public static int soulStealerWeight;
    public static int soulStealerBonus;

    public static boolean enableBosses;
    public static boolean allowSpawnerAbsorption;
    public static int spawnerAbsorptionBonus;

    public static boolean enableBlacklistedSpawning;

    public static short[] defaultMinKills = {64, 128, 256, 512, 1024};
    private static byte[] defaultSpawns = {2, 4, 4, 4, 6};
    private static byte[] defaultDelay = {20, 10, 5, 5, 2};
    private static boolean[] defaultPlayer = {true, true, false, false, false};
    private static boolean[] defaultLight = {true, true, true, true, false};
    private static boolean[] defaultRedstone = {false, false, false, false, true};

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


        category = "General";
        categories.add(category);
        spawnCap = config.getInt("spawnCap", category, 30, 0, 256, "Max amount of mobs spawned by a given spawner in a 16 block radius.");

        category = "Enchantments";
        categories.add(category);
        soulStealerID = config.getInt("soulStealerID", category, 70, 63, 256, "ID for the Soul Stealer enchantment. If you have Enchantment ID conflicts, change this.");
        soulStealerWeight = config.getInt("soulStealerWeight", category, 3, 1, 10, "Weight of the Soul Stealer enchantment. Higher values make it more common.");
        soulStealerBonus = config.getInt("soulStealerBonus", category, 1, 1, 5, "Amount of bonus kills to provide per enchantment level.");

        category = "Debug";
        categories.add(category);
        enableBlacklistedSpawning = config.getBoolean("enableBlacklistedSpawning", category, false, "Allows disabled entities to still be spawned by the cage. They are, however, still not obtainable on a shard.");

        short[] minKills = new short[5];

        for (int i = 0; i < 5; i++) {
            category = String.format("Tiers.Tier %d", i + 1);
            categories.add(category);

            minKills[i] = (short) config.getInt("minimumKills", category, defaultMinKills[i], 1, 8096, "Minimum kills for the tier");
            TierHandler.setNumSpawns(i, (byte) config.getInt("amountToSpawn", category, defaultSpawns[i], 1, 10, "Number of spawns per operation"));
            TierHandler.setSpawnDelay(i, (byte) config.getInt("spawnCooldown", category, defaultDelay[i], 1, 60, "Cooldown time for soul cages (in seconds)"));
            TierHandler.setPlayerChecks(i, config.getBoolean("requirePlayer", category, defaultPlayer[i], "Needs a player nearby to spawn entities"));
            TierHandler.setLightChecks(i, config.getBoolean("followLightLevel", category, defaultLight[i], "Needs appropriate light to spawn entities"));
            TierHandler.setRedstoneChecks(i, config.getBoolean("redstoneControl", category, defaultRedstone[i], "Reacts to a redstone signal"));
        }

        TierHandler.setTierReqKills(minKills);

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

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent event) {
        if (event.modID.equals(SoulShardsTOW.MODID)) {
            syncConfig();
            handleEntityList("Entity List");
        }
    }
}
