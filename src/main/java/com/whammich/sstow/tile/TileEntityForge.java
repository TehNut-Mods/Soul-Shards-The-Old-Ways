package com.whammich.sstow.tile;

import com.whammich.sstow.block.BlockForge;
import com.whammich.sstow.util.ForgeRecipeHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Getter
public class TileEntityForge extends TileInventory implements ITickable {

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int FUEL_SLOT = 2;

    @Setter
    private int forgeBurnTime;
    @Setter
    private int currentBurnTime;
    @Setter
    private int forgeCookTime;

    private ForgeRecipeHandler.ForgeRecipe forgeRecipe;

    public TileEntityForge() {
        super(3, "forge");
    }

    @Override
    public void update() {
        if (getWorld().isRemote)
            return;

        if (forgeBurnTime > 0)
            setActiveState(true);
        else
            setActiveState(false);

        for (int i = 0; i < 3; i++)
            System.out.println(i + " " + getStackInSlot(i).getDisplayName());

        if (getStackInSlot(INPUT_SLOT) == null) {
            forgeCookTime = 0;
            return;
        }

        if (!ForgeRecipeHandler.isValidInput(getStackInSlot(INPUT_SLOT)))
            return;

        if (forgeBurnTime <= 0 && getStackInSlot(FUEL_SLOT) != null) {
            forgeBurnTime = ForgeRecipeHandler.getBurnTime(getStackInSlot(FUEL_SLOT));
            getStackInSlot(FUEL_SLOT).stackSize--;
        }

        this.forgeRecipe = ForgeRecipeHandler.getRecipe(getStackInSlot(0));
        if (forgeCookTime > 0)
            forgeCookTime = forgeRecipe.getTicksRequired();
        if ((getStackInSlot(FUEL_SLOT) != null && ForgeRecipeHandler.isValidFuel(getStackInSlot(FUEL_SLOT))) || forgeBurnTime > 0) {
            if (canCraft(forgeRecipe)) {
                forgeCookTime--;
                forgeBurnTime--;

                if (forgeCookTime <= 0) {
                    if (getStackInSlot(OUTPUT_SLOT) == null)
                        setInventorySlotContents(OUTPUT_SLOT, forgeRecipe.getOutput());
                    else if (getStackInSlot(OUTPUT_SLOT).getItem() == getStackInSlot(OUTPUT_SLOT).getItem())
                        getStackInSlot(OUTPUT_SLOT).stackSize += forgeRecipe.getOutput().stackSize;

                    getStackInSlot(INPUT_SLOT).stackSize--;
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        forgeBurnTime = tagCompound.getInteger("forgeBurnTime");
        forgeCookTime = tagCompound.getInteger("forgeCookTime");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("forgeBurnTime", forgeBurnTime);
        tagCompound.setInteger("forgeCookTime", forgeCookTime);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == OUTPUT_SLOT)
            return false;
        if (index == FUEL_SLOT)
            return ForgeRecipeHandler.isValidFuel(stack);

        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale) {
        if (getStackInSlot(INPUT_SLOT) == null)
            return 0;

        if (forgeRecipe == null)
            return 0;

        return forgeCookTime * scale / forgeRecipe.getTicksRequired();
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale) {
        if (this.currentBurnTime == 0)
            this.currentBurnTime = ForgeRecipeHandler.getBurnTime(getStackInSlot(FUEL_SLOT));

        return forgeBurnTime * scale / this.currentBurnTime;
    }

    private void setActiveState(boolean active) {
        getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockForge.ACTIVE, active));
    }

    public boolean getActiveState() {
        return getWorld().getBlockState(pos).getValue(BlockForge.ACTIVE);
    }

    private boolean canCraft(ForgeRecipeHandler.ForgeRecipe recipe) {

        ItemStack outputStack = recipe.getOutput();
        ItemStack currentOutputStack = getStackInSlot(OUTPUT_SLOT);
        if (outputStack == null)
            return false;
        if (currentOutputStack == null)
            return true;
        if (!currentOutputStack.isItemEqual(outputStack))
            return false;
        int result = currentOutputStack.stackSize + outputStack.stackSize;
        return result <= getInventoryStackLimit() && result <= currentOutputStack.getMaxStackSize();
    }
}
