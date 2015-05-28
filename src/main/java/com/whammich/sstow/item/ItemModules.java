package com.whammich.sstow.item;

import java.util.List;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemModules extends Item {
	private static String[] names = { "redstone", "light",
			 "dimension", "detection"//, "t5controller",

	};
	private IIcon[] icon = new IIcon[4];

	public ItemModules() {
		super();

		setUnlocalizedName(Reference.modID + ".modules");
		setCreativeTab(Register.CREATIVE_TAB);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "."
				+ names[stack.getItemDamage() % names.length];
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.icon[meta];
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.icon[0] = iconRegister.registerIcon(Reference.modID + ":moduleRedstone");
		this.icon[1] = iconRegister.registerIcon(Reference.modID + ":moduleLight");
		this.icon[2] = iconRegister.registerIcon(Reference.modID + ":moduleDimension");
		this.icon[3] = iconRegister.registerIcon(Reference.modID + ":modulePlayer");
		//this.icon[4] = iconRegister.registerIcon(Reference.modID + ":moduleController");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (int i = 0; i < names.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
}