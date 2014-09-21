package ssr.utils;

import java.util.Iterator;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import ssr.config.MobBlackList;
import ssr.gameObjs.ObjHandler;

public class Utils 
{
	public static boolean isEntityAccepted(String entName)
	{
         return (DynamicMobMapping.entityList.contains(entName) && !MobBlackList.bList.contains(entName));
	}

	public static void hideItems()
	{
		Iterator modsIT = Loader.instance().getModList().iterator();
		ModContainer modc;
        while (modsIT.hasNext())
        {
            modc = (ModContainer) modsIT.next();
            if ("Not Enough Items".equals(modc.getName().trim()))
            {
            	ItemStack stack = new ItemStack(ObjHandler.sCage, 0, 1);
            	codechicken.nei.api.API.hideItem(stack);
            	stack.setItemDamage(2);
            	codechicken.nei.api.API.hideItem(stack);
            }
        }
	}

	public static int getEntityID(String ent)
	{
        int entId;
		if(ent == "Horse") entId = 0;
		else if(ent == "Sheep") entId = 1;
		else if(ent == "Villager") entId = 2;
		else if(ent == "Zombie") entId = 3;
		else entId = -1;
		return entId;
	}

}
