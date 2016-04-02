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
import com.whammich.sstow.util.PosWithStack;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    public static List<PosWithStack> multiblock = new ArrayList<PosWithStack>();
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
                    name = "Wither Skeleton";

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
        if (ShardHelper.isBound(stack))
            list.add(TextHelper.localizeEffect("tooltip.soulshardstow.bound", Utils.getEntityNameTranslated(ShardHelper.getBoundEntity(stack))));

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
        originBlock = new BlockStack(Blocks.glowstone);
        multiblock.clear();
        multiblock.add(new PosWithStack(new BlockPos(0, 0, 0), new BlockStack(Blocks.glowstone)));
        multiblock.add(new PosWithStack(new BlockPos(1, 0, 0), new BlockStack(Blocks.quartz_block)));
        multiblock.add(new PosWithStack(new BlockPos(-1, 0, 0), new BlockStack(Blocks.quartz_block)));
        multiblock.add(new PosWithStack(new BlockPos(0, 0, 1), new BlockStack(Blocks.quartz_block)));
        multiblock.add(new PosWithStack(new BlockPos(0, 0, -1), new BlockStack(Blocks.quartz_block)));
        multiblock.add(new PosWithStack(new BlockPos(1, 0, 1), new BlockStack(Blocks.obsidian)));
        multiblock.add(new PosWithStack(new BlockPos(1, 0, -1), new BlockStack(Blocks.obsidian)));
        multiblock.add(new PosWithStack(new BlockPos(-1, 0, 1), new BlockStack(Blocks.obsidian)));
        multiblock.add(new PosWithStack(new BlockPos(-1, 0, -1), new BlockStack(Blocks.obsidian)));
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

        if (dead instanceof EntitySkeleton && ((EntitySkeleton) dead).getSkeletonType() == 1)
            entName = "Wither Skeleton";

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
    public void onInteract(PlayerInteractEvent event) {
        if (multiblock.isEmpty())
            buildMultiblock();

        // TODO - Change back to `PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK` when Forge re-implements
        if (event.getAction() != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
            return;

        if (event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.diamond && originBlock.equals(BlockStack.getStackFromPos(event.getWorld(), event.getPos()))) {
            for (PosWithStack posWithStack : multiblock) {
                BlockStack worldStack = BlockStack.getStackFromPos(event.getWorld(), event.getPos().add(posWithStack.getPos()));
                if (!posWithStack.getBlock().equals(worldStack))
                    return;
            }

            for (PosWithStack posWithStack : multiblock)
                event.getWorld().destroyBlock(event.getPos().add(posWithStack.getPos()), false);

            if (!event.getWorld().isRemote) {
                EntityItem invItem = new EntityItem(event.getWorld(), event.getEntityPlayer().posX, event.getEntityPlayer().posY + 0.25, event.getEntityPlayer().posZ, new ItemStack(ItemHelper.getItem(getClass()), 1, 0));
                event.getWorld().spawnEntityInWorld(invItem);
            }

            if (!event.getEntityPlayer().capabilities.isCreativeMode) {
                if (event.getEntityPlayer().getHeldItemMainhand().stackSize > 1)
                    event.getEntityPlayer().getHeldItemMainhand().stackSize--;
                else
                    event.getEntityPlayer().setHeldItem(EnumHand.MAIN_HAND, null);
            }

            event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
        }
    }
}
