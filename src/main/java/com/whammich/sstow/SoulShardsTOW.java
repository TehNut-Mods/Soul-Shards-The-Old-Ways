package com.whammich.sstow;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.commands.CommandSSTOW;
import com.whammich.sstow.compat.ICompatibility;
import com.whammich.sstow.proxy.CommonProxy;
import com.whammich.sstow.registry.*;
import com.whammich.sstow.util.EntityMapper;
import com.whammich.sstow.util.IMCHandler;
import com.whammich.sstow.util.TierHandler;
import com.whammich.sstow.util.Utils;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Getter
@Mod(modid = SoulShardsTOW.MODID, name = SoulShardsTOW.NAME, version = SoulShardsTOW.VERSION, dependencies = SoulShardsTOW.DEPEND, updateJSON = SoulShardsTOW.JSON_CHECKER, guiFactory = "com.whammich.sstow.client.gui.GuiFactory")
public class SoulShardsTOW {

    public static final String MODID = "soulshardstow";
    public static final String NAME = "Soul Shards - The Old Ways";
    public static final String VERSION = "@VERSION@";
    public static final String DEPEND = "required-after:Forge@[12.16.0.1840,);";
    public static final String JSON_CHECKER = "https://gist.githubusercontent.com/TehNut/e8db2be209d32d1ebbc3/raw/VersionChecker-SSTOW.json";

    @Instance(MODID)
    public static SoulShardsTOW instance;

    @SidedProxy(clientSide = "com.whammich.sstow.proxy.ClientProxy", serverSide = "com.whammich.sstow.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs soulShardsTab = new CreativeTabs("soulShards") {
        @Override
        public Item getTabIconItem() {
            return ModObjects.shard;
        }

        @Override
        public ItemStack getIconItemStack() {
            ItemStack shard = new ItemStack(ModObjects.shard);
            ShardHelper.setTierForShard(shard, TierHandler.tiers.size() - 1);
            Utils.setMaxedKills(shard);
            ShardHelper.setBoundEntity(shard, "Pig");
            return shard;
        }
    };

    private File configDir;
    private Logger logHelper = LogManager.getLogger("SoulShardsTOW");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "sstow");
        ConfigHandler.init(new File(configDir, "SoulShards.cfg"));
        JsonConfigHandler.initShard(new File(configDir, "ShardTiers.json"));
        JsonConfigHandler.initMultiblock(new File(configDir, "Multiblock.json"));

        ModObjects.initItems();
        ModObjects.initBlocks();
        ModRecipes.init();
        ModEnchantments.init();
        ModCompatibility.registerModCompat();
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.PRE_INIT);

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.INIT);

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        EntityMapper.init();
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.POST_INIT);

        proxy.postInit(event);
    }

    @EventHandler
    public void onIMCRecieved(FMLInterModComms.IMCEvent event) {
        IMCHandler.handleIMC(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSSTOW());
    }
}