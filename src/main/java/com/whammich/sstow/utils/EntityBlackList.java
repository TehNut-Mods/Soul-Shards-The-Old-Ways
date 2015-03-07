package com.whammich.sstow.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import com.whammich.sstow.utils.EntityMapper;

public class EntityBlackList {

	public static List<String> bList = new ArrayList<String>();

	public static void init(File configFile) {
		Configuration config = new Configuration(configFile);

		try {
			config.load();
			Iterator<String> iter = EntityMapper.entityList.iterator();
			while (iter.hasNext()) {
				String name = iter.next();
				boolean val = config.get("Entity Blacklist", name, false)
						.getBoolean(false);
				if (val)
					bList.add(name);
			}
			ModLogger.logInfo("Soul Shards Loaded Entity Blacklist");
		} catch (Exception e) {
			ModLogger
					.logFatal("Soul Shards had a problem loading Entity Blacklist");
			e.printStackTrace();
		} finally {
			if (config.hasChanged())
				config.save();
		}
	}
}
