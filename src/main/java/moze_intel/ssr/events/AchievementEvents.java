package moze_intel.ssr.events;

import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.item.Item;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;

public class AchievementEvents {
	@SubscribeEvent
	public void CageCrafted(ItemCraftedEvent event) {
		if (event.crafting.getItem() == Item
				.getItemFromBlock(ObjHandler.SOUL_CAGE)) {
			event.player.addStat(SSRAchievement.achievementCage, 1);
		}
	}

	@SubscribeEvent
	public void corruptedCrafted(ItemCraftedEvent event) {
		if (event.crafting.getItem().equals(ObjHandler.CORRUPTED_ESSENCE)) {
			event.player.addStat(SSRAchievement.corruption, 1);
		}
	}

	@SubscribeEvent
	public void forgeCrafted(ItemCraftedEvent event) {
		if (event.crafting.getItem() == Item
				.getItemFromBlock(ObjHandler.SOUL_FORGE)) {
			event.player.addStat(SSRAchievement.soulForge, 1);
		}
	}

	@SubscribeEvent
	public void VileSmelted(ItemSmeltedEvent event) {
		if (event.smelting.getItem().equals(ObjHandler.VILE_DUST)) {
			event.player.addStat(SSRAchievement.theoldWays, 1);
		}
	}

	@SubscribeEvent
	public void ShardPickUp(ItemPickupEvent event) {
		if (event.pickedUp.getEntityItem().getItem()
				.equals(ObjHandler.SOUL_SHARD)) {
			event.player.addStat(SSRAchievement.achievementShard, 1);
		}
	}

}
