package com.whammich.sstow.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

public class Entitylist {

	public static List<String> wList = new ArrayList<String>();

	public static void init(File configFile) {
		Configuration config = new Configuration(configFile);

		try {
			config.load();
			Iterator<String> iter = EntityMapper.entityList.iterator();
			while (iter.hasNext()) {
				String name = iter.next();
				boolean val = config.get("entitylist", name, true).getBoolean(true);
				if (val){
					wList.add(name);
				}
			}
			ModLogger.logInfo(Utils.localize("chat.sstow.util.entitylistload"));
		} catch (Exception e) {
			ModLogger.logFatal(Utils.localize("chat.sstow.util.entitylistloadfail"));
			e.printStackTrace();
		} finally {
			if (config.hasChanged())
				config.save();
		}
	}
}
