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
import net.minecraft.entity.item.EntityItem;
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
import tehnut.lib.annot.Handler;
import tehnut.lib.annot.ModItem;
import tehnut.lib.annot.Used;
import tehnut.lib.util.TextHelper;

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
            Entity ent = EntityMapper.getNewEntityInstance(world, name);

            if (ent == null)
                return stack;

            if (ent instanceof EntitySkeleton && ((EntitySkeleton) ent).getSkeletonType() == 1)
                name = "Wither Skeleton";

            if (!EntityMapper.isEntityValid(name))
                return stack;

            if (Utils.isShardBound(stack)) {
                if (Utils.getShardBoundEnt(stack).equals(name)) {
                    Utils.increaseShardKillCount(stack, (short) ConfigHandler.spawnerAbsorptionBonus);
                    world.destroyBlock(mop.getBlockPos(), false);
                }
            } else if (EntityMapper.isEntityValid(name)) {
                if (stack.stackSize > 1) {
                    stack.stackSize--;
                    ItemStack newStack = new ItemStack(ModItems.getItem(ItemSoulShard.class));
                    Utils.setShardBoundEnt(newStack, name);
                    Utils.writeEntityHeldItem(newStack, (EntityLiving) ent);
                    Utils.increaseShardKillCount(newStack, (short) ConfigHandler.spawnerAbsorptionBonus);

                    boolean emptySpot = false;
                    int counter = 0;

                    while (!emptySpot && counter < 36) {
                        ItemStack inventoryStack = player.inventory.getStackInSlot(counter);
                        if (inventoryStack == null) {
                            player.inventory.addItemStackToInventory(newStack);
                            emptySpot = true;
                        }
                        counter++;
                    }

                    if (!emptySpot)
                        player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, newStack));

                } else {
                    Utils.setShardBoundEnt(stack, name);
                    Utils.writeEntityHeldItem(stack, (EntityLiving) ent);
                    Utils.increaseShardKillCount(stack, (short) ConfigHandler.spawnerAbsorptionBonus);
                }
                world.destroyBlock(mop.getBlockPos(), true);
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
            list.add(TextHelper.localizeEffect("tooltip.SoulShardsTOW.bound", Utils.getEntityNameTransltated(Utils.getShardBoundEnt(stack))));

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
            if (!Utils.isShardBound(shard)) {
                Utils.setShardBoundEnt(shard, entName);
                Utils.writeEntityHeldItem(shard, dead);
                Utils.setShardBoundPlayer(shard, player);
            }

            Utils.writeEntityArmor(shard, dead);

            int soulStealer = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.soulStealer.effectId, player.getHeldItem());
            soulStealer *= ConfigHandler.soulStealerBonus;

            if (player.getHeldItem() != null && player.getHeldItem().getItem() == ModItems.getItem(ItemSoulSword.class))
                soulStealer += 1;

            Utils.increaseShardKillCount(shard, (short) (1 + soulStealer));
        }
    }
}
