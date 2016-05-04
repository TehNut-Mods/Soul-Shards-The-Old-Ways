package com.whammich.sstow.compat.hardmode;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.compat.CompatibilityType;
import com.whammich.sstow.compat.ICompatibility;
import net.minecraftforge.common.MinecraftForge;

public class CompatibilityHardMode implements ICompatibility {

    @Override
    public void loadCompatibility(InitializationPhase phase) {
        if (phase == InitializationPhase.PRE_INIT)
            MinecraftForge.EVENT_BUS.register(new HandlerHardMode());
    }

    @Override
    public String getModId() {
        return SoulShardsTOW.MODID;
    }

    @Override
    public boolean enableCompat() {
        return ConfigHandler.compatibilityType == CompatibilityType.HARDMODE;
    }
}
