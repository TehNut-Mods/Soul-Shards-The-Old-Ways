package com.whammich.sstow.item;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulWeapon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSoulSword extends ItemSword implements ISoulWeapon {

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
}
