package com.whammich.sstow.gameObjs.block;

import java.util.Random;

import com.whammich.sstow.gameObjs.ObjHandler;
import com.whammich.sstow.gameObjs.tile.ForgeTile;
import com.whammich.sstow.SSTheOldWays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Forge_Block extends BlockContainer {

	private IIcon top;
	private IIcon front;
	private IIcon bottom;

	private static boolean isBurning;
	private final boolean isBurning2;
	private final Random random = new Random();

	public Forge_Block(boolean isActive) {
		super(Material.rock);
		this.setBlockName("sstow.forge_block");
		this.setHardness(3.5F);
		isBurning2 = isActive;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World world, int xCoord, int yCoord,
			int zCoord, int side) {
		return Container.calcRedstoneFromInventory((IInventory) world
				.getTileEntity(xCoord, yCoord, zCoord));
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("sstow:soulForge_side");
		this.front = iconRegister
				.registerIcon(this.isBurning2 ? "sstow:soulForge_active"
						: "sstow:soulForge_idle");
		this.top = iconRegister.registerIcon("furnace_top");
		this.bottom = iconRegister.registerIcon("obsidian");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side == 1) {
			return this.top;
		} else if (side == 0) {
			return this.bottom;
		} else if (side != meta) {
			return this.blockIcon;
		} else {
			return front;
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		player.openGui(SSTheOldWays.modInstance, 0, world, x, y, z);
		return true;
	}

	public Item getItemDropped(int par1, Random random, int par3) {
		return Item.getItemFromBlock(ObjHandler.SOUL_FORGE);
	}

	public Item getItem(World world, int par2, int par3, int par4) {
		return Item.getItemFromBlock(ObjHandler.SOUL_FORGE);
	}

	@SideOnly(Side.CLIENT)
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
	}

	public TileEntity createNewTileEntity(World world, int var1) {
		return new ForgeTile();
	}

	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack itemstack) {
		int dir = MathHelper
				.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (dir == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (dir == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (dir == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (dir == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

		if (itemstack.hasDisplayName()) {
			((ForgeTile) world.getTileEntity(x, y, z)).furnaceName(itemstack
					.getDisplayName());
		}
	}

	public static void updateBlockstate(boolean burning, World world, int x,
			int y, int z) {
		int dir = world.getBlockMetadata(x, y, z);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		isBurning = true;
		if (burning) {
			world.setBlock(x, y, z, ObjHandler.SOUL_FORGE_ACTIVE);
		} else {
			world.setBlock(x, y, z, ObjHandler.SOUL_FORGE);
		}
		isBurning = false;
		world.setBlockMetadataWithNotify(x, y, z, dir, 2);

		if (tileentity != null) {
			tileentity.validate();
			world.setTileEntity(x, y, z, tileentity);
		}
	}

	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		if (!isBurning) {
			ForgeTile forgeTile = (ForgeTile) world.getTileEntity(x, y, z);
			if (forgeTile != null) {
				for (int i = 0; i < forgeTile.getSizeInventory(); ++i) {
					ItemStack itemstack = forgeTile.getStackInSlot(i);
					if (itemstack != null) {
						float f = this.random.nextFloat() * 0.6F + 0.1F;
						float f1 = this.random.nextFloat() * 0.6F + 0.1F;
						float f2 = this.random.nextFloat() * 0.6F + 0.1F;

						while (itemstack.stackSize > 0) {
							int j = random.nextInt(21) + 10;
							if (j > itemstack.stackSize) {
								j = itemstack.stackSize;
							}
							itemstack.stackSize -= j;
							EntityItem itemEntity = new EntityItem(world,
									(double) ((float) x + f),
									(double) ((float) y + f1),
									(double) ((float) z + f2), new ItemStack(
											itemstack.getItem(), j,
											itemstack.getItemDamage()));

							if (itemstack.hasTagCompound()) {
								itemEntity.getEntityItem().setTagCompound(
										((NBTTagCompound) itemstack
												.getTagCompound().copy()));
							}
							float f3 = 0.025F;
							itemEntity.motionX = (double) ((float) this.random
									.nextGaussian() * f3);
							itemEntity.motionY = (double) ((float) this.random
									.nextGaussian() * f3 + 0.2F);
							itemEntity.motionZ = (double) ((float) this.random
									.nextGaussian() * f3);
							world.spawnEntityInWorld(itemEntity);
						}
					}
				}
				world.func_147453_f(x, y, z, block);
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z,
			Random random) {
		if (this.isBurning2) {
			int direction = world.getBlockMetadata(x, y, z);

			float xx = (float) x + 0.5F;
			float yy = (float) y + random.nextFloat() * 6.0F / 16.0F;
			float zz = (float) z + 0.5F;
			float zz2 = random.nextFloat() * 0.3F - 0.2F;
			float xx2 = 0.5F;

			if (direction == 4) {
				world.spawnParticle("smoke", (double) (xx - xx2), (double) yy,
						(double) (zz + zz2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx - xx2), (double) yy,
						(double) (zz + zz2), 0.0F, 0.0F, 0.0F);
			} else if (direction == 5) {
				world.spawnParticle("smoke", (double) (xx + xx2), (double) yy,
						(double) (zz + zz2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + xx2), (double) yy,
						(double) (zz + zz2), 0.0F, 0.0F, 0.0F);
			} else if (direction == 3) {
				world.spawnParticle("smoke", (double) (xx + zz2), (double) yy,
						(double) (zz + xx2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + zz2), (double) yy,
						(double) (zz + xx2), 0.0F, 0.0F, 0.0F);
			} else if (direction == 2) {
				world.spawnParticle("smoke", (double) (xx + zz2), (double) yy,
						(double) (zz - xx2), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + zz2), (double) yy,
						(double) (zz - xx2), 0.0F, 0.0F, 0.0F);
			}
		}
	}
}
