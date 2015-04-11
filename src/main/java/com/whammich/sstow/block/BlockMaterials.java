package com.whammich.sstow.block;

import java.util.List;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMaterials extends Block {

	@SideOnly(Side.CLIENT)
	protected IIcon[] blockIcon;
	
	public BlockMaterials() {
		super(Material.iron);
		this.setBlockName("sstow.block.material");
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
	}

	public static final String[] names = new String[] { 
		"soulium",		// 0 
		"ender",		// 1 
	};
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = new IIcon[names.length];

		for (int i = 0; i < this.blockIcon.length; ++i) {
			this.blockIcon[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + names[i]);
		}
	}

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta > 6)
			meta = 0;
		return blockIcon[meta];
		
	}
}
