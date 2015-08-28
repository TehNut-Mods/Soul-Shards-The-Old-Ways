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
	protected IIcon topIcon;

	public BlockXenolith() {
		super(Material.rock);
		setCreativeTab(Register.CREATIVE_TAB);
		setLightOpacity(255);
		useNeighborBrightness = true;
		blockHardness = 3.0F;
		blockResistance = 3.0F;
		setBlockName(Reference.modID + ".block.xenolith");
	}

	public static final String[] names = new String[] {
		"Raw",			// 0
		"Polished",		// 1
		"Decorative",	// 2
		"Nether",		// 3
		"Soulium",		// 4
		"Redstone",		// 5
		"Ender",		// 6
		"Bricks",		// 7
		"BricksSmall"	// 8
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
    public int damageDropped(int meta) {
        return meta;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {

		this.blockIcon = new IIcon[names.length];
		this.bottomIcon = iconRegister.registerIcon("nether_brick");
		this.topIcon = iconRegister.registerIcon(Reference.modID + ":xenolith/xenolithRaw");

		for (int i = 0; i < this.blockIcon.length; ++i) {
			this.blockIcon[i] = iconRegister.registerIcon(Reference.modID + ":xenolith/xenolith" + names[i]);
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta == 3) {
			if (side == 0) {
				return bottomIcon;
			} else if (side == 1) {
				return topIcon;
			} else {
				return blockIcon[meta];
			}
		}

		if (meta > 8) {
			meta = 0;
		}
		return blockIcon[meta];

	}
}
