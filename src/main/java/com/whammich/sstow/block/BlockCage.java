package com.whammich.sstow.block;

import com.whammich.sstow.SoulShardsTOW;
import tehnut.lib.annot.ModBlock;
import tehnut.lib.annot.Used;
import tehnut.lib.block.base.BlockBoolean;
import com.whammich.sstow.tile.TileEntityCage;
import com.whammich.sstow.util.Utils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@ModBlock(name = "BlockCage", tileEntity = TileEntityCage.class)
@Used
public class BlockCage extends BlockBoolean {

    public BlockCage() {
        super(Material.iron, "shard");
        setUnlocalizedName(SoulShardsTOW.MODID + ".cage");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
        blockHardness = 3.0F;
        blockResistance = 3.0F;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos pos) {
        TileEntityCage tile = (TileEntityCage) world.getTileEntity(pos);
        if (tile.getStackInSlot(0) != null) {
            ItemStack shard = tile.getStackInSlot(0);
            int tier = Utils.getShardTier(shard);
            switch (tier) {
                case 1: return 2;
                case 2: return 5;
                case 3: return 7;
                case 4: return 10;
                case 5: return 15;
                default: return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileEntityCage tileCage = (TileEntityCage) world.getTileEntity(blockPos);
        if (tileCage != null)
            tileCage.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityCage) {
            TileEntityCage cage = (TileEntityCage) tile;

            return cage.insertItem(player, 0);
        }

        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityCage)
            ((TileEntityCage) tile).checkRedstone();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

        @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCage();
    }
}
