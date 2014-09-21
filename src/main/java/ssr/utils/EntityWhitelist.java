package ssr.utils;

import java.util.ArrayList;
import java.util.List;

import ssr.SSRCore;
import ssr.config.MobBlackList;
import ssr.config.SoulConfig;


public class EntityWhitelist 
{
	public static List<String> peacefuls = new ArrayList();
	public static List<String> mobs = new ArrayList();
	
	public static void init()
	{
		peacefuls.add("Pig");
		peacefuls.add("Chicken");
		peacefuls.add("Cow");
		peacefuls.add("Mooshroom");
		peacefuls.add("Sheep");
		peacefuls.add("Zombie Pigman");
		peacefuls.add("Iron Golem");
		peacefuls.add("Snow Golem");
		peacefuls.add("Villager");
		peacefuls.add("Squid");
		mobs.add("Zombie");
		mobs.add("Creeper");
		mobs.add("Skeleton");
		mobs.add("Spider");
		mobs.add("Cave Spider");
		mobs.add("Enderman");
		mobs.add("Slime");
		mobs.add("Magma Cube");
		mobs.add("Witch");
		mobs.add("Blaze");
		mobs.add("Ghast");
		mobs.add("Wither Skeleton");
		SSRCore.SoulLog.info("SSR: Loaded entity lists ("+(peacefuls.size() + mobs.size())+" entities).");
	}
	
	public static boolean isEntityAccepted(String entName)
	{
		if (peacefuls.contains(entName) && !MobBlackList.bList.contains(entName)) return true;
		else if (!SoulConfig.disallowMobs && mobs.contains(entName) && !MobBlackList.bList.contains(entName)) return true;
		else return false;
	}
}
