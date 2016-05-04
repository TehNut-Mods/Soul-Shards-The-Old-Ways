package com.whammich.sstow.compat.enderio;

import com.whammich.sstow.compat.ICompatibility;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class CompatibilityEnderIO implements ICompatibility {

    public final String ENCHANTER_SOULSTEALER =
        "<enchantment name=\"enchantment.soulStealer\" costPerLevel=\"4\">\n" +
        "<itemStack modID=\"soulshardstow\" itemName=\"ItemMaterials\" itemMeta=\"0\"/>\n" +
        "</enchantment>";

    @Override
    public void loadCompatibility(InitializationPhase phase) {
        if (phase == InitializationPhase.INIT)
            FMLInterModComms.sendMessage("EnderIO", "recipe:enchanter", ENCHANTER_SOULSTEALER);
    }

    @Override
    public String getModId() {
        return "EnderIO";
    }

    @Override
    public boolean enableCompat() {
        return true;
    }
}
