package com.whammich.sstow.compat.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.whammich.sstow.utils.Reference;

public class RecipeHandlerSoulShards extends TemplateRecipeHandler {

	public class CachedSoulShardsRecipe extends CachedRecipe {

		@Override
		public PositionedStack getResult() {
			// TODO Auto-generated method stub
			return result;
		}

		PositionedStack result;
	}

	@Override
	public String getRecipeName() {
		// TODO Auto-generated method stub
		return "Soul Forge";
	}

	@Override
	public String getGuiTexture() {
		// TODO Auto-generated method stub
		return Reference.MOD_ID + ":textures/gui/container/furnace.png";
	}

}
