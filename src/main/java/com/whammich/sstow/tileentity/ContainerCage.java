package com.whammich.sstow.tileentity;

import com.whammich.sstow.guihandler.slots.SlotLocked;
import com.whammich.sstow.guihandler.slots.SlotModule;
import com.whammich.sstow.guihandler.slots.SlotShard;
import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCage extends Container {

	public final TileEntityCage tileCage;

	private static final int INV_START = 6,
			INV_END = INV_START + 26, HOTBAR_START = INV_END + 1,
			HOTBAR_END = HOTBAR_START + 8;

	public ContainerCage(InventoryPlayer player, TileEntityCage TileEntityCage) {
		this.tileCage = TileEntityCage;
		this.addSlotToContainer(new SlotShard(TileEntityCage, 0, 17, 17));
		this.addSlotToContainer(new SlotModule(TileEntityCage, 1, 36, 17));
		if (Config.redstoneModule) {
		this.addSlotToContainer(new SlotLocked(TileEntityCage, 2, 70, 17));}
		if (Config.lightModule) {
		this.addSlotToContainer(new SlotLocked(TileEntityCage, 3, 88, 17));}
		if (Config.dimensionModule) {
		this.addSlotToContainer(new SlotLocked(TileEntityCage, 4, 106, 17));}
		if (Config.playerModule) {
		this.addSlotToContainer(new SlotLocked(TileEntityCage, 5, 124, 17));}
//		if (Config.Module_CONTROL){
//		this.addSlotToContainer(new SlotLocked(TileEntityCage, 6, 152, 17));}
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileCage.isUseableByPlayer(player);
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int slots) {
		ItemStack stack = null;
		Slot slot = (Slot) this.inventorySlots.get(slots);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slots < INV_START) {
				if (!this.mergeItemStack(stack1, INV_START, HOTBAR_END,
						true)) {
					return null;
				}
				slot.onSlotChange(stack1, stack);
			} else {
				if (slots >= INV_START) {
					if 
					(stack.getItem() == Register.ItemShardSoul
							&& Utils.isShardBound(stack) 
							&& Utils.getShardTier(stack) > 0
							&& !this.mergeItemStack(stack1, 0, 0, false)){
						return null;
					} else if (stack.getItem() == Register.ItemModules
							&& !this.mergeItemStack(stack1, 1, 1, false))
							{
						return null;}
				} else if (slots >= HOTBAR_START && slots < HOTBAR_END + 1) {
					if (!this.mergeItemStack(stack1, INV_START, INV_END + 1,
							false)) {
						return null;
					}
				}
			}

			if (stack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (stack1.stackSize == stack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, stack1);
		}

		return stack;
	}
}