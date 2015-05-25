package com.whammich.sstow.guihandler.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.whammich.sstow.utils.Register;

public class SlotModule extends Slot {

	public SlotModule(IInventory inventory, int slot, int xCoord, int yCoord) {
		super(inventory, slot, xCoord, yCoord);
	}

	@Override
	public boolean isItemValid(ItemStack stack){
		return (stack.getItem() == Register.ItemModules);
	}
	
}
