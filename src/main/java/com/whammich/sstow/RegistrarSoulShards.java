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
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@SuppressWarnings("ConstantConditions")
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        // Shard
        for (int i = 0; i < TierHandler.tiers.size(); i++)
            ModelLoader.registerItemVariants(SHARD, new ModelResourceLocation(SHARD.getRegistryName(), "tier=" + i));
        ModelLoader.registerItemVariants(SHARD, new ModelResourceLocation(SHARD.getRegistryName(), "tier=unbound"));
        ModelLoader.setCustomMeshDefinition(SHARD, stack -> {
            if (ShardHelper.isBound(stack))
                return new ModelResourceLocation(SHARD.getRegistryName(), "tier=" + ShardHelper.getTierFromShard(stack));

            return new ModelResourceLocation(SHARD.getRegistryName(), "tier=unbound");
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

//    @SubscribeEvent TODO - Re-enable after forge fucking stops fucking disabling it
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ResourceLocation id = new ResourceLocation(SoulShardsTOW.MODID, "corrupted_ingot");
        event.getRegistry().register(new ShapedOreRecipe(id, ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED), "CVC", "VIV", "CVC", 'C', "dustCorrupted", 'V', "dustVile", 'I', "ingotIron").setRegistryName(id));

        id = new ResourceLocation(SoulShardsTOW.MODID, "corrupted_essence");
        event.getRegistry().register(new ShapelessOreRecipe(id, ItemMaterials.getStack(ItemMaterials.CORRUPTED_ESSENCE), "gemLapis", "dustRedstone", Blocks.OBSIDIAN, Blocks.OBSIDIAN).setRegistryName(id));

        id = new ResourceLocation(SoulShardsTOW.MODID, "vile_sword");
        event.getRegistry().register(new ShapedOreRecipe(id, ItemMaterials.getStack(ItemMaterials.INGOT_CORRUPTED), "I", "I", "S", 'I', "ingotCorrupted", 'S', "stickWood").setRegistryName(id));

        id = new ResourceLocation(SoulShardsTOW.MODID, "cage");
        event.getRegistry().register(new ShapedOreRecipe(id, CAGE, "IBI", "B B", "IBI", 'I', "ingotCorrupted", 'B', Blocks.IRON_BARS).setRegistryName(id));

        id = new ResourceLocation(SoulShardsTOW.MODID, "shard_reset");
        event.getRegistry().register(new ShapelessOreRecipe(id, SHARD, SHARD));
    }
}
