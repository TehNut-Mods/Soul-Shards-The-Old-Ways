package moze_intel.ssr.events;

import net.minecraft.item.ItemStack;
import moze_intel.ssr.events.SSRAchievement;
import moze_intel.ssr.gameObjs.ObjHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class ShardPickup {
	@SubscribeEvent
	public void pickupShard(PlayerEvent.ItemPickupEvent e) {
		if (e.pickedUp.getEntityItem().getItem().equals(new ItemStack(ObjHandler.SOUL_CAGE, 0))) {
			e.player.addStat(SSRAchievement.achievementCage, 1);
		}
	}
}
