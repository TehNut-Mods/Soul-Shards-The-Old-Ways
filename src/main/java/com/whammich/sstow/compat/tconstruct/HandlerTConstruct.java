package com.whammich.sstow.compat.tconstruct;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.item.ItemMaterials;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.texture.MetalTextureTexture;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.ToolClientProxy;

public class HandlerTConstruct {

    public static Modifier modSoulStealer;
    public static Material materialCorrupted;
    public static AbstractTrait traitVile;

    public static void init() {
        modSoulStealer = new ModifierSoulStealer();
        modSoulStealer.addItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED), 16, 1);

        materialCorrupted = new Material("corrupted", 0x7A10A7);
        traitVile = new TraitVile();

        materialCorrupted.setCastable(true);
        materialCorrupted.addTrait(traitVile, HeadMaterialStats.TYPE);
        materialCorrupted.addItem(ItemMaterials.INGOT_CORRUPTED, 1, Material.VALUE_Ingot);
        materialCorrupted.setRepresentativeItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
        TinkerRegistry.addMaterial(materialCorrupted);
        TinkerRegistry.addMaterialStats(
                materialCorrupted,
                new HeadMaterialStats(204, 7.00F, 4.00F, HarvestLevels.DIAMOND),
                new HandleMaterialStats(0.85F, 60),
                new ExtraMaterialStats(50)
            );
    }

    @SideOnly(Side.CLIENT)
    public static void initRender() {
        ((ToolClientProxy) TinkerTools.proxy).registerModifierModel(modSoulStealer, new ResourceLocation(SoulShardsTOW.MODID, "models/item/modifiers/soulStealer"));

        materialCorrupted.setRenderInfo(new MaterialRenderInfo.AbstractMaterialRenderInfo() {
            @Override
            public TextureAtlasSprite getTexture(TextureAtlasSprite baseTexture, String location) {
                return new MetalTextureTexture(Util.resource("items/materials/ardite_rust"), baseTexture, location, 0x372458, 0.6f, 1.0f, 0.1f);
            }
        });
    }
}
