package com.whammich.sstow.compat.jei;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.RegistrarSoulShards;
import com.whammich.sstow.util.TierHandler;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;
import java.util.Collections;

@JEIPlugin
public class SoulShardsJEIPlugin extends BlankModPlugin {

    @Override
    public void register(@Nonnull IModRegistry registry) {
        ItemStack input = new ItemStack(RegistrarSoulShards.SHARD);
        ShardHelper.setTierForShard(input, 1);
        ShardHelper.setKillsForShard(input, TierHandler.getMaxKills(1));
        ShardHelper.setBoundEntity(input, new ResourceLocation("minecraft", "pig"));

        ItemStack output = input.copy();
        ShardHelper.setKillsForShard(output, ShardHelper.getKillsFromShard(output) * 2);

        registry.getJeiHelpers().getVanillaRecipeFactory().createAnvilRecipe(input, Collections.singletonList(input), Collections.singletonList(output));

        registry.addIngredientInfo(new ItemStack(RegistrarSoulShards.SHARD), ItemStack.class, I18n.translateToLocalFormatted("jei.SoulShards.soulshard.desc", ConfigHandler.catalystItem.getDisplayName()));
    }
}
