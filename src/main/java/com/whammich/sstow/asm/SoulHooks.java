package com.whammich.sstow.asm;

import com.whammich.sstow.utils.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.regex.Pattern;

public class SoulHooks {
	public static final Pattern fakePlayer = Pattern.compile("\\[.*\\]");

	public static int getModifiedXP(int xp, EntityLivingBase entity, EntityPlayer killer) {
		if (!isSSMob(entity))
			return xp;
		return (int) (xp * (killer == null || isFakePlayer(killer) ? Config.fakePlayerXP : Config.playerXP));
	}

	private static boolean isSSMob(EntityLivingBase entity) {
		return entity.getEntityData().getBoolean("SSTOW");
	}

	private static boolean isFakePlayer(EntityPlayer player) {
		return player instanceof FakePlayer || fakePlayer.matcher(player.getCommandSenderName()).find();
	}
}
