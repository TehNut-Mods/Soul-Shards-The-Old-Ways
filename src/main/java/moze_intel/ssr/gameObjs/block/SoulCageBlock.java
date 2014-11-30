package moze_intel.ssr.gameObjs.block;

import java.util.Calendar;
import java.util.Random;

import moze_intel.ssr.gameObjs.ObjHandler;
import moze_intel.ssr.gameObjs.tile.SoulCageTile;
import moze_intel.ssr.utils.HolidayHelper;
import moze_intel.ssr.utils.SSRLogger;
import moze_intel.ssr.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulCageBlock extends Block implements ITileEntityProvider {
	@SideOnly(Side.CLIENT)
	public IIcon[] icons = new IIcon[5];

	public SoulCageBlock() {
		super(Material.iron);
		this.setBlockName("ssr_cage_block");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
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
				SSRLogger.logFatal("ERROR: no tile entity found at coords: "
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

			if (tile instanceof SoulCageTile) {
				((SoulCageTile) tile).checkRedstone();
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
				SSRLogger.logFatal("ERROR: no tile entity found at coords: "
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
		return new SoulCageTile();
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
				icons[i] = iconRegister.registerIcon("ssr:cage_" + i + "_xmas");
			}
		} else {
			icons = new IIcon[5];
			for (int i = 0; i < 5; i++) {
				icons[i] = iconRegister.registerIcon("ssr:cage_" + i);
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
