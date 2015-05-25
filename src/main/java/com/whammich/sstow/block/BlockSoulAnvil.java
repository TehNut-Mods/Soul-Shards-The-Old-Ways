package com.whammich.sstow.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.whammich.sstow.tileentity.TileEntitySoulAnvil;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSoulAnvil extends BlockContainer {

	public BlockSoulAnvil() {
		super(Material.anvil);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setBlockName("sstow.block.soulanvil");
	}
	
	public int getRenderType() {
		return -1;
	}
	
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySoulAnvil();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(Reference.modID + ":blockSoulium");
	}
	
}
