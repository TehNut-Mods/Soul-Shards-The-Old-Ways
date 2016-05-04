package com.whammich.sstow.registry;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ModRenders {

    public static void initRenders() {
        // Soul Shard
        final ResourceLocation shardLocation = new ResourceLocation(SoulShardsTOW.MODID, "item/ItemSoulShard");
        ModelLoader.registerItemVariants(ModObjects.shard, new ModelResourceLocation(shardLocation, "tier=unbound"));
        for (int i = 0; i < TierHandler.tiers.size(); i++)
            ModelLoader.registerItemVariants(ModObjects.shard, new ModelResourceLocation(shardLocation, "tier=" + i));
        ModelLoader.setCustomMeshDefinition(ModObjects.shard, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                if (ShardHelper.isBound(stack))
                    return new ModelResourceLocation(shardLocation, "tier=" + ShardHelper.getTierFromShard(stack));

                return new ModelResourceLocation(shardLocation, "tier=unbound");
            }
        });

        // Vile Sword
        final ResourceLocation swordLocation = new ResourceLocation(SoulShardsTOW.MODID, "item/ItemSoulSword");
        ModelLoader.registerItemVariants(ModObjects.sword, new ModelResourceLocation(swordLocation, "type=vile"));
        ModelLoader.setCustomMeshDefinition(ModObjects.sword, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(swordLocation, "type=vile");
            }
        });

        // Materials
        final ResourceLocation materialLocation = new ResourceLocation(SoulShardsTOW.MODID, "item/ItemMaterials");
        ModelLoader.setCustomModelResourceLocation(ModObjects.materials, 0, new ModelResourceLocation(materialLocation, "type=ingotsoulium"));
        ModelLoader.setCustomModelResourceLocation(ModObjects.materials, 1, new ModelResourceLocation(materialLocation, "type=dustcorrupted"));
        ModelLoader.setCustomModelResourceLocation(ModObjects.materials, 2, new ModelResourceLocation(materialLocation, "type=dustvile"));

        // Cage
        final ResourceLocation cageLocation = new ResourceLocation(SoulShardsTOW.MODID, "BlockCage");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModObjects.cage), 0, new ModelResourceLocation(cageLocation, "active=false"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModObjects.cage), 1, new ModelResourceLocation(cageLocation, "active=true"));
    }
}
