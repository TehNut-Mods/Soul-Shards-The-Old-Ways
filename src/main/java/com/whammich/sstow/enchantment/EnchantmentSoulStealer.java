package com.whammich.sstow.enchantment;

import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentSoulStealer extends Enchantment {

    public EnchantmentSoulStealer(int id, int weight) {
        super(id, new ResourceLocation(SoulShardsTOW.MODID, "soulStealer"), weight, EnumEnchantmentType.WEAPON);
    }

    @Override
    public int getMinEnchantability(int level) {
        return (level - 1) * 11;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 20;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
