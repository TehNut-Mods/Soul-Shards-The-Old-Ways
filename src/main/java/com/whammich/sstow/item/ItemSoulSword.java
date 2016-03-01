package com.whammich.sstow.item;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulWeapon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import com.whammich.repack.tehnut.lib.annot.ModItem;
import com.whammich.repack.tehnut.lib.annot.Used;

@ModItem(name = "ItemSoulSword")
@Used
public class ItemSoulSword extends ItemSword implements ISoulWeapon {

    public ItemSoulSword() {
        super(ToolMaterial.IRON);

        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setUnlocalizedName(SoulShardsTOW.MODID + ".soulSword");
    }

    @Override
    public int getBonusSouls(ItemStack stack) {
        return 1;
    }
}
