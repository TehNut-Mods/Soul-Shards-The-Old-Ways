package com.whammich.sstow.api.event;

import com.whammich.sstow.api.ShardHelper;
import lombok.Getter;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
@Getter
public class CageSpawnEvent extends Event {

    private final ItemStack shard;
    private final int tier;
    private final String owner;
    private final EntityLiving entityLiving;

    /**
     * This event is fired whenever a mob is about to be spawned by a Soul Cage.
     * <p>
     * Only called on the server side.
     * <p>
     * If cancelled, the spawn does not occur.
     *
     * @param shard        - The ItemStack inside the SoulCage. Guarenteed to be an {@link com.whammich.sstow.api.ISoulShard}
     * @param owner        - The owner of the cage (UUID)
     * @param entityLiving - The entity about to be spawned.
     */
    public CageSpawnEvent(ItemStack shard, String owner, EntityLiving entityLiving) {
        this.shard = shard;
        this.tier = ShardHelper.getTierFromShard(shard);
        this.owner = owner;
        this.entityLiving = entityLiving;
    }
}
