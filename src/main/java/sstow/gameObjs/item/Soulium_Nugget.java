package sstow.gameObjs.item;

import sstow.gameObjs.ObjHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Soulium_Nugget extends Item {

	public Soulium_Nugget() {
		this.setUnlocalizedName("soulium_nugget");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soulium_nugget";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("sstow:soulium_nugget");
	}
}
