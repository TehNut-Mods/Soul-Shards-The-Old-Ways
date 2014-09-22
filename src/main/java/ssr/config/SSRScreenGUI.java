package ssr.config;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import ssr.config.Config;
import ssr.config.Config.Section;
import ssr.SSRCore;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.IConfigElement;

public class SSRScreenGUI extends GuiConfig {
	public SSRScreenGUI(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), SSRCore.MODID,
				false, false, GuiConfig
						.getAbridgedConfigPath("/Soul Shards Reborn/"));
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parent) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (Section section : Config.sections) {
			list.add(new ConfigElement<ConfigCategory>(Config.config
					.getCategory(section.lc())));
		}
		return list;
	}
}