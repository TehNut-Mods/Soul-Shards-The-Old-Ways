package com.whammich.sstow.client.gui.config;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.util.Config;
import com.whammich.sstow.util.Config.Section;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiOldWaysConfig extends GuiConfig {

    public GuiOldWaysConfig(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), SoulShardsTOW.MODID, false, false, GuiConfig.getAbridgedConfigPath("/Whammich/"));
    }

    @SuppressWarnings("rawtypes")
    private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();

        for (Section section : Config.sections)
            list.add(new ConfigElement(Config.config.getCategory(section.lc())));

        return list;
    }
}