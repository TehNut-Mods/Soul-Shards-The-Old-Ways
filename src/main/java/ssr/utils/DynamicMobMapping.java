package ssr.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import ssr.SSRCore;

public class DynamicMobMapping {
	public static List<String> entityList = new ArrayList();
	private static List<String> entBlackList = new ArrayList();
	private static World worldObj;

	public static void init(World world) {
		worldObj = world;
		LoadBlacklist();
		FillList();
	}

	private static void FillList() {
		Map<String, Class> map = EntityList.stringToClassMapping;
		Iterator<String> iter = map.keySet().iterator();

		while (iter.hasNext()) {
			String id = iter.next();
			if (id.equals("Monster") || id.equals("Mob"))
				continue;
			Entity ent = CreateEntityFromName(
					(Class) EntityList.stringToClassMapping.get(id), id);
			if (ent instanceof EntityLiving) {
				EntityLiving eLiving = (EntityLiving) ent;
				String name = eLiving.getCommandSenderName();

				if (!entBlackList.contains(name))
					entityList.add(name);
			}
		}
		entityList.add("Wither Skeleton");
	}

	private static void LoadBlacklist() {
		entBlackList.add("Ender Dragon");
		entBlackList.add("Giant");
		entBlackList.add("Monster");
		entBlackList.add("Wither");
	}

	/* Yes, minecraft already has this, but I wanted custom exception handling */
	private static Entity CreateEntityFromName(Class entClass, String name) {
		if (!EntityLiving.class.isAssignableFrom(entClass))
			return null;
		Entity entity = null;
		try {
			Constructor c = entClass.getConstructor(World.class);
			entity = (EntityLiving) c.newInstance(worldObj);
		} catch (Exception e) {
			SSRCore.SoulLog.fatal("SSR: Skipping entity mapping for: " + name);
			SSRCore.SoulLog.fatal("Please report this to dev: " + e.toString());
		}
		return entity;
	}
}
