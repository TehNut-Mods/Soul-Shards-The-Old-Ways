package com.whammich.sstow.item;

import com.google.common.collect.Lists;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ItemSoulShard extends Item implements ISoulShard {

    public static List<Pair<BlockPos, IBlockState>> multiblock = Lists.newArrayList();
    public static IBlockState originBlock = null;

    public ItemSoulShard() {
        super();

        setUnlocalizedName(SoulShardsTOW.MODID + ".shard");
        setCreativeTab(SoulShardsTOW.TAB_SS);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);

        if (!Utils.hasMaxedKills(stack)) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntityMobSpawner) {
                if (ConfigHandler.allowSpawnerAbsorption) {
                    WeightedSpawnerEntity spawnerEntity = ReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, ((TileEntityMobSpawner) tile).getSpawnerBaseLogic(), "field_98282_f", "spawnData", "randomEntity");
                    ResourceLocation name = new ResourceLocation(spawnerEntity.getNbt().getString("id"));
                    EntityLiving ent = (EntityLiving) EntityList.createEntityByIDFromName(name, world);

                    if (ent == null)
                        return EnumActionResult.FAIL;

                    if (!EntityMapper.isEntityValid(name) || SoulShardsAPI.isEntityBlacklisted(ent))
                        return EnumActionResult.FAIL;

                    if (ShardHelper.isBound(stack) && ShardHelper.getBoundEntity(stack).equals(name)) {
                        if (!world.isRemote)
                            Utils.increaseShardKillCount(stack, ConfigHandler.spawnerAbsorptionBonus);
                        world.destroyBlock(pos, false);
                        return EnumActionResult.SUCCESS;
                    }
                } else {
                    player.sendStatusMessage(new TextComponentTranslation("chat.sstow.absorb.disabled"), true);
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
        TierHandler.Tier maxTier = TierHandler.getTier(TierHandler.tiers.size() - 1);
        return 1.0D - ((double) ShardHelper.getKillsFromShard(stack) / (double) maxTier.getMinKills());
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + (ShardHelper.isBound(stack) ? "" : ".unbound");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return Utils.hasMaxedKills(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tabs, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tabs))
            return;

        for (int i = 0; i <= TierHandler.tiers.size() - 1; i++) {
            ItemStack stack = new ItemStack(this);

            ShardHelper.setKillsForShard(stack, TierHandler.getMinKills(i));
            ShardHelper.setTierForShard(stack, i);

            list.add(stack);
        }

        if (ConfigHandler.addShardsForAllMobs) {
            for (ResourceLocation ent : EntityMapper.entityList) {
                if (!ConfigHandler.ignoreBlacklistForTab && !EntityMapper.isEntityValid(ent))
                    continue;

                ItemStack stack = new ItemStack(this);
                Utils.setMaxedKills(stack);
                ShardHelper.setTierForShard(stack, TierHandler.tiers.size() - 1);
                ShardHelper.setBoundEntity(stack, ent);
                list.add(stack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag advanced) {
        if (ShardHelper.isBound(stack)) {
            ResourceLocation boundEnt = ShardHelper.getBoundEntity(stack);
            boolean disabled = !ConfigHandler.entityList.contains(boundEnt);
            list.add((disabled ? TextFormatting.RED.toString() : "") + I18n.format("tooltip.soulshardstow.bound", Utils.getEntityNameTranslated(boundEnt)));
        }

        if (ShardHelper.getKillsFromShard(stack) >= 0)
            list.add(I18n.format("tooltip.soulshardstow.kills", ShardHelper.getKillsFromShard(stack)));

        list.add(I18n.format("tooltip.soulshardstow.tier", ShardHelper.getTierFromShard(stack)));
    }

    public static void buildMultiblock() {
        originBlock = Blocks.GLOWSTONE.getDefaultState();
        multiblock.clear();
        multiblock.add(Pair.of(new BlockPos(0, 0, 0), Blocks.GLOWSTONE.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(1, 0, 0), Blocks.QUARTZ_BLOCK.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(-1, 0, 0), Blocks.QUARTZ_BLOCK.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(0, 0, 1), Blocks.QUARTZ_BLOCK.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(0, 0, -1), Blocks.QUARTZ_BLOCK.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(1, 0, 1), Blocks.OBSIDIAN.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(1, 0, -1), Blocks.OBSIDIAN.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(-1, 0, 1), Blocks.OBSIDIAN.getDefaultState()));
        multiblock.add(Pair.of(new BlockPos(-1, 0, -1), Blocks.OBSIDIAN.getDefaultState()));
    }
}
