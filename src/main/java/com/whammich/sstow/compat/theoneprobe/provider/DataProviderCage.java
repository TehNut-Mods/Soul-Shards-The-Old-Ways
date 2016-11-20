package com.whammich.sstow.compat.theoneprobe.provider;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.tile.TileEntityCage;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.Utils;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tehnut.lib.util.helper.TextHelper;

public class DataProviderCage implements IProbeInfoProvider {

    @Override
    public String getID() {
        return SoulShardsTOW.MODID + ":cage";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        Block block = blockState.getBlock();

        if (block instanceof BlockCage) {
            TileEntity tile = world.getTileEntity(data.getPos());
            if (tile != null && tile instanceof TileEntityCage) {
                TileEntityCage cage = (TileEntityCage) tile;

                if (!cage.getStackHandler().getStackInSlot(0).isEmpty()) {
                    boolean disabled = !ConfigHandler.entityList.contains(cage.getEntName());;
                    probeInfo.text((disabled ? TextFormatting.RED.toString() : "") + TextHelper.localizeEffect("waila.soulshardstow.boundTo", Utils.getEntityNameTranslated(cage.getEntName())));
                    probeInfo.text(TextHelper.localizeEffect("waila.soulshardstow.tier", cage.getTier()));
                    if (mode == ProbeMode.DEBUG)
                        probeInfo.text(TextHelper.localizeEffect("waila.soulshardstow.owner", cage.getOwner()));
                }
            }
        }
    }
}
