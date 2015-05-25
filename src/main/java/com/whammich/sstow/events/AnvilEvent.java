package com.whammich.sstow.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import amerifrance.guideapi.api.GuideRegistry;

import com.whammich.sstow.compat.guideapi.JournalBook;
import com.whammich.sstow.item.ItemLootPage;
import com.whammich.sstow.item.ItemShardSoul;
import com.whammich.sstow.utils.TierHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AnvilEvent {
	
	@SubscribeEvent
	public void SpecialAnvil(AnvilUpdateEvent event) {

		String unlockKey;

		// If the slots are empty, do nothing
		if (event.left == null || event.right == null) {
			return;
		}

		// If the left slot is a loot page, and the right slot is the lore
		// book(s)
		if (event.left.getItem() instanceof ItemLootPage
				&& event.right.getItem() == GuideRegistry.getItemStackForBook(JournalBook.journalBook).getItem()) {

			unlockKey = event.left.stackTagCompound.getString("key");

			// Check the left slot for an NBT value, if false, return, if true
			// continue
			if (unlockKey == null) {
				return;
			}

			// Set and copy the right slot into a new instance
			ItemStack targetStack = event.right;
			ItemStack resultStack = targetStack.copy();

			// Set the unlock boolean to true for page x
			resultStack.stackTagCompound.setBoolean(unlockKey, true);

			// Place the updated book in the result slot
			event.output = resultStack;

			// Charge the player 5 XP levels
			event.cost = 5;
		}
		
		// If the left and right slots are soul shards proceed
		if(event.left.getItem() instanceof ItemShardSoul && event.right.getItem() instanceof ItemShardSoul) {
			byte leftTier = event.left.stackTagCompound.getByte("Tier");
			byte rightTier = event.right.stackTagCompound.getByte("Tier");
			short killResult = (short) (event.left.stackTagCompound.getShort("KillCount") 
					+ event.right.stackTagCompound.getShort("KillCount"));
			
			String killLeft = event.left.stackTagCompound.getString("Entity");
			String killRight = event.right.stackTagCompound.getString("Entity");
			
			if(killLeft != killRight) {
				return;
			}
			
			// Is the left input tier higher than the right tier?
			if(leftTier >= rightTier || leftTier <= rightTier) {
							
				// Set and copy the right slot into a new instance
				ItemStack targetStack = event.right;
				ItemStack resultStack = targetStack.copy();

				// Set the kill count NBT with the killResult variable
				resultStack.stackTagCompound.setShort("KillCount", killResult);

				// Set the tier based on kill count
				resultStack.stackTagCompound.setByte("Tier", TierHandler.getCorrectTier(resultStack));
								
				// Tag the anvil shard with the cheater boolean (I will change this to a string)
				resultStack.stackTagCompound.setBoolean("Anviled", true);
				
				// Place the combined shard in the result slot
				event.output = resultStack;
				
				// Charge the player 30 XP levels
				event.cost = 30;
			}
			
		}
		
		
		
		
		
	}
}
