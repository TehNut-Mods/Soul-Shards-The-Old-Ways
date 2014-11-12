package moze_intel.ssr.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class KillCMD extends CommandBase {
	@Override
	public String getCommandName() {
		return "ssr_killall";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/ssr_killall";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params) {
		int killCounter = 0;

		for (Entity ent : (List<Entity>) sender.getEntityWorld().loadedEntityList) {
			if (ent.getEntityData().getBoolean("SSR")) {
				ent.setDead();
				killCounter++;
			}
		}

		if (killCounter == 0) {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED
					+ "No spawned entities were found!"));
		} else {
			sender.addChatMessage(new ChatComponentText(
					EnumChatFormatting.GREEN + "Killed " + killCounter
							+ " entities!"));
		}
	}
}
