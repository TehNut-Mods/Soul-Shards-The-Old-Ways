package com.whammich.sstow.event;

import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.registry.ModEnchantments;
import com.whammich.sstow.util.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tehnut.lib.annot.Handler;
import tehnut.lib.annot.Used;

@Handler
@Used
public class PlayerKillEntityEvent {
    @SubscribeEvent
    public void onEntityKill(LivingDeathEvent event) {
        World world = event.entity.worldObj;

        if (world.isRemote || !(event.entity instanceof EntityLiving) || !(event.source.getEntity() instanceof EntityPlayer))
            return;

        EntityLiving dead = (EntityLiving) event.entity;
        EntityPlayer player = (EntityPlayer) event.source.getEntity();
        String entName = EntityList.getEntityString(dead);

        if (Strings.isNullOrEmpty(entName)) {
            SoulShardsTOW.instance.getLogHelper().severe("Player killed an entity with no unlocalized name: %s", dead);
            return;
        }

        if (!ConfigHandler.entityList.contains(entName))
            return;

        if (!EntityMapper.isEntityValid(entName))
            return;

        if (dead instanceof EntitySkeleton && ((EntitySkeleton) dead).getSkeletonType() == 1)
            entName = "Wither Skeleton";

        ItemStack shard = Utils.getShardFromInv(player, entName);

        if (shard != null) {
            if (!Utils.isShardBound(shard)) {
                Utils.setShardBoundEnt(shard, entName);
                Utils.writeEntityHeldItem(shard, dead);
                Utils.setShardBoundPlayer(shard, player);
            }

            Utils.writeEntityArmor(shard, dead);

            int soulStealer = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.soulStealer.effectId, player.getHeldItem());
            soulStealer *= Config.enchantBonus;

            Utils.increaseShardKillCount(shard, (short) (1 + soulStealer));
        }
    }
}