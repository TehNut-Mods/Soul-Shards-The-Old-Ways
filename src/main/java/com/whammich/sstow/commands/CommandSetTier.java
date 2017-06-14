package com.whammich.sstow.commands;

import com.google.common.collect.Lists;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

public class CommandSetTier extends CommandBase {

    @Override
    public String getName() {
        return "settier";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "settier <tier>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0)
            throw new CommandException(getUsage(sender));

        int tierAmount = Integer.parseInt(args[1]);
        int minKills = TierHandler.getMinKills(tierAmount);
        if (!((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).isEmpty() && ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ISoulShard) {
            ItemStack shard = ((EntityPlayerMP) sender).getHeldItem(EnumHand.MAIN_HAND);
            for (int i = 1; i <= tierAmount; i++) {
                ShardHelper.setTierForShard(shard, 1);
                ShardHelper.setKillsForShard(shard, minKills);
            }
        } else {
            sender.sendMessage(new TextComponentTranslation("chat.sstow.command.noshard"));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        List<String> tabCompletion = Lists.newArrayList();
        switch (args.length) {
            case 1:
                for (int i = 0; i < TierHandler.tiers.size(); i++)
                    tabCompletion.add(String.valueOf(i));
        }

        return tabCompletion;
    }
}
