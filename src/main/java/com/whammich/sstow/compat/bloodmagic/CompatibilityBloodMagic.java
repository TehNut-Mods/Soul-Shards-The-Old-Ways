package com.whammich.sstow.compat.bloodmagic;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.compat.CompatibilityType;
import com.whammich.sstow.compat.ICompatibility;
import net.minecraftforge.common.MinecraftForge;

public class CompatibilityBloodMagic implements ICompatibility {

    @Override
    public void loadCompatibility(InitializationPhase phase) {
        if (phase == InitializationPhase.PRE_INIT)
            MinecraftForge.EVENT_BUS.register(new HandlerBloodMagic());
    }

    @Override
    public String getModId() {
        return "BloodMagic";
    }

    @Override
    public boolean enableCompat() {
        return ConfigHandler.compatibilityType == CompatibilityType.BLOODMAGIC;
    }
}
