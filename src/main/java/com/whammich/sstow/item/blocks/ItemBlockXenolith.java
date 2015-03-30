package com.whammich.sstow.item.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockXenolith extends ItemBlock {

	public static final String[] names = new String[] { 
		"raw",    		// 0 
		"decorative",	// 1
		"nether",		// 2
		"soulium"		// 3
	};

	public ItemBlockXenolith(Block block) {
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
