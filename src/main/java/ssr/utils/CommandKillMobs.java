package ssr.utils;

import java.util.Iterator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class CommandKillMobs extends CommandBase {
	@Override
	public String getCommandName() {
		return "ssr_killmobs";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return this.getCommandName();
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] astring) {
		World world = sender.getEntityWorld();
		Iterator<Entity> iter = world.loadedEntityList.iterator();
		int count = 0;

		while (iter.hasNext()) {
			Entity ent = iter.next();

			if (ent instanceof EntityLiving
					&& ent.getEntityData().getBoolean("fromSSR")) {
				ent.setDead();
				count++;
			}
		}
		if (count > 0)
			sender.addChatMessage(CreateMessage(1, "Removed " + count
					+ " spawned entities."));
		else
			sender.addChatMessage(CreateMessage(2,
					"No spawned entities were found!"));
	}

	/**
	 * @param level
	 *            0 Default, 1 Success 2 Failure
	 * @param text
	 */
	private ChatComponentText CreateMessage(int level, String text) {
		ChatComponentText cct = null;
		switch (level) {
		case 0:
			cct = new ChatComponentText(EnumChatFormatting.WHITE + text);
			break;
		case 1:
			cct = new ChatComponentText(EnumChatFormatting.GREEN + text);
			break;
		case 2:
			cct = new ChatComponentText(EnumChatFormatting.RED + text);
			break;
		}
		return cct;
	}
}
