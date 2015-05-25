package com.whammich.sstow.events;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.whammich.sstow.entity.EntityHarmlessLightningBolt;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CreateConservoEvent {

	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent event) {

		if (event.world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		if (event.entityPlayer.getHeldItem() == null 
				|| event.entityPlayer.getHeldItem().getItem() != Register.baubleGems 
				|| event.entityPlayer.getHeldItem().getItemDamage() != 0 
				|| !event.entityPlayer.isSneaking()) {
			return;
		}

		if (event.world.getBlock(event.x, event.y, event.z) != Blocks.ender_chest) {
			return;
		}

		if (checkStructure(event.world, event.x, event.y, event.z)) {
			if (!event.entityPlayer.capabilities.isCreativeMode) {
				event.entityPlayer.getHeldItem().stackSize--;
			}

			event.world.func_147480_a(event.x, event.y, event.z, false);

			ForgeDirection dir = ForgeDirection.getOrientation(event.face);

			event.world.addWeatherEffect(new EntityHarmlessLightningBolt(event.world, event.x, event.y, event.z));
			for (int dx = -2; dx <= 2; dx +=4) {
				for (int dz = -2; dz <= 2; dz +=4) {
					event.world.addWeatherEffect(new EntityHarmlessLightningBolt(event.world, event.x + dx, event.y + 3, event.z + dz));
				}
			}
			
			
			event.world.spawnEntityInWorld(new EntityItem(event.world,
					event.x + (dir.offsetX * 1.75D), event.y
					+ (dir.offsetY * 1.75D) + 0.5D, event.z
					+ (dir.offsetZ * 1.75D), new ItemStack(Register.baubleGems, 1, 3)));
		}
	}

	private boolean checkStructure(World world, int x, int y, int z) {
		ForgeDirection[] VALID_DIRECTIONS = new ForgeDirection[] {
				ForgeDirection.NORTH, ForgeDirection.SOUTH,
				ForgeDirection.EAST, ForgeDirection.WEST };
 
		for (ForgeDirection dir : VALID_DIRECTIONS) {
			int newX = x + dir.offsetX;
			int newZ = z + dir.offsetZ;
 
			if (world.getBlock(newX, y, newZ) != Register.BlockMaterials 
					|| world.getBlockMetadata(newX, y, newZ) != 1) {
				return false;
			}
 
			if (world.getBlock(newX + dir.offsetX, y, newZ + dir.offsetZ) != Register.BlockXenolith 
					|| world.getBlockMetadata(newX + dir.offsetX, y, newZ + dir.offsetZ) != 0) {
				return false;
			}
 
		}
		
		for (int dx = -2; dx <= 2; dx +=4) {
			for (int dz = -2; dz <= 2; dz +=4) {
				for (int dy = 0; dy < 2; dy++) {
					if(world.getBlock(x + dx, y + dy, z + dz) != Register.BlockXenolith 
						|| world.getBlockMetadata(x + dx, y + dy, z + dz) != 7) {
					return false;
					}
				}
				
				if(world.getBlock(x + dx, y + 2, z + dz)  != Register.BlockXenolith 
						|| world.getBlockMetadata(x + dx, y + 2, z + dz) != 6) {
					return false;
				}
				
				if (world.getBlock(x + dx/2, y, z + dz/2) != Register.BlockXenolith
						|| world.getBlockMetadata(x + dx/2, y, z + dz/2) != 0) {
					return false;
				}
			}
		}
		return true;
	}

}
