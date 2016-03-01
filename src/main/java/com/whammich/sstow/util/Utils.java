package com.whammich.sstow.util;

import com.mojang.authlib.GameProfile;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.registry.ModItems;
import com.whammich.sstow.tile.TileEntityCage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import com.whammich.repack.tehnut.lib.util.TextHelper;
import net.minecraftforge.common.UsernameCache;

import javax.annotation.Nullable;
import java.util.UUID;

public final class Utils {

    @Nullable
    public static ItemStack getShardFromInv(EntityPlayer player, String entity) {
        ItemStack lastResort = null;

        for (int i = 0; i <= 8; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack != null && stack.getItem() == ModItems.getItem(ItemSoulShard.class) && !hasMaxedKills(stack)) {
                if (!ShardHelper.isBound(stack) && lastResort == null)
                    lastResort = stack;
                else if (ShardHelper.getBoundEntity(stack).equals(entity))
                    return stack;
            }
        }
        return lastResort;
    }

    public static void increaseShardKillCount(ItemStack shard, short amount) {
        if (!shard.hasTagCompound() || hasMaxedKills(shard))
            return;

        ShardHelper.setKillsForShard(shard, getClampedKillCount(ShardHelper.getKillsFromShard(shard) + amount));
        checkAndFixShard(shard);
    }

    public static void checkAndFixShard(ItemStack shard) {
        if (!TierHandler.isShardValid(shard))
            ShardHelper.setTierForShard(shard, TierHandler.getCorrectTier(shard));
    }

    public static boolean hasMaxedKills(ItemStack shard) {
        return ShardHelper.isBound(shard) && ShardHelper.getKillsFromShard(shard) >= TierHandler.getMaxKills(5);
    }

    public static ItemStack setMaxedKills(ItemStack shard) {
        ShardHelper.setKillsForShard(shard, TierHandler.getMaxKills(5));
        return shard;
    }

    public static String getEntityNameTranslated(String unlocName) {
        if (unlocName.equals("Wither Skeleton"))
            return unlocName;

        String result = TextHelper.localize("entity." + unlocName + ".name");

        if (result == null)
            return unlocName;

        return result;
    }

    private static short getClampedKillCount(int amount) {
        int value = MathHelper.clamp_int(amount, 0, TierHandler.getMaxKills(5));

        if (value > Short.MAX_VALUE)
            return Short.MAX_VALUE;

        return (short) value;
    }

    public static boolean isCageBorn(EntityLivingBase living) {
        return living.getEntityData().hasKey(TileEntityCage.SSTOW) && living.getEntityData().getBoolean(TileEntityCage.SSTOW);
    }

    public static int getBlockLightLevel(World world, BlockPos pos, boolean day) {
        return world.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getLightSubtracted(pos, day ? 0 : 16);
    }

    public static boolean isOwnerOnline(String owner) {
        if (MinecraftServer.getServer() == null)
            return false;

        String username = UsernameCache.getLastKnownUsername(UUID.fromString(owner));

        for (GameProfile profile : MinecraftServer.getServer().getGameProfiles())
            if (profile.getName().equals(username))
                return true;

        return false;
    }
}
