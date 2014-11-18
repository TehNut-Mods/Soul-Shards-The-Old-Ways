package moze_intel.ssr.utils;

import java.util.ArrayList;
import java.util.List;

import moze_intel.ssr.SSRCore;
import moze_intel.ssr.utils.SSRConfig.Section;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class screenGUI extends GuiConfig {
	public screenGUI(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(parentScreen), SSRCore.ID, false,
				false, GuiConfig.getAbridgedConfigPath("/Soul Shards Reborn/"));
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		for (Section section : SSRConfig.sections) {
			list.add(new ConfigElement<ConfigCategory>(SSRConfig.config.getCategory(section.lc())));
		}
		return list;
	}
}