package com.whammich.sstow.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ShardHelper {

    public static final String TIER = "Tier";
    public static final String KILL_COUNT = "KillCount";
    public static final String ENTITY = "Entity";

    public static int getTierFromShard(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ISoulShard))
            return 0;

        return checkNBT(stack).getTagCompound().getInteger(TIER);
    }

    public static ItemStack setTierForShard(ItemStack stack, int tier) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ISoulShard))
            return stack;

        checkNBT(stack).getTagCompound().setInteger(TIER, tier);
        return stack;
    }

    public static int getKillsFromShard(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ISoulShard))
            return 0;

        return checkNBT(stack).getTagCompound().getInteger(KILL_COUNT);
    }

    public static ItemStack setKillsForShard(ItemStack stack, int kills) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ISoulShard))
            return stack;

        checkNBT(stack).getTagCompound().setInteger(KILL_COUNT, kills);
        return stack;
    }

    public static ResourceLocation getBoundEntity(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ISoulShard))
            return new ResourceLocation("blank", "blank");

        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(ENTITY))
            return new ResourceLocation("blank", "blank");

        return new ResourceLocation(checkNBT(stack).getTagCompound().getString(ENTITY));
    }

    public static ItemStack setBoundEntity(ItemStack stack, ResourceLocation entity) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ISoulShard))
            return stack;

        checkNBT(stack).getTagCompound().setString(ENTITY, entity.toString());
        return stack;
    }

    public static boolean isBound(ItemStack stack) {
        return !getBoundEntity(stack).equals(new ResourceLocation("blank", "blank"));
    }

    private static ItemStack checkNBT(ItemStack stack) {
        if (stack.getTagCompound() == null)
            stack.setTagCompound(new NBTTagCompound());

        return stack;
    }
}
