package com.whammich.sstow.util;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;
import java.util.Map;

public final class EntityMapper {

    public static List<ResourceLocation> entityList = Lists.newArrayList();
    public static Map<ResourceLocation, String> translationMap = Maps.newHashMap();

    public static void mapEntities() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (Map.Entry<ResourceLocation, EntityEntry> entry : ForgeRegistries.ENTITIES.getEntries()) {
            Class<? extends Entity> entClass = entry.getValue().getEntityClass();

            if (entClass == null)
                continue;

            if (entityList.contains(entry.getKey())) {
                SoulShardsTOW.LOGGER.info("Already mapped, skipping {}", entry);
                continue;
            }

            if (EntityLiving.class.isAssignableFrom(entClass)) {
                translationMap.put(entry.getKey(), entry.getValue().getName());
                entityList.add(entry.getKey());
            }
        }

        ConfigHandler.handleEntityList("Entity List");
        SoulShardsTOW.LOGGER.info("Finished mapping, found {} entities in {}", entityList.size(), stopwatch.stop());
    }

    public static boolean isEntityValid(ResourceLocation entName) {
        return ConfigHandler.entityList.contains(entName);
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends EntityLiving> getLivingClass(ResourceLocation entName) {
        return (Class<? extends EntityLiving>) ForgeRegistries.ENTITIES.getValue(entName).getEntityClass();
    }
}