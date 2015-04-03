package com.whammich.sstow.item.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockXenolith extends ItemBlock {

	public static final String[] names = new String[] { 
		"raw",    		// 0
		"polished",		// 1
		"decorative",	// 2
		"nether",		// 3
		"soulium",		// 4
		"redstone",		// 5
		"ender",		// 6
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
