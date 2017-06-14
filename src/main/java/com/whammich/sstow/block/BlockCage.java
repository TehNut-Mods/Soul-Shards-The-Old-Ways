package com.whammich.sstow.block;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.tile.TileEntityCage;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.BooleanUtils;

public class BlockCage extends Block {

    public BlockCage() {
        super(Material.IRON);
        setUnlocalizedName(SoulShardsTOW.MODID + ".cage");
        setCreativeTab(SoulShardsTOW.TAB_SS);
        setDefaultState(blockState.getBaseState().withProperty(SoulShardsAPI.ACTIVE, false));
        setHardness(3.0F);
        setResistance(3.0F);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SoulShardsAPI.ACTIVE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        return state.withProperty(SoulShardsAPI.ACTIVE, ((TileEntityCage) tile).getActiveState());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SoulShardsAPI.ACTIVE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(SoulShardsAPI.ACTIVE, BooleanUtils.toBoolean(MathHelper.clamp(meta, 0, 1)));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntityCage tile = (TileEntityCage) world.getTileEntity(pos);
        return (int) ((float) tile.getTier() / (float) TierHandler.maxTier) * 15;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        TileEntityCage tileCage = (TileEntityCage) world.getTileEntity(blockPos);
        if (tileCage != null)
            tileCage.dropItems();


        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
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
