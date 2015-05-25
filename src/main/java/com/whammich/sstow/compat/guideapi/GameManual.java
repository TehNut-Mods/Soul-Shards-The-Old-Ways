package com.whammich.sstow.compat.guideapi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.categories.CategoryItemStack;
import amerifrance.guideapi.entries.EntryUniText;

import com.whammich.sstow.compat.guideapi.pages.PageSoulForge;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import cpw.mods.fml.common.registry.GameRegistry;

public class GameManual {

	public static Book manualBook;
	public static List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();

	public static void registerguide() {
		createBook();
		manualBook = new Book(categories, "guide.book.manual.title", "guide.book.manual.message", "guide.book.manual.name", new Color(102, 0, 102));
		GuideRegistry.registerBook(manualBook);
		GameRegistry.addRecipe(new ShapedOreRecipe(GuideRegistry.getItemStackForBook(manualBook), "C", "B", 'C', "essenceCorrupted", 'B', Items.writable_book));
	}
	
	public static void createBook(){
		forgeRecipes();
		craftingRecipes();
	}
	
	public static void forgeRecipes() {
		List<EntryAbstract> forgeEntry = new ArrayList<EntryAbstract>();
		ArrayList<IPage> forgePage = new ArrayList<IPage>();
		PageSoulForge pageSoulForge = new PageSoulForge(new ItemStack(Items.diamond));
		PageSoulForge pageSoulForge2 = new PageSoulForge(new ItemStack(Items.wheat_seeds));
		PageSoulForge pageSoulForge3 = new PageSoulForge(new ItemStack(Items.iron_ingot));
		PageSoulForge pageSoulForge4 = new PageSoulForge(new ItemStack(Blocks.iron_block));
		PageSoulForge pageSoulForge5 = new PageSoulForge(new ItemStack(Blocks.stone));
		PageSoulForge pageSoulForge6 = new PageSoulForge(new ItemStack(Blocks.log, 1, 0));
		PageSoulForge pageSoulForge7 = new PageSoulForge(new ItemStack(Blocks.log, 1, 1));
		PageSoulForge pageSoulForge8 = new PageSoulForge(new ItemStack(Blocks.log, 1, 2));
		PageSoulForge pageSoulForge9 = new PageSoulForge(new ItemStack(Blocks.log, 1, 3));
		PageSoulForge pageSoulForge10 = new PageSoulForge(new ItemStack(Blocks.log2, 1, 0));
		PageSoulForge pageSoulForge11 = new PageSoulForge(new ItemStack(Blocks.log2, 1, 1));
		PageSoulForge pageSoulForge12 = new PageSoulForge(new ItemStack(Blocks.obsidian));
		forgePage.add(pageSoulForge);
		forgePage.add(pageSoulForge2);
		forgePage.add(pageSoulForge3);
		forgePage.add(pageSoulForge4);
		forgePage.add(pageSoulForge5);
		forgePage.add(pageSoulForge6);
		forgePage.add(pageSoulForge7);
		forgePage.add(pageSoulForge8);
		forgePage.add(pageSoulForge9);
		forgePage.add(pageSoulForge10);
		forgePage.add(pageSoulForge11);
		forgePage.add(pageSoulForge12);
		forgeEntry.add(new EntryUniText(forgePage, Utils.localize("guide.book.manual.smelting"))); //First
		categories.add(new CategoryItemStack(forgeEntry, Utils.localize("guide.book.manual.forging"), new ItemStack(Register.BlockForge, 1, 3)));
	}
	
	public static void craftingRecipes() {
		List<EntryAbstract> craftingEntry = new ArrayList<EntryAbstract>();
		ArrayList<IPage> craftingPage = new ArrayList<IPage>();
//		craftingPage.add(new PageIRecipe(Register.soulCage));
//		craftingPage.add(new PageIRecipe(Register.soulForge));
		craftingEntry.add(new EntryUniText(craftingPage, Utils.localize("guide.book.manual.crafting"))); //First
		categories.add(new CategoryItemStack(craftingEntry, Utils.localize("guide.book.manual.crafting"), new ItemStack(Blocks.crafting_table)));

	}
	
	
	
	
}
