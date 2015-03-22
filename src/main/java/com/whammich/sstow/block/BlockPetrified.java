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
	@SideOnly(Side.CLIENT)
	protected IIcon[] sideIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon[] topIcon;

	public BlockPetrified() {
		super(Material.rock);
		setCreativeTab(Register.CREATIVE_TAB);
		setLightOpacity(255);
		useNeighborBrightness = true;
		blockHardness = 3.0F;
		blockResistance = 3.0F;
		setBlockName("petrified_log");
	}

	public static final String[] names = new String[] { "oak", "spruce",
			"birch", "jungle", "acacia", "big_oak" };

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
		list.add(new ItemStack(item, 1, 3));
		list.add(new ItemStack(item, 1, 4));
		list.add(new ItemStack(item, 1, 5));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.sideIcon = new IIcon[names.length];
		this.topIcon = new IIcon[names.length];

		for (int i = 0; i < this.sideIcon.length; ++i) {
			this.sideIcon[i] = iconRegister.registerIcon(Reference.MOD_ID
					+ ":petrified_logs/petrified_log_" + names[i]);
			this.topIcon[i] = iconRegister.registerIcon(Reference.MOD_ID
					+ ":petrified_logs/petrified_log_" + names[i] + "_top");
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getSideIcon(int side) {
		return this.sideIcon[side % this.sideIcon.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getTopIcon(int side) {
		return this.topIcon[side % this.topIcon.length];
	}

}
