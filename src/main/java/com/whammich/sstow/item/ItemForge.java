package com.whammich.sstow.item;

import com.whammich.sstow.utils.Register;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemForge extends ItemBlock {
	
	public ItemForge(Block block) {
		super(Register.BlockForge);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soul_forge";
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
