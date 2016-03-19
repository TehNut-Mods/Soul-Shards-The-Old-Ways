package com.whammich.sstow.block;

import com.whammich.repack.tehnut.lib.annot.ModBlock;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.repack.tehnut.lib.iface.IVariantProvider;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.tile.TileEntityCage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@ModBlock(name = "BlockCage", tileEntity = TileEntityCage.class)
@Used
public class BlockCage extends Block implements IVariantProvider {

    public BlockCage() {
        super(Material.iron);
        setUnlocalizedName(SoulShardsTOW.MODID + ".cage");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setDefaultState(blockState.getBaseState().withProperty(SoulShardsAPI.ACTIVE, false));

        blockHardness = 3.0F;
        blockResistance = 3.0F;
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
        switch (meta) {
            case 0:
                return getDefaultState().withProperty(SoulShardsAPI.ACTIVE, false);
            case 1:
                return getDefaultState().withProperty(SoulShardsAPI.ACTIVE, true);
            default:
                return getDefaultState().withProperty(SoulShardsAPI.ACTIVE, false);
        }
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntityCage tile = (TileEntityCage) world.getTileEntity(pos);
        if (tile.getStackInSlot(0) != null) {
            ItemStack shard = tile.getStackInSlot(0);
            int tier = ShardHelper.getTierFromShard(shard);
            switch (tier) {
                case 1:
                    return 2;
                case 2:
                    return 5;
                case 3:
                    return 7;
                case 4:
                    return 10;
                case 5:
                    return 15;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityCage) {
            TileEntityCage cage = (TileEntityCage) tile;
            if (heldItem != null && cage.getStackInSlot(0) == null && cage.isItemValidForSlot(0, heldItem) && !player.isSneaking()) {
                cage.setInventorySlotContents(0, heldItem.copy());
                cage.setTier(ShardHelper.getTierFromShard(heldItem));
                cage.setEntName(ShardHelper.getBoundEntity(heldItem));
                if (!world.isRemote)
                    cage.setOwner(player.getGameProfile().getId().toString());
                heldItem.stackSize--;
                return true;
            } else if (cage.getStackInSlot(0) != null && player.getHeldItemMainhand() == null && player.isSneaking()) {
                cage.setTier(0);
                cage.setEntName("");
                cage.setActiveTime(0);
                cage.setOwner("");
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
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isVisuallyOpaque() {
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

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(new ImmutablePair<Integer, String>(0, "active=false"));
        ret.add(new ImmutablePair<Integer, String>(1, "active=true"));
        return ret;
    }
}
