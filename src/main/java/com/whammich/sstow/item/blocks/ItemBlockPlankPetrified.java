package com.whammich.sstow.item.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.whammich.sstow.block.BlockPlankPetrified;

public class ItemBlockPlankPetrified extends ItemBlock {

	public ItemBlockPlankPetrified(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + BlockPlankPetrified.names[stack.getItemDamage() % BlockPlankPetrified.names.length];
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
}
