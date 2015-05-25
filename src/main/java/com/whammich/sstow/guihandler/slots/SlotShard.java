package com.whammich.sstow.guihandler.slots;

import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotShard extends Slot {

	public SlotShard(IInventory inventory, int slot, int xCoord, int yCoord) {
		super(inventory, slot, xCoord, yCoord);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return (stack.getItem() == Register.ItemShardSoul
				&& stack.getItem() == Register.ItemShardSoul
				&& Utils.isShardBound(stack) && Utils.getShardTier(stack) > 0);
	}

}
