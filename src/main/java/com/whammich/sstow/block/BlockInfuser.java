package com.whammich.sstow.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.whammich.sstow.SSTheOldWays;
import com.whammich.sstow.tileentity.TileEntityInfuser;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockInfuser extends BlockContainer {

	public BlockInfuser() {
		super(Material.rock);
		setCreativeTab(Register.CREATIVE_TAB);
		setBlockName(Reference.modID + ".block.infuser");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var1) {
		return new TileEntityInfuser();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(world.isRemote){
			return true;
		}
		player.openGui(SSTheOldWays.modInstance, 0, world, x, y, z);
		return true;
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(Reference.modID + ":infuser");
	}
}
