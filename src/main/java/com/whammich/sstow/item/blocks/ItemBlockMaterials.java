package com.whammich.sstow.item.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.whammich.sstow.block.BlockMaterials;

public class ItemBlockMaterials extends ItemBlock {
	
	public ItemBlockMaterials(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + BlockMaterials.names[stack.getItemDamage() % BlockMaterials.names.length];
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
}
