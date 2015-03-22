package com.whammich.sstow.item.blocks;

import com.whammich.sstow.block.BlockPetrified;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPetrified extends ItemBlock {

	public ItemBlockPetrified(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + BlockPetrified.names[stack.getItemDamage() % BlockPetrified.names.length];
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
