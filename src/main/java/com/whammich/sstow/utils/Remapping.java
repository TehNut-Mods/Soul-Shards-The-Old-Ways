package com.whammich.sstow.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Remapping {
	public static String[] oldItemNames = { "sstow_fixed", "sstow_soul_sword",
			"sstow_soul_pickaxe", "sstow_soul_axe", "sstow_soul_hoe",
			"sstow_soul_spade", "sstow_soul_shard",

	};

	public static Item[] newItemNames = { Register.ItemFixedDummy,
			Register.ItemSwordSoul, Register.ItemPickaxeSoul,
			Register.ItemAxeSoul, Register.ItemHoeSoul, Register.ItemSpadeSoul,
			Register.ItemShardSoul };;

	public static String[] oldBlockNames = { "sstow_forge",
			"sstow_soulium_block", "sstow_soul_cage", };

	public static Block[] newBlockNames = { Register.BlockForge,
			Register.BlockSoulium, Register.BlockCage, };
}
