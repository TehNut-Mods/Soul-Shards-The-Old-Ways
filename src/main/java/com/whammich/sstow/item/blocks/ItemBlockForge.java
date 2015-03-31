package com.whammich.sstow.item.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockForge extends ItemBlock {

	public ItemBlockForge(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public boolean isValidArmor(ItemStack itemStack, int armorType, Entity entity) {
		return true;
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		return Block.getBlockFromItem(this).getIcon(2, damage);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		list.add(new ItemStack(this, 1, 3));
	}

}
