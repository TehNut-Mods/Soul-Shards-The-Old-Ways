package com.whammich.sstow.item;

import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.registry.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.whammich.repack.tehnut.lib.annot.ModItem;
import com.whammich.repack.tehnut.lib.annot.Used;

import java.util.ArrayList;
import java.util.List;

@ModItem(name = "ItemMaterials")
@Used
public class ItemMaterials extends Item {

    public static final String INGOT_CORRUPTED = "ingotCorrupted";
    public static final String CORRUPTED_ESSENCE = "dustCorrupted";

    private static List<String> names = new ArrayList<String>();

    public ItemMaterials() {
        super();

        setUnlocalizedName(SoulShardsTOW.MODID + ".material.");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setHasSubtypes(true);

        buildItems();
    }

    private void buildItems() {
        names.add(0, INGOT_CORRUPTED);
        names.add(1, CORRUPTED_ESSENCE);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + names.get(stack.getItemDamage());
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(this, 1, i));
    }

    public static ItemStack getStack(String name, int amount) {
        return new ItemStack(ModItems.getItem(ItemMaterials.class), amount, names.indexOf(name));
    }

    public static ItemStack getStack(String name) {
        return getStack(name, 1);
    }
}
