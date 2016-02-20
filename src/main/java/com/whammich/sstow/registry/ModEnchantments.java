package com.whammich.sstow.registry;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModEnchantments {

    public static Enchantment soulStealer;

    public static void init() {
        soulStealer = new EnchantmentSoulStealer(ConfigHandler.soulStealerID, ConfigHandler.soulStealerWeight);
        Enchantment.addToBookList(soulStealer);
    }
}
