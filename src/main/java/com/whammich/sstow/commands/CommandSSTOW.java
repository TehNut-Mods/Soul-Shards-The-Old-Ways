package com.whammich.sstow.commands;

import com.google.common.base.Strings;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import tehnut.lib.util.helper.ItemHelper;
import tehnut.lib.util.helper.TextHelper;

public class CommandSSTOW extends CommandBase {
    @Override
    public String getCommandName() {
        return "sstow";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sstow <killall|settier|setent> [tier|entityName]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) {

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
                    sender.addChatMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.notfound")));
                } else {
                    sender.addChatMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.killed", killCounter)));
                }
            } else if (params[0].equalsIgnoreCase("settier")) {

                if (params.length == 2) {
                    int tierAmount = Integer.parseInt(params[1]);
                    int minKills = TierHandler.getMinKills(tierAmount);
                    if (((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND) != null && ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemHelper.getItem(ItemSoulShard.class)) {
                        ItemStack shard = ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND);
                        for (int i = 1; i <= tierAmount; i++) {
                            ShardHelper.setTierForShard(shard, 1);
                            ShardHelper.setKillsForShard(shard, minKills);
                        }
                    }
                } else {
                    sender.addChatMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.setwrong")));
                }
            } else if (params[0].equalsIgnoreCase("setent") && !Strings.isNullOrEmpty(params[1])) {
                if (((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND) != null && ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemHelper.getItem(ItemSoulShard.class)) {
                    ItemStack shard = ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND);
                    String entName = "";
                    for (int i = 1; i < params.length; i++)
                        entName += (entName.length() > 0 ? " " : "") + params[i];
                    ShardHelper.setBoundEntity(shard, entName);
                }
            } else {
                sender.addChatMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.wrongcommand")));
            }
        }
    }
}