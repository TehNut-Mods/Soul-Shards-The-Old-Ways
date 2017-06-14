package com.whammich.sstow.registry;

import com.whammich.sstow.RegistrarSoulShards;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.item.ItemMaterials;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(Blocks.SOUL_SAND, ItemMaterials.getStack(ItemMaterials.DUST_VILE), 0.4F);

        // Corrupted Ingot
        CraftingManager.register(
                new ResourceLocation(SoulShardsTOW.MODID, "corrupted_ingot"),
                new ShapedRecipes(
                        "corrupted_ingot", 3, 3,
                        NonNullList.from(Ingredient.EMPTY,
                                new OreIngredient("dustCorrupted"), new OreIngredient("dustVile"), new OreIngredient("dustCorrupted"),
                                new OreIngredient("dustVile"), new OreIngredient("ingotIron"), new OreIngredient("dustVile"),
                                new OreIngredient("dustCorrupted"), new OreIngredient("dustVile"), new OreIngredient("dustCorrupted")
                        ),
                        ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED)
                )
        );

        // Corrupted Essence
        CraftingManager.register(
                new ResourceLocation(SoulShardsTOW.MODID, "corrupted_essence"),
                new ShapelessRecipes(
                        "corrupted_essence",
                        ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE),
                        NonNullList.from(Ingredient.EMPTY,
                                new OreIngredient("gemLapis"),
                                new OreIngredient("dustRedstone"),
                                Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN)),
                                Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN))
                        )
                )
        );

        // Vile Sword
        CraftingManager.register(
                new ResourceLocation(SoulShardsTOW.MODID, "vile_sword"),
                new ShapedRecipes(
                        "vile_sword", 1, 3,
                        NonNullList.from(Ingredient.EMPTY,
                                new OreIngredient("ingotCorrupted"),
                                new OreIngredient("ingotCorrupted"),
                                new OreIngredient("stickWood")
                        ),
                        new ItemStack(RegistrarSoulShards.VILE_SWORD)
                )
        );

        // Cage
        CraftingManager.register(
                new ResourceLocation(SoulShardsTOW.MODID, "cage"),
                new ShapedRecipes(
                        "cage", 3, 3,
                        NonNullList.from(Ingredient.EMPTY,
                                new OreIngredient("ingotCorrupted"), Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)), new OreIngredient("ingotCorrupted"),
                                Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)), Ingredient.EMPTY, Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)),
                                new OreIngredient("ingotCorrupted"), Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)), new OreIngredient("ingotCorrupted")
                        ),
                        new ItemStack(RegistrarSoulShards.CAGE)
                )
        );

        // Convenience
        CraftingManager.register(
                new ResourceLocation(SoulShardsTOW.MODID, "shard_reset"),
                new ShapelessRecipes(
                        "shard_reset",
                        new ItemStack(RegistrarSoulShards.SHARD),
                        NonNullList.from(Ingredient.EMPTY,
                                Ingredient.fromStacks(new ItemStack(RegistrarSoulShards.SHARD))
                        )
                )
        );
    }
}
