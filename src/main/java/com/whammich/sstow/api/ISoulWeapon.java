package com.whammich.sstow.api;

import net.minecraft.item.ItemStack;

public interface ISoulWeapon {

    /**
     * Provides the amount of bonus souls (not counting Soul Stealer) that the
     * weapon provides per kill.
     *
     * @param stack - The weapon ItemStack
     * @return - The amount of bonus souls provided by the weapon.
     */
    int getBonusSouls(ItemStack stack);
}
