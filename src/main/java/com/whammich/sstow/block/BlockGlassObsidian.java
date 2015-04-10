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
        setBlockName("sstow.block.obsidian.glass");
        setBlockTextureName(Reference.MOD_ID + ":glassObsidian");
        setCreativeTab(Register.CREATIVE_TAB);
        setLightOpacity(255);
        setHardness(50.0F);
        setResistance(2000.0F);

        useNeighborBrightness = true;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        Block sideBlock = blockAccess.getBlock(x, y, z);
        if (sideBlock == this)
            return false;

        return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
}
