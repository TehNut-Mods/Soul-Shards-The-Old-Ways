package com.whammich.sstow;

import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.commands.CommandSSTOW;
import com.whammich.sstow.item.ItemSoulShard;
import com.whammich.sstow.proxy.CommonProxy;
import com.whammich.sstow.registry.*;
import com.whammich.sstow.util.*;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.*;
import com.whammich.repack.tehnut.lib.annot.Handler;
import com.whammich.repack.tehnut.lib.annot.ModBlock;
import com.whammich.repack.tehnut.lib.annot.ModItem;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.repack.tehnut.lib.iface.ICompatibility;
import com.whammich.repack.tehnut.lib.util.LogHelper;

import java.io.File;
import java.util.Set;

@Getter
@Mod(modid = SoulShardsTOW.MODID, name = SoulShardsTOW.NAME, version = SoulShardsTOW.VERSION, guiFactory = "com.whammich.sstow.client.gui.GuiFactory")
public class SoulShardsTOW {

    public static final String MODID = "SoulShardsTOW";
    public static final String NAME = "SoulShardsTOW";
    public static final String VERSION = "@VERSION@";

    @Instance(MODID)
    public static SoulShardsTOW instance;

    @SidedProxy(clientSide = "com.whammich.sstow.proxy.ClientProxy", serverSide = "com.whammich.sstow.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs soulShardsTab = new CreativeTabs("soulShards") {
        @Override
        public Item getTabIconItem() {
            return ModItems.getItem(ItemSoulShard.class);
        }

        @Override
        public ItemStack getIconItemStack() {
            ItemStack shard = new ItemStack(ModItems.getItem(ItemSoulShard.class));
            ShardHelper.setTierForShard(shard, 5);
            Utils.setMaxedKills(shard);
            ShardHelper.setBoundEntity(shard, "Pig");
            return shard;
        }
    };

    private File configDir;
    private LogHelper logHelper;
    private Set<ASMDataTable.ASMData> modItems;
    private Set<ASMDataTable.ASMData> modBlocks;
    private Set<ASMDataTable.ASMData> eventHandlers;

    @Mod.EventHandler
    @Used
    public void preInit(FMLPreInitializationEvent event) {
        logHelper = new LogHelper(event.getModLog());
        configDir = new File(event.getModConfigurationDirectory(), "sstow");
        ConfigHandler.init(new File(configDir, "SoulShards.cfg"));

        modItems = event.getAsmData().getAll(ModItem.class.getCanonicalName());
        modBlocks = event.getAsmData().getAll(ModBlock.class.getCanonicalName());
        eventHandlers = event.getAsmData().getAll(Handler.class.getCanonicalName());

        ModItems.init();
        ModBlocks.init();
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