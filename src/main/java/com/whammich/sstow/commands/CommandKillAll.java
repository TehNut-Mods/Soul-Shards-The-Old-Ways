package com.whammich.sstow.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandKillAll extends CommandBase {

    @Override
    public String getName() {
        return "killall";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "killall";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int killCounter = 0;

        for (Entity ent : sender.getEntityWorld().loadedEntityList) {
            if (ent.getEntityData().getBoolean("SSTOW")) {
                ent.setDead();
                killCounter++;
            }
        }

        if (killCounter == 0) {
            sender.sendMessage(new TextComponentTranslation("chat.sstow.command.notfound"));
        } else {
            sender.sendMessage(new TextComponentTranslation("chat.sstow.command.killed", killCounter));
        }
    }
}
