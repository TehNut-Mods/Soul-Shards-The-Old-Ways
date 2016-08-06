package com.whammich.sstow.item;

import com.google.common.base.Strings;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.SoulShardsTOW;
import com.whammich.sstow.api.ISoulShard;
import com.whammich.sstow.api.ISoulWeapon;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.SoulShardsAPI;
import com.whammich.sstow.registry.ModEnchantments;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
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
import net.minecraft.util.text.TextComponentString;
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
import tehnut.lib.annot.Handler;
import tehnut.lib.annot.ModItem;
import tehnut.lib.annot.Used;
import tehnut.lib.iface.IMeshProvider;
import tehnut.lib.util.BlockStack;
import tehnut.lib.util.helper.ItemHelper;
import tehnut.lib.util.helper.TextHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@ModItem(name = "ItemSoulShard")
@Used
@Handler
public class ItemSoulShard extends Item implements ISoulShard, IMeshProvider {

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
        if (!Utils.hasMaxedKills(stack)) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntityMobSpawner) {
                if (ConfigHandler.allowSpawnerAbsorption) {
                    WeightedSpawnerEntity spawnerEntity = ObfuscationReflectionHelper.getPrivateValue(MobSpawnerBaseLogic.class, ((TileEntityMobSpawner) tile).getSpawnerBaseLogic(), "randomEntity", "field_98282_f");
                    String name = spawnerEntity.getNbt().getString("id");
                    EntityLiving ent = (EntityLiving) EntityList.createEntityByName(name, world);

                    if (ent == null)
                        return EnumActionResult.FAIL;

                    if (!EntityMapper.isEntityValid(name) || SoulShardsAPI.isEntityBlacklisted(ent))
                        return EnumActionResult.FAIL;

                    if (ent instanceof EntitySkeleton) {
                        switch (((EntitySkeleton) ent).getSkeletonType()) {
                            case STRAY: name = SoulShardsAPI.HUSK; break;
                            case WITHER: name = SoulShardsAPI.WITHER_SKELETON; break;
                        }
                    }

                    if (ent instanceof EntityZombie) {
                        switch (((EntityZombie) ent).getZombieType()) {
                            case HUSK: name = SoulShardsAPI.HUSK; break;
                        }
                    }

                    if (ShardHelper.isBound(stack) && ShardHelper.getBoundEntity(stack).equals(name)) {
                        if (!world.isRemote)
                            Utils.increaseShardKillCount(stack, ConfigHandler.spawnerAbsorptionBonus);
                        world.destroyBlock(pos, false);
                        return EnumActionResult.SUCCESS;
                    }
                } else {
                    if (world.isRemote)
                        player.addChatComponentMessage(new TextComponentString(TextHelper.localizeEffect("chat.sstow.absorb.disabled")));
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
            ItemStack stack = new ItemStack(item);

            ShardHelper.setKillsForShard(stack, TierHandler.getMinKills(i));
            ShardHelper.setTierForShard(stack, i);

            list.add(stack);
        }

        if (ConfigHandler.addShardsForAllMobs) {
            for (String ent : EntityMapper.entityList) {
                if (ent.equals(SoulShardsAPI.WITHER_SKELETON_OLD))
                    continue;

                if (!ConfigHandler.ignoreBlacklistForTab && !EntityMapper.isEntityValid(ent))
                    continue;

                ItemStack stack = new ItemStack(item);
                Utils.setMaxedKills(stack);
                ShardHelper.setTierForShard(stack, TierHandler.tiers.size() - 1);
                ShardHelper.setBoundEntity(stack, ent);
                list.add(stack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {
        if (ShardHelper.isBound(stack)) {
            String boundEnt = ShardHelper.getBoundEntity(stack);
            boolean disabled;
            if (!EntityMapper.specialCases.contains(boundEnt))
                disabled = !ConfigHandler.entityList.contains(boundEnt) || SoulShardsAPI.isEntityBlacklisted(EntityList.NAME_TO_CLASS.get(boundEnt).getCanonicalName());
            else
                disabled = !ConfigHandler.entityList.contains(boundEnt);
            list.add((disabled ? TextFormatting.RED.toString() : "") + TextHelper.localizeEffect("tooltip.soulshardstow.bound", Utils.getEntityNameTranslated(boundEnt)));
        }

        if (ShardHelper.getKillsFromShard(stack) >= 0)
            list.add(TextHelper.localizeEffect("tooltip.soulshardstow.kills", ShardHelper.getKillsFromShard(stack)));

        list.add(TextHelper.localizeEffect("tooltip.soulshardstow.tier", ShardHelper.getTierFromShard(stack)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getMeshDefinition() {
        return new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                if (ShardHelper.isBound(stack))
                    return new ModelResourceLocation(new ResourceLocation("soulshardstow:item/ItemSoulShard"), "tier=" + ShardHelper.getTierFromShard(stack));

                return new ModelResourceLocation(new ResourceLocation("soulshardstow:item/ItemSoulShard"), "tier=unbound");
            }
        };
    }

    @Override
    public List<String> getVariants() {
        List<String> ret = new ArrayList<String>();
        ret.add("tier=unbound");
        for (int i = 0; i < TierHandler.tiers.size(); i++)
            ret.add("tier=" + i);
        return ret;
    }

    @Nullable
    @Override
    public ResourceLocation getCustomLocation() {
        return null;
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

    @SubscribeEvent
    @Used
    public void onEntityKill(LivingDeathEvent event) {
        World world = event.getEntity().worldObj;

        if (world.isRemote || !(event.getEntity() instanceof EntityLiving) || !(event.getSource().getEntity() instanceof EntityPlayer) || event.getSource().getEntity() instanceof FakePlayer)
            return;

        EntityLiving dead = (EntityLiving) event.getEntity();
        EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
        String entName = EntityList.getEntityString(dead);

        if (Strings.isNullOrEmpty(entName)) {
            SoulShardsTOW.instance.getLogHelper().severe("Player killed an entity with no mapped name: {}", dead);
            return;
        }

        if (dead instanceof EntitySkeleton) {
            switch (((EntitySkeleton) dead).getSkeletonType()) {
                case STRAY: entName = SoulShardsAPI.STRAY; break;
                case WITHER: entName = SoulShardsAPI.WITHER_SKELETON; break;
            }
        }

        if (dead instanceof EntityZombie) {
            switch (((EntityZombie) dead).getZombieType()) {
                case HUSK: entName = SoulShardsAPI.HUSK; break;
            }
        }

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

    @SubscribeEvent
    @Used
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        if (multiblock.isEmpty())
            buildMultiblock();

        if (event.getItemStack() != null && ItemStack.areItemsEqual(event.getItemStack(), ConfigHandler.catalystItem) && originBlock.equals(BlockStack.getStackFromPos(event.getWorld(), event.getPos()))) {
            for (Pair<BlockPos, BlockStack> multiblockPair : multiblock) {
                BlockStack worldStack = BlockStack.getStackFromPos(event.getWorld(), event.getPos().add(multiblockPair.getLeft()));
                if (!multiblockPair.getRight().equals(worldStack))
                    return;
            }

            for (Pair<BlockPos, BlockStack> multiblockPair : multiblock)
                event.getWorld().destroyBlock(event.getPos().add(multiblockPair.getLeft()), false);

            if (!event.getWorld().isRemote) {
                EntityItem invItem = new EntityItem(event.getWorld(), event.getEntityPlayer().posX, event.getEntityPlayer().posY + 0.25, event.getEntityPlayer().posZ, new ItemStack(ItemHelper.getItem(getClass()), 1, 0));
                event.getWorld().spawnEntityInWorld(invItem);
            }

            if (!event.getEntityPlayer().capabilities.isCreativeMode) {
                if (event.getItemStack().stackSize > 1)
                    event.getItemStack().stackSize--;
                else
                    event.getEntityPlayer().setHeldItem(event.getHand(), null);
            }

            event.getEntityPlayer().swingArm(event.getHand());
            event.setCanceled(true);
        }
    }
}
