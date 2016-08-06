package com.whammich.sstow.api;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoulShardsAPI {

    /**
     * Old notation for Wither Skeleton saving. To be removed when breaking changes are made in
     * the 1.11 port.
     */
    @Deprecated
    public static final String WITHER_SKELETON_OLD = "Wither Skeleton";
    public static final String WITHER_SKELETON = "WitherSkeleton";
    public static final String STRAY = "Stray";
    public static final String HUSK = "Husk";

    public static final IEntityHandler DEFAULT_ENTITY_HANDLER = new IEntityHandler() {
        @Nullable
        @Override
        public ActionResult<? extends EntityLiving> handleLiving(World world, String entityName, BlockPos pos) {
            EntityLiving living = (EntityLiving) EntityList.createEntityByName(entityName, world);
            if (living == null)
                return null;
            living.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), MathHelper.wrapDegrees(world.rand.nextFloat() * 360F), 0F);
            living.onInitialSpawn(world.getDifficultyForLocation(pos), null);
            return ActionResult.newResult(EnumActionResult.SUCCESS, living);
        }
    };

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    private static final Map<Class<? extends EntityLiving>, IEntityHandler> ENTITY_HANDLER_MAP = new HashMap<Class<? extends EntityLiving>, IEntityHandler>();
    private static ArrayList<String> entityBlacklist = new ArrayList<String>();

    /**
     * Blacklists an entity from being bound and spawned.
     * <p/>
     * IMC: {@code FMLInterModComs.sendMessage("SoulShardsTOW", "blacklistEntity", EntityClass.getCanonicalName())}
     * Example: {@code FMLInterModComs.sendMessage("SoulShardsTOW", "blacklistEntity", EntityWolf.class.getCanonicalName())}
     *
     * @param className - The canonical name of the Entity Class
     */
    public static void blacklistEntity(String className) {
        if (!entityBlacklist.contains(className))
            entityBlacklist.add(className);
    }

    /**
     * {@link #blacklistEntity(String)}
     *
     * @param entityClass - The Entity class.
     */
    public static void blacklistEntity(Class<? extends EntityLiving> entityClass) {
        blacklistEntity(entityClass.getCanonicalName());
    }

    public static boolean isEntityBlacklisted(String className) {
        return entityBlacklist.contains(className);
    }

    public static boolean isEntityBlacklisted(EntityLiving living) {
        return isEntityBlacklisted(living.getClass().getCanonicalName());
    }

    public static void registerEntityHandler(Class<? extends EntityLiving> entityClass, IEntityHandler entityHandler) {
        ENTITY_HANDLER_MAP.put(entityClass, entityHandler);
    }

    public static IEntityHandler getEntityHandler(@Nonnull Class<? extends EntityLiving> entityClass) {
        // Return exact match (EntityZombie.class -> EntityZombie.class)
        if (ENTITY_HANDLER_MAP.containsKey(entityClass))
            return ENTITY_HANDLER_MAP.get(entityClass);

        // Return handler for parent class (EntityPigZombie -> EntityZombie.class)
        for (Map.Entry<Class<? extends EntityLiving>, IEntityHandler> check : ENTITY_HANDLER_MAP.entrySet())
            if (check.getKey().isAssignableFrom(entityClass))
                return check.getValue();

        // Return default if nothing is found
        return DEFAULT_ENTITY_HANDLER;
    }
}
