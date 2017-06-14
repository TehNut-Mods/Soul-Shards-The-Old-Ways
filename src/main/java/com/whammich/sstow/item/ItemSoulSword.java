package com.whammich.sstow.item;

import com.whammich.sstow.RegistrarSoulShards;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulWeapon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemSoulSword extends ItemSword implements ISoulWeapon {

    public ItemSoulSword() {
        super(RegistrarSoulShards.MATERIAL_SOUL);

        setCreativeTab(SoulShardsTOW.TAB_SS);
        setUnlocalizedName(SoulShardsTOW.MODID + ".soulSword");
    }

    @Override
    public int getBonusSouls(ItemStack stack) {
        return 1;
    }
}
