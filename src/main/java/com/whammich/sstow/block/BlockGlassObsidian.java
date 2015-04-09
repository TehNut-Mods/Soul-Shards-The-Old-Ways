package com.whammich.sstow.block;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockGlassObsidian extends Block {

	public BlockGlassObsidian() {
		super(Material.rock);
		setCreativeTab(Register.CREATIVE_TAB);
		setLightOpacity(255);
		useNeighborBrightness = true;
		setHardness(50.0F);
		setResistance(2000.0F);
		setBlockName("sstow.block.obsidian.glass");
	}
	
    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }
    
    @Override
    public int getRenderBlockPass()
	{
            return 1;
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
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
