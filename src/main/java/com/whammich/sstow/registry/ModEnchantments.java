package com.whammich.sstow.registry;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import net.minecraft.enchantment.Enchantment;

public class ModEnchantments {

    public static Enchantment soulStealer;

    public static void init() {
        soulStealer = new EnchantmentSoulStealer(ConfigHandler.soulStealerID, ConfigHandler.soulStealerWeight);
        Enchantment.addToBookList(soulStealer);
    }
}
