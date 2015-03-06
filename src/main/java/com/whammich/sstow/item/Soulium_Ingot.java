package com.whammich.sstow.item;

import com.whammich.sstow.utils.Register;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Soulium_Ingot extends Item {

	public Soulium_Ingot() {
		this.setUnlocalizedName("soulium_ingot");
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soulium_ingot";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("sstow:soulium_ingot");
	}
}
