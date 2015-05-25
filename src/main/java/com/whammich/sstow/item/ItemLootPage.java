package com.whammich.sstow.item;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLootPage extends Item {

	public IIcon icon[];
	public Random random;
	
	public ItemLootPage() {
		setUnlocalizedName("guide.book.missing");
		setMaxStackSize(1);
	}

//	@SideOnly(Side.CLIENT)
//	public IIcon getIconIndex(ItemStack stack) {
//		return icon[stack.stackTagCompound.getInteger("TI")];
//	}
	
//	@SideOnly(Side.CLIENT)
//	public void registerIcons(IIconRegister iconRegister) {
//		for (int i = 0; i < 9; i++)
//			this.icon[i] = iconRegister.registerIcon(Reference.modID + ":pages/" + random.nextInt(10));
//	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":pages/5");
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held) {
		if (Utils.displayShiftForDetail && !Utils.isShiftKeyDown())
			list.add(Utils.shiftForDetails());

		if (Utils.isShiftKeyDown()) {
			list.add(stack.stackTagCompound.getString("book"));
			list.add(Utils.localize("info.sstow.tooltip.page") + " " + stack.stackTagCompound.getString("page"));
		}
	}
}
