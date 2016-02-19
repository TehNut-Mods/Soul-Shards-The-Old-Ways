package com.whammich.sstow.tile;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.util.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.*;

@Getter
@Setter
public class TileEntityCage extends TileInventory implements ITickable {

    private int counter;
    private int updateCounter;
    private int tier;
    private String entName;
    private boolean redstoneActive;
    private boolean initChecks;
    private boolean active;

    public TileEntityCage() {
        super(1, "cage");
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        if (!isInitChecks()) {
            checkRedstone();
            setInitChecks(true);
        }

        if (!getWorld().getBlockState(getPos()).getValue(getCage().getBoolProp()) || getTier() <= 0) {
            setUpdateCounter(0);
            setCounter(0);
            return;
        }

        if (getUpdateCounter() == 19) {
            EntityLiving ent = EntityMapper.getNewEntityInstance(getWorld(), getEntName());

            setActive(canEntitySpawn(ent));

            setUpdateCounter(0);
        } else {
            setUpdateCounter(getUpdateCounter() + 1);
        }

        if (!active) {
            setCounter(0);
            return;
        }

        if (getCounter() >= TierHandler.getCooldown(getTier() - 1) * 20 - 1) {
            if (Config.debug)
                SoulShardsTOW.instance.getLogHelper().info("Successfully spawned: " + getEntName());

            EntityLiving[] toSpawn = new EntityLiving[TierHandler.getNumSpawns(getTier() - 1)];

            ItemStack heldItem = Utils.getEntityHeldItem(getStackInSlot(0));
            for (int i = 0; i < toSpawn.length; i++) {
                toSpawn[i] = EntityMapper.getNewEntityInstance(getWorld(), getEntName());

                if ((toSpawn[i] instanceof EntitySlime))
                    toSpawn[i].getDataWatcher().updateObject(16, 1);

                if (heldItem != null)
                    toSpawn[i].setCurrentItemOrArmor(0, heldItem);

                for (int j = 1; j <= 4; j++)
                    toSpawn[i].setCurrentItemOrArmor(j, Utils.getEntityArmor(getStackInSlot(0), j));

                toSpawn[i].getEntityData().setBoolean("SSTOW", true);
                toSpawn[i].forceSpawn = true;
                toSpawn[i].enablePersistence();
            }
            spawnEntities(toSpawn);
            counter = 0;
        } else {
            counter += 1;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        initChecks = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        setActive(tagCompound.getBoolean("active"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("active", isActive());
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
        this.getWorld().markBlockForUpdate(getPos());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(getPos(), 0, tag);
    }

    public BlockCage getCage() {
        return (BlockCage) getWorld().getBlockState(getPos()).getBlock();
    }

    public void checkRedstone() {
        setRedstoneActive(getWorld().isBlockIndirectlyGettingPowered(getPos()) > 0);
    }

    private boolean canEntitySpawn(EntityLiving ent) {
        if ((Config.floodPrevention) && (hasReachedSpawnLimit(ent)))
            return false;

        if ((TierHandler.getChecksRedstone(getTier() - 1)) && (isRedstoneActive() == Config.invertRedstone))
            return false;

        if ((TierHandler.getChecksPlayer(getTier() - 1)) && (!isPlayerClose(getPos())))
            return false;

        if ((TierHandler.getChecksLight(getTier() - 1)) && (!canSpawnInLight(ent)))
            return false;

        if ((TierHandler.getChecksWorld(getTier() - 1)) && (!canSpawnInWorld(ent)))
            return false;

        return true;
    }

    private boolean isPlayerClose(BlockPos pos) {
        return getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 16.0D) != null;
    }

    private boolean canSpawnInWorld(EntityLiving ent) {
        int dimension = getWorld().provider.getDimensionId();

        if ((ent instanceof EntitySkeleton)) {
            EntitySkeleton skele = (EntitySkeleton) ent;

            return (skele.getSkeletonType() == 1) && (dimension == -1) || dimension == 0;
        }

        if (((ent instanceof EntityBlaze)) || ((ent instanceof EntityPigZombie)) || ((ent instanceof EntityGhast)) || ((ent instanceof EntityMagmaCube)))
            return dimension == -1;

        return !(ent instanceof EntityEnderman) || dimension == 1;
    }

    private boolean canSpawnInLight(EntityLiving ent) {
        int light = getWorld().getLight(getPos());
        if (((ent instanceof EntityMob)) || ((ent instanceof IMob))) {
            return light <= 8;
        }

        return !(((ent instanceof EntityAnimal)) || ((ent instanceof IAnimals))) || light > 8;
    }

    private boolean canSpawnAtCoords(EntityLiving ent) {
        return worldObj.getCollidingBoundingBoxes(ent, ent.getEntityBoundingBox()).isEmpty();
    }

    private boolean hasReachedSpawnLimit(EntityLiving ent) {
        AxisAlignedBB aabb = AxisAlignedBB.fromBounds(getPos().getX() - 16, getPos().getY() - 16, getPos().getZ() - 16, getPos().getX() + 16, getPos().getY() + 16, getPos().getZ() + 16);
        int mobCount = 0;

        for (EntityLiving entity : getWorld().getEntitiesWithinAABB(ent.getClass(), aabb))
            if (entity.getEntityData().getBoolean("SSTOW"))
                mobCount++;

        return mobCount >= Config.maxEntities;
    }

    private void spawnEntities(EntityLiving[] ents) {
        for (EntityLiving ent : ents) {
            int counter = 0;
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

            if (!ent.isDead)
                getWorld().spawnEntityInWorld(ent);
        }
    }
}
