package com.whammich.sstow.registry;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

public class ModEnchantments {

    public static Enchantment soulStealer;

    public static void init() {
        soulStealer = new EnchantmentSoulStealer();
        // TODO - Change to FML registry
        Enchantment.enchantmentRegistry.register(ConfigHandler.soulStealerID, new ResourceLocation(SoulShardsTOW.MODID, "soulStealer"), soulStealer);
    }
}
