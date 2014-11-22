package moze_intel.ssr.utils;

import mcp.mobius.waila.api.IWailaRegistrar;
import moze_intel.ssr.utils.SoulCageDataProvider;
public class WailaRegistrar {

	public static void wailaCallback(IWailaRegistrar registrar) {
		SSRLogger.logInfo("[Waila-Compat] Got registrar: " + registrar);
		// Tanks
		// registrar.registerBodyProvider(new HighOvenTankDataProvider(),
		// DeepTankLogic.class);
		// registrar.registerBodyProvider(new HighOvenTankDataProvider(),
		// HighOvenLogic.class);
		// registrar.registerBodyProvider(new SteamTurbineDataProvider(),
		// TurbineLogic.class);
	}

}