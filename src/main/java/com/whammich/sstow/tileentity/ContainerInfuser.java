package com.whammich.sstow.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerInfuser extends Container {

	private TileEntityInfuser tileInfuser;

	public ContainerInfuser(InventoryPlayer invPlayer, TileEntityInfuser tileInfuser){
		this.tileInfuser = tileInfuser;
		addSlotToContainer(new Slot(tileInfuser, 0, 56, 17));
		bindPlayerInventory(invPlayer);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileInfuser.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack()) {

			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 2) {
				if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);

			} else if (par2 != 1 && par2 != 0) {

				if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {

					if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
						return null;
					}

				} else if (TileEntityForge.isItemFuel(itemstack1)) {

					if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
						return null;
					}

				} else if (par2 >= 2 && par2 < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
						return null;
					}
				} else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
					return null;
				}

			} else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {

				slot.putStack((ItemStack) null);

			} else {

				slot.onSlotChanged();

			}
			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);

		}
		return itemstack;
	}

}