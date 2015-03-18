package com.whammich.sstow.events;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Register;

import net.minecraft.item.Item;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraft.item.ItemStack;

public class AchievementEvents {

	// Soul Cage Crafting Event
	@SubscribeEvent
	public void CageCrafted(ItemCraftedEvent event) {
		// System.out.println("Register Soul Cage Event");
		if (event.crafting.getItem() == Item
				.getItemFromBlock(Register.BlockCage)) {
			event.player.addStat(Achievements.soulcage, 1);
		} else {
			return;
		}
	}

	// Corrupted Essence Crafting Event
	@SubscribeEvent
	public void corruptedCrafted(ItemCraftedEvent event) {
		if (!Loader.isModLoaded("Natura")) {
			// System.out.println("Register Corrupted Crafted Event");
			if (event.crafting.equals(new ItemStack(Register.ItemMaterials, 1, 4))) {
				event.player.addStat(Achievements.corruption, 1);
			} else {
				return;
			}
		} else {
			return;
		}
	}

	// Corrupted Essence Smelting Event (Natura Detected)
	@SubscribeEvent
	public void corruptedSmelted(ItemSmeltedEvent event) {
		if (Loader.isModLoaded("Natura")) {
			// System.out.println("Register Corrupted Smelted Event");
			if (event.smelting.equals(new ItemStack(Register.ItemMaterials, 1, 4))) {
				event.player.addStat(Achievements.corruption, 1);
			} else {
				return;
			}
		} else {
			return;
		}
	}

	// Soul Forge Crafting Event
	@SubscribeEvent
	public void forgeCrafted(ItemCraftedEvent event) {
		// System.out.println("Register Soul Forge Event");
		if (event.crafting.getItem() == Item
				.getItemFromBlock(Register.BlockForge)) {
			event.player.addStat(Achievements.soulforge, 1);
		} else {
			return;
		}
	}

	// Vile Dust Smelting Event
	@SubscribeEvent
	public void VileSmelted(ItemSmeltedEvent event) {
		if (!Loader.isModLoaded("Natura")) {
			// System.out.println("Register Vile Smelt Event");
			if (event.smelting.equals(new ItemStack(Register.ItemMaterials, 1, 3))) {
				event.player.addStat(Achievements.viledust, 1);
			} else {
				return;
			}
		} else {
			return;
		}
	}

	// Vile Dust Crafting Event (Natura Detected)
	@SubscribeEvent
	public void VileCrafted(ItemCraftedEvent event) {
		if (Loader.isModLoaded("Natura")) {
			// System.out.println("Register Vile Craft Event");
			if (event.crafting.equals(new ItemStack(Register.ItemMaterials, 1, 3))) {
				event.player.addStat(Achievements.viledust, 1);
			} else {
				return;
			}
		} else {
			return;
		}
	}

	// Soul Shard Pick Up Event (Easy Mode)
	@SubscribeEvent
	public void ShardPickup(ItemPickupEvent event) {
		// if (Config.EASYMODE == true) {
		// System.out.println("Register Shard Pickup Event");
		if (event.pickedUp.getEntityItem().getItem()
				.equals(Register.ItemShardSoul)) {
			event.player.addStat(Achievements.unboundshard, 1);
		} else {
			return;
		}
		// } else {
		return;
		// }
	}

	// Soul Shard Smelting Event
	@SubscribeEvent
	public void ShardSmelt(ItemSmeltedEvent event) {
		if (Config.EASYMODE == false) {
			// System.out.println("Register Shard Smelt Event");
			if (event.smelting.getItem().equals(Register.ItemShardSoul)) {
				event.player.addStat(Achievements.unboundshard, 1);
			} else {
				return;
			}
		} else {
			return;
		}
	}

}
