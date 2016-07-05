package com.whammich.sstow.util;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.SoulShardsAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.monster.ZombieType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EntityMapper {

    public static List<String> entityList = new ArrayList<String>();
    public static List<String> specialCases = new ArrayList<String>();

    public static void init() {
		Stopwatch stopwatch = Stopwatch.createStarted();
        for (Map.Entry<Class<? extends Entity>, String> entry : EntityList.CLASS_TO_NAME.entrySet()) {
            if (entityList.contains(entry.getValue())) {
                SoulShardsTOW.instance.getLogHelper().info("Already mapped, skipping {}", entry.getValue());
                continue;
            }

            if (entry.getValue().equals("Mob") || entry.getValue().equals("Monster"))
                continue;

            if (EntityLiving.class.isAssignableFrom(entry.getKey()))
                entityList.add(entry.getValue());
        }

        entityList.add(SoulShardsAPI.WITHER_SKELETON_OLD);
        specialCases.add(SoulShardsAPI.WITHER_SKELETON_OLD);
        entityList.add(SoulShardsAPI.WITHER_SKELETON);
        specialCases.add(SoulShardsAPI.WITHER_SKELETON);
        entityList.add(SoulShardsAPI.HUSK);
        specialCases.add(SoulShardsAPI.HUSK);
        entityList.add(SoulShardsAPI.STRAY);
        specialCases.add(SoulShardsAPI.STRAY);

        ConfigHandler.handleEntityList("Entity List");
		SoulShardsTOW.instance.getLogHelper().info("Finished mapping, found {} entities in {}", entityList.size(), stopwatch.stop());
	}

    public static boolean isEntityValid(String entName) {
        return ConfigHandler.entityList.contains(entName);
    }

    public static EntityLiving getNewEntityInstance(World world, String ent, BlockPos pos) {
		Stopwatch stopwatch = Stopwatch.createStarted();
        if (Strings.isNullOrEmpty(ent))
            return null;

        if (ent.equals(SoulShardsAPI.WITHER_SKELETON) || ent.equals(SoulShardsAPI.WITHER_SKELETON_OLD)) {
            EntitySkeleton skeleton = new EntitySkeleton(world);
            skeleton.setSkeletonType(SkeletonType.WITHER);
            skeleton.onInitialSpawn(world.getDifficultyForLocation(pos), null);
            return skeleton;
        }

        if (ent.equals(SoulShardsAPI.STRAY)) {
            EntitySkeleton skeleton = new EntitySkeleton(world);
            skeleton.setSkeletonType(SkeletonType.STRAY);
            skeleton.onInitialSpawn(world.getDifficultyForLocation(pos), null);
            return skeleton;
        }

        if (ent.equals(SoulShardsAPI.HUSK)) {
            EntityZombie zombie = new EntityZombie(world);
            zombie.setZombieType(ZombieType.HUSK);
            zombie.onInitialSpawn(world.getDifficultyForLocation(pos), null);
            return zombie;
        }



        EntityLiving spawnedEntity = (EntityLiving) EntityList.createEntityByName(ent, world);
        // This will ensure custom handlers from other mods that have custom initialization logic will be called properly.
        spawnedEntity.onInitialSpawn(world.getDifficultyForLocation(pos), null);

		SoulShardsTOW.instance.getLogHelper().debug("Created entity for {} in {}", ent, stopwatch.stop());
        return spawnedEntity;
    }
}