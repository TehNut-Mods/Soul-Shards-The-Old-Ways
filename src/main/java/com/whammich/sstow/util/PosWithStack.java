package com.whammich.sstow.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PosWithStack {

    private final BlockPos pos;
    private final IBlockState block;

    public PosWithStack(BlockPos pos, IBlockState block) {
        this.pos = pos;
        this.block = block;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockState getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pos", pos)
                .append("block", block)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PosWithStack)) return false;

        PosWithStack that = (PosWithStack) o;

        if (pos != null ? !pos.equals(that.pos) : that.pos != null) return false;
        return block != null ? block.equals(that.block) : that.block == null;
    }

    @Override
    public int hashCode() {
        int result = pos != null ? pos.hashCode() : 0;
        result = 31 * result + (block != null ? block.hashCode() : 0);
        return result;
    }
}
