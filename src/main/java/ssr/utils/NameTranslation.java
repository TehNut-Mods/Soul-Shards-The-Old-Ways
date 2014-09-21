package ssr.utils;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.StatCollector;

public class NameTranslation 
{
	public static String entityName(EntityLiving ent)
	{
		String s = EntityList.getEntityString(ent);
		String name = StatCollector.translateToLocal("entity." + s + ".name");
		return name;
	}
	
	public static String entityName(String unlocalName)
	{
		return StatCollector.translateToLocal("entity." + unlocalName + ".name");
	}
}
