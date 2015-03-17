package com.whammich.sstow;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;

import com.whammich.sstow.commands.CommandSSTOW;
import com.whammich.sstow.events.AchievementEvents;
import com.whammich.sstow.events.Achievements;
import com.whammich.sstow.events.CreateShardEvent;
import com.whammich.sstow.events.PlayerKillEntityEvent;
import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.EntityBlackList;
import com.whammich.sstow.utils.EntityMapper;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GuiFactory_class)
public class SSTheOldWays {

	@Instance(Reference.MOD_ID)
	public static SSTheOldWays modInstance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		Register.registerObjs();
		// System.out.println("Achievements Loading");
		Achievements.Get();
		// System.out.println("Achievements Loaded");
		MinecraftForge.EVENT_BUS.register(new PlayerKillEntityEvent());
		MinecraftForge.EVENT_BUS.register(new CreateShardEvent());
		// System.out.println("Registering Achievement Events");
		FMLCommonHandler.instance().bus().register(new AchievementEvents());
		// System.out.println("Achievement Events Registed");
		FMLInterModComms.sendMessage("Waila", "register",
				Reference.Waila_callBack);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		EntityMapper.init();
		EntityBlackList
				.init(new File(Config.configDirectory + "/BlackList.cfg"));
	}
	
	@EventHandler
	public void onMissingMapping(FMLMissingMappingsEvent event){
		for (MissingMapping m : event.get()){
			if (m.type == GameRegistry.Type.ITEM && m.name.contains("sstow_soul_sword")) {
				m.remap(Register.ItemSwordSoul);
			} else if (m.type == GameRegistry.Type.ITEM && m.name.contains("sstow_soul_pickaxe")) {
				m.remap(Register.ItemPickaxeSoul);
			} else if (m.type == GameRegistry.Type.ITEM && m.name.contains("sstow_soul_axe")) {
				m.remap(Register.ItemAxeSoul);
			} else if (m.type == GameRegistry.Type.ITEM && m.name.contains("sstow_soul_hoe")) {
				m.remap(Register.ItemHoeSoul);
			} else if (m.type == GameRegistry.Type.ITEM && m.name.contains("sstow_soul_spade")) {
				m.remap(Register.ItemSpadeSoul);
			} else if (m.type == GameRegistry.Type.ITEM && m.name.contains("sstow_materials")) {
				m.remap(Register.ItemMaterials);
			}else if (m.type == GameRegistry.Type.ITEM && m.name.contains("sstow_soul_shard")) {
				m.remap(Register.ItemShardSoul);
			}
		}
	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSSTOW());

	}
}
