package moze_intel.ssr.utils;

import cpw.mods.fml.common.event.FMLInterModComms;

public class WailaPlugin implements ICompatPlugin {

	@Override
	public String getModId() {
		return "Waila";
	}

	@Override
	public void preInit() {
		// Nothing
	}

	@Override
	public void init() {
		SSRLogger
				.logInfo("Waila detected. Registering SSR with Waila registry.");
		FMLInterModComms.sendMessage("Waila", "register",
				"moze_intel.ssr.utils.WailaRegistrar.wailaCallback");
	}

	@Override
	public void postInit() {
		// Nothing
	}
}