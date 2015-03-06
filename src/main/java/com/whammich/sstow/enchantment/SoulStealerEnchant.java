package com.whammich.sstow.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class SoulStealerEnchant extends Enchantment {
	public SoulStealerEnchant(int id, int weight) {
		super(id, weight, EnumEnchantmentType.weapon);
		this.name = "soul_stealer";
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
