package moze_intel.ssr.events;

import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.item.Item;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class CreateCageEvent {
	@SubscribeEvent
	public void SomethingCrafted(ItemCraftedEvent event) {
		if (event.crafting.getItem() == Item
				.getItemFromBlock(ObjHandler.SOUL_CAGE)) {
			event.player.addStat(SSRAchievement.achievementCage, 1);
		}
	}
}
