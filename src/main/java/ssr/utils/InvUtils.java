package ssr.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import ssr.gameObjs.ObjHandler;

public class InvUtils {

	public static boolean HasShard(EntityPlayerMP player, String entName) {
		boolean hasFound = false;
		ItemStack[] inv = player.inventory.mainInventory;
		for (int i = 8; i >= 0; i--)
			if (isAcceptable(inv[i], entName))
				hasFound = true;
		return hasFound;
	}

	public static ItemStack getStack(EntityPlayerMP player, String entName) {
		ItemStack result = null;
		ItemStack[] inv = player.inventory.mainInventory;
		for (int i = 8; i >= 0; i--)
			if (isAcceptable(inv[i], entName))
				result = inv[i];
		return result;
	}

	private static boolean isAcceptable(ItemStack stack, String entName) {
		boolean result = false;
		if (stack != null && stack.getItem() == ObjHandler.sShard) {
			if (!stack.hasTagCompound())
				result = true;
			else {
				int kills = stack.stackTagCompound.getInteger("KillCount");
				String name = stack.stackTagCompound.getString("EntityType");
				if ((kills < TierHandling.getMax(5) || name.equals("empty"))
						&& (name.equals(entName) || name.equals("empty")))
					result = true;
			}
		}
		return result;
	}
}
