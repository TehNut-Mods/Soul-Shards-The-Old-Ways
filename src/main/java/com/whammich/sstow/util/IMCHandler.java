package com.whammich.sstow.util;

import com.whammich.sstow.api.SoulShardsAPI;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class IMCHandler {

    public static void handleIMC(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages())
            if (message.key.equals("blacklistEntity") && message.isStringMessage())
                SoulShardsAPI.blacklistEntity(message.getStringValue());
    }
}
