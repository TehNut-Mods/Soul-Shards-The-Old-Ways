package com.whammich.sstow.item;

import com.whammich.sstow.SoulShardsTOW;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import tehnut.lib.annot.ModItem;
import tehnut.lib.annot.Used;
import tehnut.lib.iface.IVariantProvider;
import tehnut.lib.util.helper.ItemHelper;

import java.util.ArrayList;
import java.util.List;

@ModItem(name = "ItemMaterials")
@Used
public class ItemMaterials extends Item implements IVariantProvider {

    public static final String INGOT_CORRUPTED = "ingotCorrupted";
    public static final String CORRUPTED_ESSENCE = "dustCorrupted";
    public static final String DUST_VILE = "dustVile";

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
        names.add(2, DUST_VILE);
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

    @Override
    public List<Pair<Integer, String>> getVariants() {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        ret.add(Pair.of(0, "type=ingotsoulium"));
        ret.add(Pair.of(1, "type=dustcorrupted"));
        ret.add(Pair.of(2, "type=dustvile"));
        return ret;
    }

    public static ItemStack getStack(String name, int amount) {
        return new ItemStack(ItemHelper.getItem(ItemMaterials.class), amount, names.indexOf(name));
    }

    public static ItemStack getStack(String name) {
        return getStack(name, 1);
    }
}
