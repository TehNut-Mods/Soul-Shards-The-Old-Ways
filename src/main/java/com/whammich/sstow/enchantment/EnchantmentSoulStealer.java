package com.whammich.sstow.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSoulStealer extends Enchantment {

    public EnchantmentSoulStealer() {
        super(Rarity.UNCOMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});

        setName("soulStealer");
        setRegistryName("soulStealer");
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
