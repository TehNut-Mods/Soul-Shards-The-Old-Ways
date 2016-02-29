package com.whammich.sstow.block;

import com.whammich.repack.tehnut.lib.annot.ModBlock;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.tile.TileEntityForge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@ModBlock(name = "BlockForge", tileEntity = TileEntityForge.class)
public class BlockForge extends Block {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public List<IBlockState> metaList = new ArrayList<IBlockState>();

    public BlockForge() {
        super(Material.rock);

        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setUnlocalizedName(SoulShardsTOW.MODID + ".forge");
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));

        buildMetaMap();
    }

    private void buildMetaMap() {
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, true));
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.EAST).withProperty(ACTIVE, false));
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.EAST).withProperty(ACTIVE, true));
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(ACTIVE, false));
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(ACTIVE, true));
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.WEST).withProperty(ACTIVE, false));
        metaList.add(getDefaultState().withProperty(FACING, EnumFacing.WEST).withProperty(ACTIVE, true));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        playerIn.openGui(SoulShardsTOW.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    public IBlockState getStateFromMeta(int meta) {
        return metaList.get(meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return metaList.indexOf(state);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, ACTIVE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(ACTIVE)) {
            EnumFacing enumfacing = state.getValue(FACING);
            double xOff = pos.getX() + 0.5D;
            double yOff = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double zOff = pos.getZ() + 0.5D;
            double off = 0.52D;
            double random = rand.nextDouble() * 0.6D - 0.3D;

            switch (enumfacing) {
                case NORTH:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xOff + random, yOff, zOff - off, 0.0D, 0.0D, 0.0D, 0);
                    world.spawnParticle(EnumParticleTypes.FLAME, xOff + random, yOff, zOff - off, 0.0D, 0.0D, 0.0D, 0);
                    break;
                case EAST:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xOff + off, yOff, zOff + random, 0.0D, 0.0D, 0.0D, 0);
                    world.spawnParticle(EnumParticleTypes.FLAME, xOff + off, yOff, zOff + random, 0.0D, 0.0D, 0.0D, 0);
                    break;
                case SOUTH:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xOff + random, yOff, zOff + off, 0.0D, 0.0D, 0.0D, 0);
                    world.spawnParticle(EnumParticleTypes.FLAME, xOff + random, yOff, zOff + off, 0.0D, 0.0D, 0.0D, 0);
                    break;
                case WEST:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xOff - off, yOff, zOff + random, 0.0D, 0.0D, 0.0D, 0);
                    world.spawnParticle(EnumParticleTypes.FLAME, xOff - off, yOff, zOff + random, 0.0D, 0.0D, 0.0D, 0);
                    break;
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityForge();
    }
}
