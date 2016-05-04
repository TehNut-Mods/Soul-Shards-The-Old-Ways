package com.whammich.sstow.event;

import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.*;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.registry.ModEnchantments;
import com.whammich.sstow.registry.ModObjects;
import com.whammich.sstow.tile.TileEntityCage;
import com.whammich.sstow.util.BlockStack;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.Utils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

public class EventHandler {

    // Handles retrieving and inputting of Shard into an ISoulCage
    @SubscribeEvent
    public void onInteractCage(PlayerInteractEvent.RightClickBlock event) {
        TileEntity tile = event.getWorld().getTileEntity(event.getPos());
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = event.getItemStack();

        if (tile != null && tile instanceof ISoulCage) {
            TileEntityCage cage = (TileEntityCage) tile;
            if (heldItem != null && cage.getStackInSlot(0) == null && cage.isItemValidForSlot(0, heldItem) && !player.isSneaking()) {
                cage.setInventorySlotContents(0, heldItem.copy());
                cage.setTier(ShardHelper.getTierFromShard(heldItem));
                cage.setEntName(ShardHelper.getBoundEntity(heldItem));
                if (!event.getWorld().isRemote)
                    cage.setOwner(player.getGameProfile().getId().toString());
                heldItem.stackSize--;
                player.swingArm(event.getHand());
            } else if (cage.getStackInSlot(0) != null && player.getHeldItemMainhand() == null && player.isSneaking()) {
                if (!event.getWorld().isRemote) {
                    EntityItem invItem = new EntityItem(event.getWorld(), player.posX, player.posY + 0.25, player.posZ, cage.getStackInSlot(0));
                    event.getWorld().spawnEntityInWorld(invItem);
                }
                cage.reset();
                player.swingArm(event.getHand());
            }
        }
    }

    // Handles the killing of entities and it's interaction with shards. IE: Setting the shard entity, adding kills, etc.
    @SubscribeEvent
    public void onEntityKill(LivingDeathEvent event) {
        World world = event.getEntity().worldObj;

        if (world.isRemote || !(event.getEntity() instanceof EntityLiving) || !(event.getSource().getEntity() instanceof EntityPlayer) || event.getSource().getEntity() instanceof FakePlayer)
            return;

        EntityLiving dead = (EntityLiving) event.getEntity();
        EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
        String entName = EntityList.getEntityString(dead);

        if (Strings.isNullOrEmpty(entName)) {
            SoulShardsTOW.instance.getLogHelper().error("Player killed an entity with no mapped name: {}", dead);
            return;
        }

        if (dead instanceof EntitySkeleton && ((EntitySkeleton) dead).getSkeletonType() == 1)
            entName = SoulShardsAPI.WITHER_SKELETON;

        ItemStack shard = Utils.getShardFromInv(player, entName);

        if (shard == null)
            return;

        if (!ConfigHandler.entityList.contains(entName) || SoulShardsAPI.isEntityBlacklisted(dead))
            return;

        if (!EntityMapper.isEntityValid(entName))
            return;

        if (!ConfigHandler.countCageBornForShard && Utils.isCageBorn(dead))
            return;

        if (!ShardHelper.isBound(shard))
            ShardHelper.setBoundEntity(shard, entName);

        int soulStealer = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.soulStealer, player.getHeldItemMainhand());
        soulStealer *= ConfigHandler.soulStealerBonus;

        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ISoulWeapon)
            soulStealer += ((ISoulWeapon) player.getHeldItemMainhand().getItem()).getBonusSouls(player.getHeldItemMainhand());

        Utils.increaseShardKillCount(shard, 1 + soulStealer);
    }

    // Handles creation of the shard via multi-block structure.
    @SubscribeEvent
    public void onInteractOrigin(PlayerInteractEvent.RightClickBlock event) {
        if (ItemSoulShard.multiblock.isEmpty())
            ItemSoulShard.buildMultiblock();

        if (event.getItemStack() != null && event.getItemStack().getItem() == Items.DIAMOND && ItemSoulShard.originBlock.equals(BlockStack.getStackFromPos(event.getWorld(), event.getPos()))) {
            for (Pair<BlockPos, BlockStack> multiblockPair : ItemSoulShard.multiblock) {
                BlockStack worldStack = BlockStack.getStackFromPos(event.getWorld(), event.getPos().add(multiblockPair.getLeft()));
                if (!multiblockPair.getRight().equals(worldStack))
                    return;
            }

            for (Pair<BlockPos, BlockStack> multiblockPair : ItemSoulShard.multiblock)
                event.getWorld().destroyBlock(event.getPos().add(multiblockPair.getLeft()), false);

            if (!event.getWorld().isRemote) {
                EntityItem invItem = new EntityItem(event.getWorld(), event.getEntityPlayer().posX, event.getEntityPlayer().posY + 0.25, event.getEntityPlayer().posZ, new ItemStack(ModObjects.shard, 1, 0));
                event.getWorld().spawnEntityInWorld(invItem);
            }

            if (!event.getEntityPlayer().capabilities.isCreativeMode) {
                if (event.getItemStack().stackSize > 1)
                    event.getItemStack().stackSize--;
                else
                    event.getEntityPlayer().setHeldItem(event.getHand(), null);
            }

            event.getEntityPlayer().swingArm(event.getHand());
        }
    }

    // Handles anvil interaction for Shard combining
    @SubscribeEvent
    public void onAnvil(AnvilUpdateEvent event) {
        if (event.getLeft() != null && event.getLeft().getItem() instanceof ISoulShard && event.getRight() != null && event.getRight().getItem() instanceof ISoulShard) {
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

    // Disables experience drops
    @SubscribeEvent
    public void onDeath(LivingExperienceDropEvent event) {
        if (!ConfigHandler.enableExperienceDrop && Utils.isCageBorn(event.getEntityLiving()))
            event.setDroppedExperience(0);
    }

    // Re-syncs Config when changed in-game.
    @SubscribeEvent
    public void configChanged(ConfigChangedEvent event) {
        if (event.getModID().equals(SoulShardsTOW.MODID)) {
            ConfigHandler.syncConfig();
            ConfigHandler.handleEntityList("Entity List");
        }
    }
}
