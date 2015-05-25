package com.whammich.sstow.item.blocks;

import com.whammich.sstow.block.BlockXenolith;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockXenolith extends ItemBlock {

	public ItemBlockXenolith(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + BlockXenolith.names[stack.getItemDamage() % BlockXenolith.names.length].toLowerCase();
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
