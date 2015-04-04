package com.whammich.sstow.block;

import java.util.List;

import net.minecraft.block.Block;
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

public class BlockXenolith extends Block {

	@SideOnly(Side.CLIENT)
	protected IIcon[] blockIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon bottomIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon otherIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon redIcon;

	public BlockXenolith() {
		super(Material.rock);
		setCreativeTab(Register.CREATIVE_TAB);
		setLightOpacity(255);
		useNeighborBrightness = true;
		blockHardness = 3.0F;
		blockResistance = 3.0F;
		setBlockName("sstow.block.xenolith");
	}

	public static final String[] names = new String[] { 
			"raw",			// 0
			"polished",		// 1
			"decorative",	// 2
			"nether",		// 3
			"soulium",		// 4
			"redstone",		// 5
			"ender",		// 6
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
		this.bottomIcon = iconRegister.registerIcon("nether_brick");
		this.otherIcon = iconRegister.registerIcon(Reference.MOD_ID + ":xenolith/xenolith_raw");
		this.redIcon = iconRegister.registerIcon(Reference.MOD_ID + ":xenolith/xenolith_redstone_tb");
		
		for (int i = 0; i < this.blockIcon.length; ++i) {
			this.blockIcon[i] = iconRegister.registerIcon(Reference.MOD_ID
					+ ":xenolith/xenolith_" + names[i]);
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta == 3) {
			if (side == 0) {
				return bottomIcon;
			} else if(side == 1) {
				return otherIcon;
			} else {
				return blockIcon[meta];
			}
		} 
//		else if (meta == 5){
//			if (side == 0) {
//				return redIcon;
//			} else if(side == 1) {
//				return redIcon;
//			} else {
//				return blockIcon[meta];
//			}
//		}

		if (meta > 6) {
			meta = 0;
		}
		return blockIcon[meta];

	}
}
