package com.whammich.sstow.compat.hardmode;

import com.whammich.repack.tehnut.lib.annot.Used;
import com.whammich.sstow.api.ShardHelper;
import com.whammich.sstow.api.event.CageSpawnEvent;
import com.whammich.sstow.api.event.ShardTierChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerHardMode {

    @SubscribeEvent
    @Used
    public void onCageSpawn(CageSpawnEvent event) {
        int currentKills = ShardHelper.getKillsFromShard(event.getShard());
        if (currentKills <= 0) {
            event.setCanceled(true);
            return;
        }

        ShardHelper.setKillsForShard(event.getShard(), currentKills - 1);
    }

    @SubscribeEvent
    @Used
    public void onTierChange(ShardTierChangeEvent event) {
        if (event.getNewTier() < ShardHelper.getTierFromShard(event.getShardStack()))
            event.setCanceled(true);
    }
}
