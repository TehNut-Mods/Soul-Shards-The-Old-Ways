package com.whammich.sstow.compat.waila;

import com.whammich.repack.tehnut.lib.iface.ICompatibility;

public class CompatibilityWaila implements ICompatibility {

    @Override
    public void loadCompatibility(InitializationPhase phase) {
//        if (phase == InitializationPhase.INIT)
//            FMLInterModComms.sendMessage("Waila", "register", WailaCallbackHandler.class.getCanonicalName() + ".callbackRegister");
    }

    @Override
    public String getModId() {
        return "Waila";
    }

    @Override
    public boolean enableCompat() {
        return true;
    }
}
