package com.whammich.sstow.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.whammich.sstow.SSTheOldWays;
import com.whammich.sstow.tileentity.TileEntityInfuser;
import com.whammich.sstow.utils.Register;

public class BlockInfuser extends BlockContainer {

	public BlockInfuser() {
		super(Material.rock);
		setCreativeTab(Register.CREATIVE_TAB);
		setBlockName("sstow.block.infuser");
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
	
}
