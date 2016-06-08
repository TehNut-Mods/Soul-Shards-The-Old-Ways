package com.whammich.sstow.compat.theoneprobe;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import tehnut.lib.iface.ICompatibility;

import javax.annotation.Nonnull;

public class CompatibilityTheOneProbe implements ICompatibility {

    @Override
    public void loadCompatibility(InitializationPhase phase) {
        if (phase == InitializationPhase.INIT)
            FMLInterModComms.sendFunctionMessage(getModId(), "getTheOneProbe", HandlerTheOneProbe.class.getCanonicalName());
    }

    @Override
    @Nonnull
    public String getModId() {
        return "theoneprobe";
    }

    @Override
    public boolean enableCompat() {
        return true;
    }
}
