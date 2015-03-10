package com.whammich.sstow.utils;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.whammich.sstow.SSTheOldWays;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.block.BlockForge;
import com.whammich.sstow.block.BlockSoulium;
import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import com.whammich.sstow.guihandler.GuiHandler;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemAxeSoul;
import com.whammich.sstow.item.ItemHoeSoul;
import com.whammich.sstow.item.ItemPickaxeSoul;
import com.whammich.sstow.item.ItemShardSoul;
import com.whammich.sstow.item.ItemSpadeSoul;
import com.whammich.sstow.item.ItemSwordSoul;
import com.whammich.sstow.item.ItemfixedAchievement;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.tileentity.TileEntityForge;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Register {
	// Tool material for the soul tools/sword
	public static ToolMaterial SOULIUM = EnumHelper.addToolMaterial("SOULIUM",
			3, 3122, 12.0F, 6F, 30);

	// Setting up the enchantment details from the config
	public static Enchantment SOUL_STEALER = new EnchantmentSoulStealer(
			Config.ENCHANT_ID, Config.ENCHANT_WEIGHT);

	// Set the creative tab
	public static SoulShardTab CREATIVE_TAB = new SoulShardTab();

	// Set up the mod items
    public static Item MATERIALS = new ItemMaterials();
	public static Item FIXED = new ItemfixedAchievement();
	public static Item SOUL_SHARD = new ItemShardSoul();
	public static Item SOUL_SWORD = new ItemSwordSoul(SOULIUM);
	public static Item SOUL_PICK = new ItemPickaxeSoul(SOULIUM);
	public static Item SOUL_AXE = new ItemAxeSoul(SOULIUM);
	public static Item SOUL_HOE = new ItemHoeSoul(SOULIUM);
	public static Item SOUL_SPADE = new ItemSpadeSoul(SOULIUM);

	// Set up the mod blocks
	public static Block SOUL_CAGE = new BlockCage();
	public static Block SOUL_FORGE = new BlockForge(false).setCreativeTab(CREATIVE_TAB);
	public static Block SOUL_FORGE_ACTIVE = new BlockForge(true).setBlockName("sstow.forge_block_active");;
	public static Block SOULIUM_BLOCK = new BlockSoulium();

	public static int enchantmentSoulStealingId;

	public static void registerObjs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(SSTheOldWays.modInstance, new GuiHandler());

        registerItems();
        registerBlocks();
        registerOreDictEntries();
        registerTileEntities();
        registerRecipes();
    }

    private static void registerItems() {
        if (Loader.isModLoaded("MineFactoryReloaded"))
            GameRegistry.registerItem(FIXED, "sstow_fixed");

        GameRegistry.registerItem(MATERIALS, "sstow_materials");
        GameRegistry.registerItem(SOUL_SWORD, "sstow_soul_sword");
        GameRegistry.registerItem(SOUL_PICK, "sstow_soul_pickaxe");
        GameRegistry.registerItem(SOUL_AXE, "sstow_soul_axe");
        GameRegistry.registerItem(SOUL_HOE, "sstow_soul_hoe");
        GameRegistry.registerItem(SOUL_SPADE, "sstow_soul_spade");
        GameRegistry.registerItem(SOUL_SHARD, "sstow_soul_shard");
    }

    private static void registerBlocks() {
        GameRegistry.registerBlock(SOUL_FORGE, "sstow_forge");
        GameRegistry.registerBlock(SOUL_FORGE_ACTIVE, SOUL_FORGE_ACTIVE.getUnlocalizedName());
        GameRegistry.registerBlock(SOULIUM_BLOCK, "sstow_soulium_block");
        GameRegistry.registerBlock(SOUL_CAGE, "sstow_soul_cage");
    }

    private static void registerOreDictEntries() {
        OreDictionary.registerOre("nuggetIron", new ItemStack(MATERIALS, 1, 0));
        OreDictionary.registerOre("nuggetSoulium", new ItemStack(MATERIALS, 1, 1));
        OreDictionary.registerOre("ingotSoulium", new ItemStack(MATERIALS, 1, 2));
        OreDictionary.registerOre("dustVile", new ItemStack(MATERIALS, 1, 3));
        OreDictionary.registerOre("essenceCorrupted", new ItemStack(MATERIALS, 1, 4));
    }

    private static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityForge.class, "sstow_soul_forge_tile");
        GameRegistry.registerTileEntity(TileEntityCage.class, "sstow_soul_cage_tile");
    }

    private static void registerRecipes() {
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MATERIALS, 9, 2), SOULIUM_BLOCK));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MATERIALS, 9, 1), "ingotSoulium"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MATERIALS, 9, 0), "ingotIron"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOUL_FORGE), "SSS", "SCS", "OOO", 'S', "cobblestone", 'C', "essenceCorrupted", 'O', Blocks.obsidian));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MATERIALS, 1, 2), "AAA", "AAA", "AAA", 'A', "nuggetSoulium"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.iron_ingot), "AAA", "AAA", "AAA", 'A', "nuggetIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOULIUM_BLOCK), "AAA", "AAA", "AAA", 'A', "ingotSoulium"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOUL_SWORD), "A", "A", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOUL_PICK), "AAA", "CBC", "CBC", 'A', "ingotSoulium", 'B', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOUL_AXE), "AAC", "ABC", "CBC", 'A', "ingotSoulium", 'B', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOUL_HOE), "AAC", "CBC", "CBC", 'A', "ingotSoulium", 'B', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOUL_SPADE), "A", "B", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SOUL_CAGE), "SIS", "IXI", "SIS", 'I', Blocks.iron_bars, 'S', "ingotSoulium"));

        if (Loader.isModLoaded("Natura")) {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MATERIALS, 2, 3), Blocks.soul_sand, "dustGlowstone"));
            GameRegistry.addSmelting(new ItemStack(MATERIALS, 1, 3), new ItemStack(MATERIALS, 1, 4), 0.35F);
        } else {
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(MATERIALS, 1, 4), "dustGlowstone", "dustVile"));
            GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(MATERIALS, 1, 3), 0.35F);
        }
    }
}
