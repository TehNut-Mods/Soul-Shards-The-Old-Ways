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
	public static final Item SOULIUM_INGOT = new SouliumIngot();
	public static final Block SOUL_CAGE = new SoulCageBlock();
	public static final Block SOUL_FORGE = new SoulForgeBlock();
	public static final Block SOULIUM_BLOCK = new SouliumBlock();

	public static void registerObjs() {

		if (SSRConfig.THEOLDWAYS == true) {
			GameRegistry.registerItem(VILE_DUST, "ssr_vile_dust");
			
			GameRegistry.registerItem(CORRUPTED_ESSENCE,
					"ssr_corrupted_essence");
			
			GameRegistry.registerItem(SOULIUM_INGOT, "ssr_soulium_ingot");
			
			GameRegistry.registerBlock(SOUL_FORGE, SoulForgeItem.class,
					"ssr_forge_block");
			
			GameRegistry.registerTileEntity(SoulForgeTile.class,
					"ssr_soul_forge_tile");
			
			GameRegistry.registerBlock(SOULIUM_BLOCK, SouliumBlockItem.class,
					"ssr_soulium_block");
			
			GameRegistry.addShapelessRecipe(new ItemStack(
					ObjHandler.CORRUPTED_ESSENCE, 1), Items.glowstone_dust,
					ObjHandler.VILE_DUST);
			
			GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_FORGE),
					"SSS", "SCS", "OOO", 'S', Blocks.stone, 'C',
					ObjHandler.CORRUPTED_ESSENCE, 'O', Blocks.obsidian);
			
			GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(
					ObjHandler.VILE_DUST), 0.35F);
			
			GameRegistry.addShapedRecipe(
					new ItemStack(ObjHandler.SOULIUM_BLOCK), "AAA", "AAA",
					"AAA", 'A', ObjHandler.SOULIUM_INGOT);
			
			GameRegistry.addShapelessRecipe(new ItemStack(
					ObjHandler.SOULIUM_INGOT, 9), ObjHandler.SOULIUM_BLOCK);
		}

		GameRegistry.registerItem(SOUL_SHARD, "ssr_soul_shard");
		
		GameRegistry.registerBlock(SOUL_CAGE, SoulCageItem.class,
				"ssr_soul_cage");
		
		GameRegistry.registerTileEntity(SoulCageTile.class,
				"ssr_soul_cage_tile");
		
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
