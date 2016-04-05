package com.whammich.sstow.api;

/**
 * Primarily for internal implementation only. Added to API as a way to instanceof check TE's for the Cage
 * as well as to obtain some basic informaton from it.
 * <p>
 * If you want to create your own Soul Cage, you will need to extend TileEntityCage and override
 * the methods here. Inserting/Withdrawing the shard is handled by the {@link net.minecraftforge.event.entity.player.PlayerInteractEvent}
 * found in {@link com.whammich.sstow.block.BlockCage}.
 * <p>
 * For your block, use the {@link SoulShardsAPI#ACTIVE} property. You don't necessarily need to use it, but that is
 * how the on/off state of the Tile is determined.
 * <p>
 * The javadocs here are directed towards users who wish to create their own Soul Cages.
 */
public interface ISoulCage {

    /**
     * Whether or not the Soul Cage is in a state during which spawning is viable.
     * <p>
     * This is checked every tick on the server only.
     * If this returns false, {@link SoulShardsAPI#ACTIVE} is set to false.
     *
     * @return - If the Soul Cage is in a state during which spawning is viable.
     */
    boolean canSpawn();

    /**
     * The time in ticks to wait between spawn attempts.
     * <p>
     * The calculation for this is
     * {@code {@link com.whammich.sstow.tile.TileEntityCage#getActiveTime()} % getCooldown() == 0}.
     *
     * @return - The time in ticks to wait between spawn attempts.
     */
    int getCooldown();

    /**
     * The amount of mobs to try to spawn at once. Each mob is attempted 5 times before giving up
     * and moving onto the next one.
     *
     * @return - The amount of mobs to try to spawn at once.
     */
    int getSpawnAmount();
}
