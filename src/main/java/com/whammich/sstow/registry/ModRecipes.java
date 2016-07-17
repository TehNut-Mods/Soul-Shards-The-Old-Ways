package com.whammich.sstow.registry;

import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.item.ItemSoulSword;
import com.whammich.sstow.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tehnut.lib.annot.Handler;
import tehnut.lib.annot.Used;
import tehnut.lib.util.helper.BlockHelper;
import tehnut.lib.util.helper.ItemHelper;

@Handler
public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(Blocks.SOUL_SAND, ItemMaterials.getStack(ItemMaterials.DUST_VILE), 0.4F);

        GameRegistry.addRecipe(new ShapedOreRecipe(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED), "EVE", "VIV", "EVE", 'E', "dustCorrupted", 'I', "ingotIron", 'V', "dustVile"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE), "gemLapis", "dustRedstone", Blocks.OBSIDIAN, Blocks.OBSIDIAN));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHelper.getItem(ItemSoulSword.class)), "I", "I", "S", 'I', "ingotCorrupted", 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockHelper.getBlock(BlockCage.class)), "IBI", "B B", "IBI", 'I', "ingotCorrupted", 'B', Blocks.IRON_BARS));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemHelper.getItem(ItemSoulShard.class)), ItemHelper.getItem(ItemSoulShard.class)));
    }


    @SubscribeEvent
    @Used
    public void onAnvil(AnvilUpdateEvent event) {
        if (event.getLeft() != null && event.getLeft().getItem() instanceof ISoulShard && event.getRight() != null && event.getRight().getItem() instanceof ISoulShard) {
            if (ShardHelper.isBound(event.getLeft()) && ShardHelper.getKillsFromShard(event.getRight()) > 0) {
                if (Utils.hasMaxedKills(event.getLeft()))
                    return;

                ItemStack output = event.getLeft().copy();
                Utils.increaseShardKillCount(output, ShardHelper.getKillsFromShard(event.getRight()));
                event.setOutput(output);
                event.setCost(ShardHelper.getTierFromShard(output) * 6);
            }
        }
    }
}
