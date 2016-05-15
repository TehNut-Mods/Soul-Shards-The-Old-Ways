package com.whammich.sstow.client;

import com.whammich.sstow.iface.IOwnableTile;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tehnut.lib.annot.Handler;

@Handler(client = true)
public class ClientEventHandler {

    @SubscribeEvent
    public void onDebugMenu(RenderGameOverlayEvent.Text event) {
        if (!Minecraft.getMinecraft().gameSettings.showDebugInfo)
            return;

        RayTraceResult rayTrace = Minecraft.getMinecraft().objectMouseOver;
        if (!Minecraft.getMinecraft().thePlayer.isSneaking()) {
            event.getLeft().add(TextFormatting.ITALIC + "Sneak for Ownable information");
            return;
        }

        if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
            TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(rayTrace.getBlockPos());
            if (tileEntity instanceof IOwnableTile) {
                IOwnableTile ownableTile = (IOwnableTile) tileEntity;
                if (ownableTile.getOwnerUUID() != null) {
                    event.getLeft().add("Owner UUID: " + ownableTile.getOwnerUUID());
                    event.getLeft().add("Owner Name: " + Minecraft.getMinecraft().getIntegratedServer().getEntityFromUuid(ownableTile.getOwnerUUID()).getName());
                }
            }
        }
    }
}
