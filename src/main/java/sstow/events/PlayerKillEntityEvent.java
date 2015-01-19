package sstow.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import sstow.gameObjs.ObjHandler;
import sstow.utils.Config;
import sstow.utils.EntityMapper;
import sstow.utils.TOWLogger;
import sstow.utils.Utils;
import cofh.core.entity.CoFHFakePlayer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerKillEntityEvent {
	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent event) {
		World world = event.entity.worldObj;
		if (Loader.isModLoaded("CoFHCore")
				&& (event.source.getEntity() instanceof CoFHFakePlayer)) {
			return;
		}
		if (world.isRemote || !(event.entity instanceof EntityLiving)
				|| !(event.source.getEntity() instanceof EntityPlayer)) {
			return;
		}

		EntityLiving dead = (EntityLiving) event.entity;

		if (dead.getEntityData().getBoolean("SSTOW")) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.source.getEntity();

		String entName = EntityList.getEntityString(dead);

		if (entName == null || entName.isEmpty()) {
			TOWLogger
					.logFatal("Player killed entity with no unlocalized name: "
							+ dead);
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
					ObjHandler.SOUL_STEALER.effectId, player.getHeldItem());
			soulStealer *= Config.ENCHANT_KILL_BONUS;

			Utils.increaseShardKillCount(shard, (short) (1 + soulStealer));
			Utils.checkForAchievements(player, shard);
		}
	}
}
