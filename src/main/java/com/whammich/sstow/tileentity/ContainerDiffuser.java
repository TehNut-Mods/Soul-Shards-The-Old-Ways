package com.whammich.sstow.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerDiffuser extends Container {

	private TileEntityDiffuser tileDiffuser;

	public ContainerDiffuser(InventoryPlayer invPlayer, TileEntityDiffuser tileDiffuser){
		this.tileDiffuser = tileDiffuser;
		addSlotToContainer(new Slot(tileDiffuser, 0, 56, 17));
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
		return this.tileDiffuser.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack()) {

			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 2) {
				if (!this.mergeItemStack(itemstack1, 4, 37, false)) {
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

				} else if (par2 >= 4 && par2 < 31) {
					if (!this.mergeItemStack(itemstack1, 31, 37, false)) {
						return null;
					}
				} else if (par2 >= 30 && par2 < 37 && !this.mergeItemStack(itemstack1, 4, 31, false)) {
					return null;
				}

			} else if (!this.mergeItemStack(itemstack1, 4, 37, false)) {
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