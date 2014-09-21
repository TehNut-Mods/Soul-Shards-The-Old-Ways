package ssr;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import ssr.config.SoulConfig;
import ssr.config.SoulConfig.Section;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.IConfigElement;

public class ssrscreenGUI extends GuiConfig {
	public ssrscreenGUI(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), SSRCore.MODID,
				false, false, GuiConfig
						.getAbridgedConfigPath("/Soul Shards Reborn/"));
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parent) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (Section section : SoulConfig.sections) {
			list.add(new ConfigElement<ConfigCategory>(SoulConfig.config
					.getCategory(section.lc())));
		}
		return list;
	}
}