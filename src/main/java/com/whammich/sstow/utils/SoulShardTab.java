package com.whammich.sstow.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulShardTab extends CreativeTabs {

	public SoulShardTab() {
		super("soul_shards_the_old_ways");
	}

	@Override
	public ItemStack getIconItemStack() {
		ItemStack shard = new ItemStack(Register.ItemShardSoul);

		Utils.setShardTier(shard, (byte) 5);
		Utils.setShardKillCount(shard, TierHandler.getMaxKills(5));
		Utils.setShardBoundEnt(shard, "NULL");

		return shard;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return null;
	}
}
