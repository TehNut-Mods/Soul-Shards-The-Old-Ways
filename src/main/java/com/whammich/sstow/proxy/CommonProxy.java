package com.whammich.sstow.proxy;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.client.gui.GuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.repack.tehnut.lib.iface.IProxy;

public class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        for (ASMDataTable.ASMData data : SoulShardsTOW.instance.getEventHandlers()) {
            try {
                Class<?> asmClass = Class.forName(data.getClassName());
                boolean client = asmClass.getAnnotation(Handler.class).client();

                Object handler = asmClass.newInstance();

                if (!client)
                    MinecraftForge.EVENT_BUS.register(handler);
            } catch (Exception e) {
                SoulShardsTOW.instance.getLogHelper().error(String.format("Unable to register event handler for class %s", data.getClassName()));
            }
        }

        NetworkRegistry.INSTANCE.registerGuiHandler(SoulShardsTOW.instance, new GuiHandler());
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
