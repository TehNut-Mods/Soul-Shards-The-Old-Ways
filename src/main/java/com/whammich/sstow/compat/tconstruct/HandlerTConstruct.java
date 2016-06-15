package com.whammich.sstow.compat.tconstruct;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.item.ItemMaterials;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ToolClientProxy;

public class HandlerTConstruct {

    public static Modifier soulStealer;

    public static void init() {
        soulStealer = new ModifierSoulStealer();
        soulStealer.addItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED), 16, 1);
    }

    @SideOnly(Side.CLIENT)
    public static void initRender() {
        ((ToolClientProxy) TinkerTools.proxy).registerModifierModel(soulStealer, new ResourceLocation(SoulShardsTOW.MODID, "models/item/modifiers/soulStealer"));
    }
}
