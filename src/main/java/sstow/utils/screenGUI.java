package sstow.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import sstow.Main;
import sstow.utils.Config.Section;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class screenGUI extends GuiConfig {

	public screenGUI(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), Main.MODID, false,
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