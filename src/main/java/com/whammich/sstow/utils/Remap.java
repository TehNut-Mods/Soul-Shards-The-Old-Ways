package com.whammich.sstow.utils;

import net.minecraft.item.Item;

public class Remap {
	public static String[] oldItemNames = { 
		"sstow_soul_sword",
		"sstow_soul_pickaxe", 
		"sstow_soul_axe", 
		"sstow_soul_hoe",
		"sstow_soul_spade", 
		"sstow_soul_shard"

	};

	public static Item[] newItemNames = { 
		Register.ItemSwordSoul, 
		Register.ItemPickaxeSoul,
		Register.ItemAxeSoul,
		Register.ItemHoeSoul,
		Register.ItemSpadeSoul,
		Register.ItemShardSoul 
		
	};
}
