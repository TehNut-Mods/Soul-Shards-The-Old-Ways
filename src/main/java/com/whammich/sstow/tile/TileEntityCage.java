package com.whammich.sstow.tile;

import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.registry.ModItems;
import com.whammich.sstow.util.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;

@Getter
@Setter
public class TileEntityCage extends TileInventory implements ITickable {

    private int activeTime;
    private int tier;
    private String entName = "";

    public TileEntityCage() {
        super(1, "cage");
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        if (tier == 0 || Strings.isNullOrEmpty(entName)) {
            setActiveState(false);
            return;
        }

        if (!ConfigHandler.entityList.contains(entName) && !ConfigHandler.enableBlacklistedSpawning) {
            setActiveState(false);
            return;
        }

        if (TierHandler.getChecksRedstone(tier - 1) && isRedstoned()) {
            setActiveState(false);
            return;
        }

        if (TierHandler.getChecksPlayer(tier - 1) && !isPlayerClose()) {
            setActiveState(false);
            return;
        }

        if (SoulShardsAPI.isEntityBlacklisted(EntityMapper.getNewEntityInstance(getWorld(), entName, getPos()))) {
            setActiveState(false);
            return;
        }

        activeTime++;
        setActiveState(true);

        if (activeTime % (TierHandler.getCooldown(tier - 1) * 20) == 0) {
            EntityLiving[] toSpawn = new EntityLiving[TierHandler.getNumSpawns(tier - 1)];

            for (int i = 0; i < toSpawn.length; i++) {
                toSpawn[i] = EntityMapper.getNewEntityInstance(getWorld(), entName, getPos());

                toSpawn[i].getEntityData().setBoolean("SSTOW", true);
                toSpawn[i].forceSpawn = true;
                toSpawn[i].enablePersistence();
            }

            spawnEntities(toSpawn);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        this.tier = tagCompound.getInteger("tier");
        this.entName = tagCompound.getString("entName");
        this.activeTime = tagCompound.getInteger("activeTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("tier", tier);
        tagCompound.setString("entName", entName);
        tagCompound.setInteger("activeTime", activeTime);
    }

    public void setActiveState(boolean activeState) {
        getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockCage.ACTIVE, activeState));
    }

    public boolean getActiveState() {
        return getWorld().getBlockState(getPos()).getValue(BlockCage.ACTIVE);
    }

    private boolean canSpawnAtCoords(EntityLiving ent) {
        return worldObj.getCollidingBoundingBoxes(ent, ent.getEntityBoundingBox()).isEmpty();
    }

    private void spawnEntities(EntityLiving[] ents) {
        for (EntityLiving ent : ents) {
            int counter = 0;

            if (TierHandler.getChecksLight(tier - 1) && !canSpawnInLight(ent))
                break;

            do {
                counter++;
                if (counter >= 5) {
                    ent.setDead();
                    break;
                }

                double x = getPos().getX() + (getWorld().rand.nextDouble() - getWorld().rand.nextDouble()) * 4.0D;
                double y = getPos().getY() + getWorld().rand.nextInt(3) - 1;
                double z = getPos().getZ() + (getWorld().rand.nextDouble() - getWorld().rand.nextDouble()) * 4.0D;
                ent.setLocationAndAngles(x, y, z, getWorld().rand.nextFloat() * 360.0F, 0.0F);
            } while (!canSpawnAtCoords(ent) || counter >= 5);

            if (!ent.isDead && !hasReachedSpawnCap(ent))
                getWorld().spawnEntityInWorld(ent);
        }
    }

    private boolean isRedstoned() {
        return getWorld().isBlockPowered(getPos());
    }

    private boolean canSpawnInLight(EntityLiving entityLiving) {
        return !(entityLiving instanceof EntityMob || entityLiving instanceof IMob) || Utils.getBlockLightLevel(getWorld(), getPos(), getWorld().isDaytime()) <= 8;
    }

    private boolean isPlayerClose() {
        return getWorld().getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 16D) != null;
    }

    private boolean hasReachedSpawnCap(EntityLiving living) {
        AxisAlignedBB box = AxisAlignedBB.fromBounds(getPos().getX() - 16, getPos().getY() - 16, getPos().getZ() - 16, getPos().getX() + 16, getPos().getY() + 16, getPos().getZ() + 16);

        int mobCount = 0;

        for (EntityLiving entity : getWorld().getEntitiesWithinAABB(living.getClass(), box))
            if (entity.getEntityData().getBoolean("SSTOW"))
                mobCount++;

        return mobCount > ConfigHandler.spawnCap;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() == ModItems.getItem(ItemSoulShard.class) && Utils.getShardTier(stack) > 0 && Utils.isShardBound(stack);
    }
}
