package com.whammich.sstow.item;

import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ISoulWeapon;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.registry.ModEnchantments;
import com.whammich.sstow.util.*;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemSoulShard extends Item implements ISoulShard {

    public static List<Pair<BlockPos, BlockStack>> multiblock = new ArrayList<Pair<BlockPos, BlockStack>>();
    public static BlockStack originBlock = null;

    public ItemSoulShard() {
        super();

        setUnlocalizedName(SoulShardsTOW.MODID + ".shard");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!Utils.hasMaxedKills(stack) && ConfigHandler.allowSpawnerAbsorption) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntityMobSpawner) {
                WeightedSpawnerEntity spawnerEntity = ObfuscationReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, ((TileEntityMobSpawner) tile).getSpawnerBaseLogic(), "randomEntity", "field_98282_f");
                String name = spawnerEntity.getNbt().getString("id");
                EntityLiving ent = EntityMapper.getNewEntityInstance(world, name, pos);

                if (ent == null)
                    return EnumActionResult.FAIL;

                if (!EntityMapper.isEntityValid(name) || SoulShardsAPI.isEntityBlacklisted(ent))
                    return EnumActionResult.FAIL;

                if (ent instanceof EntitySkeleton && ((EntitySkeleton) ent).getSkeletonType() == 1)
                    name = SoulShardsAPI.WITHER_SKELETON;

                if (ShardHelper.isBound(stack) && ShardHelper.getBoundEntity(stack).equals(name)) {
                    if (!world.isRemote)
                        Utils.increaseShardKillCount(stack, ConfigHandler.spawnerAbsorptionBonus);
                    world.destroyBlock(pos, false);
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return ConfigHandler.displayDurabilityBar && ShardHelper.getKillsFromShard(stack) < TierHandler.getMaxKills(TierHandler.tiers.size() - 1);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - ((double) ShardHelper.getKillsFromShard(stack) / (double) TierHandler.getTier(TierHandler.tiers.size() - 1).getMinKills());
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + (ShardHelper.isBound(stack) ? "" : ".unbound");
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (oldStack.getItem() instanceof ISoulShard && newStack.getItem() instanceof ISoulShard) {
            if (ShardHelper.getTierFromShard(oldStack) != ShardHelper.getTierFromShard(newStack))
                return true;

            if (!ShardHelper.getBoundEntity(oldStack).equals(ShardHelper.getBoundEntity(newStack)))
                return true;
        }

        return slotChanged;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return Utils.hasMaxedKills(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (int i = 0; i <= TierHandler.tiers.size() - 1; i++) {
            ItemStack stack = new ItemStack(item, 1);

            ShardHelper.setKillsForShard(stack, TierHandler.getMinKills(i));
            ShardHelper.setTierForShard(stack, i);

            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {
        if (ShardHelper.isBound(stack)) {
            String boundEnt = ShardHelper.getBoundEntity(stack);
            boolean disabled;
            if (!boundEnt.equals(SoulShardsAPI.WITHER_SKELETON) && !boundEnt.equals(SoulShardsAPI.WITHER_SKELETON_OLD))
                disabled = !ConfigHandler.entityList.contains(boundEnt) || SoulShardsAPI.isEntityBlacklisted(EntityList.NAME_TO_CLASS.get(boundEnt).getCanonicalName());
            else
                disabled = !ConfigHandler.entityList.contains(boundEnt);
            list.add((disabled ? TextFormatting.RED.toString() : "") + TextHelper.localizeEffect("tooltip.soulshardstow.bound", Utils.getEntityNameTranslated(boundEnt)));
        }

        if (ShardHelper.getKillsFromShard(stack) >= 0)
            list.add(TextHelper.localizeEffect("tooltip.soulshardstow.kills", ShardHelper.getKillsFromShard(stack)));

        list.add(TextHelper.localizeEffect("tooltip.soulshardstow.tier", ShardHelper.getTierFromShard(stack)));
    }

    public static void buildMultiblock() {
        originBlock = new BlockStack(Blocks.GLOWSTONE);
        multiblock.clear();
        multiblock.add(Pair.of(new BlockPos(0, 0, 0), new BlockStack(Blocks.GLOWSTONE)));
        multiblock.add(Pair.of(new BlockPos(1, 0, 0), new BlockStack(Blocks.QUARTZ_BLOCK)));
        multiblock.add(Pair.of(new BlockPos(-1, 0, 0), new BlockStack(Blocks.QUARTZ_BLOCK)));
        multiblock.add(Pair.of(new BlockPos(0, 0, 1), new BlockStack(Blocks.QUARTZ_BLOCK)));
        multiblock.add(Pair.of(new BlockPos(0, 0, -1), new BlockStack(Blocks.QUARTZ_BLOCK)));
        multiblock.add(Pair.of(new BlockPos(1, 0, 1), new BlockStack(Blocks.OBSIDIAN)));
        multiblock.add(Pair.of(new BlockPos(1, 0, -1), new BlockStack(Blocks.OBSIDIAN)));
        multiblock.add(Pair.of(new BlockPos(-1, 0, 1), new BlockStack(Blocks.OBSIDIAN)));
        multiblock.add(Pair.of(new BlockPos(-1, 0, -1), new BlockStack(Blocks.OBSIDIAN)));
    }
}
