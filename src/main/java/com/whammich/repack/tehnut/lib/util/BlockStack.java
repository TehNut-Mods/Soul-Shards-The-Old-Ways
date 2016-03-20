package com.whammich.repack.tehnut.lib.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameData;

public class BlockStack {

    private final Block block;
    private final int meta;
    private final IBlockState state;

    public BlockStack(Block block, int meta) {
        this.block = block;
        this.meta = meta;
        this.state = block.getStateFromMeta(meta);
    }

    public BlockStack(Block block) {
        this(block, 0);
    }

    public ItemStack getItemStack() {
        return new ItemStack(block, 1, meta);
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    public IBlockState getState() {
        return state;
    }

    @Override
    public String toString() {
        return GameData.getBlockRegistry().getNameForObject(getBlock()) + ":" + getMeta();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockStack that = (BlockStack) o;

        if (getMeta() != that.getMeta()) return false;
        return getBlock() != null ? getBlock().equals(that.getBlock()) : that.getBlock() == null;

    }

    @Override
    public int hashCode() {
        int result = getBlock() != null ? getBlock().hashCode() : 0;
        result = 31 * result + getMeta();
        return result;
    }

    public static BlockStack getStackFromPos(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return new BlockStack(state.getBlock(), state.getBlock().getMetaFromState(state));
    }
}
