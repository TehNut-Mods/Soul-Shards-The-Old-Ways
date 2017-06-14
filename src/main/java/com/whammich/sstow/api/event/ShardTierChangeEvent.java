package com.whammich.sstow.api.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ShardTierChangeEvent extends Event {

    private final ItemStack shardStack;
    private final int newTier;

    /**
     * Fired when a shard changes tier.
     * <p>
     * Fired from {@link com.whammich.sstow.util.Utils#checkAndFixShard(ItemStack)}
     */
    public ShardTierChangeEvent(ItemStack shardStack, int newTier) {
        this.shardStack = shardStack;
        this.newTier = newTier;
    }

    public ItemStack getShardStack() {
        return shardStack;
    }

    public int getNewTier() {
        return newTier;
    }
}
