package com.whammich.sstow.compat.guideapi.pages;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import amerifrance.guideapi.pages.PageImage;

public class PageMissingImage extends PageImage {

	public String unlockKey;

	public PageMissingImage(ResourceLocation image, String key) {
		super(image);
		this.unlockKey = key;
	}

	@Override
	public boolean canSee(EntityPlayer player, ItemStack bookStack) {
		return bookStack.stackTagCompound.getBoolean(unlockKey);
	}

}
