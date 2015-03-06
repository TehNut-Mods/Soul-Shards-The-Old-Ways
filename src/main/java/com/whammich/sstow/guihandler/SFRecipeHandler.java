package com.whammich.sstow.guihandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Register;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class SFRecipeHandler {
	private static final SFRecipeHandler SMELTING_BASE = new SFRecipeHandler();

	private Map<ItemStack, ItemStack> smeltingList = new HashMap<ItemStack, ItemStack>();
	private Map<ItemStack, Float> experienceList = new HashMap<ItemStack, Float>();
	
	
	public static SFRecipeHandler smelting() {
		return SMELTING_BASE;
	}

	private ItemStack NuggetIngot() {
		if (Config.NUGGETS == 9) {
			return new ItemStack(Register.SOULIUM_INGOT, 1);
		} else {
			return new ItemStack(Register.SOULIUM_NUGGET, Config.NUGGETS);
		}
	}

	private ItemStack IngotBlock() {
		if (Config.INGOTS == 9) {
			return new ItemStack(Register.SOULIUM_BLOCK, 1);
		} else {
			return new ItemStack(Register.SOULIUM_INGOT, Config.INGOTS);
		}
	}

	private SFRecipeHandler() {
		if (!Config.EASYMODE) {
			this.addRecipe(new ItemStack(Items.diamond), new ItemStack(
					Register.SOUL_SHARD, Config.SHARDS), 1F);
		}
		this.addRecipe(new ItemStack(Items.iron_ingot), NuggetIngot(), 0.8F);
		this.addRecipe(new ItemStack(Blocks.iron_block), IngotBlock(), 1F);
	}

	public void addRecipe(ItemStack input, ItemStack output, float experience) {
		this.smeltingList.put(input, output);
		this.experienceList.put(output, Float.valueOf(experience));
	}

	public ItemStack getSmeltingResult(ItemStack itemstack) {
		Iterator<?> iterator = this.smeltingList.entrySet().iterator();
		Entry<?, ?> entry;

		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry<?, ?>) iterator.next();
		} while (!canBeSmelted(itemstack, (ItemStack) entry.getKey()));
		return (ItemStack) entry.getValue();
	}

	private boolean canBeSmelted(ItemStack itemstack, ItemStack itemstack2) {
		return itemstack2.getItem() == itemstack.getItem()
				&& (itemstack2.getItemDamage() == 32767 || itemstack2
						.getItemDamage() == itemstack.getItemDamage());
	}

	public float giveExperience(ItemStack itemstack) {
		Iterator<?> iterator = this.experienceList.entrySet().iterator();
		Entry<?, ?> entry;

		do {
			if (!iterator.hasNext()) {
				return 0.0f;
			}

			entry = (Entry<?, ?>) iterator.next();
		} while (!this.canBeSmelted(itemstack, (ItemStack) entry.getKey()));

		if (itemstack.getItem().getSmeltingExperience(itemstack) != -1) {
			return itemstack.getItem().getSmeltingExperience(itemstack);
		}

		return ((Float) entry.getValue()).floatValue();
	}
}
