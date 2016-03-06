package com.whammich.sstow.proxy;

import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.client.mesh.CustomMeshDefinition;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.item.ItemSoulSword;
import com.whammich.sstow.registry.ModBlocks;
import com.whammich.sstow.registry.ModItems;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Used
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        for (ASMDataTable.ASMData data : SoulShardsTOW.instance.getEventHandlers()) {
            try {
                Class<?> asmClass = Class.forName(data.getClassName());
                boolean client = asmClass.getAnnotation(Handler.class).client();

                Object handler = asmClass.newInstance();

                if (client)
                    MinecraftForge.EVENT_BUS.register(handler);
            } catch (Exception e) {
                SoulShardsTOW.instance.getLogHelper().error(String.format("Unable to register event handler for class %s", data.getClassName()));
            }
        }

        ModelLoader.setCustomMeshDefinition(ModItems.getItem(ItemSoulSword.class), new CustomMeshDefinition.SoulSword());
        registerItemVariant(ItemSoulSword.class, "type=vile");

        ModelLoader.setCustomMeshDefinition(ModItems.getItem(ItemSoulShard.class), new CustomMeshDefinition.SoulShard());
        registerItemVariant(ItemSoulShard.class, "tier=unbound");
        for (int i = 0; i < TierHandler.tiers.size(); i++)
            registerItemVariant(ItemSoulShard.class, "tier=" + i);

        registerItemModel(ItemMaterials.class, 0, "type=ingotsoulium");
        registerItemModel(ItemMaterials.class, 1, "type=dustcorrupted");
        registerItemModel(ItemMaterials.class, 2, "type=dustvile");

        registerBlockModel(BlockCage.class, 0, "active=false");
        registerBlockModel(BlockCage.class, 1, "active=true");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    private static void registerBlockModel(Class<? extends Block> blockClass, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.getBlock(blockClass)), meta, new ModelResourceLocation(new ResourceLocation(SoulShardsTOW.MODID, ModBlocks.getName(blockClass)), variant));
    }

    private static void registerItemModel(Class<? extends Item> itemClass, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(ModItems.getItem(itemClass), meta, new ModelResourceLocation(new ResourceLocation(SoulShardsTOW.MODID, "item/" + ModItems.getName(itemClass)), variant));
    }

    private static void registerItemVariant(Class<? extends Item> itemClass, String variant) {
        ModelLoader.registerItemVariants(ModItems.getItem(itemClass), new ModelResourceLocation(new ResourceLocation(SoulShardsTOW.MODID, "item/" + ModItems.getName(itemClass)), variant));
    }
}
