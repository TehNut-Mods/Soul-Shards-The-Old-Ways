package com.whammich.sstow.compat.waila;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.tile.TileEntityCage;
import com.whammich.sstow.util.Utils;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tehnut.lib.util.helper.TextHelper;

import java.util.List;

public class DataProviderCage implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getNBTData().getString("entName").equalsIgnoreCase(Utils.EMPTY_ENT.toString()))
            return currenttip;

        if (accessor.getPlayer().isSneaking()) {
            if (accessor.getTileEntity() != null && accessor.getTileEntity() instanceof TileEntityCage) {
                if (accessor.getNBTData().hasKey("entName")) {
                    ResourceLocation entName = new ResourceLocation(accessor.getNBTData().getString("entName"));
                    boolean disabled = !ConfigHandler.entityList.contains(entName);
                    int tier = accessor.getNBTData().getInteger("tier");
                    String owner = accessor.getNBTData().getString("owner");

                    currenttip.add((disabled ? TextFormatting.RED.toString() : "") + TextHelper.localizeEffect("waila.soulshardstow.boundTo", Utils.getEntityNameTranslated(entName)));
                    currenttip.add(TextHelper.localizeEffect("waila.soulshardstow.tier", tier));

                    if (config.getConfig(SoulShardsWailaPlugin.CONFIG_OWNER))
                        currenttip.add(TextHelper.localizeEffect("waila.soulshardstow.owner", owner));
                }
            }
        } else {
            currenttip.add(TextHelper.localizeEffect("waila.soulshardstow.sneak"));
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (!((TileEntityCage) te).getEntName().toString().equals(Utils.EMPTY_ENT.toString())) {
            tag.setString("entName", ((TileEntityCage) te).getEntName().toString());
            tag.setInteger("tier", ((TileEntityCage) te).getTier());
            tag.setString("owner", ((TileEntityCage) te).getOwner());
        }
        return tag;
    }
}
