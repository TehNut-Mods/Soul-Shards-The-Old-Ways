package moze_intel.ssr.gameObjs;

import moze_intel.ssr.utils.SSRConfig;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ObjHandler {
	public static final Enchantment SOUL_STEALER = new SoulStealerEnchant(
			SSRConfig.ENCHANT_ID, SSRConfig.ENCHANT_WEIGHT);
	public static final SSRCreativeTab CREATIVE_TAB = new SSRCreativeTab();
	public static final Item VILE_DUST = new VileDustItem();
	public static final Item CORRUPTED_ESSENCE = new CorruptedEssenceItem();
	public static final Item SOUL_SHARD = new SoulShardItem();
	public static final Block SOUL_CAGE = new SoulCageBlock();
	public static final Block SOUL_FORGE = new SoulForgeBlock();

	public static void registerObjs() {

		GameRegistry.registerItem(VILE_DUST, "ssr_vile_dust");
		GameRegistry.registerItem(CORRUPTED_ESSENCE, "ssr_corrupted_essence");
		GameRegistry.registerItem(SOUL_SHARD, "ssr_soul_shard");
		GameRegistry.registerBlock(SOUL_CAGE, SoulCageItem.class,
				"ssr_soul_cage");

		GameRegistry.registerBlock(SOUL_FORGE, "ssr_soul_forge");

		GameRegistry.registerTileEntity(SoulCageTile.class,
				"ssr_soul_cage_tile");

		// GameRegistry.registerTileEntity(SoulForgeTile.class,
		// "ssr_soul_forge_tile");

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_CAGE),
				"III", "IXI", "III", 'I', Blocks.iron_bars);

		GameRegistry.addShapelessRecipe(new ItemStack(
				ObjHandler.CORRUPTED_ESSENCE, 1), Items.glowstone_dust,
				ObjHandler.VILE_DUST);

		GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(
				ObjHandler.VILE_DUST), 0.35F);

		/*
		 * if (SSRConfig.ENABLE_ENDSTONE_RECIPE)
		 * GameRegistry.addShapedRecipe(new ItemStack(Blocks.end_stone, 2),
		 * " a ", "bcb", " a ", 'a', Blocks.stone, 'b', new ItemStack(
		 * Blocks.sand, 1, 0), 'c', Items.ender_pearl);
		 */
	}
}
