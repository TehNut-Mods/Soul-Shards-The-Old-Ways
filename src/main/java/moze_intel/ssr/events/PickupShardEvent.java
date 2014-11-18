package moze_intel.ssr.events;

import moze_intel.ssr.gameObjs.ObjHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class PickupShardEvent {
	@SubscribeEvent
	public void pickupShard(PlayerEvent.ItemPickupEvent e) {
		if (e.pickedUp.getEntityItem().getItem().equals(ObjHandler.SOUL_SHARD)) {
			e.player.addStat(SSRAchievement.achievementShard, 1);
		}
	}
}
