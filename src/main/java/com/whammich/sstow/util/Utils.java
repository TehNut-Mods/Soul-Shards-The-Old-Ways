package com.whammich.sstow.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.api.event.ShardTierChangeEvent;
import com.whammich.sstow.tile.TileEntityCage;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tehnut.lib.util.helper.TextHelper;

import javax.annotation.Nullable;
import java.util.UUID;

public final class Utils {

    @Nullable
    public static ItemStack getShardFromInv(EntityPlayer player, String entity) {
        ItemStack ret = null;

        ItemStack offhand = player.getHeldItemOffhand();
        if (offhand != null && offhand.getItem() instanceof ISoulShard) {
            if (!ShardHelper.isBound(offhand))
                ret = offhand;
            else if (ShardHelper.getBoundEntity(offhand).equals(entity))
                return offhand;
        }

        for (int i = 0; i <= 8; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack != null && stack.getItem() instanceof ISoulShard && !hasMaxedKills(stack)) {
                if (!ShardHelper.isBound(stack) && ret == null)
                    ret = stack;
                else if (ShardHelper.getBoundEntity(stack).equals(entity))
                    return stack;
            }
        }

        return ret;
    }

    public static void increaseShardKillCount(ItemStack shard, int amount) {
        if (!shard.hasTagCompound() || hasMaxedKills(shard))
            return;

        ShardHelper.setKillsForShard(shard, getClampedKillCount(ShardHelper.getKillsFromShard(shard) + amount));
        checkAndFixShard(shard);
    }

    public static void checkAndFixShard(ItemStack shard) {
        if (!isShardValid(shard)) {
            if (!MinecraftForge.EVENT_BUS.post(new ShardTierChangeEvent(shard, getCorrectTier(shard))))
                ShardHelper.setTierForShard(shard, getCorrectTier(shard));
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
        if (unlocName.equals(SoulShardsAPI.WITHER_SKELETON_OLD))
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
        if (Strings.isNullOrEmpty(owner) || FMLCommonHandler.instance().getMinecraftServerInstance() == null)
            return false;

        String username = UsernameCache.getLastKnownUsername(UUID.fromString(owner));

        for (GameProfile profile : FMLCommonHandler.instance().getMinecraftServerInstance().getGameProfiles())
            if (profile.getName().equals(username))
                return true;

        return false;
    }

    public static void notifyUpdateBasic(World world, BlockPos pos) {
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }
}
