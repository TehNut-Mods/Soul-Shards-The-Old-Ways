package com.whammich.sstow;

import com.whammich.sstow.block.BlockCage;
import com.whammich.sstow.enchantment.EnchantmentSoulStealer;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.item.ItemSoulSword;
import com.whammich.sstow.tile.TileEntityCage;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber
public class RegistrarSoulShards {

    public static final Item.ToolMaterial MATERIAL_SOUL = EnumHelper.addToolMaterial("SOUL", 2, 250, 6.0F, 2.0F, 14);

    public static final BlockCage CAGE = new BlockCage();

    public static final ItemSoulShard SHARD = new ItemSoulShard();
    public static final ItemMaterials MATERIALS = new ItemMaterials();
    public static final ItemSoulSword VILE_SWORD = new ItemSoulSword();

    public static final Enchantment SOUL_STEALER = new EnchantmentSoulStealer();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(CAGE.setRegistryName("cage"));
        GameRegistry.registerTileEntity(TileEntityCage.class, CAGE.getRegistryName().toString());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(SHARD.setRegistryName("soul_shard"));
        event.getRegistry().register(MATERIALS.setRegistryName("materials"));
        event.getRegistry().register(VILE_SWORD.setRegistryName("vile_sword"));
        event.getRegistry().register(new ItemBlock(CAGE).setRegistryName(CAGE.getRegistryName()));

        OreDictionary.registerOre(ItemMaterials.INGOT_CORRUPTED, ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
        OreDictionary.registerOre(ItemMaterials.CORRUPTED_ESSENCE, ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE));
        OreDictionary.registerOre(ItemMaterials.DUST_VILE, ItemMaterials.getStack(ItemMaterials.DUST_VILE));

        MATERIAL_SOUL.setRepairItem(ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED));
    }

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().register(SOUL_STEALER.setRegistryName("soul_stealer"));
    }

    // TODO - Recipes like this when Forge updates
//    @SubscribeEvent
//    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
//
//    }
}
