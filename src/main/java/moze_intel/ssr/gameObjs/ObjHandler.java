package moze_intel.ssr.gameObjs;

import moze_intel.ssr.gameObjs.block.SoulCageBlock;
import moze_intel.ssr.gameObjs.block.SoulForgeBlock;
import moze_intel.ssr.gameObjs.block.SouliumBlock;
import moze_intel.ssr.gameObjs.item.CorruptedEssenceItem;
import moze_intel.ssr.gameObjs.item.IronNugget;
import moze_intel.ssr.gameObjs.item.SoulAxeItem;
import moze_intel.ssr.gameObjs.item.SoulForgeItem;
import moze_intel.ssr.gameObjs.item.SoulHoeItem;
import moze_intel.ssr.gameObjs.item.SoulPickaxeItem;
import moze_intel.ssr.gameObjs.item.SoulShardItem;
import moze_intel.ssr.gameObjs.item.SoulSpadeItem;
import moze_intel.ssr.gameObjs.item.SoulSwordItem;
import moze_intel.ssr.gameObjs.item.SouliumBlockItem;
import moze_intel.ssr.gameObjs.item.SouliumIngot;
import moze_intel.ssr.gameObjs.item.SouliumNugget;
import moze_intel.ssr.gameObjs.item.VileDustItem;
import moze_intel.ssr.gameObjs.tile.SoulCageTile;
import moze_intel.ssr.gameObjs.tile.SoulForgeTile;
import moze_intel.ssr.utils.SSRConfig;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public class ObjHandler {

	public static ToolMaterial SOULIUM = EnumHelper.addToolMaterial("SOULIUM",
			2, 750, 7.0F, 2.5F, 22);

	public static Enchantment SOUL_STEALER = new SoulStealerEnchant(
			SSRConfig.ENCHANT_ID, SSRConfig.ENCHANT_WEIGHT);

	public static SSRCreativeTab CREATIVE_TAB = new SSRCreativeTab();
	public static Item VILE_DUST = new VileDustItem();
	public static Item CORRUPTED_ESSENCE = new CorruptedEssenceItem();
	public static Item SOUL_SHARD = new SoulShardItem();
	public static Item SOULIUM_INGOT = new SouliumIngot();
	public static Item SOULIUM_NUGGET = new SouliumNugget();
	public static Item SOUL_SWORD = new SoulSwordItem(SOULIUM);
	public static Item SOUL_PICK = new SoulPickaxeItem(SOULIUM);
	public static Item SOUL_AXE = new SoulAxeItem(SOULIUM);
	public static Item SOUL_HOE = new SoulHoeItem(SOULIUM);
	public static Item SOUL_SPADE = new SoulSpadeItem(SOULIUM);
	public static Item IRON_NUGGET = new IronNugget();
	public static Block SOUL_CAGE = new SoulCageBlock();
	public static Block SOUL_FORGE = new SoulForgeBlock();
	public static Block SOULIUM_BLOCK = new SouliumBlock();

	public static void registerObjs() {

		if (SSRConfig.THEOLDWAYS == true) {
			GameRegistry.registerItem(VILE_DUST, "ssr_vile_dust");

			GameRegistry.registerItem(CORRUPTED_ESSENCE,
					"ssr_corrupted_essence");

			GameRegistry.registerItem(SOULIUM_INGOT, "ssr_soulium_ingot");
			GameRegistry.registerItem(SOULIUM_NUGGET, "ssr_soulium_nugget");
			GameRegistry.registerItem(IRON_NUGGET, "ssr_iron_nugget");
			GameRegistry.registerItem(SOUL_SWORD, "ssr_soul_sword");
			GameRegistry.registerItem(SOUL_PICK, "ssr_soul_pickaxe");
			GameRegistry.registerItem(SOUL_AXE, "ssr_soul_axe");
			GameRegistry.registerItem(SOUL_HOE, "ssr_soul_hoe");
			GameRegistry.registerItem(SOUL_SPADE, "ssr_soul_spade");

			GameRegistry.registerBlock(SOUL_FORGE, SoulForgeItem.class,
					"ssr_forge_block");

			GameRegistry.registerBlock(SOULIUM_BLOCK, SouliumBlockItem.class,
					"ssr_soulium_block");

			GameRegistry.registerTileEntity(SoulForgeTile.class,
					"ssr_soul_forge_tile");

			GameRegistry.addShapelessRecipe(new ItemStack(
					ObjHandler.CORRUPTED_ESSENCE, 1), Items.glowstone_dust,
					ObjHandler.VILE_DUST);

			GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_FORGE),
					"SSS", "SCS", "OOO", 'S', Blocks.stone, 'C',
					ObjHandler.CORRUPTED_ESSENCE, 'O', Blocks.obsidian);

			GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(
					ObjHandler.VILE_DUST), 0.35F);

			GameRegistry.addShapedRecipe(
					new ItemStack(ObjHandler.SOULIUM_INGOT), "AAA", "AAA",
					"AAA", 'A', ObjHandler.SOULIUM_NUGGET);

			GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot),
					"AAA", "AAA", "AAA", 'A', ObjHandler.IRON_NUGGET);

			GameRegistry.addShapedRecipe(
					new ItemStack(ObjHandler.SOULIUM_BLOCK), "AAA", "AAA",
					"AAA", 'A', ObjHandler.SOULIUM_INGOT);

			GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_SWORD),
					"A", "A", "B", 'A', ObjHandler.SOULIUM_INGOT, 'B',
					Items.iron_ingot);

			GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_PICK),
					"AAA", "CBC", "CBC", 'A', ObjHandler.SOULIUM_INGOT, 'B',
					Items.iron_ingot);

			GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_AXE),
					"AAC", "ABC", "CBC", 'A', ObjHandler.SOULIUM_INGOT, 'B',
					Items.iron_ingot);

			GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_HOE),
					"AAC", "CBC", "CBC", 'A', ObjHandler.SOULIUM_INGOT, 'B',
					Items.iron_ingot);

			GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_SPADE),
					"A", "B", "B", 'A', ObjHandler.SOULIUM_INGOT, 'B',
					Items.iron_ingot);

			GameRegistry.addShapelessRecipe(new ItemStack(
					ObjHandler.SOULIUM_INGOT, 9), ObjHandler.SOULIUM_BLOCK);
			GameRegistry.addShapelessRecipe(new ItemStack(
					ObjHandler.SOULIUM_NUGGET, 9), ObjHandler.SOULIUM_INGOT);
			GameRegistry.addShapelessRecipe(new ItemStack(
					ObjHandler.IRON_NUGGET, 9), Items.iron_ingot);
		}

		GameRegistry.registerItem(SOUL_SHARD, "ssr_soul_shard");

		GameRegistry.registerBlock(SOUL_CAGE, "ssr_soul_cage");

		GameRegistry.registerTileEntity(SoulCageTile.class,
				"ssr_soul_cage_tile");

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_CAGE),
				"III", "IXI", "III", 'I', Blocks.iron_bars);

		
		if (SSRConfig.ENABLE_ENDSTONE_RECIPE){
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.end_stone, 2),
				" a ", "bcb", " a ", 'a', Blocks.stone, 'b', new ItemStack(
			Blocks.sand, 1, 0), 'c', Items.ender_pearl);
		}
	}
}
