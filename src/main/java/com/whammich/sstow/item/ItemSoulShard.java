package com.whammich.sstow.item;

import com.google.common.base.Strings;
import com.whammich.repack.tehnut.lib.util.BlockStack;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.registry.ModEnchantments;
import com.whammich.sstow.registry.ModItems;
import com.whammich.sstow.util.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.repack.tehnut.lib.annot.ModItem;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.repack.tehnut.lib.util.TextHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ModItem(name = "ItemSoulShard")
@Used
@Handler
public class ItemSoulShard extends Item {

    private static Map<BlockPos, BlockStack> multiblock = new HashMap<BlockPos, BlockStack>();

    public ItemSoulShard() {
        super();

        setUnlocalizedName(SoulShardsTOW.MODID + ".shard");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!Utils.hasMaxedKills(stack) && ConfigHandler.allowSpawnerAbsorption) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntityMobSpawner) {
                String name = ObfuscationReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, ((TileEntityMobSpawner) tile).getSpawnerBaseLogic(), "mobID");
                Entity ent = EntityMapper.getNewEntityInstance(world, name, pos);

                if (ent == null)
                    return false;

                if (ent instanceof EntitySkeleton && ((EntitySkeleton) ent).getSkeletonType() == 1)
                    name = "Wither Skeleton";

                if (!EntityMapper.isEntityValid(name))
                    return false;

                if (Utils.isShardBound(stack) && Utils.getShardBoundEnt(stack).equals(name)) {
                    if (!world.isRemote)
                        Utils.increaseShardKillCount(stack, (short) ConfigHandler.spawnerAbsorptionBonus);
                    world.destroyBlock(pos, false);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + (Utils.isShardBound(stack) ? "" : ".unbound");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return Utils.hasMaxedKills(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (int i = 0; i <= 5; i++) {
            ItemStack stack = new ItemStack(item, 1);

            Utils.setShardKillCount(stack, TierHandler.getMinKills(i));
            Utils.setShardTier(stack, (byte) i);

            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {
        if (Utils.isShardBound(stack))
            list.add(TextHelper.localizeEffect("tooltip.SoulShardsTOW.bound", Utils.getEntityNameTranslated(Utils.getShardBoundEnt(stack))));

        if (Utils.getShardKillCount(stack) >= 0)
            list.add(TextHelper.localizeEffect("tooltip.SoulShardsTOW.kills", Utils.getShardKillCount(stack)));

        list.add(TextHelper.localizeEffect("tooltip.SoulShardsTOW.tier", Utils.getShardTier(stack)));
    }

    private void buildMultiblock() {
        multiblock.put(new BlockPos(0, 0, 0), new BlockStack(Blocks.glowstone));
        multiblock.put(new BlockPos(1, 0, 0), new BlockStack(Blocks.end_stone));
        multiblock.put(new BlockPos(0, 0, 1), new BlockStack(Blocks.end_stone));
        multiblock.put(new BlockPos(0, 0, -1), new BlockStack(Blocks.end_stone));
        multiblock.put(new BlockPos(-1, 0, 0), new BlockStack(Blocks.end_stone));
        multiblock.put(new BlockPos(1, 0, 1), new BlockStack(Blocks.obsidian));
        multiblock.put(new BlockPos(-1, 0, -1), new BlockStack(Blocks.obsidian));
        multiblock.put(new BlockPos(-1, 0, 1), new BlockStack(Blocks.obsidian));
        multiblock.put(new BlockPos(1, 0, -1), new BlockStack(Blocks.obsidian));
    }

    @SubscribeEvent
    public void onEntityKill(LivingDeathEvent event) {
        World world = event.entity.worldObj;

        if (world.isRemote || !(event.entity instanceof EntityLiving) || !(event.source.getEntity() instanceof EntityPlayer) || event.source.getEntity() instanceof FakePlayer)
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
            if (!Utils.isShardBound(shard))
                Utils.setShardBoundEnt(shard, entName);

            int soulStealer = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.soulStealer.effectId, player.getHeldItem());
            soulStealer *= ConfigHandler.soulStealerBonus;

            if (player.getHeldItem() != null && player.getHeldItem().getItem() == ModItems.getItem(ItemSoulSword.class))
                soulStealer += 1;

            Utils.increaseShardKillCount(shard, (short) (1 + soulStealer));
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (multiblock.isEmpty())
            buildMultiblock();

        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == Items.diamond && event.world.getBlockState(event.pos).getBlock() == Blocks.glowstone) {
            for (BlockPos multiPos : multiblock.keySet()) {
                BlockStack worldStack = BlockStack.getStackFromPos(event.world, event.pos.add(multiPos));
                if (!multiblock.get(multiPos).equals(worldStack))
                    return;
            }

            for (BlockPos multiPos : multiblock.keySet())
                event.world.destroyBlock(event.pos.add(multiPos), false);

            if (!event.world.isRemote) {
                EntityItem invItem = new EntityItem(event.world, event.entityPlayer.posX, event.entityPlayer.posY + 0.25, event.entityPlayer.posZ, new ItemStack(ModItems.getItem(getClass()), 1, 0));
                event.world.spawnEntityInWorld(invItem);
            }
            if (!event.entityPlayer.capabilities.isCreativeMode)
                event.entityPlayer.getHeldItem().stackSize--;
            event.entityPlayer.swingItem();
        }
    }
}
