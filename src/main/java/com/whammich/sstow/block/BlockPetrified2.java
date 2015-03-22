package com.whammich.sstow.block;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockPetrified2 extends BlockRotatedPillar {
    @SideOnly(Side.CLIENT)
    protected IIcon[] sideIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon[] topIcon;

    public BlockPetrified2() {
        super(Material.rock);
        setBlockName("sstow.petrified.log");
        setCreativeTab(Register.CREATIVE_TAB);
        setLightOpacity(255);
        useNeighborBrightness = true;
        blockHardness = 3.0F;
        blockResistance = 3.0F;
    }

    public static final String[] names = { "acacia", "big_oak" };

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.sideIcon = new IIcon[names.length];
        this.topIcon = new IIcon[names.length];

        for (int i = 0; i < this.sideIcon.length; ++i) {
            this.sideIcon[i] = iconRegister.registerIcon(Reference.MOD_ID + ":petrified_logs/petrified_log_" + names[i]);
            this.topIcon[i] = iconRegister.registerIcon(Reference.MOD_ID + ":petrified_logs/petrified_log_" + names[i] + "_top");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getSideIcon(int side) {
        return this.sideIcon[side % this.sideIcon.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getTopIcon(int side) {
        return this.topIcon[side % this.topIcon.length];
    }
}