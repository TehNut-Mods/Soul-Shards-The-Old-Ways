package com.whammich.sstow.block;

import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.whammich.repack.tehnut.lib.annot.ModBlock;
import com.whammich.repack.tehnut.lib.annot.Used;
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
public class BlockCage extends Block {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockCage() {
        super(Material.iron);
        setUnlocalizedName(SoulShardsTOW.MODID + ".cage");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false));

        blockHardness = 3.0F;
        blockResistance = 3.0F;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, ACTIVE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        return state.withProperty(ACTIVE, ((TileEntityCage) tile).getActiveState());
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 0: return getDefaultState().withProperty(ACTIVE, false);
            case 1: return getDefaultState().withProperty(ACTIVE, true);
            default: return getDefaultState().withProperty(ACTIVE, false);
        }
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
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
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
            if (player.getHeldItem() != null && cage.getStackInSlot(0) == null && cage.isItemValidForSlot(0, player.getHeldItem()) && !player.isSneaking()) {
                cage.setInventorySlotContents(0, player.getHeldItem().copy());
                cage.setTier(Utils.getShardTier(player.getHeldItem()));
                cage.setEntName(Utils.getShardBoundEnt(player.getHeldItem()));
                player.getHeldItem().stackSize--;
                return true;
            } else if ( cage.getStackInSlot(0) != null && player.getHeldItem() == null && player.isSneaking()) {
                cage.setTier(0);
                cage.setEntName("");
                cage.setActiveTime(0);
                if (!world.isRemote) {
                    EntityItem invItem = new EntityItem(world, player.posX, player.posY + 0.25, player.posZ, cage.getStackInSlot(0));
                    world.spawnEntityInWorld(invItem);
                }
                cage.clear();
                return true;
            }
        }

        return false;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
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
