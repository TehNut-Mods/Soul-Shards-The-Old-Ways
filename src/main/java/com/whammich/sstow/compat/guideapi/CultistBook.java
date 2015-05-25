package com.whammich.sstow.compat.guideapi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.categories.CategoryItemStack;
import amerifrance.guideapi.entries.EntryUniText;
import amerifrance.guideapi.pages.PageImage;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

public class CultistBook {
	public static Book cultistBook;
	public static List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();

	public static void registerguide() {
		createLoreEntries();
		createItemEntries();
		createBlockEntries();
		createRitualEntries();
		createJournalEntries();
		cultistBook = new Book(categories, "guide.book.cultist.title", "guide.book.cultist.message", "guide.book.cultist.name", new Color(84, 8, 137));
		GuideRegistry.registerBook(cultistBook);
	}
	
	public static void createLoreEntries() {
		List<EntryAbstract> loreEntry = new ArrayList<EntryAbstract>();
		ArrayList<IPage> lorePage = new ArrayList<IPage>();
		lorePage.add(new PageImage(new ResourceLocation(Reference.modID + ":textures/pages/shard.png")));
		
		loreEntry.add(new EntryUniText(lorePage, Utils.localize("guide.book.cultist.lore.entry"))); //First
		categories.add(new CategoryItemStack(loreEntry, Utils.localize("guide.book.cultist.lore.category"), new ItemStack(Register.BlockForge, 1, 3)));
	}
	
	public static void createItemEntries() {
		List<EntryAbstract> itemEntry = new ArrayList<EntryAbstract>();
		ArrayList<IPage> itemPage = new ArrayList<IPage>();
		
		itemEntry.add(new EntryUniText(itemPage, Utils.localize("guide.book.manual.cultist.item.entry"))); //First
		categories.add(new CategoryItemStack(itemEntry, Utils.localize("guide.book.cultist.item.category"), new ItemStack(Register.BlockForge, 1, 3)));
	}
	
	public static void createBlockEntries() {
		List<EntryAbstract> blockEntry = new ArrayList<EntryAbstract>();
		ArrayList<IPage> blockPage = new ArrayList<IPage>();
		
		blockEntry.add(new EntryUniText(blockPage, Utils.localize("guide.book.cultist.block.entry"))); //First
		categories.add(new CategoryItemStack(blockEntry, Utils.localize("guide.book.cultist.block.category"), new ItemStack(Register.BlockForge, 1, 3)));
	}
	
	public static void createRitualEntries() {
		List<EntryAbstract> ritualEntry = new ArrayList<EntryAbstract>();
		ArrayList<IPage> ritualPage = new ArrayList<IPage>();
		
		ritualEntry.add(new EntryUniText(ritualPage, Utils.localize("guide.book.cultist.ritual.entry"))); //First
		categories.add(new CategoryItemStack(ritualEntry, Utils.localize("guide.book.cultist.ritual.category"), new ItemStack(Register.BlockForge, 1, 3)));
	}
	
	public static void createJournalEntries() {
		List<EntryAbstract> journalEntry = new ArrayList<EntryAbstract>();
		ArrayList<IPage> journalPage = new ArrayList<IPage>();
		
		journalEntry.add(new EntryUniText(journalPage, Utils.localize("guide.book.cultist.journal.entry"))); //First
		categories.add(new CategoryItemStack(journalEntry, Utils.localize("guide.book.cultist.journal.category"), new ItemStack(Register.BlockForge, 1, 3)));
	}
	


}
