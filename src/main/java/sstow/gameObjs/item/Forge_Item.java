package sstow.gameObjs.item;

import sstow.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

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
