package com.whammich.sstow.api;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public interface IEntityHandler {

    /**
     * Handles custom spawn data for entities. If a handler for a given Entity class is not found, it will check the
     * inheritance tree. If one is still not found, {@link SoulShardsAPI#DEFAULT_ENTITY_HANDLER} will be used.
     *
     * Register with {@link SoulShardsAPI#registerEntityHandler(Class, IEntityHandler)}
     *
     * @param world      - The world to spawn the entity in
     * @param entityName - The registered name of the Entity to spawn. Use {@link net.minecraft.entity.EntityList#createEntityByName(String, World)}
     *                   to get your entity instance.
     * @param pos        - The position in the world that the Entity should be spawned at. Make sure to set your position.
     *
     * @return - An ActionResult containing the constructed entity.
     *         SUCCESS - Spawn entity, call {@link EntityLiving#onInitialSpawn(DifficultyInstance, IEntityLivingData)}
     *         PASS    - Spawn entity, do not call onInitialSpawn()
     *         FAIL    - Do not spawn an entity
     */
    ActionResult<? extends EntityLiving> handleLiving(World world, String entityName, BlockPos pos);
}
