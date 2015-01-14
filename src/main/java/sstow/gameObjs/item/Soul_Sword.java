package sstow.gameObjs.item;

import sstow.gameObjs.ObjHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Soul_Sword extends ItemSword {

	public Soul_Sword(ToolMaterial Material) {
		super(Material);
		this.setUnlocalizedName("soul_sword");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soul_sword";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("sstow:soul_sword");
	}
}
