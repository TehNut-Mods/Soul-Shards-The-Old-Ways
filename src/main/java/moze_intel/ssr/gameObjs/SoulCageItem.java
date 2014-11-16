package moze_intel.ssr.gameObjs;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import moze_intel.ssr.gameObjs.ObjHandler;

public class SoulCageItem extends ItemBlock {
	
	public SoulCageItem(Block block) {
		super(ObjHandler.SOUL_CAGE);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "tile.ssr.soul_cage_"
				+ MathHelper.clamp_int(stack.getItemDamage(), 0, 2);
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
