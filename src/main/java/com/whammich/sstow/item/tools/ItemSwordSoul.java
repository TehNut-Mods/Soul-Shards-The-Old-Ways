package com.whammich.sstow.item.tools;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class ItemSwordSoul extends ItemSword {

    private static String[] browniePoints = { "Xoguian" };
    private IIcon[] icon = new IIcon[browniePoints.length];

	public ItemSwordSoul(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soultool.sword";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":swordSoul");

        for (int i = 0; i < browniePoints.length; i++) {
            icon[i] = iconRegister.registerIcon(Reference.modID + ":brownie/" + browniePoints[i].toLowerCase().replaceAll(" ", ""));
        }
	}

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        for (int i = 0; i < browniePoints.length; i++) {
            if (stack.getDisplayName().equalsIgnoreCase(browniePoints[i]))
                return icon[i];
        }

        return itemIcon;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconIndex(stack);
    }
}
