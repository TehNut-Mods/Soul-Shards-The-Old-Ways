package com.whammich.sstow.util;

import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EntityMapper {

    public static List<String> entityList = new ArrayList<String>();

    public static void init() {
        for (Map.Entry<Class<? extends Entity>, String> entry : EntityList.classToStringMapping.entrySet()) {
            if (entityList.contains(entry.getValue())) {
                SoulShardsTOW.instance.getLogHelper().info("Already mapped, skipping {}", entry.getValue());
                continue;
            }

            if (IBossDisplayData.class.isAssignableFrom(entry.getKey()) && !ConfigHandler.enableBosses) {
                SoulShardsTOW.instance.getLogHelper().info("Boss detected, skipping {}", entry.getValue());
                continue;
            }

            if (EntityLiving.class.isAssignableFrom(entry.getKey()))
                entityList.add(entry.getValue());
        }

        entityList.add("Wither Skeleton");

        SoulShardsTOW.instance.getLogHelper().info("Finished mapping, entities found: {}", entityList.size());
        ConfigHandler.handleEntityList("Entity List");
    }

    public static boolean isEntityValid(String entName) {
        return ConfigHandler.entityList.contains(entName);
    }

    public static EntityLiving getNewEntityInstance(World world, String ent, BlockPos pos) {
        if (Strings.isNullOrEmpty(ent))
            return null;

        if (ent.equals("Wither Skeleton")) {
            EntitySkeleton skele = new EntitySkeleton(world);
            skele.setSkeletonType(1);
            return skele;
        }

        if (ent.equals("Villager")) {
            EntityVillager villager = new EntityVillager(world);
            VillagerRegistry.setRandomProfession(villager, villager.worldObj.rand);
            return villager;
        }

        EntityLiving spawnedEntity = (EntityLiving) EntityList.createEntityByName(ent, world);
        // This will ensure custom handlers from other mods that have custom initialization logic will be called properly.
        spawnedEntity.onInitialSpawn(world.getDifficultyForLocation(pos), null);

        return spawnedEntity;
    }
}