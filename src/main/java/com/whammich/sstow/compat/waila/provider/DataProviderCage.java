//package com.whammich.sstow.compat.waila.provider;
//
//import com.whammich.sstow.ConfigHandler;
//import com.whammich.sstow.api.SoulShardsAPI;
//import com.whammich.sstow.compat.waila.WailaCallbackHandler;
//import com.whammich.sstow.tile.TileEntityCage;
//import com.whammich.sstow.util.Utils;
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import mcp.mobius.waila.api.IWailaDataProvider;
//import net.minecraft.entity.EntityList;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.world.World;
//import tehnut.lib.util.helper.TextHelper;
//
//import java.util.List;
//
//public class DataProviderCage implements IWailaDataProvider {
//
//    @Override
//    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return null;
//    }
//
//    @Override
//    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return null;
//    }
//
//    @Override
//    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        if (accessor.getPlayer().isSneaking()) {
//            if (accessor.getTileEntity() != null && accessor.getTileEntity() instanceof TileEntityCage) {
//                TileEntityCage cage = (TileEntityCage) accessor.getTileEntity();
//
//                if (cage.getStackInSlot(0) != null) {
//                    boolean disabled;
//                    if (!cage.getEntName().equals(SoulShardsAPI.WITHER_SKELETON) && !cage.getEntName().equals(SoulShardsAPI.WITHER_SKELETON_OLD))
//                        disabled = !ConfigHandler.entityList.contains(cage.getEntName()) || SoulShardsAPI.isEntityBlacklisted(EntityList.NAME_TO_CLASS.get(cage.getEntName()).getCanonicalName());
//                    else
//                        disabled = !ConfigHandler.entityList.contains(cage.getEntName());
//                    currenttip.add((disabled ? TextFormatting.RED.toString() : "") + TextHelper.localizeEffect("waila.soulshardstow.boundTo", Utils.getEntityNameTranslated(cage.getEntName())));
//                    currenttip.add(TextHelper.localizeEffect("waila.soulshardstow.tier", cage.getTier()));
//
//                    if (config.getConfig(WailaCallbackHandler.CONFIG_OWNER))
//                        currenttip.add(TextHelper.localizeEffect("waila.soulshardstow.owner", cage.getOwner()));
//                }
//            }
//        } else {
//            currenttip.add(TextHelper.localizeEffect("waila.soulshardstow.sneak"));
//        }
//
//        return currenttip;
//    }
//
//    @Override
//    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return null;
//    }
//
//    @Override
//    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
//        return null;
//    }
//}
