package com.whammich.sstow.compat.nei;

import com.whammich.sstow.utils.Reference;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeHandlerSoulShards extends TemplateRecipeHandler {

	public class CachedSoulShardsRecipe extends CachedRecipe {

		
		
		@Override
		public PositionedStack getResult() {
			// TODO Auto-generated method stub
			return null;
		}
		
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
