package com.whammich.sstow.tile;

import com.google.common.base.Stopwatch;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulCage;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.api.event.CageSpawnEvent;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileEntityCage extends TileEntity implements ITickable, ISoulCage {

    public static final String TIER = "tier";
    public static final String ACTIVE_TIME = "activeTime";
    public static final String ENT_NAME = "entName";
    public static final String OWNER = "owner";
    public static final String SSTOW = "SSTOW";
    public static final String ITEMS = "Items";

    private int activeTime;
    private int tier;
    private ResourceLocation entName = Utils.EMPTY_ENT;
    private String owner = "";
    private ItemStackHandler stackHandler = new ItemStackHandler(1) {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (stack.getItem() instanceof ISoulShard && ShardHelper.isBound(stack))
                return super.insertItem(slot, stack, simulate);

            return stack;
        }
    };

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        if (!stackHandler.getStackInSlot(0).isEmpty()) {
            ItemStack shard = stackHandler.getStackInSlot(0);
            setEntName(ShardHelper.getBoundEntity(shard));
            setTier(ShardHelper.getTierFromShard(shard));
        } else {
            reset();
        }

        if (!canSpawn()) {
            setActiveState(false);
            return;
        }

        activeTime++;
        setActiveState(true);

        if (activeTime % getCooldown() == 0)
            spawnEntities(getSpawnAmount(), getEntName());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        this.tier = tagCompound.getInteger(TIER);
        this.entName = new ResourceLocation(tagCompound.getString(ENT_NAME));
        this.activeTime = tagCompound.getInteger(ACTIVE_TIME);
        this.owner = tagCompound.getString(OWNER);
        stackHandler.deserializeNBT(tagCompound.getCompoundTag(ITEMS));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger(TIER, tier);
        tagCompound.setString(ENT_NAME, entName.toString());
        tagCompound.setInteger(ACTIVE_TIME, activeTime);
        tagCompound.setString(OWNER, owner);
        tagCompound.setTag(ITEMS, stackHandler.serializeNBT());
        return tagCompound;
    }

    @Override
    public boolean canSpawn() {
        if (tier == 0)
            return false;

        if (TierHandler.getTier(tier).equals(TierHandler.BLANK_TIER))
            return false;

        if (ConfigHandler.requireOwnerOnline && !Utils.isOwnerOnline(owner))
            return false;

        if (!ConfigHandler.entityList.contains(entName) && !ConfigHandler.enableBlacklistedSpawning)
            return false;

        if (!checkRedstone())
            return false;

        if (TierHandler.checksPlayer(tier) && !isPlayerClose())
            return false;

        if (!EntityMapper.isEntityValid(entName))
            return false;

        return true;
    }

    @Override
    public int getCooldown() {
        return TierHandler.getCooldown(tier) * (ConfigHandler.cooldownUsesSeconds ? 20 : 1);
    }

    @Override
    public int getSpawnAmount() {
        return TierHandler.getSpawnAmount(tier);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public final SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -999, writeToNBT(new NBTTagCompound()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public final NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public final void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public void reset() {
        stackHandler.setStackInSlot(0, ItemStack.EMPTY);
        setTier(0);
        setEntName(Utils.EMPTY_ENT);
        setActiveTime(0);
        setOwner("");
    }

    public boolean getActiveState() {
        IBlockState state = getWorld().getBlockState(getPos());
        if (!state.getProperties().containsKey(SoulShardsAPI.ACTIVE))
            return false;
        return state.getValue(SoulShardsAPI.ACTIVE);
    }

    public void setActiveState(boolean activeState) {
        getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(SoulShardsAPI.ACTIVE, activeState));
    }

    private void spawnEntities(int amount, ResourceLocation entName) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < amount; i++) {
            for (int attempts = 0; attempts < 5; attempts++) {

                double x = getPos().getX() + (getWorld().rand.nextDouble() - getWorld().rand.nextDouble()) * 4.0D;
                double y = getPos().getY() + getWorld().rand.nextInt(3) - 1;
                double z = getPos().getZ() + (getWorld().rand.nextDouble() - getWorld().rand.nextDouble()) * 4.0D;
                BlockPos spawnAt = new BlockPos(x, y, z);

                EntityLiving entityLiving = (EntityLiving) EntityList.createEntityByIDFromName(entName, getWorld());
                if (entityLiving == null)
                    return;

                if (TierHandler.checksLight(tier) && !canSpawnInLight(entityLiving, entityLiving.getPosition()))
                    continue;

                entityLiving.setLocationAndAngles(spawnAt.getX(), spawnAt.getY(), spawnAt.getZ(), MathHelper.wrapDegrees(getWorld().rand.nextFloat() * 360F), 0F);
                entityLiving.getEntityData().setBoolean(SSTOW, true);
                entityLiving.forceSpawn = true;
                entityLiving.enablePersistence();

                if (canSpawnAtCoords(entityLiving) && !entityLiving.isDead && !hasReachedSpawnCap(entityLiving)) {
                    if (!ConfigHandler.enableBosses && !entityLiving.isNonBoss())
                        continue;

                    CageSpawnEvent event = new CageSpawnEvent(stackHandler.getStackInSlot(0), getOwner(), entityLiving);
                    if (MinecraftForge.EVENT_BUS.post(event))
                        continue;

                    getWorld().spawnEntity(entityLiving);
                    entityLiving.onInitialSpawn(getWorld().getDifficultyForLocation(spawnAt), null);
                    break;
                }
            }
        }
        SoulShardsTOW.debug("Spawned {} entities in {}", amount, stopwatch.stop());
    }

    private boolean canSpawnAtCoords(EntityLiving ent) {
        return ent.isNotColliding();
    }

    private boolean isRedstoned() {
        return getWorld().isBlockPowered(getPos());
    }

    private boolean canSpawnInLight(EntityLiving entityLiving, BlockPos pos) {
        return !(entityLiving instanceof EntityMob || entityLiving instanceof IMob) || Utils.getBlockLightLevel(getWorld(), pos, getWorld().isDaytime()) <= 8;
    }

    private boolean isPlayerClose() {
        return getWorld().isAnyPlayerWithinRangeAt(getPos().getX(), getPos().getY(), getPos().getZ(), 16D);
    }

    private boolean checkRedstone() {
        if (ConfigHandler.forceRedstoneRequirement)
            return isRedstoned();

        if (TierHandler.checksRedstone(getTier()) && isRedstoned())
            return false;

        return true;
    }

    private boolean hasReachedSpawnCap(EntityLiving living) {
        AxisAlignedBB box = new AxisAlignedBB(getPos().getX() - 16, getPos().getY() - 16, getPos().getZ() - 16, getPos().getX() + 16, getPos().getY() + 16, getPos().getZ() + 16);

        int mobCount = 0;

        for (EntityLiving entity : getWorld().getEntitiesWithinAABB(living.getClass(), box))
            if (Utils.isCageBorn(entity))
                mobCount++;

        return mobCount >= ConfigHandler.spawnCap;
    }

    public void dropItems() {
        ItemStack stack = stackHandler.getStackInSlot(0);
        if (stack.isEmpty())
            return;

        InventoryHelper.spawnItemStack(getWorld(), getPos().getX() - 0.5D, getPos().getY(), getPos().getZ(), stack);
    }

    public int getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(int activeTime) {
        this.activeTime = activeTime;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public ResourceLocation getEntName() {
        return entName;
    }

    public void setEntName(ResourceLocation entName) {
        this.entName = entName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ItemStackHandler getStackHandler() {
        return stackHandler;
    }
}
