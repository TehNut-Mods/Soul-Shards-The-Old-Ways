package com.whammich.sstow.events;

import java.lang.reflect.Field;

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
import com.whammich.sstow.utils.EntityMapper;
import com.whammich.sstow.utils.Entitylist;
import com.whammich.sstow.utils.ModLogger;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerKillEntityEvent {
	@SuppressWarnings("rawtypes")
	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent event) {
		
		World world = event.entity.worldObj;
		
		if (world.isRemote || !(event.entity instanceof EntityLiving) || !(event.source.getEntity() instanceof EntityPlayer)) {
			return;
		}
		
		EntityLiving dead = (EntityLiving) event.entity;
		EntityPlayer player = (EntityPlayer) event.source.getEntity();
		String entName = EntityList.getEntityString(dead);
		
		if (dead.getEntityData() != null && dead.getEntityData().hasKey("SSTOW")) {
			if(player instanceof FakePlayer){
			try {
				Class entityLivingClass = EntityLiving.class;
				Field xpValue;
				
				try {
					xpValue = entityLivingClass.getDeclaredField("field_70728_aV");
					
				} catch (Exception e) {
					xpValue = entityLivingClass.getDeclaredField("experienceValue");
					
				}

				xpValue.setAccessible(true);
				xpValue.setInt(dead,(int)(xpValue.getInt(dead)* Config.FakePlayerXP));
				ModLogger.logInfo("FakePlayer XP Drop Value: " + Config.FakePlayerXP);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				Class entityLivingClass = EntityLiving.class;
				Field xpValue;
				
				try {
					xpValue = entityLivingClass.getDeclaredField("field_70728_aV");
					
				} catch (Exception e) {
					xpValue = entityLivingClass.getDeclaredField("experienceValue");
					
				}

				xpValue.setAccessible(true);
				xpValue.setInt(dead,(int)(xpValue.getInt(dead)* Config.PlayerXP));
				ModLogger.logInfo("Player XP Drop Value: " + Config.PlayerXP);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			return;
		}





		if (Entitylist.bList.contains(entName)) {
			return;
		}
		
		if (entName == null || entName.isEmpty()) {
			ModLogger.logFatal(Utils.localizeFormatted("chat.sstow.debug.nounlocname", "" + dead));
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
				Utils.setShardBoundPlayer(shard, player);
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
