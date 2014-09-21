package ssr;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import ssr.config.SoulConfig;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.IConfigElement;

public class ssrscreenGUI extends GuiConfig {
	public ssrscreenGUI(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(
				SoulConfig.config.getCategory("misc")).getChildElements(),
				SSRCore.MODID, false, false, GuiConfig
						.getAbridgedConfigPath("/Soul Shards Reborn/"));
	}

	private static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
		// TODO Auto-generated method stub
		return null;
	}
}