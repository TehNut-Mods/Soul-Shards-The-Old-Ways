package com.whammich.sstow.item;

import com.google.common.base.Strings;
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
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.repack.tehnut.lib.annot.ModItem;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.repack.tehnut.lib.util.TextHelper;

import java.util.List;

@ModItem(name = "ItemSoulShard")
@Used
@Handler
public class ItemSoulShard extends Item {

    public ItemSoulShard() {
        super();

        setUnlocalizedName(SoulShardsTOW.MODID + ".shard");
        setCreativeTab(SoulShardsTOW.soulShardsTab);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote || (Utils.hasMaxedKills(stack)) || !ConfigHandler.allowSpawnerAbsorption)
            return stack;

        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return stack;

        TileEntity tile = world.getTileEntity(mop.getBlockPos());

        if (tile instanceof TileEntityMobSpawner) {
            String name = ObfuscationReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, ((TileEntityMobSpawner) tile).getSpawnerBaseLogic(), "mobID");
            Entity ent = EntityMapper.getNewEntityInstance(world, name, mop.getBlockPos());

            if (ent == null)
                return stack;

            if (ent instanceof EntitySkeleton && ((EntitySkeleton) ent).getSkeletonType() == 1)
                name = "Wither Skeleton";

            if (!EntityMapper.isEntityValid(name))
                return stack;

            if (Utils.isShardBound(stack) && Utils.getShardBoundEnt(stack).equals(name)) {
                Utils.increaseShardKillCount(stack, (short) ConfigHandler.spawnerAbsorptionBonus);
                world.destroyBlock(mop.getBlockPos(), false);
            }
        }

        return stack;
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
}
