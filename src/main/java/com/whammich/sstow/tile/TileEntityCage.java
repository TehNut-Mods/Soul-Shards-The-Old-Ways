package com.whammich.sstow.tile;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulCage;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.api.event.CageSpawnEvent;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tehnut.lib.annot.Handler;
import tehnut.lib.annot.Used;
import tehnut.lib.util.helper.ItemHelper;

@Getter
@Setter
@Handler
public class TileEntityCage extends TileInventory implements ITickable, ISoulCage {

    public static final String TIER = "tier";
    public static final String ACTIVE_TIME = "activeTime";
    public static final String ENT_NAME = "entName";
    public static final String OWNER = "owner";
    public static final String SSTOW = "SSTOW";

    private int activeTime;
    private int tier;
    private String entName = "";
    private String owner = "";

    public TileEntityCage() {
        super(1, "cage");
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        if (getStackInSlot(0) != null) {
            ItemStack shard = getStackInSlot(0);
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
        this.entName = tagCompound.getString(ENT_NAME);
        this.activeTime = tagCompound.getInteger(ACTIVE_TIME);
        this.owner = tagCompound.getString(OWNER);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger(TIER, tier);
        tagCompound.setString(ENT_NAME, entName);
        tagCompound.setInteger(ACTIVE_TIME, activeTime);
        tagCompound.setString(OWNER, owner);
        return tagCompound;
    }

    @Override
    public boolean canSpawn() {
        if (tier == 0 || Strings.isNullOrEmpty(entName))
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

        if (!EntityMapper.specialCases.contains(entName))
            if (SoulShardsAPI.isEntityBlacklisted(EntityList.NAME_TO_CLASS.get(entName).getCanonicalName()))
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

    public void reset() {
        clear();
        setTier(0);
        setEntName("");
        setActiveTime(0);
        setOwner("");
    }

    public boolean getActiveState() {
        Block b = getWorld().getBlockState(getPos()).getBlock();
        if(!b.getBlockState().getProperties().contains(SoulShardsAPI.ACTIVE))
            return false;
        return getWorld().getBlockState(getPos()).getValue(SoulShardsAPI.ACTIVE);
    }

    public void setActiveState(boolean activeState) {
        getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(SoulShardsAPI.ACTIVE, activeState));
    }

    private void spawnEntities(int amount, String entName) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < amount; i++) {

            for (int attempts = 0; attempts < 5; attempts++) {

                double x = getPos().getX() + (getWorld().rand.nextDouble() - getWorld().rand.nextDouble()) * 4.0D;
                double y = getPos().getY() + getWorld().rand.nextInt(3) - 1;
                double z = getPos().getZ() + (getWorld().rand.nextDouble() - getWorld().rand.nextDouble()) * 4.0D;
                BlockPos spawnAt = new BlockPos(x, y, z);

                ActionResult<? extends EntityLiving> handlerResult = SoulShardsAPI.getEntityHandler(EntityMapper.getLivingClass(entName)).handleLiving(getWorld(), entName, spawnAt);
                if (handlerResult.getType() == EnumActionResult.FAIL)
                    continue;

                EntityLiving entityLiving = handlerResult.getResult();

                if (TierHandler.checksLight(tier) && !canSpawnInLight(entityLiving, entityLiving.getPosition()))
                    continue;

                entityLiving.setLocationAndAngles(entityLiving.posX, entityLiving.posY, entityLiving.posZ, MathHelper.wrapDegrees(getWorld().rand.nextFloat() * 360F), 0F);
                entityLiving.getEntityData().setBoolean(SSTOW, true);
                entityLiving.forceSpawn = true;
                entityLiving.enablePersistence();

                if (canSpawnAtCoords(entityLiving) && !entityLiving.isDead && !hasReachedSpawnCap(entityLiving)) {
                    if (!ConfigHandler.enableBosses && !entityLiving.isNonBoss())
                        continue;

                    CageSpawnEvent event = new CageSpawnEvent(getStackInSlot(0), getOwner(), entityLiving);
                    if (MinecraftForge.EVENT_BUS.post(event))
                        continue;

                    getWorld().spawnEntityInWorld(entityLiving);
                    if (handlerResult.getType() == EnumActionResult.SUCCESS)
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

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() == ItemHelper.getItem(ItemSoulShard.class) && ShardHelper.getTierFromShard(stack) > 0 && ShardHelper.isBound(stack);
    }

    @SubscribeEvent
    @Used
    public void onDeath(LivingExperienceDropEvent event) {
        if (!ConfigHandler.enableExperienceDrop && Utils.isCageBorn(event.getEntityLiving()))
            event.setDroppedExperience(0);
    }
}
