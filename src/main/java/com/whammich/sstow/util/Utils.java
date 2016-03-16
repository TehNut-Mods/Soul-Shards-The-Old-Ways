package com.whammich.sstow.util;

import com.mojang.authlib.GameProfile;
import com.whammich.repack.tehnut.lib.util.TextHelper;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.event.ShardTierChangeEvent;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.registry.ModItems;
import com.whammich.sstow.tile.TileEntityCage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

    public static void increaseShardKillCount(ItemStack shard, int amount) {
        if (!shard.hasTagCompound() || hasMaxedKills(shard))
            return;

        ShardHelper.setKillsForShard(shard, getClampedKillCount(ShardHelper.getKillsFromShard(shard) + amount));
        checkAndFixShard(shard);
    }

    public static void checkAndFixShard(ItemStack shard) {
        if (!isShardValid(shard)) {
            ShardHelper.setTierForShard(shard, getCorrectTier(shard));
            MinecraftForge.EVENT_BUS.post(new ShardTierChangeEvent(shard, getCorrectTier(shard)));
        }
    }

    public static boolean isShardValid(ItemStack shard) {
        int kills = ShardHelper.getKillsFromShard(shard);
        int tier = ShardHelper.getTierFromShard(shard);

        return kills >= TierHandler.getMinKills(tier) && kills <= TierHandler.getMaxKills(tier);
    }

    public static int getCorrectTier(ItemStack shard) {
        int kills = ShardHelper.getKillsFromShard(shard);

        for (int i = 0; i <= TierHandler.tiers.size(); i++)
            if (kills >= TierHandler.getMinKills(i) && kills <= TierHandler.getMaxKills(i))
                return i;

        SoulShardsTOW.instance.getLogHelper().error("Soul shard has an incorrect kill counter of: {}", kills);
        return 0;
    }

    public static boolean hasMaxedKills(ItemStack shard) {
        return ShardHelper.isBound(shard) && ShardHelper.getKillsFromShard(shard) >= TierHandler.getMaxKills(TierHandler.tiers.size() - 1);
    }

    public static ItemStack setMaxedKills(ItemStack shard) {
        ShardHelper.setKillsForShard(shard, TierHandler.getMaxKills(TierHandler.tiers.size() - 1));
        return shard;
    }

    public static String getEntityNameTranslated(String unlocName) {
        if (unlocName.equals("Wither Skeleton"))
            return unlocName;

        return TextHelper.localize("entity." + unlocName + ".name");
    }

    private static int getClampedKillCount(int amount) {
        return MathHelper.clamp_int(amount, 0, TierHandler.getMaxKills(TierHandler.tiers.size() - 1));
    }

    public static boolean isCageBorn(EntityLivingBase living) {
        return living.getEntityData().hasKey(TileEntityCage.SSTOW) && living.getEntityData().getBoolean(TileEntityCage.SSTOW);
    }

    public static int getBlockLightLevel(World world, BlockPos pos, boolean day) {
        return world.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getLightSubtracted(pos, day ? 0 : 16);
    }

    public static boolean isOwnerOnline(String owner) {
        if (FMLCommonHandler.instance().getMinecraftServerInstance() == null)
            return false;

        String username = UsernameCache.getLastKnownUsername(UUID.fromString(owner));

        for (GameProfile profile : FMLCommonHandler.instance().getMinecraftServerInstance().getGameProfiles())
            if (profile.getName().equals(username))
                return true;

        return false;
    }
}
