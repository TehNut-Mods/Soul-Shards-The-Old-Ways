package com.whammich.sstow.util;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.IEntityHandler;
import com.whammich.sstow.api.SoulShardsAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.*;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityMapper {

    public static List<String> entityList = new ArrayList<String>();
    public static List<String> specialCases = new ArrayList<String>();
    public static Map<String, Class<? extends EntityLiving>> specialToClass = new HashMap<String, Class<? extends EntityLiving>>();

    public static void mapEntities() {
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

        addSpecialCase(SoulShardsAPI.WITHER_SKELETON_OLD, EntitySkeleton.class);
        addSpecialCase(SoulShardsAPI.WITHER_SKELETON, EntitySkeleton.class);
        addSpecialCase(SoulShardsAPI.STRAY, EntitySkeleton.class);
        addSpecialCase(SoulShardsAPI.HUSK, EntityZombie.class);
        addSpecialCase(SoulShardsAPI.ELDER_GUARDIAN, EntityGuardian.class);

        ConfigHandler.handleEntityList("Entity List");
        SoulShardsTOW.instance.getLogHelper().info("Finished mapping, found {} entities in {}", entityList.size(), stopwatch.stop());
    }

    public static void initHandlers() {
        SoulShardsAPI.registerEntityHandler(EntityZombie.class, new IEntityHandler() {
            @Nullable
            @Override
            public ActionResult<? extends EntityLiving> handleLiving(World world, String entityName, BlockPos pos) {
                if (entityName.equals(SoulShardsAPI.HUSK)) {
                    EntityZombie zombie = new EntityZombie(world);
                    zombie.setZombieType(ZombieType.HUSK);
                    zombie.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    return ActionResult.newResult(EnumActionResult.SUCCESS, zombie);
                }

                return SoulShardsAPI.DEFAULT_ENTITY_HANDLER.handleLiving(world, entityName, pos);
            }
        });

        SoulShardsAPI.registerEntityHandler(EntityGuardian.class, new IEntityHandler() {
            @Nullable
            @Override
            public ActionResult<? extends EntityLiving> handleLiving(World world, String entityName, BlockPos pos) {
                if (entityName.equals(SoulShardsAPI.ELDER_GUARDIAN)) {
                    EntityGuardian guardian = new EntityGuardian(world);
                    guardian.setElder();
                    guardian.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    return ActionResult.newResult(EnumActionResult.SUCCESS, guardian);
                }

                return SoulShardsAPI.DEFAULT_ENTITY_HANDLER.handleLiving(world, entityName, pos);
            }
        });

        SoulShardsAPI.registerEntityHandler(EntitySkeleton.class, new IEntityHandler() {
            @Nullable
            @Override
            public ActionResult<? extends EntityLiving> handleLiving(World world, String entityName, BlockPos pos) {
                if (entityName.equals(SoulShardsAPI.WITHER_SKELETON) || entityName.equals(SoulShardsAPI.WITHER_SKELETON_OLD)) {
                    EntitySkeleton skeleton = new EntitySkeleton(world);
                    skeleton.onInitialSpawn(world.getDifficultyForLocation(pos), null);
                    skeleton.setSkeletonType(SkeletonType.WITHER);
                    skeleton.tasks.addTask(4, (EntityAIBase) ObfuscationReflectionHelper.getPrivateValue(EntitySkeleton.class, skeleton, "aiAttackOnCollide", "field_85038_e"));
                    skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    skeleton.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
                    skeleton.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    return ActionResult.newResult(EnumActionResult.PASS, skeleton);
                }

                if (entityName.equals(SoulShardsAPI.STRAY)) {
                    EntitySkeleton skeleton = new EntitySkeleton(world);
                    skeleton.setSkeletonType(SkeletonType.STRAY);
                    skeleton.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    return ActionResult.newResult(EnumActionResult.SUCCESS, skeleton);
                }

                return SoulShardsAPI.DEFAULT_ENTITY_HANDLER.handleLiving(world, entityName, pos);
            }
        });
    }

    public static boolean isEntityValid(String entName) {
        return ConfigHandler.entityList.contains(entName);
    }

    public static void addSpecialCase(String name, Class<? extends EntityLiving> homeClass) {
        specialCases.add(name);
        entityList.add(name);
        specialToClass.put(name, homeClass);
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends EntityLiving> getLivingClass(String entName) {
        if (specialCases.contains(entName))
            return specialToClass.get(entName);
        return (Class<? extends EntityLiving>) EntityList.NAME_TO_CLASS.get(entName);
    }
}