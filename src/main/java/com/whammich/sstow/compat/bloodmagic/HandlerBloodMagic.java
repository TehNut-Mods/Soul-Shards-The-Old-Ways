package com.whammich.sstow.compat.bloodmagic;

import WayofTime.bloodmagic.api.saving.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.api.event.CageSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tehnut.lib.annot.Used;

public class HandlerBloodMagic {

    @SubscribeEvent
    @Used
    public void onCageSpawn(CageSpawnEvent event) {
        SoulNetwork soulNetwork = NetworkHelper.getSoulNetwork(event.getOwner());

        if (soulNetwork.getCurrentEssence() < ConfigHandler.lpPerMob) {
            soulNetwork.causeNausea();
            event.setCanceled(true);
            return;
        }

        soulNetwork.syphon(ConfigHandler.lpPerMob);
    }
}
