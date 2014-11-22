package moze_intel.ssr.commands;

import java.util.List;

import moze_intel.ssr.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class AddTierCMD extends CommandBase {
	@Override
	public String getCommandName() {
		return "ssr_addtier";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/ssr_addtier";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params) {
		sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN
				+ "Tier Added"));
		
		//sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED
		//		+ "Tier Removed"));
	}


}
