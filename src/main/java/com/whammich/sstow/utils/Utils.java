package com.whammich.sstow.utils;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public final class Utils {

	public static ItemStack getShardFromInv(EntityPlayer player, String entity) {
		ItemStack lastResort = null;

		for (int i = 0; i <= 8; i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);

			if (stack != null && stack.getItem() == Register.ItemShardSoul
					&& !hasMaxedKills(stack)) {
				if (!isShardBound(stack) && lastResort == null) {
					lastResort = stack;
				} else if (getShardBoundEnt(stack).equals(entity)) {
					return stack;
				}
			}
		}

		if (lastResort != null && lastResort.stackSize > 1) {
			boolean emptySpot = false;
			int counter = 0;

			ItemStack newShard = new ItemStack(Register.ItemShardSoul, 1);
			while (!emptySpot && counter < 36) {
				ItemStack inventoryStack = player.inventory
						.getStackInSlot(counter);
				if (inventoryStack == null) {
					--lastResort.stackSize;
					player.inventory.addItemStackToInventory(newShard);
					emptySpot = true;
					return player.inventory.getStackInSlot(counter);
				}
				counter++;
			}

			if (!emptySpot) {

				--lastResort.stackSize;
				if (!Utils.isShardBound(newShard)) {
					Utils.setShardBoundEnt(newShard, entity);
					Utils.writeEntityHeldItem(newShard,
							(EntityLiving) EntityList.createEntityByName(
									entity, player.worldObj));
				}

				int soulStealer = EnchantmentHelper.getEnchantmentLevel(
						Register.SOUL_STEALER.effectId, player.getHeldItem());
				soulStealer *= Config.ENCHANT_KILL_BONUS;
				Utils.increaseShardKillCount(newShard,
						(short) (1 + soulStealer));
				// Utils.checkForAchievements(player, newShard);
				player.worldObj.spawnEntityInWorld(new EntityItem(
						player.worldObj, player.posX, player.posY, player.posZ,
						newShard));
				return null;

			}

		}
		return lastResort;
	}

	// public static void checkForAchievements(EntityPlayer player, ItemStack
	// shard) {
	// int tier = (int) getShardTier(shard);
	// switch (tier) {
	// case 0:
	// break;
	// case 1:
	// player.addStat(Achievements.shardt1, 1);
	// break;
	// case 2:
	// player.addStat(Achievements.shardt2, 1);
	// break;
	// case 3:
	// player.addStat(Achievements.shardt3, 1);
	// break;
	// case 4:
	// player.addStat(Achievements.shardt4, 1);
	// break;
	// case 5:
	// player.addStat(Achievements.shardt5, 1);
	// break;
	// default:
	// break;
	// }
	// }

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

	/*
	 * Returns an empty string if unbound.
	 */
	public static String getShardBoundEnt(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return "";
		}

		return shard.stackTagCompound.getString("Entity");
	}

	/*
	 * Does not check if the shard is already bound!
	 */
	public static void setShardBoundEnt(ItemStack shard, String value) {
		if (!shard.hasTagCompound()) {
			shard.setTagCompound(new NBTTagCompound());
			shard.stackTagCompound.setDouble("antiStack",
					new Random().nextDouble());
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

	public static void writeEntityArmor(ItemStack shard, EntityLiving ent) {
		for (int i = 1; i <= 4; i++) {
			ItemStack armor = ent.getEquipmentInSlot(i);

			if (armor != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				armor.writeToNBT(nbt);

				if (nbt.hasKey("ench")) {
					nbt.removeTag("ench");
				}

				if (shard.stackTagCompound.hasKey("armor" + i)) {
					if (shard.stackTagCompound.getTag("armor" + i) != null) {
						NBTTagCompound oldnbt = (NBTTagCompound) shard.stackTagCompound
								.getTag("armor" + i);
						ItemStack oldArmor = ItemStack
								.loadItemStackFromNBT(oldnbt);
						if (oldArmor.getItem() != armor.getItem()) {
							shard.stackTagCompound.removeTag("armor" + i);
						}
					}
				} else {
					shard.stackTagCompound.setTag("armor" + i, nbt);
				}
			} else {
				shard.stackTagCompound.removeTag("armor" + i);
			}
		}
	}

	public static ItemStack getEntityArmor(ItemStack shard, int armorSlot) {
		if (shard.stackTagCompound.hasKey("armor" + armorSlot)
				&& shard.stackTagCompound.getTag("armor" + armorSlot) != null) {
			NBTTagCompound oldnbt = (NBTTagCompound) shard.stackTagCompound
					.getTag("armor" + armorSlot);
			ItemStack oldArmor = ItemStack.loadItemStackFromNBT(oldnbt);
			return oldArmor;
		} else {
			return null;
		}
	}

	public static void setShardBoundPlayer(ItemStack shard, EntityPlayer player) {
		shard.stackTagCompound.setString("owner", player.getDisplayName());
	}

	public static String getShardBoundPlayer(ItemStack shard) {
		if (!shard.hasTagCompound()) {
			return null;
		}

		if (shard.stackTagCompound.hasKey("owner")) {
			return shard.stackTagCompound.getString("owner");
		}

		return null;
	}
	
	public static String localize(String key) {
		return StatCollector.translateToLocal(key);
	}
	
	public static String localizeFormatted(String key, String keyFormat) {
		return String.format(localize(key), localize(keyFormat));
	}
}
