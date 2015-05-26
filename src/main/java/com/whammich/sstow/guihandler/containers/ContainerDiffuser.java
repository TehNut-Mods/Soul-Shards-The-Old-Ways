package com.whammich.sstow.guihandler.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import com.whammich.sstow.tileentity.TileEntityDiffuser;

public class ContainerDiffuser extends Container {

	private TileEntityDiffuser tileDiffuser;

	public ContainerDiffuser(InventoryPlayer invPlayer, TileEntityDiffuser tileDiffuser){
		this.tileDiffuser = tileDiffuser;
		addSlotToContainer(new Slot(tileDiffuser, 0, 80, 19));
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

}