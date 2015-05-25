package com.whammich.sstow.compat.guideapi.pages;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import amerifrance.guideapi.pages.PageUnlocText;

public class PageMissingText extends PageUnlocText {

	public String unlockKey;

	public PageMissingText(String unlocText, String key) {
		super(unlocText);
		this.unlockKey = key;
	}

	@Override
	public boolean canSee(EntityPlayer player, ItemStack bookStack) {
		return bookStack.stackTagCompound.getBoolean(unlockKey);
	}

}
