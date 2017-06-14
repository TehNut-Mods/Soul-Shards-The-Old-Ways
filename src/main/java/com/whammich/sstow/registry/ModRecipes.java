package com.whammich.sstow.registry;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.item.ItemSoulSword;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(Blocks.SOUL_SAND, ItemMaterials.getStack(ItemMaterials.DUST_VILE), 0.4F);

        // TODO
//        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED), "EVE", "VIV", "EVE", 'E', "dustCorrupted", 'I', "ingotIron", 'V', "dustVile"));
//        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE), "gemLapis", "dustRedstone", Blocks.OBSIDIAN, Blocks.OBSIDIAN));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHelper.getItem(ItemSoulSword.class)), "I", "I", "S", 'I', "ingotCorrupted", 'S', "stickWood"));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHelper.getBlock(BlockCage.class)), "IBI", "B B", "IBI", 'I', "ingotCorrupted", 'B', Blocks.IRON_BARS));
//        if (ConfigHandler.convenienceReset)
//            GameRegistry.addRecipe(new ShapelessOreRecipe(ItemHelper.getItem(ItemSoulShard.class), ItemHelper.getItem(ItemSoulShard.class)));
    }
}
