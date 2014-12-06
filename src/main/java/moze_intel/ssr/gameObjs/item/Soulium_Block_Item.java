package moze_intel.ssr.gameObjs.item;

import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class Soulium_Block_Item extends ItemBlock {
	
	public Soulium_Block_Item(Block block) {
		super(ObjHandler.SOULIUM_BLOCK);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "tile.ssr.soulium_block";
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
