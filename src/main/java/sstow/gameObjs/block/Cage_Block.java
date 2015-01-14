package sstow.gameObjs.block;

import java.util.Random;

import sstow.gameObjs.ObjHandler;
import sstow.gameObjs.tile.CageTile;
import sstow.utils.HolidayHelper;
import sstow.utils.TOWLogger;
import sstow.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Cage_Block extends BlockContainer {

	public IIcon[] icons = new IIcon[5];

	public Cage_Block() {
		super(Material.iron);
		this.setBlockName("sstow.cage_block");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int xPos, int yPos,
			int zPos, int p_149736_5_) {
		CageTile tile = (CageTile) world.getTileEntity(xPos, yPos, zPos);
		if (tile.getStackInSlot(0) != null) {
			ItemStack shard = tile.getStackInSlot(0);
			int tier = Utils.getShardTier(shard);
			switch (tier) {
			case 1:
				return 2;
			case 2:
				return 5;
			case 3:
				return 7;
			case 4:
				return 10;
			case 5:
				return 15;

			default:
				return 0;
			}

		} else {
			return 0;
		}
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z,
			int side) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float f1, float f2, float f3) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile == null) {
				TOWLogger.logFatal("ERROR: no tile entity found at coords: "
						+ x + " " + y + " " + " " + z);
				return false;
			}

			if (player.isSneaking()) {
				if (world.getBlockMetadata(x, y, z) == 0) {
					return false;
				}

				ForgeDirection dir = ForgeDirection.getOrientation(side);

				world.spawnEntityInWorld(new EntityItem(world, x
						+ (dir.offsetX * 1.75D), y + (dir.offsetY * 1.75D)
						+ 0.5D, z + (dir.offsetZ * 1.75D), ((IInventory) tile)
						.decrStackSize(0, 1)));
			} else {
				if (world.getBlockMetadata(x, y, z) != 0) {
					return false;
				}

				ItemStack stack = player.getHeldItem();

				if (stack == null || stack.getItem() != ObjHandler.SOUL_SHARD
						|| !Utils.isShardBound(stack)
						|| Utils.getShardTier(stack) == 0) {
					return false;
				}

				ItemStack newShard = stack.copy();
				newShard.stackSize = 1;
				((IInventory) tile).setInventorySlotContents(0, newShard);

				if (!player.capabilities.isCreativeMode) {
					stack.stackSize--;
				}
			}
		}

		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile instanceof CageTile) {
				((CageTile) tile).checkRedstone();
			}
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if (!world.isRemote && world.getBlockMetadata(x, y, z) != 0) {
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		}
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		if (!world.isRemote && meta != 0) {
			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile == null) {
				TOWLogger.logFatal("ERROR: no tile entity found at coords: "
						+ x + " " + y + " " + " " + z);
				return;
			}

			ItemStack stack = ((IInventory) tile).decrStackSize(0, 1);

			if (stack != null) {
				world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new CageTile();
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return 1;
	}

	@Override
	public int damageDropped(int par1) {
		return 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		if (HolidayHelper.isChristmas()) {
			icons = new IIcon[4];
			for (int i = 0; i < 4; i++) {
				icons[i] = iconRegister.registerIcon("sstow:cage_" + i + "_xmas");
			}
		} else {
			icons = new IIcon[5];
			for (int i = 0; i < 5; i++) {
				icons[i] = iconRegister.registerIcon("sstow:cage_" + i);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int sideInt, int meta) {
		ForgeDirection dir = ForgeDirection.getOrientation(sideInt);
		if (HolidayHelper.isChristmas()) {
			// Check block direction
			if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
				// meta 2 == block activated
				return icons[3];
			} else {
				// meta 2 == block activated
				if (meta == 2) {
					return icons[2];
					// meta 1 == block has shard
				} else if (meta == 1) {
					return icons[1];
					// meta 0 == block is empty
				} else {
					return icons[0];
				}
			}
		} else {
			// Check block direction
			if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
				// meta 2 == block activated
				if (meta == 2) {
					return icons[4];
				} else {
					return icons[3];
				}
			} else {
				// meta 2 == block activated
				if (meta == 2) {
					return icons[2];
					// meta 1 == block has shard
				} else if (meta == 1) {
					return icons[1];
					// meta 0 == block is empty
				} else {
					return icons[0];
				}
			}
		}
	}
}
