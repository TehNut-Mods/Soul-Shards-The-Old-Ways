package com.whammich.sstow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.util.TierHandler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigHandler {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

    public static void init(File jsonConfig) {
        try {
            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                Map<Integer, TierHandler.Tier> defaultMap = handleDefaults();
                String json = gson.toJson(defaultMap, new TypeToken<Map<Integer, TierHandler.Tier>>() { }.getType());
                FileWriter writer = new FileWriter(jsonConfig);
                writer.write(json);
                writer.close();
            }

            TierHandler.tiers = gson.fromJson(new FileReader(jsonConfig), new TypeToken<Map<Integer, TierHandler.Tier>>() { }.getType());
        } catch (IOException e) {
            SoulShardsTOW.instance.getLogHelper().severe("Failed to create a default Tier configuration file.");
        }
    }

    public static Map<Integer, TierHandler.Tier> handleDefaults() {
        Map<Integer, TierHandler.Tier> ret = new HashMap<Integer, TierHandler.Tier>();

        ret.put(0, new TierHandler.Tier(0, 63, true, false, false, 0, 0));
        ret.put(1, new TierHandler.Tier(64, 127, true, true, false, 2, 20));
        ret.put(2, new TierHandler.Tier(128, 255, true, true, false, 4, 10));
        ret.put(3, new TierHandler.Tier(256, 511, false, true, false, 4, 5));
        ret.put(4, new TierHandler.Tier(512, 1023, false, true, false, 4, 5));
        ret.put(5, new TierHandler.Tier(1024, 1024, false, false, true, 6, 2));

        return ret;
    }
}
