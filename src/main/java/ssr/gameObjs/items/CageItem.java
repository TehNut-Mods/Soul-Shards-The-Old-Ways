package ssr.gameObjs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import ssr.gameObjs.ObjHandler;

public class CageItem extends ItemBlock {

	public CageItem(Block block) {
		super(ObjHandler.sCage);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "";
		switch (stack.getItemDamage()) {
		case 0:
			name = "empty_sc";
			break;
		case 1:
			name = "inactive_sc";
			break;
		case 2:
			name = "active_sc";
			break;
		}
		return name;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
}
