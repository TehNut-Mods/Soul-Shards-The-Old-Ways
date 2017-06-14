package com.whammich.sstow.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class TierHandler {

    public static final Tier BLANK_TIER = new TierHandler.Tier(0, 0, true, false, false, 0, 0);
    public static Map<Integer, Tier> tiers = new HashMap<Integer, Tier>();
    public static int maxTier = 0;

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

    public static class Tier {
        private final int minKills;
        private final int maxKills;
        private final boolean checkPlayer;
        private final boolean checkLight;
        private final boolean checkRedstone;
        private final int spawnAmount;
        private final int cooldown;

        public Tier(int minKills, int maxKills, boolean checkPlayer, boolean checkLight, boolean checkRedstone, int spawnAmount, int cooldown) {
            this.minKills = minKills;
            this.maxKills = maxKills;
            this.checkPlayer = checkPlayer;
            this.checkLight = checkLight;
            this.checkRedstone = checkRedstone;
            this.spawnAmount = spawnAmount;
            this.cooldown = cooldown;
        }

        public int getMinKills() {
            return minKills;
        }

        public int getMaxKills() {
            return maxKills;
        }

        public boolean isCheckPlayer() {
            return checkPlayer;
        }

        public boolean isCheckLight() {
            return checkLight;
        }

        public boolean isCheckRedstone() {
            return checkRedstone;
        }

        public int getSpawnAmount() {
            return spawnAmount;
        }

        public int getCooldown() {
            return cooldown;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("minKills", minKills)
                    .append("maxKills", maxKills)
                    .append("checkPlayer", checkPlayer)
                    .append("checkLight", checkLight)
                    .append("checkRedstone", checkRedstone)
                    .append("spawnAmount", spawnAmount)
                    .append("cooldown", cooldown)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tier)) return false;

            Tier tier = (Tier) o;

            if (minKills != tier.minKills) return false;
            if (maxKills != tier.maxKills) return false;
            if (checkPlayer != tier.checkPlayer) return false;
            if (checkLight != tier.checkLight) return false;
            if (checkRedstone != tier.checkRedstone) return false;
            if (spawnAmount != tier.spawnAmount) return false;
            return cooldown == tier.cooldown;
        }

        @Override
        public int hashCode() {
            int result = minKills;
            result = 31 * result + maxKills;
            result = 31 * result + (checkPlayer ? 1 : 0);
            result = 31 * result + (checkLight ? 1 : 0);
            result = 31 * result + (checkRedstone ? 1 : 0);
            result = 31 * result + spawnAmount;
            result = 31 * result + cooldown;
            return result;
        }
    }
}
