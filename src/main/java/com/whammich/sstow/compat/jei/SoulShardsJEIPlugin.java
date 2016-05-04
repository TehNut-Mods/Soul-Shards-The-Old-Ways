package com.whammich.sstow.compat.jei;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.registry.ModObjects;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class SoulShardsJEIPlugin extends BlankModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addDescription(new ItemStack(ModObjects.shard), "jei.SoulShards.soulshard.desc");

        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ModObjects.shard, ShardHelper.KILL_COUNT);
        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ModObjects.shard, ShardHelper.TIER);
        registry.getJeiHelpers().getNbtIgnoreList().ignoreNbtTagNames(ModObjects.shard, ShardHelper.ENTITY);
    }
}
