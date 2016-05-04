package com.whammich.sstow.registry;

import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.item.ItemSoulSword;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModObjects {

    public static Item materials;
    public static Item shard;
    public static Item sword;

    public static Block cage;

    public static void initItems() {
        materials = registerItem(new ItemMaterials(), "ItemMaterials");
        shard = registerItem(new ItemSoulShard(), "ItemSoulShard");
        sword = registerItem(new ItemSoulSword(), "ItemSoulSword");

        OreDictionary.registerOre(ItemMaterials.INGOT_CORRUPTED, ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
        OreDictionary.registerOre(ItemMaterials.CORRUPTED_ESSENCE, ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE));
        OreDictionary.registerOre(ItemMaterials.DUST_VILE, ItemMaterials.getStack(ItemMaterials.DUST_VILE));

        ItemSoulSword.MATERIAL_SOUL.setRepairItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
    }

    public static void initBlocks() {
        cage = registerBlock(new ItemBlock(new BlockCage()), "BlockCage");
    }

    private static Block registerBlock(ItemBlock itemBlock, String name) {
        Block block = itemBlock.getBlock();
        if (block.getRegistryName() == null)
            block.setRegistryName(name);

        if (itemBlock.getRegistryName() == null)
            itemBlock.setRegistryName(block.getRegistryName());

        GameRegistry.register(block);
        GameRegistry.register(itemBlock);
        return block;
    }

    private static Block registerBlock(Block block, String name) {
        if (block.getRegistryName() == null)
            block.setRegistryName(name);

        GameRegistry.register(block);
        return block;
    }

    private static Item registerItem(Item item, String name) {
        if (item.getRegistryName() == null)
            item.setRegistryName(name);

        GameRegistry.register(item);
        return item;
    }
}
