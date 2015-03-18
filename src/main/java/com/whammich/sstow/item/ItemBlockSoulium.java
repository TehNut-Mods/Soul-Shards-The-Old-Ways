package com.whammich.sstow.item;

import com.whammich.sstow.utils.Register;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockSoulium extends ItemBlock {
	
	public ItemBlockSoulium(Block block) {
		super(Register.BlockSoulium);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "tile.sstow.soulium_block";
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
