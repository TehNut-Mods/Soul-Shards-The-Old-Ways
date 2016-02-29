package com.whammich.sstow.client.gui;

import com.whammich.sstow.tile.TileEntityForge;
import com.whammich.sstow.tile.container.ContainerForge;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

@NoArgsConstructor
public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0)
            return new ContainerForge(player.inventory, (TileEntityForge) world.getTileEntity(new BlockPos(x, y, z)));

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0)
            return new GuiForge(player.inventory, (TileEntityForge) world.getTileEntity(new BlockPos(x, y, z)));

        return null;
    }
}