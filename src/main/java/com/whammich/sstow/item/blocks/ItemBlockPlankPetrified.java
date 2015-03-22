package com.whammich.sstow.item.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPlankPetrified extends ItemBlock {
	String[] names = { "oak", "spruce", "birch", "jungle", "acacia", "big_oak" };

	public ItemBlockPlankPetrified(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
}
