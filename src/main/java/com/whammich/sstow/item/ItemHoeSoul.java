package com.whammich.sstow.item;

import com.whammich.sstow.utils.Register;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHoeSoul extends ItemHoe {

	public ItemHoeSoul(ToolMaterial Material) {
		super(Material);
		this.setUnlocalizedName("soul_hoe");
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soultool.hoe";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("sstow:soul_hoe");
	}
}
