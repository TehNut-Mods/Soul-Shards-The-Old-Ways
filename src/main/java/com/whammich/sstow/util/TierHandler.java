package com.whammich.sstow.util;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

public class TierHandler {

    public static Map<Integer, Tier> tiers = new HashMap<Integer, Tier>();
    public static final Tier BLANK_TIER = new TierHandler.Tier(0, 0, true, false, false, 0, 0);

    public static Tier getTier(int tier) {
        return tiers.get(tier) == null ? BLANK_TIER : tiers.get(tier);
    }

    public static int getMinKills(int tier) {
        return getTier(tier).getMinKills();
    }

    public static int getMaxKills(int tier) {
        return getTier(tier).getMaxKills();
    }

    public static int getSpawnAmount(int tier) {
        return getTier(tier).getSpawnAmount();
    }

    public static int getCooldown(int tier) {
        return getTier(tier).getCooldown();
    }

    public static boolean checksPlayer(int tier) {
        return getTier(tier).isCheckPlayer();
    }

    public static boolean checksLight(int tier) {
        return getTier(tier).isCheckLight();
    }

    public static boolean checksRedstone(int tier) {
        return getTier(tier).isCheckRedstone();
    }

    @RequiredArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Tier {
        private final int minKills;
        private final int maxKills;
        private final boolean checkPlayer;
        private final boolean checkLight;
        private final boolean checkRedstone;
        private final int spawnAmount;
        private final int cooldown;
    }
}
