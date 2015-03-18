package com.whammich.sstow.block;

import java.util.List;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPetrified extends BlockRotatedPillar {

	private IIcon blockIconTop;

	private static String[] names = { "wood.oak", "wood.spruce", "wood.birch", "wood.jungle", "wood.acacia", "wood.darkoak" };

	private IIcon[] icon = new IIcon[6];

	public BlockPetrified() {
		super(Material.rock);
		this.setBlockName("sstow.petrified");
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
	}

    @Override
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int side) {
		return blockIcon;
	}
    @Override
    public String getUnlocalizedName() {
        return getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
    }
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon[meta];
    }
	protected IIcon getTopIcon(int side) {
		return blockIconTop;
	}
	
    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister iconRegister) {
//		this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_birch");
//		this.blockIconTop = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_birch_top");
//	}
	
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
    	this.icon[0] = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_oak");
    	this.icon[1] = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_spruce");
    	this.icon[2] = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_birch");
    	this.icon[3] = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_jungle");
    	this.icon[4] = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_acacia");
    	this.icon[5] = iconRegister.registerIcon(Reference.MOD_ID + ":pet_log_darkoak");
    }
}
