package com.whammich.sstow.compat.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;

import com.whammich.sstow.guihandler.GuiSoulForge;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.SFRecipeHandler;

public class ForgeRecipeHandler extends FurnaceRecipeHandler {

	public ArrayList<SmeltingPair> byProductRecipes = new ArrayList<SmeltingPair>();

	@Override
	public ForgeRecipeHandler newInstance() {
		try {
			return getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public class SmeltingPair extends CachedRecipe {
		public SmeltingPair(ItemStack ingred, ItemStack result,
				ItemStack secondResult) {
			ingred.stackSize = 1;
			this.ingred = new PositionedStack(ingred, 51, 6);
			this.result = new PositionedStack(result, 111, 6);
			if (secondResult != null) {
				this.secondResult = new PositionedStack(secondResult, 111, 42);
			} else {
				this.secondResult = null;
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 48, Arrays.asList(ingred));
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			List<PositionedStack> newList = new ArrayList<PositionedStack>();
			if (secondResult != null) {
				newList.add(secondResult);
			}
			newList.add(getFuelStack());
			return newList;
		}

		private PositionedStack getFuelStack() {
			Map<ItemStack, Integer> allSmeltingItems = SFRecipeHandler
					.smelting().getFuelList();
			List<ItemStack> allSmeltingItemsArray = new ArrayList<ItemStack>(
					allSmeltingItems.keySet());
			int stack = (cycleticks / 48) % allSmeltingItems.size();
			ItemStack itemStack = allSmeltingItemsArray.get(stack);
			return new PositionedStack(itemStack, 51, 42);
		}

		PositionedStack ingred;
		PositionedStack result;
		PositionedStack secondResult;
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18),
				"soulforgefuel"));
		transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18),
				"soulforge"));
	}

	@Override
	public void drawExtras(int i) {
		drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
		drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("soulforge")
				&& getClass() == ForgeRecipeHandler.class) {
			loadAllRecipes();
		} else if (outputId.equals("soulforgefuel")) {
			loadAllRecipes();
		} else {
			super.loadCraftingRecipes(outputId, results);

		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("item") && getClass() == ForgeRecipeHandler.class)
			loadCraftingRecipes("soulforge");
		else
			super.loadUsageRecipes(inputId, ingredients);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) SFRecipeHandler
				.smelting().getSmeltingList();
		Map<ItemStack, ItemStack> byProducts = (Map<ItemStack, ItemStack>) SFRecipeHandler
				.smelting().getByProductList();
		// normal recipes
		for (java.util.Map.Entry<ItemStack, ItemStack> recipe : recipes
				.entrySet()) {
			if (NEIServerUtils.areStacksSameType(recipe.getValue(), result)) {
				boolean found = false;
				for (java.util.Map.Entry<ItemStack, ItemStack> byproductItem : byProducts
						.entrySet()) {
					if (NEIServerUtils.areStacksSameType(
							byproductItem.getKey(), recipe.getKey())) {
						found = true;
						byProductRecipes.add(new SmeltingPair(recipe.getKey(),
								recipe.getValue(), byproductItem.getValue()));
					}
				}

				if (!found) {
					byProductRecipes.add(new SmeltingPair(recipe.getKey(),
							recipe.getValue(), null));
				}
			}
		}

		// byproducts
		for (java.util.Map.Entry<ItemStack, ItemStack> byProductItem : byProducts
				.entrySet()) {
			if (NEIServerUtils.areStacksSameType(byProductItem.getValue(),
					result)) {
				for (java.util.Map.Entry<ItemStack, ItemStack> recipeItem : recipes
						.entrySet()) {
					if (NEIServerUtils.areStacksSameType(recipeItem.getKey(),
							byProductItem.getKey())) {
						byProductRecipes.add(new SmeltingPair(recipeItem
								.getKey(), recipeItem.getValue(), byProductItem
								.getValue()));
					}
				}
			}
		}

	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		Map<ItemStack, Integer> fuel = (Map<ItemStack, Integer>) SFRecipeHandler
				.smelting().getFuelList();

		loadRecipe(ingredient);

		for (ItemStack item : fuel.keySet()) {
			if (item.isItemEqual(ingredient)) {
				loadAllRecipes();
			}
		}
	}

	private void loadRecipe(ItemStack ingredient) {
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) SFRecipeHandler
				.smelting().getSmeltingList();
		Map<ItemStack, ItemStack> byProducts = (Map<ItemStack, ItemStack>) SFRecipeHandler
				.smelting().getByProductList();

		for (java.util.Map.Entry<ItemStack, ItemStack> recipe : recipes
				.entrySet()) {
			if (NEIServerUtils.areStacksSameType(recipe.getKey(), ingredient)) {
				boolean found = false;
				for (java.util.Map.Entry<ItemStack, ItemStack> byproductItem : byProducts
						.entrySet()) {
					if (NEIServerUtils.areStacksSameType(
							byproductItem.getKey(), ingredient)) {
						found = true;
						byProductRecipes.add(new SmeltingPair(recipe.getKey(),
								recipe.getValue(), byproductItem.getValue()));
					}
				}

				if (!found) {
					byProductRecipes.add(new SmeltingPair(recipe.getKey(),
							recipe.getValue(), null));
				}
			}
		}
	}

	private void loadAllRecipes() {
		Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) SFRecipeHandler
				.smelting().getSmeltingList();
		for (ItemStack item : recipes.keySet()) {
			loadRecipe(item);
		}
	}

	@Override
	public String getRecipeName() {
		return "Soul forge";
	}

	@Override
	public String getOverlayIdentifier() {
		return "soulforge";
	}

	@Override
	public String getGuiTexture() {
		return Reference.MOD_ID + ":textures/gui/container/forge.png";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getGuiClass() {
		return GuiSoulForge.class;
	}

	@Override
	public int numRecipes() {
		return byProductRecipes.size();
	}

	@Override
	public List<PositionedStack> getIngredientStacks(int recipe) {
		return byProductRecipes.get(recipe).getIngredients();
	}

	@Override
	public PositionedStack getResultStack(int recipe) {
		return byProductRecipes.get(recipe).getResult();
	}

	@Override
	public List<PositionedStack> getOtherStacks(int recipe) {
		return byProductRecipes.get(recipe).getOtherStacks();
	}

}
