package com.whammich.sstow.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.VillagerRegistry;

public final class EntityMapper {
	public static List<String> entityList = new ArrayList<String>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void init() {
		for (Map.Entry<Class, String> entry : ((HashMap<Class, String>) EntityList.classToStringMapping)
				.entrySet()) {

			if (entityList.contains(entry.getValue())) {
				ModLogger.logInfo("Skipping mapping for " + entry.getValue()
						+ ": already mapped.");
				continue;
			}

			if (IBossDisplayData.class.isAssignableFrom(entry.getKey())) {
				ModLogger.logInfo("Skipped mapping for " + entry.getValue()
						+ ": detected as boss.");
				continue;
			}

			if (EntityLiving.class.isAssignableFrom(entry.getKey())) {
				entityList.add(entry.getValue());
			}
		}

		entityList.add("Wither Skeleton");

		ModLogger.logInfo("Finished entity mapping (" + entityList.size()
				+ " entries).");
	}

	public static boolean isEntityValid(String entName) {
		return entityList.contains(entName);
	}

	public static EntityLiving getNewEntityInstance(World world, String ent) {
		if (ent.equals("Wither Skeleton")) {
			EntitySkeleton skele = new EntitySkeleton(world);
			skele.setSkeletonType(1);
			return skele;
		}
		if (ent.equals("Villager")) {
			EntityVillager villager = new EntityVillager(world);
			VillagerRegistry.applyRandomTrade(villager, villager.worldObj.rand);
			return villager;
		}
		return (EntityLiving) EntityList.createEntityByName(ent, world);
	}
}
