package com.whammich.sstow.commands;

import com.google.common.base.Strings;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.registry.ModItems;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import com.whammich.repack.tehnut.lib.util.TextHelper;

public class CommandSSTOW extends CommandBase {
    @Override
    public String getCommandName() {
        return "sstow";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sstow <help|killall|settier|setent> [tier|entityName]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] params) {

        if ((params.length > 0) && (params.length <= 7)) {
            if (params[0].equalsIgnoreCase("killall")) {
                int killCounter = 0;

                for (Entity ent : sender.getEntityWorld().loadedEntityList) {
                    if (ent.getEntityData().getBoolean("SSTOW")) {
                        ent.setDead();
                        killCounter++;
                    }
                }

                if (killCounter == 0) {
                    sender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("chat.sstow.command.notfound")));
                } else {
                    sender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("chat.sstow.command.killed", killCounter)));
                }
            } else if (params[0].equalsIgnoreCase("settier")) {

                if (params.length == 2) {
                    int tierAmount = Integer.parseInt(params[1]);
                    int minKills = TierHandler.getMinKills(tierAmount);
                    if (((EntityPlayerMP) sender).getHeldItem() != null && ((EntityPlayerMP) sender).getHeldItem().getItem() == ModItems.getItem(ItemSoulShard.class)) {
                        ItemStack shard = ((EntityPlayerMP) sender).getHeldItem();
                        for (int i = 1; i <= tierAmount; i++) {
                            Utils.setShardTier(shard, (byte) 1);
                            Utils.setShardKillCount(shard, (short) minKills);
                        }
                    }
                } else {
                    sender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("chat.sstow.command.setwrong")));
                }
            } else if (params[0].equalsIgnoreCase("setent") && !Strings.isNullOrEmpty(params[1])) {
                if (((EntityPlayerMP) sender).getHeldItem() != null && ((EntityPlayerMP) sender).getHeldItem().getItem() == ModItems.getItem(ItemSoulShard.class)) {
                    ItemStack shard = ((EntityPlayerMP) sender).getHeldItem();
                    String entName = "";
                    for (int i = 1; i < params.length; i++)
                        entName += (entName.length() > 0 ? " " : "") + params[i];
                    Utils.setShardBoundEnt(shard, entName);
                }
            } else {
                sender.addChatMessage(new ChatComponentText(TextHelper.localizeEffect("chat.sstow.command.wrongcommand")));
            }
        }
    }
}