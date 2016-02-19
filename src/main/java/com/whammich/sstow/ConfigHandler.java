package com.whammich.sstow;

import com.whammich.sstow.util.EntityMapper;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {

    public static Configuration config;

    public static List<String> entityList = new ArrayList<String>();
    public static int soulStealerID;
    public static int soulStealerWeight;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        String category;

        category = "Entity List";
        handleEntityList(category);

        category = "Enchantments";
        soulStealerID = config.getInt("soulStealerID", category, 70, 63, 256, "ID for the Soul Stealer enchantment. If you have Enchantment ID conflicts, change this.");
        soulStealerWeight = config.getInt("soulStealerWeight", category, 3, 1, 10, "Weight of the Soul Stealer enchantment. Higher values make it more common.");

        if (config.hasChanged())
            config.save();
    }

    private static void handleEntityList(String category) {
        for (String name : EntityMapper.entityList)
            if (config.get(category, name, true).getBoolean(true))
                entityList.add(name);
    }
}
