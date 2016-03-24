package com.whammich.sstow.registry;

import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulSword;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems {

    public static void init() {
        OreDictionary.registerOre(ItemMaterials.INGOT_CORRUPTED, ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
        OreDictionary.registerOre(ItemMaterials.CORRUPTED_ESSENCE, ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE));
        OreDictionary.registerOre(ItemMaterials.DUST_VILE, ItemMaterials.getStack(ItemMaterials.DUST_VILE));

        ItemSoulSword.MATERIAL_SOUL.setRepairItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
    }
}
