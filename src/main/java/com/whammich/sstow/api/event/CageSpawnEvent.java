package com.whammich.sstow.api.event;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class CageSpawnEvent extends Event {

    private final ItemStack shard;
    private final int tier;
    private final String owner;
    private final EntityLiving entityLiving;

    /**
     * This event is fired whenever a mob is about to be spawned by a Soul Cage.
     *
     * Only called on the server side.
     *
     * If cancelled, the spawn does not occur.
     *
     * @param shard        - The ItemStack inside the SoulCage. Guarenteed to be an {@link com.whammich.sstow.api.ISoulShard}
     * @param tier         - The tier of the shard
     * @param owner        - The owner of the cage (UUID)
     * @param entityLiving - The entity about to be spawned.
     */
    public CageSpawnEvent(ItemStack shard, int tier, String owner, EntityLiving entityLiving) {
        this.shard = shard;
        this.tier = tier;
        this.owner = owner;
        this.entityLiving = entityLiving;
    }

    public ItemStack getShard() {
        return shard;
    }

    public int getTier() {
        return tier;
    }

    public String getOwner() {
        return owner;
    }

    public EntityLiving getEntityLiving() {
        return entityLiving;
    }
}
