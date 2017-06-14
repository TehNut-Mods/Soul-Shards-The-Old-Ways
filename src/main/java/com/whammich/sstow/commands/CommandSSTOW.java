package com.whammich.sstow.commands;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.List;

public class CommandSSTOW extends CommandTreeBase {

    public CommandSSTOW() {
        addSubcommand(new CommandKillAll());
        addSubcommand(new CommandSetTier());
        addSubcommand(new CommandSetEntity());
    }

    @Override
    public String getName() {
        return "sstow";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        List<String> subCommands = Lists.newArrayList();
        for (ICommand subCommand : getSubCommands())
            subCommands.add(subCommand.getName());
        return "nomagi [" + Joiner.on("|").join(subCommands) + "]";
    }
}