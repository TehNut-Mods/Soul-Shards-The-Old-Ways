package moze_intel.ssr.events;

import net.minecraft.item.ItemStack;
import moze_intel.ssr.events.SSRAchievement;
import moze_intel.ssr.gameObjs.ObjHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class ShardPickup {
	
	
	
	@SubscribeEvent
	public void pickupShard(PlayerEvent.ItemPickupEvent e) {
		if (e.pickedUp.getEntityItem().isItemEqual(
				new ItemStack(ObjHandler.SOUL_SHARD))) {
			e.player.addStat(SSRAchievement.achievementShard, 1);
		}

		if (e.pickedUp.getEntityItem().isItemEqual(
				new ItemStack(ObjHandler.SOUL_CAGE, 1))) {
			e.player.addStat(SSRAchievement.achievementCage, 1);
		}
	}

}
