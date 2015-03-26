package com.whammich.sstow.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.EntityBlackList;
import com.whammich.sstow.utils.EntityMapper;
import com.whammich.sstow.utils.ModLogger;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerKillEntityEvent {
	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent event) {
		World world = event.entity.worldObj;
		
		if (event.source.getEntity() instanceof FakePlayer)
			return;
		
		
		if (world.isRemote || !(event.entity instanceof EntityLiving) || !(event.source.getEntity() instanceof EntityPlayer)) {
			return;
		}

		EntityLiving dead = (EntityLiving) event.entity;

		if (dead.getEntityData().getBoolean("SSTOW")) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.source.getEntity();

		String entName = EntityList.getEntityString(dead);
		
		if (EntityBlackList.bList.contains(entName)){
			return;
		}
		
		if (entName == null || entName.isEmpty()) {
			ModLogger.logFatal("Player killed entity with no unlocalized name: " + dead);
			return;
		}

		if (!EntityMapper.isEntityValid(entName)) {
			return;
		}

		if (dead instanceof EntitySkeleton
				&& ((EntitySkeleton) dead).getSkeletonType() == 1) {
			entName = "Wither Skeleton";
		}

		ItemStack shard = Utils.getShardFromInv(player, entName);

		if (shard != null) {
			if (!Utils.isShardBound(shard)) {
				Utils.setShardBoundEnt(shard, entName);
				Utils.writeEntityHeldItem(shard, dead);
			}
			Utils.writeEntityArmor(shard, dead);

			int soulStealer = EnchantmentHelper.getEnchantmentLevel(
					Register.SOUL_STEALER.effectId, player.getHeldItem());
			soulStealer *= Config.ENCHANT_KILL_BONUS;

			Utils.increaseShardKillCount(shard, (short) (1 + soulStealer));
//			Utils.checkForAchievements(player, shard);
		}
	}
}
