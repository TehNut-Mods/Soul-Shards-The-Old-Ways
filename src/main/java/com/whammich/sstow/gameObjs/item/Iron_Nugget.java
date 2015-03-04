package com.whammich.sstow.gameObjs.item;

import com.whammich.sstow.gameObjs.ObjHandler;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Iron_Nugget extends Item {

	public Iron_Nugget() {
		this.setUnlocalizedName("iron_nugget");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.iron_nugget";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("sstow:iron_nugget");
	}
}
