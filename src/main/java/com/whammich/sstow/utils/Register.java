package com.whammich.sstow.utils;

import com.whammich.sstow.block.Cage_Block;
import com.whammich.sstow.block.Forge_Block;
import com.whammich.sstow.block.Soulium_Block;
import com.whammich.sstow.enchantment.SoulStealerEnchant;
import com.whammich.sstow.guihandler.GuiHandler;
import com.whammich.sstow.item.Corrupted_Essence;
import com.whammich.sstow.item.Iron_Nugget;
import com.whammich.sstow.item.Soul_Axe;
import com.whammich.sstow.item.Soul_Hoe;
import com.whammich.sstow.item.Soul_Pickaxe;
import com.whammich.sstow.item.Soul_Shard;
import com.whammich.sstow.item.Soul_Spade;
import com.whammich.sstow.item.Soul_Sword;
import com.whammich.sstow.item.Soulium_Ingot;
import com.whammich.sstow.item.Soulium_Nugget;
import com.whammich.sstow.item.Vile_Dust;
import com.whammich.sstow.item.fixedAchievement;
import com.whammich.sstow.tileentity.CageTile;
import com.whammich.sstow.tileentity.ForgeTile;
import com.whammich.sstow.SSTheOldWays;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Register {
	// Tool material for the soul tools/sword
	public static ToolMaterial SOULIUM = EnumHelper.addToolMaterial("SOULIUM",
			3, 3122, 12.0F, 6F, 30);

	// Setting up the enchantment details from the config
	public static Enchantment SOUL_STEALER = new SoulStealerEnchant(
			Config.ENCHANT_ID, Config.ENCHANT_WEIGHT);

	// Set the creative tab
	public static SoulShardTab CREATIVE_TAB = new SoulShardTab();

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
		NetworkRegistry.INSTANCE.registerGuiHandler(SSTheOldWays.modInstance,
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
		GameRegistry.addShapelessRecipe(new ItemStack(Register.SOULIUM_INGOT,
				9), Register.SOULIUM_BLOCK);

		GameRegistry.addShapelessRecipe(new ItemStack(
				Register.SOULIUM_NUGGET, 9), Register.SOULIUM_INGOT);

		GameRegistry.addShapelessRecipe(
				new ItemStack(Register.IRON_NUGGET, 9), Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOUL_FORGE),
				"SSS", "SCS", "OOO", 'S', Blocks.cobblestone, 'C',
				Register.CORRUPTED_ESSENCE, 'O', Blocks.obsidian);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOULIUM_INGOT),
				"AAA", "AAA", "AAA", 'A', Register.SOULIUM_NUGGET);

		GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "AAA",
				"AAA", "AAA", 'A', Register.IRON_NUGGET);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOULIUM_BLOCK),
				"AAA", "AAA", "AAA", 'A', Register.SOULIUM_INGOT);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOUL_SWORD), "A",
				"A", "B", 'A', Register.SOULIUM_INGOT, 'B', Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOUL_PICK),
				"AAA", "CBC", "CBC", 'A', Register.SOULIUM_INGOT, 'B',
				Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOUL_AXE), "AAC",
				"ABC", "CBC", 'A', Register.SOULIUM_INGOT, 'B',
				Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOUL_HOE), "AAC",
				"CBC", "CBC", 'A', Register.SOULIUM_INGOT, 'B',
				Items.iron_ingot);

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOUL_SPADE), "A",
				"B", "B", 'A', Register.SOULIUM_INGOT, 'B', Items.iron_ingot);

		GameRegistry.registerItem(SOUL_SHARD, "sstow_soul_shard");

		GameRegistry.registerBlock(SOUL_CAGE, "sstow_soul_cage");

		GameRegistry.registerTileEntity(CageTile.class, "sstow_soul_cage_tile");

		GameRegistry.addShapedRecipe(new ItemStack(Register.SOUL_CAGE),
				"SIS", "IXI", "SIS", 'I', Blocks.iron_bars, 'S',
				Register.SOULIUM_INGOT);

		if (Loader.isModLoaded("Natura")) {

			GameRegistry.addShapelessRecipe(new ItemStack(Register.VILE_DUST,
					2), Blocks.soul_sand, Items.glowstone_dust);

			GameRegistry.addSmelting(Register.VILE_DUST, new ItemStack(
					Register.CORRUPTED_ESSENCE), 0.35F);

		} else {

			GameRegistry.addShapelessRecipe(new ItemStack(
					Register.CORRUPTED_ESSENCE, 1), Items.glowstone_dust,
					Register.VILE_DUST);

			GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(
					Register.VILE_DUST), 0.35F);
		}

	}
}
