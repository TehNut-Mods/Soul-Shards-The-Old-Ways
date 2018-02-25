package com.whammich.sstow.compat.waila;

import com.whammich.sstow.util.Utils;
import mcp.mobius.waila.api.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class DataProviderCageBorn implements IWailaEntityProvider {

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getNBTData().getBoolean("cageBorn"))
            currenttip.add(I18n.format("waila.soulshardstow.cageBorn"));
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        if (!(ent instanceof EntityLivingBase))
            return tag;

        tag.setBoolean("cageBorn", Utils.isCageBorn((EntityLivingBase) ent));
        return tag;
    }
}
