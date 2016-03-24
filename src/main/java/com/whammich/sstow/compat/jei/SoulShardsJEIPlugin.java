package com.whammich.sstow.compat.jei;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.item.ItemSoulShard;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import tehnut.lib.annot.Used;
import tehnut.lib.util.helper.ItemHelper;

import javax.annotation.Nonnull;

@JEIPlugin
@Used
public class SoulShardsJEIPlugin extends BlankModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addDescription(new ItemStack(ItemHelper.getItem(ItemSoulShard.class)), "jei.SoulShards.soulshard.desc");

        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ItemHelper.getItem(ItemSoulShard.class), ShardHelper.KILL_COUNT);
        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ItemHelper.getItem(ItemSoulShard.class), ShardHelper.TIER);
        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ItemHelper.getItem(ItemSoulShard.class), ShardHelper.ENTITY);
    }
}
