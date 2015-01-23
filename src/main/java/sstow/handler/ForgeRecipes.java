package sstow.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sstow.gameObjs.ObjHandler;
import sstow.utils.Config;

public class ForgeRecipes {
	private static final ForgeRecipes SMELTING_BASE = new ForgeRecipes();

	private Map smeltingList = new HashMap();
	private Map experienceList = new HashMap();

	public static ForgeRecipes smelting() {
		return SMELTING_BASE;
	}

	private ForgeRecipes() {
		this.addRecipe(new ItemStack(Items.diamond), new ItemStack(
				ObjHandler.SOUL_SHARD, Config.SHARDS), 1F);
		this.addRecipe(new ItemStack(Items.iron_ingot), new ItemStack(
				ObjHandler.SOULIUM_NUGGET, Config.NUGGETS), 0.8F);
		this.addRecipe(new ItemStack(Blocks.iron_block), new ItemStack(
				ObjHandler.SOULIUM_INGOT, Config.INGOTS), 0.9F);
	}

	public void addRecipe(ItemStack input, ItemStack output, float experience) {
		this.smeltingList.put(input, output);
		this.experienceList.put(output, Float.valueOf(experience));
	}

	public ItemStack getSmeltingResult(ItemStack itemstack) {
		Iterator iterator = this.smeltingList.entrySet().iterator();
		Entry entry;

		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry) iterator.next();
		} while (!canBeSmelted(itemstack, (ItemStack) entry.getKey()));
		return (ItemStack) entry.getValue();
	}

	private boolean canBeSmelted(ItemStack itemstack, ItemStack itemstack2) {
		return itemstack2.getItem() == itemstack.getItem()
				&& (itemstack2.getItemDamage() == 32767 || itemstack2
						.getItemDamage() == itemstack.getItemDamage());
	}

	public float giveExperience(ItemStack itemstack) {
		Iterator iterator = this.experienceList.entrySet().iterator();
		Entry entry;

		do {
			if (!iterator.hasNext()) {
				return 0.0f;
			}

			entry = (Entry) iterator.next();
		} while (!this.canBeSmelted(itemstack, (ItemStack) entry.getKey()));

		if (itemstack.getItem().getSmeltingExperience(itemstack) != -1) {
			return itemstack.getItem().getSmeltingExperience(itemstack);
		}

		return ((Float) entry.getValue()).floatValue();
	}
}
