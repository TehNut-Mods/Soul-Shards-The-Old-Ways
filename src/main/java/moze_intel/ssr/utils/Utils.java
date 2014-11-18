package moze_intel.ssr.utils;

import java.util.Iterator;

import moze_intel.ssr.events.SSRAchievement;
import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public final class Utils {
	public static ItemStack getShardFromInv(EntityPlayer player, String entity) {
		ItemStack lastResort = null;

		for (int i = 0; i <= 8; i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);

			if (stack != null && stack.getItem() == ObjHandler.SOUL_SHARD
					&& !hasMaxedKills(stack)) {
				if (!isShardBound(stack) && lastResort == null) {
					lastResort = stack;
				} else if (getShardBoundEnt(stack).equals(entity)) {
					return stack;
				}
			}
		}

		return lastResort;
	}

	public static void checkForAchievements(EntityPlayer player, ItemStack shard) {
		int tier = (int) getShardTier(shard);
		switch (tier) {
		case 0:
			break;
		case 1:
			player.addStat(SSRAchievement.achievementTier1, 1);
			break;
		case 2:
			player.addStat(SSRAchievement.achievementTier2, 1);
			break;
		case 3:
			player.addStat(SSRAchievement.achievementTier3, 1);
			break;
		case 4:
			player.addStat(SSRAchievement.achievementTier4, 1);
			break;
		case 5:
			player.addStat(SSRAchievement.achievementTier5, 1);
			break;
		default:
			break;
		}
	}

	/*
	 * public static void hideItems() { Iterator modsIT =
	 * Loader.instance().getModList().iterator(); ModContainer modc; while
	 * (modsIT.hasNext()) { modc = (ModContainer) modsIT.next(); if
	 * ("Not Enough Items".equals(modc.getName().trim())) { ItemStack stack =
	 * new ItemStack(ObjHandler.SOUL_CAGE, 0, 1);
	 * codechicken.nei.api.API.hideItem(stack); stack.setItemDamage(2);
	 * codechicken.nei.api.API.hideItem(stack); } } }
	 */
	public static short getShardKillCount(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return 0;
		}

		return (short) MathHelper.clamp_int(
				shard.stackTagCompound.getShort("KillCount"), 0,
				TierHandler.getMaxKills(5));
	}

	public static void increaseShardKillCount(ItemStack shard, short amount) {
		if (!shard.hasTagCompound() || hasMaxedKills(shard)) {
			return;
		}

		setShardKillCount(shard, getClampedKillCount(getShardKillCount(shard)
				+ amount));

		checkAndFixShard(shard);
	}

	public static void checkAndFixShard(ItemStack shard) {
		if (!TierHandler.isShardValid(shard)) {
			setShardTier(shard, TierHandler.getCorrectTier(shard));
		}
	}

	public static void setShardKillCount(ItemStack shard, short value) {
		if (!shard.hasTagCompound()) {
			shard.setTagCompound(new NBTTagCompound());
		}

		shard.stackTagCompound.setShort("KillCount", value);
	}

	public static byte getShardTier(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return 0;
		}

		return (byte) MathHelper.clamp_int(
				shard.stackTagCompound.getByte("Tier"), 0, 5);
	}

	public static void setShardTier(ItemStack shard, byte tier) {
		if (!shard.hasTagCompound()) {
			shard.setTagCompound(new NBTTagCompound());
		}

		shard.stackTagCompound.setByte("Tier",
				(byte) MathHelper.clamp_int(tier, 0, 5));
	}

	/**
	 * Returns an empty string if unbound.
	 */
	public static String getShardBoundEnt(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return "";
		}

		return shard.stackTagCompound.getString("Entity");
	}

	/**
	 * Does not check if the shard is already bound!
	 */
	public static void setShardBoundEnt(ItemStack shard, String value) {
		if (!shard.hasTagCompound()) {
			shard.setTagCompound(new NBTTagCompound());
		}

		shard.stackTagCompound.setString("Entity", value);
	}

	public static boolean isShardBound(ItemStack shard) {
		return !getShardBoundEnt(shard).isEmpty();
	}

	public static boolean hasMaxedKills(ItemStack shard) {
		return isShardBound(shard)
				&& getShardKillCount(shard) >= TierHandler.getMaxKills(5);
	}

	public static String getEntityNameTransltated(String unlocalName) {
		if (unlocalName.equals("Wither Skeleton")) {
			return unlocalName;
		}

		String result = StatCollector.translateToLocal("entity." + unlocalName
				+ ".name");

		if (result == null) {
			return unlocalName;
		}

		return result;
	}

	private static short getClampedKillCount(int amount) {
		int value = MathHelper.clamp_int(amount, 0, TierHandler.getMaxKills(5));

		if (value > Short.MAX_VALUE) {
			return Short.MAX_VALUE;
		}

		return (short) value;
	}

	public static void writeEntityHeldItem(ItemStack shard, EntityLiving ent) {
		if (ent instanceof EntityZombie || ent instanceof EntityEnderman) {
			return;
		}

		ItemStack held = ent.getHeldItem();

		if (held != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			held.writeToNBT(nbt);

			if (nbt.hasKey("ench")) {
				nbt.removeTag("ench");
			}

			shard.stackTagCompound.setTag("HeldItem", nbt);
		}
	}

	public static ItemStack getEntityHeldItem(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return null;
		}

		if (shard.stackTagCompound.hasKey("HeldItem")) {
			return ItemStack
					.loadItemStackFromNBT((NBTTagCompound) shard.stackTagCompound
							.getTag("HeldItem"));
		}

		return null;
	}
}
