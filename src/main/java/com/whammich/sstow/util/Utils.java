package com.whammich.sstow.util;

import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.registry.ModEnchantments;
import com.whammich.sstow.registry.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import tehnut.lib.util.TextHelper;

import javax.annotation.Nullable;
import java.util.Random;

public final class Utils {

    @Nullable
    public static ItemStack getShardFromInv(EntityPlayer player, String entity) {
        ItemStack lastResort = null;

        for (int i = 0; i <= 8; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack != null && stack.getItem() == ModItems.getItem(ItemSoulShard.class) && !hasMaxedKills(stack)) {
                if (!isShardBound(stack) && lastResort == null)
                    lastResort = stack;
                else if (getShardBoundEnt(stack).equals(entity))
                    return stack;
            }
        }

        if (lastResort != null && lastResort.stackSize > 1) {
            int counter = 0;

            ItemStack newShard = new ItemStack(ModItems.getItem(ItemSoulShard.class), 1);
            while (counter < 36) {
                ItemStack inventoryStack = player.inventory.getStackInSlot(counter);
                if (inventoryStack == null) {
                    --lastResort.stackSize;
                    player.inventory.addItemStackToInventory(newShard);
                    return player.inventory.getStackInSlot(counter);
                }
                counter++;
            }

            --lastResort.stackSize;
            if (!Utils.isShardBound(newShard)) {
                Utils.setShardBoundEnt(newShard, entity);
                Utils.writeEntityHeldItem(newShard, (EntityLiving) EntityList.createEntityByName(entity, player.worldObj));
            }

            int soulStealer = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.soulStealer.effectId, player.getHeldItem());
            soulStealer *= ConfigHandler.soulStealerBonus;
            Utils.increaseShardKillCount(newShard, (short) (1 + soulStealer));
            player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, newShard));
            return null;
        }
        return lastResort;
    }

    public static short getShardKillCount(ItemStack shard) {
        if (!shard.hasTagCompound())
            return 0;

        return (short) MathHelper.clamp_int(shard.getTagCompound().getShort("KillCount"), 0, TierHandler.getMaxKills(5));
    }

    public static void increaseShardKillCount(ItemStack shard, short amount) {
        if (!shard.hasTagCompound() || hasMaxedKills(shard))
            return;

        setShardKillCount(shard, getClampedKillCount(getShardKillCount(shard) + amount));
        checkAndFixShard(shard);
    }

    public static void checkAndFixShard(ItemStack shard) {
        if (!TierHandler.isShardValid(shard))
            setShardTier(shard, TierHandler.getCorrectTier(shard));
    }

    public static void setShardKillCount(ItemStack shard, short value) {
        if (!shard.hasTagCompound())
            shard.setTagCompound(new NBTTagCompound());

        shard.getTagCompound().setShort("KillCount", value);
    }

    public static byte getShardTier(ItemStack shard) {
        if (!shard.hasTagCompound())
            return 0;

        return (byte) MathHelper.clamp_int(shard.getTagCompound().getByte("Tier"), 0, 5);
    }

    public static void setShardTier(ItemStack shard, byte tier) {
        if (!shard.hasTagCompound())
            shard.setTagCompound(new NBTTagCompound());

        shard.getTagCompound().setByte("Tier", (byte) MathHelper.clamp_int(tier, 0, 5));
    }

    /*
     * Returns an empty string if unbound.
     */
    public static String getShardBoundEnt(ItemStack shard) {
        if (!shard.hasTagCompound())
            return "";

        return shard.getTagCompound().getString("Entity");
    }

    /*
     * Does not check if the shard is already bound!
     */
    public static void setShardBoundEnt(ItemStack shard, String value) {
        if (!shard.hasTagCompound()) {
            shard.setTagCompound(new NBTTagCompound());
            shard.getTagCompound().setDouble("antiStack", new Random().nextDouble());
        }

        shard.getTagCompound().setString("Entity", value);
    }

    public static boolean isShardBound(ItemStack shard) {
        return !getShardBoundEnt(shard).isEmpty();
    }

    public static boolean hasMaxedKills(ItemStack shard) {
        return isShardBound(shard) && getShardKillCount(shard) >= TierHandler.getMaxKills(5);
    }

    public static ItemStack setMaxedKills(ItemStack shard) {
        setShardKillCount(shard, TierHandler.getMaxKills(5));
        return shard;
    }

    public static String getEntityNameTransltated(String unlocName) {
        if (unlocName.equals("Wither Skeleton"))
            return unlocName;

        String result = TextHelper.localize("entity." + unlocName + ".name");

        if (result == null)
            return unlocName;

        return result;
    }

    private static short getClampedKillCount(int amount) {
        int value = MathHelper.clamp_int(amount, 0, TierHandler.getMaxKills(5));

        if (value > Short.MAX_VALUE)
            return Short.MAX_VALUE;

        return (short) value;
    }

    public static void writeEntityHeldItem(ItemStack shard, EntityLiving ent) {
        if (ent instanceof EntityZombie || ent instanceof EntityEnderman)
            return;

        ItemStack held = ent.getHeldItem();

        if (held != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            held.writeToNBT(nbt);

            if (nbt.hasKey("ench"))
                nbt.removeTag("ench");

            shard.getTagCompound().setTag("HeldItem", nbt);
        }
    }

    @Nullable
    public static ItemStack getEntityHeldItem(ItemStack shard) {
        if (!shard.hasTagCompound())
            return null;

        if (shard.getTagCompound().hasKey("HeldItem"))
            return ItemStack.loadItemStackFromNBT((NBTTagCompound) shard.getTagCompound().getTag("HeldItem"));

        return null;
    }

    public static void writeEntityArmor(ItemStack shard, EntityLiving ent) {
        for (int i = 1; i <= 4; i++) {
            ItemStack armor = ent.getEquipmentInSlot(i);

            if (armor != null) {
                NBTTagCompound nbt = new NBTTagCompound();
                armor.writeToNBT(nbt);

                if (nbt.hasKey("ench"))
                    nbt.removeTag("ench");

                if (shard.getTagCompound().hasKey("armor" + i)) {
                    if (shard.getTagCompound().getTag("armor" + i) != null) {
                        NBTTagCompound oldnbt = (NBTTagCompound) shard.getTagCompound().getTag("armor" + i);
                        ItemStack oldArmor = ItemStack.loadItemStackFromNBT(oldnbt);
                        if (oldArmor.getItem() != armor.getItem())
                            shard.getTagCompound().removeTag("armor" + i);
                    }
                } else {
                    shard.getTagCompound().setTag("armor" + i, nbt);
                }
            } else {
                shard.getTagCompound().removeTag("armor" + i);
            }
        }
    }

    @Nullable
    public static ItemStack getEntityArmor(ItemStack shard, int armorSlot) {
        if (shard.getTagCompound().hasKey("armor" + armorSlot) && shard.getTagCompound().getTag("armor" + armorSlot) != null) {
            NBTTagCompound oldnbt = (NBTTagCompound) shard.getTagCompound().getTag("armor" + armorSlot);
            return ItemStack.loadItemStackFromNBT(oldnbt);
        }

        return null;
    }

    public static void setShardBoundPlayer(ItemStack shard, EntityPlayer player) {
        shard.getTagCompound().setString("owner", player.getDisplayNameString());
    }

    @Nullable
    public static String getShardBoundPlayer(ItemStack shard) {
        if (!shard.hasTagCompound())
            return null;

        if (shard.getTagCompound().hasKey("owner"))
            return shard.getTagCompound().getString("owner");

        return null;
    }

    public static String localize(String key) {
        return StatCollector.translateToLocal(key);
    }

    public static int getBlockLightLevel (World world, BlockPos pos, boolean day) {
        return world.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getLightSubtracted(pos, day ? 0 : 16);
    }
}
