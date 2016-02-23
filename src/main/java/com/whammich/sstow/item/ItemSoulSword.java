package com.whammich.sstow.item;

import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.item.ItemSword;
import com.whammich.repack.tehnut.lib.annot.ModItem;
import com.whammich.repack.tehnut.lib.annot.Used;

@ModItem(name = "ItemSoulSword")
@Used
public class ItemSoulSword extends ItemSword {

    public ItemSoulSword() {
        super(ToolMaterial.IRON);

        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setUnlocalizedName(SoulShardsTOW.MODID + ".soulSword");
    }
}
