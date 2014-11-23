package moze_intel.ssr.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class SSRCMD extends CommandBase {
	@Override
	public String getCommandName() {
		return "ssr";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/ssr help";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params) {

		if ((params.length > 0) && (params.length <= 7)) {
			if (params[0] == "help") {
				sender.addChatMessage(new ChatComponentText(
						"--- Showing SSR help page 1 of 1 ---")
						.setChatStyle(new ChatStyle()
								.setColor(EnumChatFormatting.GREEN)));
				sender.addChatMessage(new ChatComponentText(
						"/ssr killall to kill all the ssr entities"));
				sender.addChatMessage(new ChatComponentText(
						"/ssr addtier x to add tiers"));
				sender.addChatMessage(new ChatComponentText(
						"/ssr removetier x to remove tiers"));
				sender.addChatMessage(new ChatComponentText(
						"/ssr addkills x to add kills"));
				sender.addChatMessage(new ChatComponentText(
						"/ssr removekills x to remove kills"));

			} else if (params[0] == "killall") {
				int killCounter = 0;

				for (Entity ent : (List<Entity>) sender.getEntityWorld().loadedEntityList) {
					if (ent.getEntityData().getBoolean("SSR")) {
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
			} else if (params[0] == "addtier") {

				if (params.length == 2) {
					int tierAmount = Integer.parseInt(params[1]);

				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "Wrong use of command, /ssr addtier x"));
				}
			} else if (params[0] == "removetier") {
				if (params.length == 2) {
					int tierAmount = Integer.parseInt(params[1]);

				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "Wrong use of command, /ssr removetier x"));
				}
			} else if (params[0] == "addkills") {
				if (params.length == 2) {
					int killAmount = Integer.parseInt(params[1]);

				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "Wrong use of command, /ssr addkills x"));
				}
			} else if (params[0] == "removekills") {
				if (params.length == 2) {
					int killAmount = Integer.parseInt(params[1]);

				} else {
					sender.addChatMessage(new ChatComponentText(
							EnumChatFormatting.RED
									+ "Wrong use of command, /ssr removekills x"));
				}
			} else {
				sender.addChatMessage(new ChatComponentText(
						"Command not recognised, use /ssr help for a list of all the available commands")
						.setChatStyle(new ChatStyle()
								.setColor(EnumChatFormatting.RED)));
			}
		}
	}
}
