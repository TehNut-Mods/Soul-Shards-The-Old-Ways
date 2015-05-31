package com.whammich.sstow.utils;

public class Reference {
	public static final String modID = "sstow";
	public static final String modName = "Soul Shards: The Old Ways";
	public static final String modVersion = "@VERSION@";
	public static final String guiFactory_class = "com.whammich.sstow.guihandler.GuiFactory";
	public static final String requiredDependencies = "required-after:Baubles@[1.0.1.10,);required-after:guideapi@[1.7.10-1.0-15,);after:Natura";
	public static final String wailaCallBack = "com.whammich.sstow.compat.waila.WailaRegister.wailaCallback";
    public static final String clientProxy = "com.whammich.sstow.proxies.ClientProxy";
    public static final String commonProxy = "com.whammich.sstow.proxies.CommonProxy";
}
