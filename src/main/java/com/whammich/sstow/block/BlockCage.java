package com.whammich.sstow.block;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulCage;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.tile.TileEntityCage;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import tehnut.lib.annot.Handler;
import tehnut.lib.annot.ModBlock;
import tehnut.lib.annot.Used;
import tehnut.lib.iface.IVariantProvider;

import java.util.ArrayList;
import java.util.List;

@ModBlock(name = "BlockCage", tileEntity = TileEntityCage.class)
@Used
@Handler
public class BlockCage extends Block implements IVariantProvider {

    public BlockCage() {
        super(Material.iron);
        setUnlocalizedName(SoulShardsTOW.MODID + ".cage");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
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
        return getDefaultState().withProperty(SoulShardsAPI.ACTIVE, BooleanUtils.toBoolean(MathHelper.clamp_int(meta, 0, 1)));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntityCage tile = (TileEntityCage) world.getTileEntity(pos);
        // (Current Tier / Max Tiers) * 15
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
        ret.add(Pair.of(0, "active=false"));
        ret.add(Pair.of(1, "active=true"));
        return ret;
    }

    @SubscribeEvent
    @Used
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        TileEntity tile = event.getWorld().getTileEntity(event.getPos());
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = event.getItemStack();

        if (tile != null && tile instanceof ISoulCage) {
            TileEntityCage cage = (TileEntityCage) tile;
            if (heldItem != null && cage.getStackInSlot(0) == null && cage.isItemValidForSlot(0, heldItem) && !player.isSneaking()) {
                cage.setInventorySlotContents(0, heldItem.copy());
                cage.setTier(ShardHelper.getTierFromShard(heldItem));
                cage.setEntName(ShardHelper.getBoundEntity(heldItem));
                if (!event.getWorld().isRemote)
                    cage.setOwner(player.getGameProfile().getId().toString());
                heldItem.stackSize--;
                player.swingArm(event.getHand());
            } else if (cage.getStackInSlot(0) != null && player.getHeldItemMainhand() == null && player.isSneaking()) {
                if (!event.getWorld().isRemote) {
                    EntityItem invItem = new EntityItem(event.getWorld(), player.posX, player.posY + 0.25, player.posZ, cage.getStackInSlot(0));
                    event.getWorld().spawnEntityInWorld(invItem);
                }
                cage.reset();
                player.swingArm(event.getHand());
            }
        }
    }
}
