package com.whammich.sstow;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.commands.CommandSSTOW;
import com.whammich.sstow.item.ItemSoulShard;
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
import tehnut.lib.LendingLibrary;
import tehnut.lib.annot.Used;
import tehnut.lib.iface.ICompatibility;
import tehnut.lib.util.helper.ItemHelper;
import tehnut.lib.util.helper.LogHelper;

import java.io.File;

@Getter
@Mod(modid = SoulShardsTOW.MODID, name = SoulShardsTOW.NAME, version = SoulShardsTOW.VERSION, updateJSON = SoulShardsTOW.JSON_CHECKER, guiFactory = "com.whammich.sstow.client.gui.GuiFactory")
public class SoulShardsTOW {

    public static final String MODID = "soulshardstow";
    public static final String NAME = "SoulShardsTOW";
    public static final String VERSION = "@VERSION@";
    public static final String JSON_CHECKER = "https://gist.githubusercontent.com/TehNut/e8db2be209d32d1ebbc3/raw/4c408d53b6fee1509705fe2e7ce6d827669d373d/VersionChecker-SSTOW.json";

    @Instance(MODID)
    public static SoulShardsTOW instance;

    @SidedProxy(clientSide = "com.whammich.sstow.proxy.ClientProxy", serverSide = "com.whammich.sstow.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs soulShardsTab = new CreativeTabs("soulShards") {
        @Override
        public Item getTabIconItem() {
            return ItemHelper.getItem(ItemSoulShard.class);
        }

        @Override
        public ItemStack getIconItemStack() {
            ItemStack shard = new ItemStack(ItemHelper.getItem(ItemSoulShard.class));
            ShardHelper.setTierForShard(shard, TierHandler.tiers.size() - 1);
            Utils.setMaxedKills(shard);
            ShardHelper.setBoundEntity(shard, "Pig");
            return shard;
        }
    };

    private File configDir;
    private LogHelper logHelper;
    private final LendingLibrary library;

    public SoulShardsTOW() {
        this.library = new LendingLibrary(MODID);
    }

    @Mod.EventHandler
    @Used
    public void preInit(FMLPreInitializationEvent event) {
        logHelper = new LogHelper(event.getModLog());
        configDir = new File(event.getModConfigurationDirectory(), "sstow");
        ConfigHandler.init(new File(configDir, "SoulShards.cfg"));
        JsonConfigHandler.initShard(new File(configDir, "ShardTiers.json"));
        JsonConfigHandler.initMultiblock(new File(configDir, "Multiblock.json"));

        getLibrary().registerObjects(event);

        ModItems.init();
        ModRecipes.init();
        ModEnchantments.init();
        ModCompatibility.registerModCompat();
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.PRE_INIT);

        proxy.preInit(event);
    }

    @EventHandler
    @Used
    public void init(FMLInitializationEvent event) {
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.INIT);

        proxy.init(event);
    }

    @EventHandler
    @Used
    public void postInit(FMLPostInitializationEvent event) {
        EntityMapper.init();
        ModCompatibility.loadCompat(ICompatibility.InitializationPhase.POST_INIT);

        proxy.postInit(event);
    }

    @EventHandler
    @Used
    public void onIMCRecieved(FMLInterModComms.IMCEvent event) {
        IMCHandler.handleIMC(event);
    }

    @EventHandler
    @Used
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSSTOW());
    }
}