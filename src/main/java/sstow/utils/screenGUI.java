package sstow.utils;

import java.util.ArrayList;
import java.util.List;

import sstow.SSRCore;
import sstow.utils.SSRConfig.Section;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class screenGUI extends GuiConfig {
	public screenGUI(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), SSRCore.MODID, false,
				false, GuiConfig.getAbridgedConfigPath("/Soul Shards The Old Ways/"));
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (Section section : SSRConfig.sections) {
			list.add(new ConfigElement<ConfigCategory>(SSRConfig.config
					.getCategory(section.lc())));
		}
		return list;
	}
}