package com.whammich.sstow.tile.container;

import com.whammich.sstow.tile.TileEntityForge;
import com.whammich.sstow.util.ForgeRecipeHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerForge extends Container {

    private TileEntityForge tileFurnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerForge(InventoryPlayer player, TileEntityForge tileEntityFurnace) {
        tileFurnace = tileEntityFurnace;
        addSlotToContainer(new Slot(tileEntityFurnace, 0, 56, 17));
        addSlotToContainer(new Slot(tileEntityFurnace, 1, 56, 53));
        addSlotToContainer(new SlotFurnaceOutput(player.player, tileEntityFurnace, 2, 116, 35));

        int i;
        for (i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (i = 0; i < 9; ++i)
            addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
    }

    @Override
    public void onCraftGuiOpened(ICrafting craft) {
        super.onCraftGuiOpened(craft);
        craft.sendProgressBarUpdate(this, 0, tileFurnace.getForgeCookTime());
        craft.sendProgressBarUpdate(this, 1, tileFurnace.getForgeBurnTime());
        craft.sendProgressBarUpdate(this, 2, tileFurnace.getForgeCookTime());
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (ICrafting crafter : crafters) {
            if (lastCookTime != tileFurnace.getForgeCookTime())
                crafter.sendProgressBarUpdate(this, 0, tileFurnace.getForgeCookTime());

            if (lastBurnTime != tileFurnace.getForgeBurnTime())
                crafter.sendProgressBarUpdate(this, 1, tileFurnace.getForgeBurnTime());

            if (lastItemBurnTime != tileFurnace.getCurrentBurnTime())
                crafter.sendProgressBarUpdate(this, 2, tileFurnace.getCurrentBurnTime());
        }

        lastBurnTime = tileFurnace.getForgeBurnTime();
        lastCookTime = tileFurnace.getForgeCookTime();
        lastItemBurnTime = tileFurnace.getCurrentBurnTime();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        if (id == 0)
            tileFurnace.setForgeCookTime(data);

        if (id == 1)
            tileFurnace.setForgeBurnTime(data);

        if (id == 2)
            tileFurnace.setCurrentBurnTime(data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileFurnace.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
        ItemStack slotStack = null;
        Slot slot = inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack input = slot.getStack();
            slotStack = input.copy();

            if (par2 == 2) {
                if (!mergeItemStack(input, 3, 39, true))
                    return null;
                
                slot.onSlotChange(input, slotStack);
            } else if (par2 != 1 && par2 != 0) {
                if (ForgeRecipeHandler.getOutput(input) != null) {
                    if (!mergeItemStack(input, 0, 1, false))
                        return null;
                } else if (ForgeRecipeHandler.isValidFuel(input)) {
                    if (!mergeItemStack(input, 1, 2, false))
                        return null;
                } else if (par2 >= 3 && par2 < 30) {
                    if (!mergeItemStack(input, 30, 39, false))
                        return null;
                } else if (par2 >= 30 && par2 < 39 && !mergeItemStack(input, 3, 30, false)) {
                    return null;
                }
            } else if (!mergeItemStack(input, 3, 39, false)) {
                return null;
            }
            
            if (input.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
            
            if (input.stackSize == slotStack.stackSize)
                return null;
            
            slot.onPickupFromSlot(player, input);
        }
        
        return slotStack;
    }
}
