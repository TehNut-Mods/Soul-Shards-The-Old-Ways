package com.whammich.sstow.client.mesh;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.util.Utils;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomMeshDefinition {

    public static class SoulSword implements ItemMeshDefinition {
        @Override
        public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation(new ResourceLocation("soulshardstow:item/ItemSoulSword"), "type=vile");
        }
    }

    public static class SoulShard implements ItemMeshDefinition {
        @Override
        public ModelResourceLocation getModelLocation(ItemStack stack) {
            if (ShardHelper.isBound(stack))
                return new ModelResourceLocation(new ResourceLocation("soulshardstow:item/ItemSoulShard"), "tier=" + ShardHelper.getTierFromShard(stack));

            return new ModelResourceLocation(new ResourceLocation("soulshardstow:item/ItemSoulShard"), "tier=unbound");
        }
    }
}
