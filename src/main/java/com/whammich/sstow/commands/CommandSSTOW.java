package com.whammich.sstow.commands;

import java.util.List;

import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.TierHandler;
import com.whammich.sstow.utils.Utils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class CommandSSTOW extends CommandBase {
	@Override
	public String getCommandName() {
		return "sstow";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/sstow help";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void processCommand(ICommandSender sender, String[] params) {

		if ((params.length > 0) && (params.length <= 7)) {
			if (params[0].equals("help")) {
				sender.addChatMessage(new ChatComponentText(
						"--- Showing SSTOW help page 1 of 1 ---")
						.setChatStyle(new ChatStyle()
								.setColor(EnumChatFormatting.GREEN)));
				sender.addChatMessage(new ChatComponentText(
						"/sstow killall to kill all the sstow entities"));
				sender.addChatMessage(new ChatComponentText(
						"/sstow settier x to set tier of the shard"));
				sender.addChatMessage(new ChatComponentText(
						"/sstow addkills x to add kills"));
				sender.addChatMessage(new ChatComponentText(
						"/sstow removekills x to remove kills"));

			} else if (params[0].equals("killall")) {
				int killCounter = 0;

				for (Entity ent : (List<Entity>) sender.getEntityWorld().loadedEntityList) {
					if (ent.getEntityData().getBoolean("SSTOW")) {
						ent.setDead();
						killCounter++;
					}
				}

				if (killCounter == 0) {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "No spawned entities were found!"));
				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.GREEN + "Killed " + killCounter
									+ " entities!"));
				}
			} else if (params[0].equals("settier")) {

				if (params.length == 2) {
					int tierAmount = Integer.parseInt(params[1]);
					int minKills = TierHandler.getMinKills(tierAmount);
					if (((EntityPlayerMP) sender).getHeldItem() != null
							&& ((EntityPlayerMP) sender).getHeldItem()
									.getItem() == Register.ItemShardSoul) {
						ItemStack shard = ((EntityPlayerMP) sender)
								.getHeldItem();
						for (int i = 1; i <= tierAmount; i++) {

							Utils.setShardTier(shard, (byte) 1);
							Utils.setShardKillCount(shard, (short) minKills);

						}
					}
				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "Wrong use of command, /sstow settier x"));
				}
			} else if (params[0].equals("addkills")) {

				int killAmount = 1000;

				if (params.length >= 2) {
					killAmount = Integer.parseInt(params[1]);

				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "Wrong use of command, /sstow addkills x"));
				}

				if (((EntityPlayerMP) sender).getHeldItem() != null
						&& ((EntityPlayerMP) sender).getHeldItem().getItem() == Register.ItemShardSoul) {
					ItemStack shard = ((EntityPlayerMP) sender).getHeldItem();
					for (int i = 1; i <= killAmount; i++) {

						Utils.increaseShardKillCount(shard, (short) 1);
					}
				}
			} else if (params[0].equals("removekills")) {
				if (params.length == 2) {
					int killAmount = Integer.parseInt(params[1]);

				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "Wrong use of command, /sstow removekills x"));
				}
			} else {
				sender.addChatMessage(new ChatComponentText(
						"Command not recognised, use /sstow help for a list of all the available commands")
						.setChatStyle(new ChatStyle()
								.setColor(EnumChatFormatting.RED)));
			}
		}
	}
}