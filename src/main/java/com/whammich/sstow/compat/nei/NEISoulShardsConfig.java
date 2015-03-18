package com.whammich.sstow.compat.nei;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.whammich.sstow.guihandler.GuiSoulForge;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

public class NEISoulShardsConfig implements IConfigureNEI {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return Reference.MOD_NAME;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return Reference.MOD_VERSION;
	}

	@Override
	public void loadConfig() {
		// TODO Auto-generated method stub
		API.hideItem(new ItemStack(Register.BlockForgeActive));
		API.hideItem(new ItemStack(Register.ItemFixedDummy));
		API.hideItem(new ItemStack(Register.ItemMaterials, 1, 0));

		RecipeHandlerSoulShards handler = new RecipeHandlerSoulShards();
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
		API.setGuiOffset(GuiSoulForge.class, -50, 40);
	}

}
