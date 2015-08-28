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

public class CreateShardEvent {

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

		if (event.world.getBlock(event.x, event.y, event.z) != Blocks.glowstone) {
			return;
		}

		if (checkHorizontal(event.world, event.x, event.y, event.z) || checkVertical(event.world, event.x, event.y, event.z)) {
			if (!event.entityPlayer.capabilities.isCreativeMode) {
				event.entityPlayer.getHeldItem().stackSize--;
			}

			event.world.func_147480_a(event.x, event.y, event.z, false);

			ForgeDirection dir = ForgeDirection.getOrientation(event.face);
		
			event.world.playSoundEffect(event.x, event.y, event.z, "portal.trigger", 0.1F, 1.0F);
			event.world.addWeatherEffect(new EntityHarmlessLightningBolt(event.world, event.x, event.y, event.z));

			event.world.spawnEntityInWorld(new EntityItem(event.world,
					event.x + (dir.offsetX * 1.75D), event.y
					+ (dir.offsetY * 1.75D) + 0.5D, event.z
					+ (dir.offsetZ * 1.75D), new ItemStack(
							Register.ItemShardSoul)));
		}

	}

	private boolean checkHorizontal(World world, int x, int y, int z) {
		ForgeDirection[] VALID_DIRECTIONS = new ForgeDirection[] {
				ForgeDirection.NORTH, ForgeDirection.SOUTH,
				ForgeDirection.EAST, ForgeDirection.WEST };

		for (ForgeDirection dir : VALID_DIRECTIONS) {
			int newX = x + dir.offsetX;
			int newZ = z + dir.offsetZ;

			if (world.getBlock(newX, y, newZ) != Blocks.netherrack) {
				return false;
			}

			if (world.getBlock(newX + dir.offsetX, y, newZ + dir.offsetZ) != Register.BlockXenolith 
					|| world.getBlockMetadata(newX + dir.offsetX, y, newZ + dir.offsetZ) != 0) {
				return false;
			}

			if (dir.offsetX == 0) {
				if (world.getBlock(newX + dir.offsetZ, y, newZ) != Register.BlockXenolith 
						|| world.getBlockMetadata(newX + dir.offsetX, y, newZ + dir.offsetZ) != 0) {
					return false;
				}
			} else if (dir.offsetZ == 0) {
				if (world.getBlock(newX, y, newZ - dir.offsetX) != Register.BlockXenolith 
						|| world.getBlockMetadata(newX + dir.offsetX, y, newZ + dir.offsetZ) != 0) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean checkVertical(World world, int x, int y, int z) {
		ForgeDirection[] VALID_DIRECTIONS = new ForgeDirection[] {
				ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST,
				ForgeDirection.WEST };
		boolean isFormed = true;

		for (ForgeDirection dir : VALID_DIRECTIONS) {
			int newX = x + dir.offsetX;
			int newY = y + dir.offsetY;

			if (world.getBlock(newX, newY, z) != Blocks.netherrack) {
				isFormed = false;
				break;
			}

			if (world.getBlock(newX + dir.offsetX, newY + dir.offsetY, z) != Register.BlockXenolith 
					|| world.getBlockMetadata(newX + dir.offsetX, newY + dir.offsetY, z) != 0) {
				isFormed = false;
				break;
			}

			if (dir.offsetX == 0) {
				if (world.getBlock(newX + dir.offsetY, newY, z) != Register.BlockXenolith 
						|| world.getBlockMetadata(newX + dir.offsetY, newY, z) != 0) {
					isFormed = false;
					break;
				}
			} else if (dir.offsetY == 0) {
				if (world.getBlock(newX, newY - dir.offsetX, z) != Register.BlockXenolith 
						|| world.getBlockMetadata(newX, newY - dir.offsetX, z) != 0) {
					isFormed = false;
					break;
				}
			}
		}

		if (isFormed) {
			return true;
		}

		VALID_DIRECTIONS = new ForgeDirection[] { ForgeDirection.UP,
				ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH };

		for (ForgeDirection dir : VALID_DIRECTIONS) {
			int newZ = z + dir.offsetZ;
			int newY = y + dir.offsetY;

			if (world.getBlock(x, newY, newZ) != Blocks.netherrack) {
				return false;
			}

			if (world.getBlock(x, newY + dir.offsetY, newZ + dir.offsetZ) != Register.BlockXenolith 
					|| world.getBlockMetadata(x, newY + dir.offsetY, newZ + dir.offsetZ) != 0) {
				return false;
			}

			if (dir.offsetZ == 0) {
				if (world.getBlock(x, newY, newZ + dir.offsetY) != Register.BlockXenolith 
						|| world.getBlockMetadata(x, newY, newZ + dir.offsetY) != 0) {
					return false;
				}
			} else if (dir.offsetY == 0) {
				if (world.getBlock(x, newY - dir.offsetZ, newZ) != Register.BlockXenolith 
						|| world.getBlockMetadata(x, newY - dir.offsetZ, newZ) != 0) {
					return false;
				}
			}
		}

		return true;
	}
}
