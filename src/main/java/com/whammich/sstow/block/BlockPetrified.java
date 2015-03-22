package com.whammich.sstow.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPetrified extends BlockRotatedPillar {
	private IIcon blockIconTop;
	public String texture;

	public BlockPetrified(String string) {
		super(Material.rock);
		texture = string;
		setCreativeTab(Register.CREATIVE_TAB);
		setLightOpacity(255);
		useNeighborBrightness = true;
		blockHardness = 3.0F;
		blockResistance = 3.0F;
		setBlockName("petrified");
		
	}

	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int side) {
		return blockIcon;
	}

	protected IIcon getTopIcon(int side) {
		return blockIconTop;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":log_" + texture.toLowerCase());
		this.blockIconTop = iconRegister.registerIcon(Reference.MOD_ID + ":log_" + texture.toLowerCase() + "_top");
	}
}
