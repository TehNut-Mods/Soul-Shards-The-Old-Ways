package com.whammich.sstow.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.whammich.sstow.SSTheOldWays;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.block.BlockDarkstone;
import com.whammich.sstow.block.BlockDiffuser;
import com.whammich.sstow.block.BlockForge;
import com.whammich.sstow.block.BlockGlassObsidian;
import com.whammich.sstow.block.BlockInfuser;
import com.whammich.sstow.block.BlockMaterials;
import com.whammich.sstow.block.BlockPetrified;
import com.whammich.sstow.block.BlockPetrified2;
import com.whammich.sstow.block.BlockPlankPetrified;
import com.whammich.sstow.block.BlockSoulCrystal;
import com.whammich.sstow.block.BlockXenoLight;
import com.whammich.sstow.block.BlockXenolith;
import com.whammich.sstow.compat.baubles.BaubleAnimus;
import com.whammich.sstow.compat.baubles.BaubleConservo;
import com.whammich.sstow.compat.baubles.ItemGems;
import com.whammich.sstow.compat.baubles.ItemSockets;
import com.whammich.sstow.compat.guideapi.pages.PageNBTHelper;
import com.whammich.sstow.compat.tcon.TCon;
import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import com.whammich.sstow.entity.mob.hostile.EntityZombieWitch;
import com.whammich.sstow.guihandler.GuiHandler;
import com.whammich.sstow.item.ItemLootPage;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemModules;
import com.whammich.sstow.item.ItemShardSoul;
import com.whammich.sstow.item.blocks.ItemBlockForge;
import com.whammich.sstow.item.blocks.ItemBlockMaterials;
import com.whammich.sstow.item.blocks.ItemBlockPetrified;
import com.whammich.sstow.item.blocks.ItemBlockPetrified2;
import com.whammich.sstow.item.blocks.ItemBlockPlankPetrified;
import com.whammich.sstow.item.blocks.ItemBlockXenolith;
import com.whammich.sstow.item.tools.ItemAxeShadow;
import com.whammich.sstow.item.tools.ItemAxeSoul;
import com.whammich.sstow.item.tools.ItemPickaxeShadow;
import com.whammich.sstow.item.tools.ItemPickaxeSoul;
import com.whammich.sstow.item.tools.ItemSpadeShadow;
import com.whammich.sstow.item.tools.ItemSpadeSoul;
import com.whammich.sstow.item.tools.ItemSwordShadow;
import com.whammich.sstow.item.tools.ItemSwordSoul;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.tileentity.TileEntityDiffuser;
import com.whammich.sstow.tileentity.TileEntityForge;
import com.whammich.sstow.tileentity.TileEntitySoulCrystal;
import com.whammich.sstow.world.generation.biome.BiomeGenPetForest;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Register {

	public static String[] pageNBTHelper;

	// Tool material for the soul tools/sword
	public static ToolMaterial SOULIUM = EnumHelper.addToolMaterial("SOULIUM", 3, 3122, 12.0F, 6F, 30);
	public static ToolMaterial SHADOW = EnumHelper.addToolMaterial("SHADOW", 3, 4000, 20F, 12F, 60);

	// Setting up the enchantment details from the config
	public static Enchantment SOUL_STEALER = new EnchantmentSoulStealer(Config.enchantID, Config.enchantWeight);

	// Set the creative tab
	public static SoulShardTab CREATIVE_TAB = new SoulShardTab();

	// Set up the mod items
	public static Item ItemMaterials = new ItemMaterials();
	public static Item ItemModules = new ItemModules();
	public static Item ItemShardSoul = new ItemShardSoul();
	public static Item ItemLootPage = new ItemLootPage();
	public static Item ItemSwordSoul = new ItemSwordSoul(SOULIUM);
	public static Item ItemPickaxeSoul = new ItemPickaxeSoul(SOULIUM);
	public static Item ItemAxeSoul = new ItemAxeSoul(SOULIUM);
	public static Item ItemSpadeSoul = new ItemSpadeSoul(SOULIUM);
	
	public static Item ItemSwordShadow = new ItemSwordShadow(SHADOW);
	public static Item ItemPickaxeShadow = new ItemPickaxeShadow(SHADOW);
	public static Item ItemAxeShadow = new ItemAxeShadow(SHADOW);
	public static Item ItemSpadeShadow = new ItemSpadeShadow(SHADOW);

	// Set up bauble items
	public static Item animusBauble = new BaubleAnimus();
	public static Item conservoBauble = new BaubleConservo();
	public static Item baubleGems = new ItemGems();
	public static Item baubleSockets = new ItemSockets();

	// Set up the mod blocks
	public static Block BlockCage = new BlockCage();
	public static Block BlockForge = new BlockForge(false).setCreativeTab(CREATIVE_TAB);
	public static Block BlockForgeActive = new BlockForge(true).setBlockName(Reference.modID + ".BlockForgeActive");
	public static Block BlockMaterials = new BlockMaterials();
	public static Block BlockXenoLight = new BlockXenoLight();
	
	public static Block BlockDarkstone = new BlockDarkstone();
	
	public static Block BlockXenolith = new BlockXenolith();
	public static Block BlockPetrified = new BlockPetrified();
	public static Block BlockPetrified2 = new BlockPetrified2();
	public static Block BlockPetrifiedPlanks = new BlockPlankPetrified();
	public static Block BlockObsidianGlass = new BlockGlassObsidian();

	public static Block BlockDiffuser = new BlockDiffuser(); 
	public static Block BlockInfuser = new BlockInfuser();

	// Custom Blocks
	public static Block BlockSoulCrystal = new BlockSoulCrystal();

	// Set up Biomes
	public static BiomeGenBase biomePetrifiedForest = new BiomeGenPetForest(137)
	.setBiomeName("Petrified Forest").setTemperatureRainfall(0.95F, 0.9F).setColor(000000);

	// Set up Custom Entities
	private static final Class<? extends EntityMob> entityZombieWitch = EntityZombieWitch.class; 
	
	public static void registerObjs() {
		NetworkRegistry.INSTANCE.registerGuiHandler(SSTheOldWays.modInstance, new GuiHandler());
		registerItems();
		registerBlocks();
		registerOreDictEntries();
		registerTileEntities();
		removeRecipes();
		registerRecipes();
		registerFluids();
		if(!Config.oldWaysOption) {
			if(Config.petrifiedForest){
				registerBiomes();
			}
			registerEntities();
			registerLoot();
		}
	}

	private static void registerItems() {
		GameRegistry.registerItem(ItemMaterials, "ItemMaterialsSoul");
		GameRegistry.registerItem(ItemModules, "ItemModulesSoul");
		GameRegistry.registerItem(ItemShardSoul, "ItemShardSoul");
		GameRegistry.registerItem(ItemSwordSoul, "ItemSwordSoul");
		GameRegistry.registerItem(ItemPickaxeSoul, "ItemPickaxeSoul");
		GameRegistry.registerItem(ItemAxeSoul, "ItemAxeSoul");
		GameRegistry.registerItem(ItemSpadeSoul, "ItemSpadeSoul");
		
		if(!Config.oldWaysOption) {
			GameRegistry.registerItem(ItemLootPage, "ItemLootPage");
			GameRegistry.registerItem(animusBauble, animusBauble.getUnlocalizedName());
			GameRegistry.registerItem(conservoBauble, conservoBauble.getUnlocalizedName());
			GameRegistry.registerItem(baubleGems, baubleGems.getUnlocalizedName());
			GameRegistry.registerItem(baubleSockets, baubleSockets.getUnlocalizedName());
			GameRegistry.registerItem(ItemSwordShadow, "ItemSwordShadow");
			GameRegistry.registerItem(ItemPickaxeShadow, "ItemPickaxeShadow");
			GameRegistry.registerItem(ItemAxeShadow, "ItemAxeShadow");
			GameRegistry.registerItem(ItemSpadeShadow, "ItemSpadeShadow");
			
		}

	}

	private static void registerBlocks() {
		GameRegistry.registerBlock(BlockForge, ItemBlockForge.class, "BlockForge");
		GameRegistry.registerBlock(BlockForgeActive, BlockForgeActive.getUnlocalizedName());
		GameRegistry.registerBlock(BlockMaterials, ItemBlockMaterials.class, "BlockMaterials");
		GameRegistry.registerBlock(BlockCage, "BlockCage");

		if(!Config.oldWaysOption) {
			GameRegistry.registerBlock(BlockXenoLight, "BlockXenoLight");
			GameRegistry.registerBlock(BlockDarkstone, "BlockDarkstone");
			GameRegistry.registerBlock(BlockObsidianGlass, "BlockObsidGlass");
			GameRegistry.registerBlock(BlockXenolith, ItemBlockXenolith.class, "BlockXenolith");
			GameRegistry.registerBlock(BlockPetrified, ItemBlockPetrified.class, "BlockPetrifiedLog");
			GameRegistry.registerBlock(BlockPetrified2, ItemBlockPetrified2.class, "BlockPetrifiedLog2");
			GameRegistry.registerBlock(BlockPetrifiedPlanks, ItemBlockPlankPetrified.class, "BlockPetrifiedPlanks");
			GameRegistry.registerBlock(BlockSoulCrystal, "BlockSoulCrystal");
			GameRegistry.registerBlock(BlockDiffuser, "BlockDiffuser");
			GameRegistry.registerBlock(BlockInfuser, "BlockInfuser");
		}

	}

	private static void registerOreDictEntries() {
		// Materials
		OreDictionary.registerOre("nuggetIron", new ItemStack(ItemMaterials, 1, 0));
		OreDictionary.registerOre("nuggetSoulium", new ItemStack(ItemMaterials, 1, 1));
		OreDictionary.registerOre("ingotSoulium", new ItemStack(ItemMaterials, 1, 2));
		OreDictionary.registerOre("dustVile", new ItemStack(ItemMaterials, 1, 3));
		OreDictionary.registerOre("essenceCorrupted", new ItemStack(ItemMaterials, 1, 4));
		OreDictionary.registerOre("blockSoulium", new ItemStack(BlockMaterials, 1, 0));

		if(!Config.oldWaysOption) {
			OreDictionary.registerOre("stickPetrified", new ItemStack(ItemMaterials, 1, 5));
			OreDictionary.registerOre("stickWood", new ItemStack(ItemMaterials, 1, 5));
			OreDictionary.registerOre("blockEnder", new ItemStack(BlockMaterials, 1, 1));

			// Petrified Logs OreDict
			OreDictionary.registerOre("logStone", new ItemStack(BlockPetrified, 1, 0));
			OreDictionary.registerOre("logStone", new ItemStack(BlockPetrified, 1, 1));
			OreDictionary.registerOre("logStone", new ItemStack(BlockPetrified, 1, 2));
			OreDictionary.registerOre("logStone", new ItemStack(BlockPetrified, 1, 3));
			OreDictionary.registerOre("logStone", new ItemStack(BlockPetrified2, 1, 0));
			OreDictionary.registerOre("logStone", new ItemStack(BlockPetrified2, 1, 1));

			OreDictionary.registerOre("logWood", new ItemStack(BlockPetrified, 1, 0));
			OreDictionary.registerOre("logWood", new ItemStack(BlockPetrified, 1, 1));
			OreDictionary.registerOre("logWood", new ItemStack(BlockPetrified, 1, 2));
			OreDictionary.registerOre("logWood", new ItemStack(BlockPetrified, 1, 3));
			OreDictionary.registerOre("logWood", new ItemStack(BlockPetrified2, 1, 0));
			OreDictionary.registerOre("logWood", new ItemStack(BlockPetrified2, 1, 1));

			// Petrified Planks OreDict
			OreDictionary.registerOre("plankStone", new ItemStack(BlockPetrifiedPlanks, 1, 0));
			OreDictionary.registerOre("plankStone", new ItemStack(BlockPetrifiedPlanks, 1, 1));
			OreDictionary.registerOre("plankStone", new ItemStack(BlockPetrifiedPlanks, 1, 2));
			OreDictionary.registerOre("plankStone", new ItemStack(BlockPetrifiedPlanks, 1, 3));
			OreDictionary.registerOre("plankStone", new ItemStack(BlockPetrifiedPlanks, 1, 4));
			OreDictionary.registerOre("plankStone", new ItemStack(BlockPetrifiedPlanks, 1, 5));

			OreDictionary.registerOre("plankWood", new ItemStack(BlockPetrifiedPlanks, 1, 0));
			OreDictionary.registerOre("plankWood", new ItemStack(BlockPetrifiedPlanks, 1, 1));
			OreDictionary.registerOre("plankWood", new ItemStack(BlockPetrifiedPlanks, 1, 2));
			OreDictionary.registerOre("plankWood", new ItemStack(BlockPetrifiedPlanks, 1, 3));
			OreDictionary.registerOre("plankWood", new ItemStack(BlockPetrifiedPlanks, 1, 4));
			OreDictionary.registerOre("plankWood", new ItemStack(BlockPetrifiedPlanks, 1, 5));
		}
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityForge.class, "TileEntityForge");
		GameRegistry.registerTileEntity(TileEntityCage.class, "TileEntityCage");
		GameRegistry.registerTileEntity(TileEntitySoulCrystal.class, "TileEntitySouLCrystal");
		GameRegistry.registerTileEntity(TileEntityDiffuser.class, "TileEntityDiffuser");
	}

	private static void registerRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 2), "blockSoulium"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 1), "ingotSoulium"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 9, 0), "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockForge), "SSS", "SCS", "OOO", 'S', "cobblestone", 'C', "essenceCorrupted", 'O', Blocks.obsidian));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemMaterials, 1, 2), "AAA", "AAA", "AAA", 'A', "nuggetSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.iron_ingot), "AAA", "AAA", "AAA", 'A', "nuggetIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockMaterials, 1, 0), "AAA", "AAA", "AAA", 'A', "ingotSoulium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockCage), "SIS", "IXI", "SIS", 'I', Blocks.iron_bars, 'S', "ingotSoulium"));



		if(!Config.oldWaysOption) {
			GameRegistry.addSmelting(new ItemStack(BlockXenolith, 1, 0), new ItemStack(BlockXenolith, 1, 1), 0.5F);
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.ender_pearl, 9), new ItemStack(BlockMaterials, 1, 1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockPetrifiedPlanks, 4, 0), new ItemStack(BlockPetrified, 1, 0)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockPetrifiedPlanks, 4, 1), new ItemStack(BlockPetrified, 1, 1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockPetrifiedPlanks, 4, 2), new ItemStack(BlockPetrified, 1, 2)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockPetrifiedPlanks, 4, 3), new ItemStack(BlockPetrified, 1, 3)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockPetrifiedPlanks, 4, 4), new ItemStack(BlockPetrified2, 1, 0)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockPetrifiedPlanks, 4, 5), new ItemStack(BlockPetrified2, 1, 1)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockXenolith, 1, 4), "nuggetSoulium", new ItemStack(BlockXenolith, 1, 0)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockXenolith, 1, 5), "dustRedstone", new ItemStack(BlockXenolith, 1, 0)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockXenolith, 1, 6), "pearlEnder", new ItemStack(BlockXenolith, 1, 0)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(baubleSockets, 1, 0), " N ", "NIN", "INI", 'N', "nuggetSoulium", 'I', "ingotSoulium"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(baubleSockets, 1, 1), "INI", "NIN", " N ", 'N', "nuggetSoulium", 'I', "ingotSoulium"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(animusBauble), "SSS", "S S", " A ", 'A', new ItemStack(baubleGems, 1, 2), 'S', Items.string));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(conservoBauble), "SSS", "S S", " C ", 'C', new ItemStack(baubleGems, 1, 4), 'S', Items.string));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockXenolith, 4, 7), "XX", "XX", 'X', new ItemStack(BlockXenolith, 1, 1)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockXenolith, 8, 8), "XX", "XX", 'X', new ItemStack(BlockXenolith, 1, 7)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockXenolith, 8, 4), "XXX", "XSX", "XXX", 'X', new ItemStack(BlockXenolith, 1, 0), 'S', "ingotSoulium"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockXenolith, 8, 5), "XXX", "XRX", "XXX", 'X', new ItemStack(BlockXenolith, 1, 0), 'R', "blockRedstone"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockXenolith, 8, 6), "XXX", "XHX", "XXX", 'X', new ItemStack(BlockXenolith, 1, 0), 'H', "blockEnder"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockXenolith, 8, 3), "XNX", "NXN", "XNX", 'X', new ItemStack(BlockXenolith, 1, 0), 'N', Blocks.nether_brick));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockXenoLight, 8), "XGX", "GXG", "XGX", 'X', new ItemStack(BlockXenolith, 1, 0), 'G', Blocks.glowstone));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemMaterials, 4, 5),"P", "P", 'P', "plankStone"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockMaterials, 1, 1), "AAA", "AAA", "AAA", 'A', "pearlEnder"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSwordSoul), "A", "A", "B", 'A', "ingotSoulium", 'B', "stickPetrified"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPickaxeSoul), "AAA", "CBC", "CBC", 'A', "ingotSoulium", 'B', "stickPetrified"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemAxeSoul), "AA", "AB", "CB", 'A', "ingotSoulium", 'B', "stickPetrified"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSpadeSoul), "A", "B", "B", 'A', "ingotSoulium", 'B', "stickPetrified"));

		} else {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSwordSoul), "A", "A", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPickaxeSoul), "AAA", "CBC", "CBC", 'A', "ingotSoulium", 'B', "ingotIron"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemAxeSoul), "AA", "AB", "CB", 'A', "ingotSoulium", 'B', "ingotIron"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemSpadeSoul), "A", "B", "B", 'A', "ingotSoulium", 'B', "ingotIron"));
		}

		if (Loader.isModLoaded("Natura")) {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 2, 3), Blocks.soul_sand, "dustGlowstone"));
			GameRegistry.addSmelting(new ItemStack(ItemMaterials, 1, 3), new ItemStack(ItemMaterials, 1, 4), 0.35F);
		} else {
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemMaterials, 2, 4), "dustGlowstone", "dustVile"));
			GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(ItemMaterials, 1, 3), 0.35F);
		}
	}

	private static void removeRecipes() {
//		ItemStack input = new ItemStack(Blocks.soul_sand, 1, 0);
//		ItemStack output = new ItemStack(NContent.netherGlass, 1, 0);
//		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.smelting().getSmeltingList();
//		for(Entry<ItemStack, ItemStack> entry : smeltingList.entrySet()){
//			
//			if((entry.getKey() == input) && (entry.getValue() == output)) {
//				smeltingList.remove(entry.getValue());
//			}
//			
//			System.out.println(entry.getKey() + " / " + entry.getValue());
//		}
	}
	
	private static void registerEntities() {
		EntityRegistry.registerModEntity(EntityZombieWitch.class, "EntityZombieWitch", 1, SSTheOldWays.modInstance, 30, 3, true);
		registerEntityEgg(entityZombieWitch, 0x44975, 0x5349438);
	}
	
	@SuppressWarnings("unchecked")
	private static void registerEntityEgg(Class<? extends Entity> entity, int colPrim, int colSec){
		int id = getUniqueEntityID();
		EntityList.IDtoClassMapping.put(id, entity);
		EntityList.entityEggs.put(id, new EntityList.EntityEggInfo(id, colPrim, colSec));
	}
	
	private static int getUniqueEntityID(){
		int startEID = 300;
		do {
			startEID++;
		} while (EntityList.getStringFromID(startEID) != null);
		return startEID;
	}
	
	@SuppressWarnings("deprecation")
	private static void registerBiomes() {
		BiomeDictionary.registerBiomeType(biomePetrifiedForest, Type.FOREST);
		BiomeManager.addSpawnBiome(biomePetrifiedForest);
		BiomeManager.desertBiomes.add(new BiomeEntry(biomePetrifiedForest, 1000000000));

		List<BiomeDictionary.Type> blacklistedBiomes = new ArrayList<BiomeDictionary.Type>();
		blacklistedBiomes.add(BiomeDictionary.Type.MUSHROOM);

		List<BiomeGenBase> biomes = new LinkedList<BiomeGenBase>();
		label: for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
			if (biome != null) {
				for (BiomeDictionary.Type type : BiomeDictionary.getTypesForBiome(biome))
					if (blacklistedBiomes.contains(type))
						continue label;
				biomes.add(biome);
			}

		
		
		EntityRegistry.addSpawn(EntityZombieWitch.class, 80, 4, 4, EnumCreatureType.monster, biomes.toArray(new BiomeGenBase[biomes.size()]));

	}

	public static void registerFluids() {
		if (Loader.isModLoaded("TConstruct")) {
			TCon.registerTCon();
		}
	}
	public static int pageCount = 0;
	public static ItemStack addNBTToLootPage(ItemStack stack) {
		//System.out.println("Method Called");
		if(stack.stackTagCompound == null){
			//System.out.println("Applying NBTTagCompound()");
			stack.setTagCompound(new NBTTagCompound());
		}
		//System.out.println("pageNBTHelper stuff");
		pageNBTHelper = new String[PageNBTHelper.pages.length];
		for(int i = 0; i < PageNBTHelper.pages.length; i++){
			//System.out.println("Loop: " + i++);
			String[] split = PageNBTHelper.pages[i].split("|");
			//System.out.println("Splitting");
			switch (split.length) {
			case 3:
				//System.out.println("Before Tags");
				stack.stackTagCompound.setString("key", split[0]);
				stack.stackTagCompound.setString("book", split[1]);
				stack.stackTagCompound.setString("page", split[2]);
				stack.stackTagCompound.setInteger("TI", Utils.pageSelector(10));
				//System.out.println(split[0]+" | "+split[1]+" | "+split[2]);
				pageCount++;
				return stack;
			}
		}
		return stack;
	}

	public static void registerLoot() {
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 100));
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(addNBTToLootPage(new ItemStack(ItemLootPage)), 1, 3, 10));
	}

}
