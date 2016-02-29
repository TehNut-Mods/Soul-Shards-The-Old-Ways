package com.whammich.sstow.util;

import com.whammich.repack.tehnut.lib.util.ItemStackWrapper;
import lombok.Getter;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ForgeRecipeHandler {

    private static Map<ItemStackWrapper, Integer> fuelTime = new HashMap<ItemStackWrapper, Integer>();
    private static Map<ItemStackWrapper, ForgeRecipe> recipes = new HashMap<ItemStackWrapper, ForgeRecipe>();

    public static void registerFuel(ItemStack fuel, int ticks) {
        if (!fuelTime.containsKey(ItemStackWrapper.getWrapper(fuel)))
            fuelTime.put(ItemStackWrapper.getWrapper(fuel), ticks);
    }

    public static void registerRecipe(ForgeRecipe recipe) {
        if (!recipes.containsKey(ItemStackWrapper.getWrapper(recipe.getInput())))
            recipes.put(ItemStackWrapper.getWrapper(recipe.getInput()), recipe);
    }

    public static ForgeRecipe getRecipe(ItemStack stack) {
        return recipes.get(ItemStackWrapper.getWrapper(stack));
    }

    public static int getBurnTime(ItemStack stack) {
        return fuelTime.containsKey(ItemStackWrapper.getWrapper(stack)) ? fuelTime.get(ItemStackWrapper.getWrapper(stack)) : 0;
    }

    public static ItemStack getOutput(ItemStack stack) {
        if (getRecipe(stack) == null)
            return null;

        return getRecipe(stack).getOutput();
    }

    public static boolean isValidFuel(ItemStack stack) {
        return fuelTime.containsKey(ItemStackWrapper.getWrapper(stack));
    }

    public static boolean isValidInput(ItemStack stack) {
        return recipes.containsKey(ItemStackWrapper.getWrapper(stack));
    }

    public static class ForgeRecipe {

        private final ItemStackWrapper input;
        private final ItemStackWrapper output;
        private final int ticksRequired;

        public ForgeRecipe(ItemStack input, ItemStack output, int ticksRequired) {
            this.input = ItemStackWrapper.getWrapper(input);
            this.output = ItemStackWrapper.getWrapper(output);
            this.ticksRequired = ticksRequired;
        }

        public ItemStack getInput() {
            return input.toStack();
        }

        public ItemStack getOutput() {
            return output.toStack();
        }

        public int getTicksRequired() {
            return ticksRequired;
        }
    }
}
