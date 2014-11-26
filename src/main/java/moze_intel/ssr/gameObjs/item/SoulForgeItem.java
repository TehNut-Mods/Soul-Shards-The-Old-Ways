package moze_intel.ssr.gameObjs.item;

import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class SoulForgeItem extends ItemBlock {
	
	public SoulForgeItem(Block block) {
		super(ObjHandler.SOUL_FORGE);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "tile.ssr.soul_forge";
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
