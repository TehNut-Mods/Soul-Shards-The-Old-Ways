package com.whammich.sstow.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

	public Map<ItemStack, ItemStack> getSmeltingList() {
		return smeltingList;
	}

	public Map<ItemStack, ItemStack> getByProductList() {
		return secondaryList;
	}

	public Map<ItemStack, Integer> getFuelList() {
		return fuelList;
	}

	public ItemStack NuggetIngot() {
		if (Config.nuggetsReturn == 9) {
			return new ItemStack(Register.ItemMaterials, 1, 2);
		} else {
			return new ItemStack(Register.ItemMaterials, Config.nuggetsReturn, 1);
		}
	}

	public ItemStack IngotBlock() {
		if (Config.ingotsReturn == 9) {
			return new ItemStack(Register.BlockMaterials, 1, 0);
		} else {
			return new ItemStack(Register.ItemMaterials, Config.ingotsReturn, 2);
		}
	}

	private SFRecipeHandler() {
		addFuel(new ItemStack(Register.ItemMaterials, 1, 4), 1);
			addRecipe(new ItemStack(Items.diamond), new ItemStack(Items.diamond, Config.immundusReturn, 0), null, 8, 12600, 1F);
			addRecipe(new ItemStack(Items.wheat_seeds), new ItemStack(Items.nether_wart), null, 1, 1200, 0.5F);
			addRecipe(new ItemStack(Items.gold_ingot), new ItemStack(Register.ItemMaterials, 3, 1), null, 1, 1200, 0.5F);
			addRecipe(new ItemStack(Blocks.gold_ore), new ItemStack(Register.ItemMaterials, 3, 1), null, 1, 1200, 0.5F);
			addRecipe(new ItemStack(Items.iron_ingot), NuggetIngot(), new ItemStack(Register.ItemMaterials, 9 - Config.nuggetsReturn, 0), 2, 6000, 0.9F);
			addRecipe(new ItemStack(Blocks.iron_block), IngotBlock(), new ItemStack(Items.iron_ingot, 9 - Config.ingotsReturn), 5, 12000, 0.9F);
			addRecipe(new ItemStack(Blocks.iron_ore), NuggetIngot(), new ItemStack(Register.ItemMaterials, 9 - Config.nuggetsReturn, 0), 2, 6000, 0.9F);
			addRecipe(new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.netherrack), null, 1, 1600, 0.5F);
			addRecipe(new ItemStack(Blocks.brick_block), new ItemStack(Blocks.nether_brick), null, 2, 3200, 1F);

			addRecipe(new ItemStack(Items.diamond), new ItemStack(Register.baubleGems, Config.immundusReturn, 0), null, 8, 12600, 1F);
			addRecipe(new ItemStack(Items.wheat_seeds), new ItemStack(Items.nether_wart), null, 1, 1200, 0.5F);
			addRecipe(new ItemStack(Items.gold_ingot), new ItemStack(Register.ItemMaterials, 3, 1), null, 1, 1200, 0.5F);
			addRecipe(new ItemStack(Blocks.gold_ore), new ItemStack(Register.ItemMaterials, 3, 1), null, 1, 1200, 0.5F);
			addRecipe(new ItemStack(Items.iron_ingot), NuggetIngot(), new ItemStack(Register.ItemMaterials, 9 - Config.nuggetsReturn, 0), 2, 6000, 0.9F);
			addRecipe(new ItemStack(Blocks.iron_block), IngotBlock(), new ItemStack(Items.iron_ingot, 9 - Config.ingotsReturn), 5, 12000, 0.9F);
			addRecipe(new ItemStack(Blocks.iron_ore), NuggetIngot(), new ItemStack(Register.ItemMaterials, 9 - Config.nuggetsReturn, 0), 2, 6000, 0.9F);
			addRecipe(new ItemStack(Blocks.cobblestone), new ItemStack(Blocks.netherrack), null, 1, 1600, 0.5F);
			addRecipe(new ItemStack(Blocks.brick_block), new ItemStack(Blocks.nether_brick), null, 2, 3200, 1F);
			addRecipe(new ItemStack(Blocks.stone), new ItemStack(Register.BlockXenolith), null, 2, 1600, 1F);
			addRecipe(new ItemStack(Blocks.log, 1, 0), new ItemStack(Register.BlockPetrified, 1, 0), null, 1, 1600, 2F);
			addRecipe(new ItemStack(Blocks.log, 1, 1), new ItemStack(Register.BlockPetrified, 1, 1), null, 1, 1600, 2F);
			addRecipe(new ItemStack(Blocks.log, 1, 2), new ItemStack(Register.BlockPetrified, 1, 2), null, 1, 1600, 2F);
			addRecipe(new ItemStack(Blocks.log, 1, 3), new ItemStack(Register.BlockPetrified, 1, 3), null, 1, 1600, 2F);
			addRecipe(new ItemStack(Blocks.log2, 1, 0), new ItemStack(Register.BlockPetrified2, 1, 0), null, 1, 1600, 2F);
			addRecipe(new ItemStack(Blocks.log2, 1, 1), new ItemStack(Register.BlockPetrified2, 1, 1), null, 1, 1600, 2F);
			addRecipe(new ItemStack(Blocks.obsidian, 1), new ItemStack(Register.BlockObsidianGlass), null, 10, 1600, 3F);
		
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
