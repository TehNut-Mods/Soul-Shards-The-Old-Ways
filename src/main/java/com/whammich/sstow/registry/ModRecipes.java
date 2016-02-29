package com.whammich.sstow.registry;

import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.item.ItemSoulSword;
import com.whammich.sstow.util.ForgeRecipeHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(Blocks.soul_sand, new ItemStack(ModItems.getItem(ItemMaterials.class), 1, 1), 0.4F);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.getItem(ItemMaterials.class), 1, 0), "EEE", "EIE", "EEE", 'E', "dustCorrupted", 'I', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.getItem(ItemSoulSword.class)), "I", "I", "S", 'I', "ingotCorrupted", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.getBlock(BlockCage.class)), "IBI", "B B", "IBI", 'I', "ingotCorrupted", 'B', Blocks.iron_bars));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.getItem(ItemSoulShard.class)), " I ", "IDI", " I ", 'I', "ingotCorrupted", 'D', "gemDiamond"));

        ForgeRecipeHandler.registerFuel(new ItemStack(ModItems.getItem(ItemMaterials.class), 1, 1), 1000);
        ForgeRecipeHandler.registerRecipe(new ForgeRecipeHandler.ForgeRecipe(new ItemStack(Items.diamond), new ItemStack(ModItems.getItem(ItemSoulShard.class)), 1000));
    }
}
