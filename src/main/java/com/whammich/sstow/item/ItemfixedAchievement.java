package com.whammich.sstow.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemfixedAchievement extends Item {

	public ItemfixedAchievement() {
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "fixed";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("sstow:mobessence");
	}
}
