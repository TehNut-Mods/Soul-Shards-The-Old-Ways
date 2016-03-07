package com.whammich.sstow.registry;

import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulSword;
import com.whammich.sstow.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Handler
public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(Blocks.soul_sand, ItemMaterials.getStack(ItemMaterials.DUST_VILE), 0.4F);

        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED), "EVE", "VIV", "EVE", 'E', "dustCorrupted", 'I', "ingotIron", 'V', "dustVile"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE), "gemLapis", "dustRedstone", Blocks.obsidian, Blocks.obsidian));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.getItem(ItemSoulSword.class)), "I", "I", "S", 'I', "ingotCorrupted", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.getBlock(BlockCage.class)), "IBI", "B B", "IBI", 'I', "ingotCorrupted", 'B', Blocks.iron_bars));
    }

    @SubscribeEvent
    public void onAnvil(AnvilUpdateEvent event) {
        if (event.left != null && event.left.getItem() instanceof ISoulShard && event.right != null && event.right.getItem() instanceof ISoulShard) {
            if (ShardHelper.isBound(event.left) && ShardHelper.getKillsFromShard(event.right) > 0) {
                if (Utils.hasMaxedKills(event.left))
                    return;

                ItemStack output = event.left.copy();
                Utils.increaseShardKillCount(output, ShardHelper.getKillsFromShard(event.right));
                event.output = output;
                event.cost = ShardHelper.getTierFromShard(output) * 6;
            }
        }
    }
}
