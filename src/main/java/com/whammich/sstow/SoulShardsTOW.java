package com.whammich.sstow;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.commands.CommandSSTOW;
import com.whammich.sstow.proxy.CommonProxy;
import com.whammich.sstow.registry.*;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.IMCHandler;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = SoulShardsTOW.MODID, name = SoulShardsTOW.NAME, version = SoulShardsTOW.VERSION, guiFactory = "com.whammich.sstow.client.gui.GuiFactory")
public class SoulShardsTOW {

    public static final String MODID = "soulshardstow";
    public static final String NAME = "Soul Shards - The Old Ways";
    public static final String VERSION = "@VERSION@";
    public static final Logger LOGGER = LogManager.getLogger("Soul Shards");

    @Instance(MODID)
    public static SoulShardsTOW INSTANCE;
    @SidedProxy(clientSide = "com.whammich.sstow.proxy.ClientProxy", serverSide = "com.whammich.sstow.proxy.CommonProxy")
    public static CommonProxy PROXY;

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

    private File configDir;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "sstow");
        ConfigHandler.init(new File(configDir, "SoulShards.cfg"));
        JsonConfigHandler.initShard(new File(configDir, "ShardTiers.json"));
        JsonConfigHandler.initMultiblock(new File(configDir, "Multiblock.json"));

        ModRecipes.init(); // TODO - Remove for new Forge stuff

        PROXY.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        EntityMapper.mapEntities();

        PROXY.postInit(event);
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