package sstow.gameObjs;

//Import Blocks
//Import Required Minecraft stuff
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import sstow.Main;
import sstow.gameObjs.block.Cage_Block;
import sstow.gameObjs.block.Forge_Block;
import sstow.gameObjs.block.Soulium_Block;
//Import Items
import sstow.gameObjs.item.Corrupted_Essence;
import sstow.gameObjs.item.Iron_Nugget;
import sstow.gameObjs.item.Soul_Axe;
import sstow.gameObjs.item.Soul_Hoe;
import sstow.gameObjs.item.Soul_Pickaxe;
import sstow.gameObjs.item.Soul_Shard;
import sstow.gameObjs.item.Soul_Spade;
import sstow.gameObjs.item.Soul_Sword;
import sstow.gameObjs.item.Soulium_Ingot;
import sstow.gameObjs.item.Soulium_Nugget;
import sstow.gameObjs.item.Vile_Dust;
import sstow.gameObjs.item.fixedAchievement;
//Import Tile Entities
import sstow.gameObjs.tile.CageTile;
import sstow.gameObjs.tile.ForgeTile;
import sstow.handler.GuiHandler;
//Import Config
import sstow.utils.Config;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ObjHandler {
	// Tool material for the soul tools/sword
	public static ToolMaterial SOULIUM = EnumHelper.addToolMaterial("SOULIUM",
			3, 3122, 12.0F, 6F, 30);

	// Setting up the enchantment details from the config
	public static Enchantment SOUL_STEALER = new SoulStealerEnchant(
			Config.ENCHANT_ID, Config.ENCHANT_WEIGHT);

	// Set the creative tab
	public static CreativeTab CREATIVE_TAB = new CreativeTab();

	// Set up the mod items
	public static Item FIXED = new fixedAchievement();
	public static Item VILE_DUST = new Vile_Dust();
	public static Item CORRUPTED_ESSENCE = new Corrupted_Essence();
	public static Item SOUL_SHARD = new Soul_Shard();
	public static Item SOULIUM_INGOT = new Soulium_Ingot();
	public static Item SOULIUM_NUGGET = new Soulium_Nugget();
	public static Item SOUL_SWORD = new Soul_Sword(SOULIUM);
	public static Item SOUL_PICK = new Soul_Pickaxe(SOULIUM);
	public static Item SOUL_AXE = new Soul_Axe(SOULIUM);
	public static Item SOUL_HOE = new Soul_Hoe(SOULIUM);
	public static Item SOUL_SPADE = new Soul_Spade(SOULIUM);
	public static Item IRON_NUGGET = new Iron_Nugget();

	// Set up the mod blocks
	public static Block SOUL_CAGE = new Cage_Block();
	public static Block SOUL_FORGE;
	public static Block SOUL_FORGE_ACTIVE;
	public static Block SOULIUM_BLOCK = new Soulium_Block();

	public static void initialisedBlock() {
		SOUL_FORGE = new Forge_Block(false).setCreativeTab(CREATIVE_TAB);
		SOUL_FORGE_ACTIVE = new Forge_Block(true)
				.setBlockName("sstow.forge_block_active");
	}

	public static int enchantmentSoulStealingId;

	public static void registerObjs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.modInstance,
				new GuiHandler());

		// Register Items
		if (Loader.isModLoaded("MineFactoryReloaded"))
			GameRegistry.registerItem(FIXED, "sstow_fixed");

		GameRegistry.registerItem(VILE_DUST, "sstow_vile_dust");
		GameRegistry.registerItem(CORRUPTED_ESSENCE, "sstow_corrupted_essence");
		GameRegistry.registerItem(SOULIUM_INGOT, "sstow_soulium_ingot");
		GameRegistry.registerItem(SOULIUM_NUGGET, "sstow_soulium_nugget");
		GameRegistry.registerItem(IRON_NUGGET, "sstow_iron_nugget");
		GameRegistry.registerItem(SOUL_SWORD, "sstow_soul_sword");
		GameRegistry.registerItem(SOUL_PICK, "sstow_soul_pickaxe");
		GameRegistry.registerItem(SOUL_AXE, "sstow_soul_axe");
		GameRegistry.registerItem(SOUL_HOE, "sstow_soul_hoe");
		GameRegistry.registerItem(SOUL_SPADE, "sstow_soul_spade");

		// Register Blocks
		initialisedBlock();
		GameRegistry.registerBlock(SOUL_FORGE, "sstow_forge");
		GameRegistry.registerBlock(SOUL_FORGE_ACTIVE,
				SOUL_FORGE_ACTIVE.getUnlocalizedName());
		GameRegistry.registerBlock(SOULIUM_BLOCK, "sstow_soulium_block");

		// Register Tile Entities
		GameRegistry.registerTileEntity(ForgeTile.class,
				"sstow_soul_forge_tile");

		// Register Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(ObjHandler.SOULIUM_INGOT,
				9), ObjHandler.SOULIUM_BLOCK);

		GameRegistry.addShapelessRecipe(new ItemStack(
				ObjHandler.SOULIUM_NUGGET, 9), ObjHandler.SOULIUM_INGOT);

		GameRegistry.addShapelessRecipe(
				new ItemStack(ObjHandler.IRON_NUGGET, 9), Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_FORGE),
				"SSS", "SCS", "OOO", 'S', Blocks.cobblestone, 'C',
				ObjHandler.CORRUPTED_ESSENCE, 'O', Blocks.obsidian);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOULIUM_INGOT),
				"AAA", "AAA", "AAA", 'A', ObjHandler.SOULIUM_NUGGET);

		GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "AAA",
				"AAA", "AAA", 'A', ObjHandler.IRON_NUGGET);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOULIUM_BLOCK),
				"AAA", "AAA", "AAA", 'A', ObjHandler.SOULIUM_INGOT);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_SWORD), "A",
				"A", "B", 'A', ObjHandler.SOULIUM_INGOT, 'B', Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_PICK),
				"AAA", "CBC", "CBC", 'A', ObjHandler.SOULIUM_INGOT, 'B',
				Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_AXE), "AAC",
				"ABC", "CBC", 'A', ObjHandler.SOULIUM_INGOT, 'B',
				Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_HOE), "AAC",
				"CBC", "CBC", 'A', ObjHandler.SOULIUM_INGOT, 'B',
				Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_SPADE), "A",
				"B", "B", 'A', ObjHandler.SOULIUM_INGOT, 'B', Items.iron_ingot);

		GameRegistry.registerItem(SOUL_SHARD, "sstow_soul_shard");

		GameRegistry.registerBlock(SOUL_CAGE, "sstow_soul_cage");

		GameRegistry.registerTileEntity(CageTile.class, "sstow_soul_cage_tile");

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_CAGE),
				"SIS", "IXI", "SIS", 'I', Blocks.iron_bars, 'S',
				ObjHandler.SOULIUM_INGOT);

		if (Loader.isModLoaded("Natura")) {

			GameRegistry.addShapelessRecipe(new ItemStack(ObjHandler.VILE_DUST,
					2), Blocks.soul_sand, Items.glowstone_dust);

			GameRegistry.addSmelting(ObjHandler.VILE_DUST, new ItemStack(
					ObjHandler.CORRUPTED_ESSENCE), 0.35F);

		} else {

			GameRegistry.addShapelessRecipe(new ItemStack(
					ObjHandler.CORRUPTED_ESSENCE, 1), Items.glowstone_dust,
					ObjHandler.VILE_DUST);

			GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(
					ObjHandler.VILE_DUST), 0.35F);
		}

	}
}
