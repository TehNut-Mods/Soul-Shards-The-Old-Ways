package com.whammich.sstow.events;

import java.util.ListIterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;

import com.whammich.sstow.compat.baubles.BaubleAnimus;
import com.whammich.sstow.compat.baubles.BaubleConservo;
import com.whammich.sstow.compat.baubles.ItemGems;
import com.whammich.sstow.compat.baubles.ItemSockets;
import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BaubleEvents {

	// Amulet of Animus

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(LivingDeathEvent event) {

		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;

			if(player == null || player instanceof FakePlayer) {
				return;
			}

			InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			ItemStack stack = baubles.stackList[0];
			if (stack == null) {
				return;
			}

			if (stack.getItem() instanceof BaubleAnimus) {
				event.setCanceled(true);
				baubles.stackList[0] = null;
				PlayerHandler.setPlayerBaubles(player, baubles);

				amuletBreak(player);
				
				player.setHealth(Config.crystalHeal);

				if (Config.crystalResistEnable) {
					player.addPotionEffect(new PotionEffect(
							Potion.resistance.id,
							Config.crystalResistTimer, 5));
				}

				if (Config.crystalRegenEnable) {
					player.addPotionEffect(new PotionEffect(
							Potion.regeneration.id,
							Config.crystalRegenTimer,
							Config.crystalRegenLevel));
				}
			}
		}
	}


	// Amulet of Conservo

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerDrops(PlayerDropsEvent event) {

		// If the player is empty or a fake player, stop
		if(event.entityPlayer == null || event.entityPlayer instanceof FakePlayer) {
			return;
		}

		// If the keep inventories game rule is enabled, stop
		if(event.entityPlayer.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
			return;
		}

		InventoryBaubles conBaubles = PlayerHandler.getPlayerBaubles(event.entityPlayer);
		ItemStack conStack = conBaubles.stackList[0];
		// If the bauble slot is empty, stop
		if (conStack == null) {
			return;
		}

		// Check if the bauble is the Amulet of Conservo
		if (conStack.getItem() instanceof BaubleConservo) {
			amuletBreak(event.entityPlayer);
			// Loop through all items and add them to the player
			ListIterator<EntityItem> iter = event.drops.listIterator();
			while (iter.hasNext()) {
				System.out.println(iter.toString());
				EntityItem ei = iter.next();
				ItemStack item = ei.getEntityItem();
				addToPlayer(event.entityPlayer, item);
				iter.remove();
			}

			// Loop through the baubles if they're not empty
			if (conBaubles != null) {
				for (int i = 1; i < conBaubles.getSizeInventory(); i++) {
					ItemStack item = conBaubles.getStackInSlot(i);
					addToPlayer(event.entityPlayer, item);
					conBaubles.setInventorySlotContents(i, null);
				}
			}
		}
	}

	public void amuletBreak(EntityPlayer player) {
		InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
		baubles.stackList[0] = null;
		PlayerHandler.setPlayerBaubles(player, baubles);

		if (!player.worldObj.isRemote) {
			Minecraft
			.getMinecraft()
			.getSoundHandler()
			.playSound(
					PositionedSoundRecord.func_147674_a(
							new ResourceLocation("dig.glass"), 1.0F));
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerClone(PlayerEvent.Clone event) {
		// If it wasn't death, stop
		if(!event.wasDeath) {
			return;
		}

		// If original or player are null, and if entityPlayer is a Fake Player, stop
		if(event.original == null || event.entityPlayer == null || event.entityPlayer instanceof FakePlayer){
			return;
		}

		// If global keep inventory rule is on, stop
		if(event.entityPlayer.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
			return;
		}

		// Loop through players main inventory and add it to the clone?
		for (int j = 0; j < event.original.inventory.mainInventory.length; j++){
			ItemStack stack = event.original.inventory.mainInventory[j];
			addToPlayer(event.entityPlayer, stack);
		}

		// Loop through players armour slots and add it to the clone?
		for (int j = 0; j < event.original.inventory.armorInventory.length; j++){
			ItemStack stack = event.original.inventory.armorInventory[j];
			addToPlayer(event.entityPlayer, stack);
		}
		event.entityPlayer.setHealth(event.entityPlayer.getMaxHealth() / 2);
	}

	public void addToPlayer(EntityPlayer player, ItemStack stack){
		if(stack == null || player == null){
			return;
		}

		if(stack.getItem() instanceof ItemArmor ) {
			ItemArmor armo = (ItemArmor) stack.getItem();
			int index = 3 - armo.armorType;
			if(player.inventory.armorItemInSlot(index) == null){
				player.inventory.armorInventory[index] = stack;
				return;
			}
		}

		InventoryPlayer inv = player.inventory;
		for (int k = 0; k < inv.mainInventory.length; k++){
			if(inv.mainInventory[k] == null){
				inv.mainInventory[k] = stack.copy();
				return;
			}
		}
	}

	@SubscribeEvent()
	public void BaubleAnvil(AnvilUpdateEvent event) {
		if (event.left == null || event.right == null) {
			return;
		}
		
		if(event.left.getItem() instanceof ItemSockets && event.right.getItem() instanceof ItemGems) {
			if (event.left.getItemDamage() == 0 && event.right.getItemDamage() == 1) {
				
				ItemStack targetStack = new ItemStack(Register.baubleGems, 1, 2);
				ItemStack resultStack = targetStack.copy();
				event.output = resultStack;
				event.cost = 30;
			}
			
			if (event.left.getItemDamage() == 1 && event.right.getItemDamage() == 3) {
				
				ItemStack targetStack = new ItemStack(Register.baubleGems, 1, 4);
				ItemStack resultStack = targetStack.copy();
				event.output = resultStack;
				event.cost = 30;
			}
		}
	}

}