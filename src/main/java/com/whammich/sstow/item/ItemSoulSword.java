package com.whammich.sstow.item;

import com.whammich.repack.tehnut.lib.annot.ModItem;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulWeapon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

@ModItem(name = "ItemSoulSword")
@Used
public class ItemSoulSword extends ItemSword implements ISoulWeapon {

    private static final ToolMaterial MATERIAL_SOUL = EnumHelper.addToolMaterial("SOUL", 2, 250, 6.0F, 2.0F, 14).setRepairItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));

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
