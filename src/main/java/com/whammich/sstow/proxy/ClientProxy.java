package com.whammich.sstow.proxy;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.RegistrarSoulShards;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // Shard
        for (int i = 0; i < TierHandler.tiers.size(); i++)
            ModelLoader.registerItemVariants(RegistrarSoulShards.SHARD, new ModelResourceLocation(RegistrarSoulShards.SHARD.getRegistryName(), "tier=" + i));
        ModelLoader.registerItemVariants(RegistrarSoulShards.SHARD, new ModelResourceLocation(RegistrarSoulShards.SHARD.getRegistryName(), "tier=unbound"));
        ModelLoader.setCustomMeshDefinition(RegistrarSoulShards.SHARD, stack -> {
            if (ShardHelper.isBound(stack))
                return new ModelResourceLocation(RegistrarSoulShards.SHARD.getRegistryName(), "tier=" + ShardHelper.getTierFromShard(stack));

            return new ModelResourceLocation(RegistrarSoulShards.SHARD.getRegistryName(), "tier=unbound");
        });

        // Sword
        ModelLoader.setCustomModelResourceLocation(RegistrarSoulShards.VILE_SWORD, 0, new ModelResourceLocation(RegistrarSoulShards.VILE_SWORD.getRegistryName(), "inventory"));

        // Materials
        ModelLoader.setCustomModelResourceLocation(RegistrarSoulShards.MATERIALS, 0, new ModelResourceLocation(RegistrarSoulShards.MATERIALS.getRegistryName(), "type=ingotsoulium"));
        ModelLoader.setCustomModelResourceLocation(RegistrarSoulShards.MATERIALS, 1, new ModelResourceLocation(RegistrarSoulShards.MATERIALS.getRegistryName(), "type=dustcorrupted"));
        ModelLoader.setCustomModelResourceLocation(RegistrarSoulShards.MATERIALS, 2, new ModelResourceLocation(RegistrarSoulShards.MATERIALS.getRegistryName(), "type=dustvile"));

        // Cage
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(RegistrarSoulShards.CAGE), 0, new ModelResourceLocation(RegistrarSoulShards.CAGE.getRegistryName(), "active=false"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(RegistrarSoulShards.CAGE), 1, new ModelResourceLocation(RegistrarSoulShards.CAGE.getRegistryName(), "active=true"));
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
