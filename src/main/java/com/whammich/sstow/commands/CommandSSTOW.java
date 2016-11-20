package com.whammich.sstow.commands;

import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import tehnut.lib.util.helper.ItemHelper;
import tehnut.lib.util.helper.TextHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandSSTOW extends CommandBase {

    @Override
    public String getName() {
        return "sstow";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/sstow <killall|settier|setent> [tier|entityName]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
        if ((params.length > 0) && (params.length <= 2)) {
            if (params[0].equalsIgnoreCase("killall")) {
                int killCounter = 0;

                for (Entity ent : sender.getEntityWorld().loadedEntityList) {
                    if (ent.getEntityData().getBoolean("SSTOW")) {
                        ent.setDead();
                        killCounter++;
                    }
                }

                if (killCounter == 0) {
                    sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.notfound")));
                } else {
                    sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.killed", killCounter)));
                }
            } else if (params[0].equalsIgnoreCase("settier")) {

                if (params.length == 2) {
                    int tierAmount = Integer.parseInt(params[1]);
                    int minKills = TierHandler.getMinKills(tierAmount);
                    if (!((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).isEmpty() && ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ISoulShard) {
                        ItemStack shard = ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND);
                        for (int i = 1; i <= tierAmount; i++) {
                            ShardHelper.setTierForShard(shard, 1);
                            ShardHelper.setKillsForShard(shard, minKills);
                        }
                    } else {
                        sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.noshard")));
                    }
                } else {
                    sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.setwrong")));
                }
            } else if (params[0].equalsIgnoreCase("setent") && !Strings.isNullOrEmpty(params[1])) {
                if (!((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).isEmpty() && ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ISoulShard) {
                    ItemStack shard = ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND);
                    String entName = "";
                    for (int i = 1; i < params.length; i++)
                        entName += (entName.length() > 0 ? " " : "") + params[i];
                    ShardHelper.setBoundEntity(shard, new ResourceLocation(entName));
                } else {
                    sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.noshard")));
                }
            } else {
                sender.sendMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.command.wrongcommand")));
            }
        } else {
            throw new CommandException(getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        List<String> tabCompletion = new ArrayList<String>();

        switch (args.length) {
            case 1:
                tabCompletion.addAll(getListOfStringsMatchingLastWord(args, "settier", "setent", "killall"));
                break;
            case 2:
                if (args[0].equalsIgnoreCase("settier"))
                    for (int i = 0; i < TierHandler.tiers.size(); i++)
                        tabCompletion.add(String.valueOf(i));
                else if (args[0].equalsIgnoreCase("setent"))
                    tabCompletion.addAll(getListOfStringsMatchingLastWord(args, ConfigHandler.entityList));
        }

        return tabCompletion;
    }
}