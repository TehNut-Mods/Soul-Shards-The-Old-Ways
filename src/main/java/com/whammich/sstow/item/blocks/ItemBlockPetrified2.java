package com.whammich.sstow.item.blocks;

import com.whammich.sstow.block.BlockPetrified2;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPetrified2 extends ItemBlock {

    public ItemBlockPetrified2(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + BlockPetrified2.names[stack.getItemDamage() % BlockPetrified2.names.length];
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}