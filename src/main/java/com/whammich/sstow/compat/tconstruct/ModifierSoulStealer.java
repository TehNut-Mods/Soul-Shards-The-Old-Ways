package com.whammich.sstow.compat.tconstruct;

import com.whammich.sstow.util.Utils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class ModifierSoulStealer extends ModifierTrait {

    public ModifierSoulStealer() {
        super("soulStealer", 0x7A10A7, 5, 0);

        addAspects(ModifierAspect.weaponOnly);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (wasHit) {
            if (target.getHealth() <= 0.01) {
                String entName = EntityList.getEntityString(target);
                ItemStack shardStack = Utils.getShardFromInv((EntityPlayer) player, entName);
                ModifierNBT data = new ModifierNBT(TinkerUtil.getModifierTag(tool, getModifierIdentifier()));

                if (shardStack != null)
                    Utils.increaseShardKillCount(shardStack, data.level);
            }
        }
    }
}