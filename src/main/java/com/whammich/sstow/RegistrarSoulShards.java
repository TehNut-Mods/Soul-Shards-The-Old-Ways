package com.whammich.sstow;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.item.ItemSoulSword;
import com.whammich.sstow.tile.TileEntityCage;
import com.whammich.sstow.util.TierHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("ConstantConditions")
@Mod.EventBusSubscriber(modid = SoulShardsTOW.MODID)
@GameRegistry.ObjectHolder(SoulShardsTOW.MODID)
public class RegistrarSoulShards {

    private static final Item.ToolMaterial MATERIAL_SOUL = EnumHelper.addToolMaterial("SOUL", 2, 250, 6.0F, 2.0F, 14);

    public static final Block CAGE = Blocks.AIR;

    public static final Item SOUL_SHARD = Items.AIR;
    public static final Item MATERIALS = Items.AIR;
    public static final Item VILE_SWORD = Items.AIR;

    public static final Enchantment SOUL_STEALER = null;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockCage().setRegistryName("cage"));
        GameRegistry.registerTileEntity(TileEntityCage.class, CAGE.getRegistryName().toString());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemSoulShard().setRegistryName("soul_shard"));
        event.getRegistry().register(new ItemMaterials().setRegistryName("materials"));
        event.getRegistry().register(new ItemSoulSword().setRegistryName("vile_sword"));
        event.getRegistry().register(new ItemBlock(CAGE).setRegistryName(CAGE.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().register(new EnchantmentSoulStealer().setRegistryName("soul_stealer"));
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        GameRegistry.addSmelting(Blocks.SOUL_SAND, ItemMaterials.getStack(ItemMaterials.DUST_VILE), 0.4F);

        OreDictionary.registerOre(ItemMaterials.INGOT_CORRUPTED, ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
        OreDictionary.registerOre(ItemMaterials.CORRUPTED_ESSENCE, ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE));
        OreDictionary.registerOre(ItemMaterials.DUST_VILE, ItemMaterials.getStack(ItemMaterials.DUST_VILE));

        MATERIAL_SOUL.setRepairItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        // Shard
        for (int i = 0; i < TierHandler.tiers.size(); i++)
            ModelLoader.registerItemVariants(SOUL_SHARD, new ModelResourceLocation(SOUL_SHARD.getRegistryName(), "tier=" + i));
        ModelLoader.registerItemVariants(SOUL_SHARD, new ModelResourceLocation(SOUL_SHARD.getRegistryName(), "tier=unbound"));
        ModelLoader.setCustomMeshDefinition(SOUL_SHARD, stack -> {
            if (ShardHelper.isBound(stack))
                return new ModelResourceLocation(SOUL_SHARD.getRegistryName(), "tier=" + ShardHelper.getTierFromShard(stack));

            return new ModelResourceLocation(SOUL_SHARD.getRegistryName(), "tier=unbound");
        });

        // Sword
        ModelLoader.setCustomModelResourceLocation(VILE_SWORD, 0, new ModelResourceLocation(VILE_SWORD.getRegistryName(), "inventory"));

        // Materials
        ModelLoader.setCustomModelResourceLocation(MATERIALS, 0, new ModelResourceLocation(MATERIALS.getRegistryName(), "type=ingotsoulium"));
        ModelLoader.setCustomModelResourceLocation(MATERIALS, 1, new ModelResourceLocation(MATERIALS.getRegistryName(), "type=dustcorrupted"));
        ModelLoader.setCustomModelResourceLocation(MATERIALS, 2, new ModelResourceLocation(MATERIALS.getRegistryName(), "type=dustvile"));

        // Cage
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CAGE), 0, new ModelResourceLocation(CAGE.getRegistryName(), "active=false"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CAGE), 1, new ModelResourceLocation(CAGE.getRegistryName(), "active=true"));
    }

    public static Item.ToolMaterial getMaterialSoul() {
        return MATERIAL_SOUL;
    }
}
