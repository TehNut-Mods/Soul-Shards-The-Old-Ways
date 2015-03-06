package com.whammich.sstow.guihandler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Config.Section;
import com.whammich.sstow.utils.Reference;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class GuiOldWaysConfig extends GuiConfig {

	public GuiOldWaysConfig(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), Reference.MOD_ID, false,
				false, GuiConfig.getAbridgedConfigPath("/SSTOW/"));
	}

	@SuppressWarnings("rawtypes")
	private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (Section section : Config.sections) {
			list.add(new ConfigElement<ConfigCategory>(Config.config
					.getCategory(section.lc())));
		}
		return list;
	}
}