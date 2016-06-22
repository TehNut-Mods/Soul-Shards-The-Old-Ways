package com.whammich.sstow.compat.tconstruct;

import com.whammich.sstow.util.Utils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitVile extends AbstractTrait {

    public TraitVile() {
        super("vile", 0x7A10A7);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (wasHit) {
            if (target.getHealth() <= 0.01) {
                String entName = EntityList.getEntityString(target);
                ItemStack shardStack = Utils.getShardFromInv((EntityPlayer) player, entName);

                if (shardStack != null)
                    Utils.increaseShardKillCount(shardStack, 1);
            }
        }
    }
}
