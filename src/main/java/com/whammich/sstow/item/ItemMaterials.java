package com.whammich.sstow.item;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemMaterials extends Item {

    private static String[] names = { "nugget.iron", "nugget.soulium", "ingot.soulium", "dust.vile", "essence.corrupted" };
    private IIcon[] icon = new IIcon[16];

    public ItemMaterials() {
        super();

        setUnlocalizedName("sstow.material");
        setCreativeTab(Register.CREATIVE_TAB);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + names[stack.getItemDamage() % names.length];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return this.icon[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icon[0] = iconRegister.registerIcon(Reference.MOD_ID + ":iron_nugget");
        this.icon[1] = iconRegister.registerIcon(Reference.MOD_ID + ":soulium_nugget");
        this.icon[2] = iconRegister.registerIcon(Reference.MOD_ID + ":soulium_ingot");
        this.icon[3] = iconRegister.registerIcon(Reference.MOD_ID + ":vile_dust");
        this.icon[4] = iconRegister.registerIcon(Reference.MOD_ID + ":corrupted_essence");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }
}
