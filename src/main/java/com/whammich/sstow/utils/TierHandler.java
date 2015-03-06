package com.whammich.sstow.utils;

import net.minecraft.item.ItemStack;

public final class TierHandler {
	public static short[] MIN_KILLS = { 0, 64, 128, 256, 512, 1024 };
	private static short[] MAX_KILLS = { 63, 127, 255, 511, 1023, 1024 };
	private static boolean[] NEEDS_PLAYER = { true, true, false, false, false };
	private static boolean[] CHECKS_LIGHT = { true, true, true, true, false };
	private static boolean[] CHECKS_WORLD = { true, true, true, false, false };
	private static boolean[] CHECKS_REDSTONE = { false, false, false, false,
			true };
	private static byte[] NUM_SPAWNS = { 2, 4, 4, 4, 6 };
	private static byte[] SPAWN_DELAY = { 20, 10, 5, 5, 2 };

	public static void setPlayerChecks(int tier, boolean value) {
		NEEDS_PLAYER[tier] = value;
	}

	public static void setLightChecks(int tier, boolean value) {
		CHECKS_LIGHT[tier] = value;
	}

	public static void setWorldChecks(int tier, boolean value) {
		CHECKS_WORLD[tier] = value;
	}

	public static void setRedstoneChecks(int tier, boolean value) {
		CHECKS_REDSTONE[tier] = value;
	}

	public static void setNumSpawns(int tier, byte value) {
		NUM_SPAWNS[tier] = value;
	}

	public static void setSpawnDelay(int tier, byte value) {
		SPAWN_DELAY[tier] = value;
	}

	public static boolean getChecksPlayer(int tier) {
		return NEEDS_PLAYER[tier];
	}

	public static boolean getChecksLight(int tier) {
		return CHECKS_LIGHT[tier];
	}

	public static boolean getChecksWorld(int tier) {
		return CHECKS_WORLD[tier];
	}

	public static boolean getChecksRedstone(int tier) {
		return CHECKS_REDSTONE[tier];
	}

	public static byte getNumSpawns(int tier) {
		return NUM_SPAWNS[tier];
	}

	public static byte getCooldown(int tier) {
		return SPAWN_DELAY[tier];
	}

	public static short getMinKills(int tier) {
		return MIN_KILLS[tier];
	}

	public static short getMaxKills(int tier) {
		return MAX_KILLS[tier];
	}

	public static void setTierReqKills(short[] minKills) {
		boolean error = false;

		short[] tempMin = new short[6];
		short[] tempMax = new short[6];

		tempMin[0] = 0;
		tempMax[0] = (short) (minKills[0] - 1);

		for (int i = 0; i < 5; i++) {
			tempMin[i + 1] = minKills[i];

			if (i < 4) {
				tempMax[i + 1] = (short) (minKills[i + 1] - 1);
			} else {
				tempMax[i + 1] = minKills[i];
			}
		}

		for (int i = 1; i < 6; i++)
			for (int j = 0; j < i; j++) {
				if (tempMin[i] <= tempMin[j]) {
					error = true;
					break;
				}
			}

		if (error) {
			ModLogger
					.logFatal("Custom tier kill settings are incorrect, resetting defaults.");
		} else {
			MIN_KILLS = tempMin;
			MAX_KILLS = tempMax;

			ModLogger.logInfo("Loaded custom tier kill settings!");
		}

		ModLogger.logInfo("Min kills: " + arrayToString(MIN_KILLS));
		ModLogger.logInfo("Max kills: " + arrayToString(MAX_KILLS));
	}

	public static boolean isShardValid(ItemStack shard) {
		int kills = Utils.getShardKillCount(shard);
		byte tier = Utils.getShardTier(shard);

		return kills >= MIN_KILLS[tier] && kills <= MAX_KILLS[tier];
	}

	public static byte getCorrectTier(ItemStack shard) {
		short kills = Utils.getShardKillCount(shard);

		for (byte i = 0; i <= 5; i++) {
			if (kills >= MIN_KILLS[i] && kills <= MAX_KILLS[i]) {
				return i;
			}
		}

		ModLogger.logFatal("Soul shard has an incorrect kill counter of: "
				+ kills);
		return 0;
	}

	private static String arrayToString(short[] array) {
		String message = "";

		for (int i = 0; i < array.length; i++) {
			message += array[i];

			if (i < array.length - 1) {
				message += ",";
			}
		}

		return message;
	}
}
