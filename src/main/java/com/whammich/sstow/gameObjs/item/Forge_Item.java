package com.whammich.sstow.gameObjs.item;

import com.whammich.sstow.gameObjs.ObjHandler;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class Forge_Item extends ItemBlock {
	
	public Forge_Item(Block block) {
		super(ObjHandler.SOUL_FORGE);
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
