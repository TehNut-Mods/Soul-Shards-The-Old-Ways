package com.whammich.repack.tehnut.lib.block.base;

import com.whammich.repack.tehnut.lib.block.property.UnlistedPropertyBoolean;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a block that has multiple meta-based states.
 * 
 * These states will be named true or false. Somewhere along the
 * way, each value is {@code toLowerCase()}'ed, so the blockstate JSON needs all
 * values to be lowercase.
 */
public class BlockBoolean extends Block
{
    private final List<Boolean> values;
    private final PropertyBool boolProp;
    private final IUnlistedProperty unlistedBooleanProp;
    private final BlockState realBlockState;

    public BlockBoolean(Material material, String propName)
    {
        super(material);

        this.values = Arrays.asList(false, true);

        this.boolProp = PropertyBool.create(propName);
        this.unlistedBooleanProp = new UnlistedPropertyBoolean(propName);
        this.realBlockState = createRealBlockState();
        setupStates();
    }

    public BlockBoolean(Material material)
    {
        this(material, "enabled");
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getBlockState().getBaseState().withProperty(boolProp, values.get(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return values.indexOf(state.getValue(boolProp));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public BlockState getBlockState()
    {
        return this.realBlockState;
    }

    @Override
    public BlockState createBlockState()
    {
        return Blocks.air.getBlockState();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
    }

    private void setupStates()
    {
        this.setDefaultState(getExtendedBlockState().withProperty(unlistedBooleanProp, values.get(0)).withProperty(boolProp, values.get(0)));
    }

    public ExtendedBlockState getBaseExtendedState()
    {
        return (ExtendedBlockState) this.getBlockState();
    }

    public IExtendedBlockState getExtendedBlockState()
    {
        return (IExtendedBlockState) this.getBaseExtendedState().getBaseState();
    }

    private BlockState createRealBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] {boolProp}, new IUnlistedProperty[] { unlistedBooleanProp });
    }

    public List<Boolean> getValues() {
        return new ArrayList<Boolean>(values);
    }

    public PropertyBool getBoolProp() {
        return boolProp;
    }

    public IUnlistedProperty getUnlistedBooleanProp() {
        return unlistedBooleanProp;
    }

    public BlockState getRealBlockState() {
        return realBlockState;
    }
}
