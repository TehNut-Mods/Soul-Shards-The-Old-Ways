package com.whammich.sstow.compat.baubles;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSockets extends Item {

	private static String[] names = {

	"Animus",				// 0
	"Conservo",				// 1
	};

	private IIcon[] icon = new IIcon[2];

	public ItemSockets() {
		super();
		setUnlocalizedName(Reference.modID + ".socket");
		setCreativeTab(Register.CREATIVE_TAB);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "."
				+ names[stack.getItemDamage() % names.length].toLowerCase();
	}

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon[meta];
    }

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		
		for (int i = 0; i < this.icon.length; ++i) {
			this.icon[i] = iconRegister.registerIcon(Reference.modID + ":socket" + names[i]);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (int i = 0; i < names.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

}
