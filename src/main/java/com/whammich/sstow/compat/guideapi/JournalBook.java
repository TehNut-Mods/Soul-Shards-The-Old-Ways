package com.whammich.sstow.compat.guideapi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.categories.CategoryItemStack;
import amerifrance.guideapi.entries.EntryUniText;

import com.whammich.sstow.compat.guideapi.pages.PageMissingText;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

public class JournalBook {
	public static Book journalBook;
	public static List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();

	public static void registerguide() {
		createBook();
		journalBook = new Book(categories, "guide.book.journal.title", "guide.book.journal.message", "guide.book.journal.name", new Color(211, 165, 116));
		GuideRegistry.registerBook(journalBook);
	}
	
	public static void createBook(){
		List<EntryAbstract> entries = new ArrayList<EntryAbstract>();
		ArrayList<IPage> journalPages01 = new ArrayList<IPage>();
		ArrayList<IPage> journalPages02 = new ArrayList<IPage>();
		ArrayList<IPage> journalPages03 = new ArrayList<IPage>();
		ArrayList<IPage> journalPages04 = new ArrayList<IPage>();
		
		PageMissingText missingJournalN2P2 = new PageMissingText(Utils.localize("guide.book.journal.note2.page02"), "drjournal:notes:note2:2");
		PageMissingText missingJournalN2P4 = new PageMissingText(Utils.localize("guide.book.journal.note2.page04"), "drjournal:notes:note2:4");
		//PageMissingImage imagePage = new PageMissingImage(new ResourceLocation(Reference.modID + ":textures/pages/shard.png"), null);
		// Third
		//journalPages.add(imagePage);
		// Note 1
		journalPages01.addAll(PageHelper.pagesForLongText(Utils.localize("guide.book.journal.note1.page01")));
		journalPages01.addAll(PageHelper.pagesForLongText(Utils.localize("guide.book.journal.note1.page02")));
		// Note 2
		journalPages02.addAll(PageHelper.pagesForLongText(Utils.localize("guide.book.journal.note2.page01")));
		journalPages02.add(missingJournalN2P2);
		journalPages02.addAll(PageHelper.pagesForLongText(Utils.localize("guide.book.journal.note2.page03")));
		journalPages02.add(missingJournalN2P4);
		// Note 3
		journalPages03.addAll(PageHelper.pagesForLongText(Utils.localize("guide.book.journal.note3.page01")));
		journalPages03.addAll(PageHelper.pagesForLongText(Utils.localize("guide.book.journal.note3.page02")));
		// Note 4
		journalPages04.addAll(PageHelper.pagesForLongText(Utils.localize("guide.book.journal.note4.page01")));
		// Second
		entries.add(new EntryUniText(journalPages01, Utils.localize("guide.book.journal.note1")));
		entries.add(new EntryUniText(journalPages02, Utils.localize("guide.book.journal.note2")));
		entries.add(new EntryUniText(journalPages03, Utils.localize("guide.book.journal.note3")));
		entries.add(new EntryUniText(journalPages04, Utils.localize("guide.book.journal.note4")));
		// First
		categories.add(new CategoryItemStack(entries, Utils.localize("guide.book.journal.notes"), new ItemStack(Register.ItemLootPage)));
	}
	
}
