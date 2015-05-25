package com.whammich.sstow.compat.nei;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

public class NEISoulShardsConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return Reference.modName;
	}

	@Override
	public String getVersion() {
		return Reference.modVersion;
	}

	@Override
	public void loadConfig() {
		if(Config.oldWaysOption){
			API.hideItem(new ItemStack(Register.ItemMaterials, 1, 5));
			API.hideItem(new ItemStack(Register.BlockMaterials, 1, 1));
		}
		API.hideItem(new ItemStack(Register.BlockForgeActive));
		
		ForgeRecipeHandler handler = new ForgeRecipeHandler();
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
	}

}
