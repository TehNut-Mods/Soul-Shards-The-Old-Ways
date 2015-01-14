package sstow.events;

import sstow.gameObjs.ObjHandler;
import sstow.utils.SSRConfig;
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
		if (SSRConfig.EASYMODE) {
			if (event.pickedUp.getEntityItem().getItem()
					.equals(ObjHandler.SOUL_SHARD)) {
				event.player.addStat(SSRAchievement.achievementShard, 1);
			}
		} else {
		}
	}

	public void ShardPickUp(ItemSmeltedEvent event) {
		if (!SSRConfig.EASYMODE) {
			if (event.smelting.getItem().equals(ObjHandler.SOUL_SHARD)) {
				event.player.addStat(SSRAchievement.achievementShard, 1);
			}
		} else {
		}
	}

}
