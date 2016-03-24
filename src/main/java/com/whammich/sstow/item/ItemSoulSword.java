package com.whammich.sstow.item;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulWeapon;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tehnut.lib.annot.ModItem;
import tehnut.lib.annot.Used;
import tehnut.lib.iface.IMeshProvider;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@ModItem(name = "ItemSoulSword")
@Used
public class ItemSoulSword extends ItemSword implements ISoulWeapon, IMeshProvider {

    public static final ToolMaterial MATERIAL_SOUL = EnumHelper.addToolMaterial("SOUL", 2, 250, 6.0F, 2.0F, 14);

    public ItemSoulSword() {
        super(MATERIAL_SOUL);

        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setUnlocalizedName(SoulShardsTOW.MODID + ".soulSword");
    }

    @Override
    public int getBonusSouls(ItemStack stack) {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(new ResourceLocation("soulshardstow:item/ItemSoulSword"), "type=vile");
            }
        };
    }

    @Override
    public List<String> getVariants() {
        return Collections.singletonList("type=vile");
    }

    @Nullable
    @Override
    public ResourceLocation getCustomLocation() {
        return null;
    }
}
