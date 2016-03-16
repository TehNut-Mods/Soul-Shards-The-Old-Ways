package com.whammich.sstow.proxy;

import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.repack.tehnut.lib.iface.IVariantProvider;
import com.whammich.repack.tehnut.lib.iface.IMeshProvider;
import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.tuple.Pair;

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
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void tryHandleBlockModel(Block block, String name) {
        if (block instanceof IVariantProvider) {
            IVariantProvider variantProvider = (IVariantProvider) block;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), variant.getLeft(), new ModelResourceLocation(new ResourceLocation(SoulShardsTOW.MODID, name), variant.getRight()));
        }
    }

    @Override
    public void tryHandleItemModel(Item item, String name) {
        if (item instanceof IMeshProvider) {
            IMeshProvider meshProvider = (IMeshProvider) item;
            ModelLoader.setCustomMeshDefinition(item, meshProvider.getMeshDefinition());
            for (String variant : meshProvider.getVariants())
                ModelLoader.registerItemVariants(item, new ModelResourceLocation(new ResourceLocation(SoulShardsTOW.MODID, "item/" + name), variant));
        } else if (item instanceof IVariantProvider) {
            IVariantProvider variantProvider = (IVariantProvider) item;
            for (Pair<Integer, String> variant : variantProvider.getVariants())
                ModelLoader.setCustomModelResourceLocation(item, variant.getLeft(), new ModelResourceLocation(new ResourceLocation(SoulShardsTOW.MODID, "item/" + name), variant.getRight()));
        }
    }
}
