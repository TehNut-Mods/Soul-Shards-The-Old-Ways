package com.whammich.sstow.proxies;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import com.whammich.sstow.entity.mob.hostile.EntityZombieWitch;
import com.whammich.sstow.entity.mob.hostile.render.RenderZombieWitch;
import com.whammich.sstow.entity.particle.EntityPurpleFlameFX;
import com.whammich.sstow.renderer.RenderSoulAnvil;
import com.whammich.sstow.renderer.RenderSoulCage;
import com.whammich.sstow.renderer.RenderSoulCrystal;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.tileentity.TileEntitySoulAnvil;
import com.whammich.sstow.tileentity.TileEntitySoulCrystal;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	public void load(){
		registerRenderers();
	}
	
	public static void registerRenderers() {
		// Zombie Witch
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieWitch.class, new RenderZombieWitch());

		// Soul Anvil		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoulAnvil.class, new RenderSoulAnvil());
		
		// Soul Crystal
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoulCrystal.class, new RenderSoulCrystal());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Register.BlockSoulCrystal), new RenderSoulCrystal());
		
		// Soul Cage
		TileEntitySpecialRenderer cageRender = new RenderSoulCage();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCage.class, cageRender);
		
		
	}

	public static void addPurpleFlameEffects(World world, double x, double y, double z, double movX, double movY, double movZ, int red, int green, int blue) {
		double d0 = (double)((float) x + world.rand.nextFloat());
        double d1 = (double)((float) y + world.rand.nextFloat());
        double d2 = (double)((float) z + world.rand.nextFloat());
	
		EntityPurpleFlameFX particle = new EntityPurpleFlameFX(world, x, y, z, d0, d1, d2, red, green, blue);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
	}
}
