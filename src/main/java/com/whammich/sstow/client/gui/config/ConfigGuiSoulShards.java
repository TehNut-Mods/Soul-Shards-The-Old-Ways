package com.whammich.sstow.client.gui.config;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.util.Config;
import com.whammich.sstow.util.Config.Section;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfigGuiSoulShards extends GuiConfig {

    public ConfigGuiSoulShards(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), SoulShardsTOW.MODID, false, false, GuiConfig.getAbridgedConfigPath("/sstow/"));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        list.add(new ConfigElement(ConfigHandler.config.getCategory("Entity List".toLowerCase(Locale.ENGLISH))));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Balancing".toLowerCase(Locale.ENGLISH))));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("General".toLowerCase(Locale.ENGLISH))));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Enchantments".toLowerCase(Locale.ENGLISH))));
        list.add(new ConfigElement(ConfigHandler.config.getCategory("Debug".toLowerCase(Locale.ENGLISH))));

        return list;
    }
}