package com.whammich.sstow.registry;

import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tehnut.lib.annot.ModItem;

import java.util.HashMap;
import java.util.Map;

public class ModItems {

    private static Map<Class<? extends Item>, String> classToName = new HashMap<Class<? extends Item>, String>();

    public static void init() {
        for (ASMDataTable.ASMData data : SoulShardsTOW.instance.getModItems()) {
            try {
                Class<?> asmClass = Class.forName(data.getClassName());
                Class<? extends Item> modItemClass = asmClass.asSubclass(Item.class);
                String name = modItemClass.getAnnotation(ModItem.class).name();

                Item modItem = modItemClass.newInstance();

                GameRegistry.registerItem(modItem, name);
                classToName.put(modItemClass, name);
            } catch (Exception e) {
                SoulShardsTOW.instance.getLogHelper().error(String.format("Unable to register item for class %s", data.getClassName()));
            }
        }
    }

    public static Item getItem(String name) {
        return GameRegistry.findItem(SoulShardsTOW.MODID, name);
    }

    public static Item getItem(Class<? extends Item> itemClass) {
        return getItem(classToName.get(itemClass));
    }

    public static String getName(Class<? extends Item> itemClass) {
        return classToName.get(itemClass);
    }
}
