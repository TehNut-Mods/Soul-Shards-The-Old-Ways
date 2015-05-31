package com.whammich.sstow.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.whammich.sstow.tileentity.TileEntitySoulCrystal;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSoulCrystal extends BlockContainer {
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	public BlockSoulCrystal() {
		super(Material.glass);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setCreativeTab(Register.CREATIVE_TAB);
		setBlockName(Reference.modID + ".block.soulcrystal");
		this.minX = 0.2000000029802322D;
		this.maxX = 0.800000011920929D;
		this.minY = 0.2000000029802322D;
		this.maxY = 2.0D;
		this.minZ = 0.2000000029802322D;
		this.maxZ = 0.800000011920929D;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return renderID;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySoulCrystal();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(Reference.modID + ":soulcrystal");
	}
}
