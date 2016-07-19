package com.whammich.sstow.compat.jei;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.item.ItemSoulShard;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import tehnut.lib.annot.Used;
import tehnut.lib.util.helper.ItemHelper;
import tehnut.lib.util.helper.TextHelper;

import javax.annotation.Nonnull;

@JEIPlugin
@Used
public class SoulShardsJEIPlugin extends BlankModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addDescription(new ItemStack(ItemHelper.getItem(ItemSoulShard.class)), TextHelper.localize("jei.SoulShards.soulshard.desc", ConfigHandler.catalystItem.getDisplayName()));
    }
}
