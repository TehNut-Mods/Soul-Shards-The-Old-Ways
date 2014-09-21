package ssr.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import ssr.SSRCore;

public class SoulConfig 
{
	//public static boolean autoID;
	public static boolean disallowMobs;
	public static boolean canAbsorbSpawners;
	public static boolean easyVanillaAbsorb;
	public static boolean requireOwnerOnline;
	public static boolean exceedMaxNumSpawns;
	public static boolean enableEndStoneRecipe;
	public static int vanillaBonus;
	public static int soulStealerID;
	public static int soulStealerWeight;
	public static int maxNumSpawns;
	public static int coolDown[] = new int[5];
	public static int numMobs[] = new int[5];
	public static int killReq[] = new int[5];
	public static boolean[] enableRS = new boolean[5];
	public static boolean[] needPlayer = new boolean[5];
	public static boolean[] checkLight = new boolean[5];
	public static boolean[] otherWorlds = new boolean[5];
	
	public static void init(File configFile)
	{
		Configuration config = new Configuration(configFile);
		
		try
		{
			config.load();
			disallowMobs = config.get("Misc", "Set soul shards to accept only peaceful mobs (will be ignored if set to allow non vanilla mobs)", false).getBoolean(false);
			canAbsorbSpawners = config.get("Misc", "Allow levelling up shards by absorbing vanilla spawners", true).getBoolean(true);
			easyVanillaAbsorb = config.get("Misc", "Allow absorbing of vanilla spawners even if the entity in the spawner is not equal to the entity bound to the shard", false).getBoolean(false); 
			requireOwnerOnline = config.get("Misc", "Require the owner of the Soul Cage to be online for it to spawn mobs", false).getBoolean(false);
			vanillaBonus = config.get("Misc", "Bonus amount from absorbing a vanilla spawner", 200).getInt(200);
			exceedMaxNumSpawns = config.get("Misc", "Exceed hard-coded maximum number of spawns as set by user configuration", false).getBoolean(false);
			maxNumSpawns = config.get("Misc", "The max amount of mobs spawned by Soul Cages that can be alive at once (setting this to 0 sets it to unlimited)", 80).getInt(80);
			soulStealerID = config.get("IDs", "Soul Stealer enchant ID", 95).getInt();
			soulStealerWeight = config.get("Misc", "The weight (probability) of the Soul Stealer enchant", 5).getInt(5);
			enableEndStoneRecipe = config.get("Misc", "Enable End Stone recipe", false).getBoolean(false);
			
			coolDown[0] = config.get("Tier 1 Settings", "Cool-down (in seconds)", 20).getInt(20);
			numMobs[0] = config.get("Tier 1 Settings", "Number of mobs to spawn", 2).getInt(2);
			enableRS[0] = config.get("Tier 1 Settings", "Responds to redstone signal", false).getBoolean(false);
			needPlayer[0] = config.get("Tier 1 Settings", "Needs a player within 16 blocks", true).getBoolean(true);
			checkLight[0] = config.get("Tier 1 Settings", "Check if light level is appropriate for the Mob", true).getBoolean(true);
			otherWorlds[0] = config.get("Tier 1 Settings", "Check if the dimension is appropriate for the Mob", true).getBoolean(true);
			killReq[0] = config.get("Tier 1 Settings", "Amount of kills required", 64).getInt(64);
			coolDown[1] = config.get("Tier 2 Settings", "Cool-down (in seconds)", 10).getInt(10);
			numMobs[1] = config.get("Tier 2 Settings", "Number of mobs to spawn", 4).getInt(4);
			enableRS[1] = config.get("Tier 2 Settings", "Responds to redstone signal", false).getBoolean(false);
			needPlayer[1] = config.get("Tier 2 Settings", "Needs a player within 16 blocks", true).getBoolean(true);
			checkLight[1] = config.get("Tier 2 Settings", "Check if light level is appropriate for the Mob", true).getBoolean(true);
			otherWorlds[1] = config.get("Tier 2 Settings", "Check if the dimension is appropriate for the Mob", true).getBoolean(true);
			killReq[1] = config.get("Tier 2 Settings", "Amount of kills required (must be larger than Tier 1 requirement)", 128).getInt(128);
			coolDown[2] = config.get("Tier 3 Settings", "Cool-down (in seconds)", 5).getInt(5);
			numMobs[2] = config.get("Tier 3 Settings", "Number of mobs to spawn", 4).getInt(4);
			enableRS[2] = config.get("Tier 3 Settings", "Responds to redstone signal", false).getBoolean(false);
			needPlayer[2] = config.get("Tier 3 Settings", "Needs a player within 16 blocks", false).getBoolean(false);
			checkLight[2] = config.get("Tier 3 Settings", "Check if light level is appropriate for the Mob", false).getBoolean(false);
			otherWorlds[2] = config.get("Tier 3 Settings", "Check if the dimension is appropriate for the Mob", true).getBoolean(true);
			killReq[2] = config.get("Tier 3 Settings", "Amount of kills required (must be larger than Tier 2 requirement)", 256).getInt(256);
			coolDown[3] = config.get("Tier 4 Settings", "Cool-down (in seconds)", 5).getInt(5);
			numMobs[3] = config.get("Tier 4 Settings", "Number of mobs to spawn", 4).getInt(4);
			enableRS[3] = config.get("Tier 4 Settings", "Responds to redstone signal", false).getBoolean(false);
			needPlayer[3] = config.get("Tier 4 Settings", "Needs a player within 16 blocks", false).getBoolean(false);
			checkLight[3] = config.get("Tier 4 Settings", "Check if light level is appropriate for the Mob", true).getBoolean(true);
			otherWorlds[3] = config.get("Tier 4 Settings", "Check if the dimension is appropriate for the Mob", false).getBoolean(false);
			killReq[3] = config.get("Tier 4 Settings", "Amount of kills required (must be larger than Tier 3 requirement)", 512).getInt(512);
			coolDown[4] = config.get("Tier 5 Settings", "Cool-down (in seconds)", 2).getInt(2);
			numMobs[4] = config.get("Tier 5 Settings", "Number of mobs to spawn", 6).getInt(6);
			enableRS[4] = config.get("Tier 5 Settings", "Responds to redstone signal", true).getBoolean(true);
			needPlayer[4] = config.get("Tier 5 Settings", "Needs a player within 16 blocks", false).getBoolean(false);
			checkLight[4] = config.get("Tier 5 Settings", "Check if light level is appropriate for the Mob", false).getBoolean(false);
			otherWorlds[4] = config.get("Tier 5 Settings", "Check if the dimension is appropriate for the Mob", false).getBoolean(false);
			killReq[4] = config.get("Tier 5 Settings", "Amount of kills required (must be larger than Tier 4 requirement)", 1024).getInt(1024);
			
			if (maxNumSpawns > 150)
				maxNumSpawns = 80;
			
			for (int i = 0; i < coolDown.length; i++)
			{
				if (coolDown[i] < 2)
					coolDown[i] = 2;
				if (coolDown[i] > 60)
					coolDown[i] = 60;
			}
			
			for (int i = 0; i < numMobs.length; i++)
			{
				if (numMobs[i] < 1)
					numMobs[i] = 1;

				if(exceedMaxNumSpawns)
					if (numMobs[i] > maxNumSpawns)
						numMobs[i] = maxNumSpawns;
				else
					if (numMobs[i] > 6)
						numMobs[i] = 6;
			}
			
			if (soulStealerWeight > 10 || soulStealerWeight < 1)
				soulStealerWeight = 5;
			
			SSRCore.SoulLog.info("SSR: Loaded Main configuration file.");
		}
		catch(Exception e)
		{
			SSRCore.SoulLog.warn("Soul-Shards Reborn had a problem loading it's configuration files.");
			e.printStackTrace();
		}
		finally
		{
			if (config.hasChanged())
				config.save();
		}		
	}
}
