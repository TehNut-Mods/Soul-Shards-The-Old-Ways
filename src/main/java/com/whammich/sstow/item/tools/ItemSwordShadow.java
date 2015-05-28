package com.whammich.sstow.item.tools;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSwordShadow extends ItemSword {

	public ItemSwordShadow(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.shadowtool.sword";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":swordShadow");
	}
}
