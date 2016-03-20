package com.whammich.sstow.compat.bloodmagic;

import WayofTime.bloodmagic.api.network.SoulNetwork;
import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.sstow.ConfigHandler;
import com.whammich.sstow.api.event.CageSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerBloodMagic {

    @SubscribeEvent
    @Used
    public void onCageSpawn(CageSpawnEvent event) {
        SoulNetwork soulNetwork = NetworkHelper.getSoulNetwork(event.getOwner());

        if (soulNetwork.getCurrentEssence() < ConfigHandler.lpPerMob) {
            soulNetwork.causeNauseaToPlayer();
            event.setCanceled(true);
            return;
        }

        soulNetwork.syphon(ConfigHandler.lpPerMob);
    }
}
