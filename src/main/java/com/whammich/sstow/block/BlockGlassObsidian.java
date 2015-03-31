package com.whammich.sstow.block;

import java.util.Random;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGlassObsidian extends BlockBreakable {

	private static String field_149995_b;
	private static boolean field_149996_a;

	public BlockGlassObsidian(Material material, boolean bool) {
		super(field_149995_b, Material.rock, field_149996_a);
		setCreativeTab(Register.CREATIVE_TAB);
		setLightOpacity(255);
		useNeighborBrightness = true;
		setHardness(50.0F);
		setResistance(2000.0F);
		setBlockName("sstow.block.obsidian.glass");
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":glassObsidian");
	}
}
