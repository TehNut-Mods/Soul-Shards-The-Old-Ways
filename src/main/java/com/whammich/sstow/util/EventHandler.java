package com.whammich.sstow.util;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.*;
import com.whammich.sstow.api.event.CageSpawnEvent;
import com.whammich.sstow.api.event.ShardTierChangeEvent;
import com.whammich.sstow.compat.CompatibilityType;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.RegistrarSoulShards;
import com.whammich.sstow.tile.TileEntityCage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event) {
        World world = event.getEntity().getEntityWorld();

        if (world.isRemote || !(event.getEntity() instanceof EntityLiving) || !(event.getSource().getTrueSource() instanceof EntityPlayer) || event.getSource().getTrueSource() instanceof FakePlayer)
            return;

        EntityLiving dead = (EntityLiving) event.getEntity();
        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        ResourceLocation entName = EntityList.getKey(dead);

        if (entName == null) {
            SoulShardsTOW.LOGGER.error("Player killed an entity with no mapped name: {}", dead);
            return;
        }

        ItemStack shard = Utils.getShardFromInv(player, entName);

        if (!ConfigHandler.entityList.contains(entName) || SoulShardsAPI.isEntityBlacklisted(dead))
            return;

        if (!EntityMapper.isEntityValid(entName))
            return;

        if (!ConfigHandler.countCageBornForShard && Utils.isCageBorn(dead))
            return;

        if (!ShardHelper.isBound(shard))
            ShardHelper.setBoundEntity(shard, entName);

        int soulStealer = EnchantmentHelper.getEnchantmentLevel(RegistrarSoulShards.SOUL_STEALER, player.getHeldItemMainhand());
        soulStealer *= ConfigHandler.soulStealerBonus;

        if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ISoulWeapon)
            soulStealer += ((ISoulWeapon) player.getHeldItemMainhand().getItem()).getBonusSouls(player.getHeldItemMainhand());

        Utils.increaseShardKillCount(shard, 1 + soulStealer);
    }

    @SubscribeEvent
    public static void checkExperienceDrop(LivingExperienceDropEvent event) {
        if (!ConfigHandler.enableExperienceDrop && Utils.isCageBorn(event.getEntityLiving()))
            event.setDroppedExperience(0);
    }

    @SubscribeEvent
    public static void tryHandleStructure(PlayerInteractEvent.RightClickBlock event) {
        if (ItemSoulShard.multiblock.isEmpty())
            ItemSoulShard.buildMultiblock();

        IBlockState worldState = event.getWorld().getBlockState(event.getPos());

        if (!event.getItemStack().isEmpty() && ItemStack.areItemsEqual(event.getItemStack(), ConfigHandler.catalystItem) && worldState == ItemSoulShard.originBlock) {
            for (Pair<BlockPos, IBlockState> multiblockPair : ItemSoulShard.multiblock) {
                IBlockState offState = event.getWorld().getBlockState(event.getPos().add(multiblockPair.getLeft()));
                if (!multiblockPair.getRight().equals(offState))
                    return;
            }

            for (Pair<BlockPos, IBlockState> multiblockPair : ItemSoulShard.multiblock)
                event.getWorld().destroyBlock(event.getPos().add(multiblockPair.getLeft()), false);

            if (!event.getWorld().isRemote)
                InventoryHelper.spawnItemStack(event.getWorld(), event.getEntityPlayer().posX, event.getEntityPlayer().posY + 0.25, event.getEntityPlayer().posZ, new ItemStack(RegistrarSoulShards.SHARD));

            if (!event.getEntityPlayer().capabilities.isCreativeMode)
                event.getItemStack().shrink(1);

            event.getEntityPlayer().swingArm(event.getHand());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void tryHandleCageInteract(PlayerInteractEvent.RightClickBlock event) {
        TileEntity tile = event.getWorld().getTileEntity(event.getPos());
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = event.getItemStack();

        if (tile != null && tile instanceof ISoulCage) {
            TileEntityCage cage = (TileEntityCage) tile;
            if (heldItem != ItemStack.EMPTY && cage.getStackHandler().getStackInSlot(0) == ItemStack.EMPTY && ShardHelper.isBound(heldItem) && !player.isSneaking()) {
                cage.getStackHandler().setStackInSlot(0, heldItem.copy());
                cage.setTier(ShardHelper.getTierFromShard(heldItem));
                cage.setEntName(ShardHelper.getBoundEntity(heldItem));
                if (!event.getWorld().isRemote)
                    cage.setOwner(player.getGameProfile().getId().toString());
                player.setHeldItem(event.getHand(), ItemStack.EMPTY);
                player.swingArm(event.getHand());
            } else if (cage.getStackHandler().getStackInSlot(0) != ItemStack.EMPTY && player.getHeldItemMainhand() == ItemStack.EMPTY && player.isSneaking()) {
                if (!event.getWorld().isRemote) {
                    EntityItem invItem = new EntityItem(event.getWorld(), player.posX, player.posY + 0.25, player.posZ, cage.getStackHandler().getStackInSlot(0));
                    event.getWorld().spawnEntity(invItem);
                }
                cage.reset();
                player.swingArm(event.getHand());
            }
        }
    }

    @SubscribeEvent
    public static void onAnvil(AnvilUpdateEvent event) {
        if (!event.getLeft().isEmpty() && event.getLeft().getItem() instanceof ISoulShard && !event.getRight().isEmpty() && event.getRight().getItem() instanceof ISoulShard) {
            if (ShardHelper.isBound(event.getLeft()) && ShardHelper.getKillsFromShard(event.getRight()) > 0) {
                if (Utils.hasMaxedKills(event.getLeft()))
                    return;

                ItemStack output = event.getLeft().copy();
                Utils.increaseShardKillCount(output, ShardHelper.getKillsFromShard(event.getRight()));
                event.setOutput(output);
                event.setCost(ShardHelper.getTierFromShard(output) * 6);
            }
        }
    }

    @SubscribeEvent
    public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(SoulShardsTOW.MODID)) {
            ConfigHandler.syncConfig();
            ConfigHandler.handleEntityList("Entity List");
        }
    }

    // Hard mode

    @SubscribeEvent
    public static void onCageSpawn(CageSpawnEvent event) {
        if (ConfigHandler.compatibilityType != CompatibilityType.HARDMODE)
            return;

        int currentKills = ShardHelper.getKillsFromShard(event.getShard());
        if (currentKills <= 0) {
            event.setCanceled(true);
            return;
        }

        ShardHelper.setKillsForShard(event.getShard(), currentKills - 1);
    }

    @SubscribeEvent
    public static void onTierChange(ShardTierChangeEvent event) {
        if (ConfigHandler.compatibilityType != CompatibilityType.HARDMODE)
            return;

        if (event.getNewTier() < ShardHelper.getTierFromShard(event.getShardStack()))
            event.setCanceled(true);
    }
}
