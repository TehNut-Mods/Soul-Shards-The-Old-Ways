package com.whammich.sstow.registry;

import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModEnchantments {

    public static Enchantment soulStealer;

    public static void init() {
        soulStealer = new EnchantmentSoulStealer();
        GameRegistry.register(soulStealer);
    }
}
