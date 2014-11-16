package moze_intel.ssr.gameObjs;

import cpw.mods.fml.common.registry.GameRegistry;
import moze_intel.ssr.utils.SSRConfig;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ObjHandler {
	public static final Enchantment SOUL_STEALER = new SoulStealerEnchant(SSRConfig.ENCHANT_ID, SSRConfig.ENCHANT_WEIGHT);
	public static final SSRCreativeTab CREATIVE_TAB = new SSRCreativeTab();
	public static final Item SOUL_SHARD = new SoulShardItem();
	public static final Block SOUL_CAGE = new SoulCageBlock();

	public static void registerObjs() {
		GameRegistry.registerItem(SOUL_SHARD, "ssr_soul_shard");
		GameRegistry.registerBlock(SOUL_CAGE, SoulCageItem.class, "ssr_soul_cage");
		GameRegistry.registerTileEntity(SoulCageTile.class, "ssr_soul_cage_tile");

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_CAGE),
				"III", "IXI", "III", 'I', Blocks.iron_bars);

		/*
		 * if (SSRConfig.ENABLE_ENDSTONE_RECIPE)
		 * GameRegistry.addShapedRecipe(new ItemStack(Blocks.end_stone, 2),
		 * " a ", "bcb", " a ", 'a', Blocks.stone, 'b', new ItemStack(
		 * Blocks.sand, 1, 0), 'c', Items.ender_pearl);
		 */
	}
}
