package com.whammich.sstow;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.commands.CommandSSTOW;
import com.whammich.sstow.item.ItemMaterials;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.IMCHandler;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = SoulShardsTOW.MODID, name = SoulShardsTOW.NAME, version = SoulShardsTOW.VERSION, guiFactory = "com.whammich.sstow.client.gui.GuiFactory")
public class SoulShardsTOW {

    public static final String MODID = "soulshardstow";
    public static final String NAME = "Soul Shards - The Old Ways";
    public static final String VERSION = "@VERSION@";
    public static final Logger LOGGER = LogManager.getLogger("Soul Shards");
    public static final CreativeTabs TAB_SS = new CreativeTabs("soulShards") {
        @Override
        public ItemStack getTabIconItem() {
            ItemStack shard = new ItemStack(RegistrarSoulShards.SHARD);
            ShardHelper.setTierForShard(shard, TierHandler.tiers.size() - 1);
            Utils.setMaxedKills(shard);
            ShardHelper.setBoundEntity(shard, new ResourceLocation("minecraft", "pig"));
            return shard;
        }
    };

    @Instance(MODID)
    public static SoulShardsTOW INSTANCE;

    private File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "sstow");
        if (!configDir.exists())
            configDir.mkdirs();

        ConfigHandler.init(new File(configDir, "SoulShards.cfg"));
        JsonConfigHandler.initShard(new File(configDir, "ShardTiers.json"));
        GameRegistry.addSmelting(Blocks.SOUL_SAND, ItemMaterials.getStack(ItemMaterials.DUST_VILE), 0.4F);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        EntityMapper.mapEntities();

        JsonConfigHandler.initMultiblock(new File(configDir, "Multiblock.json"));
    }

    @EventHandler
    public void onIMCRecieved(FMLInterModComms.IMCEvent event) {
        IMCHandler.handleIMC(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSSTOW());
    }

    public static void debug(String message, Object... args) {
        if (ConfigHandler.debugLogging)
            LOGGER.info("[DEBUG] " + message, args);
    }
}