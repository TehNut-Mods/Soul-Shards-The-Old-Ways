package sstow.gameObjs.item;

import sstow.gameObjs.ObjHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Soul_Pickaxe extends ItemPickaxe {

	public Soul_Pickaxe(ToolMaterial Material) {
		super(Material);
		this.setUnlocalizedName("soul_pickaxe");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soul_pickaxe";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("sstow:soul_pickaxe");
	}
}
