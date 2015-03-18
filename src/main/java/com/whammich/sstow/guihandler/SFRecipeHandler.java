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
	private Map<ItemStack, Integer> timeList = new HashMap<ItemStack, Integer>();
	private Map<ItemStack, ItemStack> secondaryList = new HashMap<ItemStack, ItemStack>();
	private Map<ItemStack, Integer> burnList = new HashMap<ItemStack, Integer>();
	private Map<ItemStack, Integer> fuelList = new HashMap<ItemStack, Integer>();

	public static SFRecipeHandler smelting() {
		return SMELTING_BASE;
	}

	private ItemStack NuggetIngot() {
		if (Config.NUGGETS == 9) {
			return new ItemStack(Register.ItemMaterials, 1, 2);
		} else {
			return new ItemStack(Register.ItemMaterials, Config.NUGGETS, 1);
		}
	}

	private ItemStack IngotBlock() {
		if (Config.INGOTS == 9) {
			return new ItemStack(Register.SOULIUM_BLOCK, 1);
		} else {
			return new ItemStack(Register.ItemMaterials, Config.INGOTS, 2);
		}
	}

	private SFRecipeHandler() {
		if (!Config.EASYMODE) {
			this.addRecipe(new ItemStack(Items.diamond), new ItemStack(
					Register.ItemShardSoul, Config.SHARDS), null, 8, 100, 1F);
		}
						// I							O			   B	 F  B	E
		this.addRecipe(new ItemStack(Items.iron_ingot), NuggetIngot(), null, 4, 20, 0.8F);
		this.addRecipe(new ItemStack(Blocks.iron_block), IngotBlock(), null, 4, 500, 0.9F);
		this.addFuel(new ItemStack(Register.ItemMaterials, 4), 1);
		this.addRecipe(new ItemStack(Blocks.stone, 1), new ItemStack(Register.XENOLITH), null, 8, 100, 1F);
		this.addRecipe(new ItemStack(Blocks.log, 1), new ItemStack(Register.PETRIFIEDWOOD), null, 8, 100, 1F);
		this.addRecipe(new ItemStack(Blocks.log2, 1), new ItemStack(Register.PETRIFIEDWOOD), null, 8, 100, 1F);
	}

	public void addRecipe(ItemStack input, ItemStack output, ItemStack byproduct, int fuelCost, int burnTime, float experience) {
		this.smeltingList.put(input, output);
		this.experienceList.put(output, Float.valueOf(experience));
		this.timeList.put(input, burnTime);
		this.secondaryList.put(input, byproduct);
		this.burnList.put(input, fuelCost);
	}

	private void addFuel(ItemStack itemStack, int fuelMod) {
		this.fuelList.put(itemStack, fuelMod);
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

	public ItemStack getSecondaryResult(ItemStack itemstack) {
		Iterator<?> iterator = this.secondaryList.entrySet().iterator();
		Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry<?, ?>) iterator.next();
		} while (!canBeSmelted(itemstack, (ItemStack) entry.getKey()));
		return (ItemStack) entry.getValue();
	}

	public int getSmeltingTime(ItemStack itemstack) {
		Iterator<?> iterator = this.timeList.entrySet().iterator();
		Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return 0;
			}
			entry = (Entry<?, ?>) iterator.next();
		} while (!canBeSmelted(itemstack, (ItemStack) entry.getKey()));
		return (Integer) entry.getValue();
	}

	public int getFuelCost(ItemStack itemstack) {
		Iterator<?> iterator = this.burnList.entrySet().iterator();
		Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return 0;
			}
			entry = (Entry<?, ?>) iterator.next();
		} while (!canBeSmelted(itemstack, (ItemStack) entry.getKey()));
		return (Integer) entry.getValue();
	}

	public Integer getFuelMod(ItemStack itemstack) {
		Iterator<?> iterator = this.fuelList.entrySet().iterator();
		Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return 0;
			}
			entry = (Entry<?, ?>) iterator.next();
		} while (!canBeSmelted(itemstack, (ItemStack) entry.getKey()));
		return (Integer) entry.getValue();
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
