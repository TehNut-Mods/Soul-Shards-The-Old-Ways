package com.whammich.sstow.compat.jei;

import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.registry.ModItems;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
@Used
public class SoulShardsJEIPlugin extends BlankModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addDescription(new ItemStack(ModItems.getItem(ItemSoulShard.class)), "jei.SoulShards.soulshard.desc");

        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ModItems.getItem(ItemSoulShard.class), ShardHelper.KILL_COUNT);
        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ModItems.getItem(ItemSoulShard.class), ShardHelper.TIER);
        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ModItems.getItem(ItemSoulShard.class), ShardHelper.ENTITY);
    }
}
