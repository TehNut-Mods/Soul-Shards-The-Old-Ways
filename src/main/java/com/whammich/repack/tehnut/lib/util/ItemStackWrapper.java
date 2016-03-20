package com.whammich.repack.tehnut.lib.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackWrapper {

    public final Item item;
    public final int stackSize;
    public final int meta;
    public NBTTagCompound nbtTag;

    public ItemStackWrapper(Item item, int stackSize, int meta) {
        this.item = item;
        this.stackSize = stackSize;
        this.meta = meta;
    }

    public ItemStackWrapper(Item item, int stackSize) {
        this(item, stackSize, 0);
    }

    public ItemStackWrapper(Item item) {
        this(item, 1, 0);
    }

    public ItemStackWrapper(Block block, int stackSize, int meta) {
        this(Item.getItemFromBlock(block), stackSize, meta);
    }

    public ItemStackWrapper(Block block, int stackSize) {
        this(block, stackSize, 0);
    }

    public ItemStackWrapper(Block block) {
        this(block, 1, 0);
    }

    public ItemStack toStack() {
        return new ItemStack(item, stackSize, meta);
    }

    public String getDisplayName() {
        return toStack().getDisplayName();
    }

    @Override
    public String toString() {
        return stackSize + "x" + item.getUnlocalizedName() + "@" + this.meta;
    }

    public ItemStack toStack(int count) {
        ItemStack result = new ItemStack(item, count, meta);
        result.setTagCompound(nbtTag);
        return result;
    }

    public void setNbtTag(NBTTagCompound nbtTag) {
        this.nbtTag = nbtTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemStackWrapper that = (ItemStackWrapper) o;

        if (stackSize != that.stackSize) return false;
        if (meta != that.meta) return false;
        if (item != null ? !item.equals(that.item) : that.item != null) return false;
        return nbtTag != null ? nbtTag.equals(that.nbtTag) : that.nbtTag == null;

    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + stackSize;
        result = 31 * result + meta;
        result = 31 * result + (nbtTag != null ? nbtTag.hashCode() : 0);
        return result;
    }

    public static ItemStackWrapper getWrapper(ItemStack stack) {
        return new ItemStackWrapper(stack.getItem(), stack.stackSize, stack.getItemDamage());
    }
}
