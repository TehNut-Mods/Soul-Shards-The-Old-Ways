package com.whammich.sstow.client.gui.config;

import com.google.common.collect.Lists;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Locale;

public class ConfigGuiSoulShards extends GuiConfig {

    public ConfigGuiSoulShards(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), SoulShardsTOW.MODID, false, false, SoulShardsTOW.NAME);
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = Lists.newArrayList();

        for (String category : ConfigHandler.categories)
            list.add(new ConfigElement(ConfigHandler.config.getCategory(category.toLowerCase(Locale.ENGLISH))));

        return list;
    }
}