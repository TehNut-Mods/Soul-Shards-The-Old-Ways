package ssr.enchants;

import ssr.SSRCore;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class SoulStealer extends Enchantment {

	public SoulStealer(int id, int weight) {
		super(id, weight, EnumEnchantmentType.weapon);
		this.name = "Soul Stealer";
	}

	@Override
	public int getMinEnchantability(int par1) {
		return (par1 - 1) * 11;
	}

	@Override
	public int getMaxEnchantability(int par1) {
		return this.getMinEnchantability(par1) + 20;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}
}
