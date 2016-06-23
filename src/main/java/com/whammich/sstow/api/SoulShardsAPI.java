package com.whammich.sstow.api;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.entity.EntityLiving;

import java.util.ArrayList;

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

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
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
}
