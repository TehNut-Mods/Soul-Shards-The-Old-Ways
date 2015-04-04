package com.whammich.sstow.block;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockXenoLight extends Block {

	public BlockXenoLight() {
		super(Material.glass);
		setBlockName("sstow.block.xenolith.light");
		setCreativeTab(Register.CREATIVE_TAB);
		blockHardness = 3.0F;
		blockResistance = 3.0F;
		setLightLevel(1.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":xenolith/xenolith_glowstone");
	}
}
