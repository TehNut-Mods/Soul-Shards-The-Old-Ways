package com.whammich.sstow.events;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
//import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CreateShardEvent {

	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent event) {
		if (Config.RITUAL == true) {

			if (event.world.isRemote || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
				return;
			}

			if (event.entityPlayer.getHeldItem() == null || event.entityPlayer.getHeldItem().getItem() != Items.diamond) {
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

//				event.entityPlayer.addChatComponentMessage(new ChatComponentText((char) 167 + "5" + (char) 167 + "o" + "You hear whispering around you,"));
//				event.entityPlayer.addChatComponentMessage(new ChatComponentText((char) 167 + "5" + (char) 167 + "o" + "but as you try to listen; silence falls"));
//				event.world.playSoundEffect(event.x, event.y, event.z, "portal.trigger", 0.05F, 1.0F);
				
//				event.world.addWeatherEffect(new EntityHarmlessLightningBolt(event.world, event.x + 1, event.y, event.z + 1));
				
				event.world.spawnEntityInWorld(new EntityItem(event.world,
						event.x + (dir.offsetX * 1.75D), event.y
								+ (dir.offsetY * 1.75D) + 0.5D, event.z
								+ (dir.offsetZ * 1.75D), new ItemStack(
								Register.ItemShardSoul)));
			}
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

			if (world.getBlock(newX + dir.offsetX, y, newZ + dir.offsetZ) != Blocks.end_stone) {
				return false;
			}

			if (dir.offsetX == 0) {
				if (world.getBlock(newX + dir.offsetZ, y, newZ) != Blocks.end_stone) {
					return false;
				}
			} else if (dir.offsetZ == 0) {
				if (world.getBlock(newX, y, newZ - dir.offsetX) != Blocks.end_stone) {
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

			if (world.getBlock(newX + dir.offsetX, newY + dir.offsetY, z) != Blocks.end_stone) {
				isFormed = false;
				break;
			}

			if (dir.offsetX == 0) {
				if (world.getBlock(newX + dir.offsetY, newY, z) != Blocks.end_stone) {
					isFormed = false;
					break;
				}
			} else if (dir.offsetY == 0) {
				if (world.getBlock(newX, newY - dir.offsetX, z) != Blocks.end_stone) {
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

			if (world.getBlock(x, newY + dir.offsetY, newZ + dir.offsetZ) != Blocks.end_stone) {
				return false;
			}

			if (dir.offsetZ == 0) {
				if (world.getBlock(x, newY, newZ + dir.offsetY) != Blocks.end_stone) {
					return false;
				}
			} else if (dir.offsetY == 0) {
				if (world.getBlock(x, newY - dir.offsetZ, newZ) != Blocks.end_stone) {
					return false;
				}
			}
		}

		return true;
	}
}
