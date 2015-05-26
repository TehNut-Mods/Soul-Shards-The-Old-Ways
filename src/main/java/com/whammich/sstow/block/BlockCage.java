package com.whammich.sstow.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.whammich.sstow.SSTheOldWays;
import com.whammich.sstow.entity.particle.EntityPurpleFlameFX;
import com.whammich.sstow.tileentity.TileEntityCage;
import com.whammich.sstow.utils.ModLogger;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCage extends BlockContainer {

	public BlockCage() {
		super(Material.iron);
		this.setBlockName("sstow.block.cage");
		this.setCreativeTab(Register.CREATIVE_TAB);
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
		TileEntityCage tile = (TileEntityCage) world.getTileEntity(xPos, yPos, zPos);
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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3) {
		player.openGui(SSTheOldWays.modInstance, 1, world, x, y, z);
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block) {
		if (!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y, z);

			if (tile instanceof TileEntityCage) {
				((TileEntityCage) tile).checkRedstone();
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
				ModLogger.logFatal(Utils.localizeFormatted("chat.sstow.debug.tileerror", "" + x + " " + y + " " + " " + z));
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
		return new TileEntityCage();
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
		blockIcon = iconRegister.registerIcon(Reference.modID + ":soulCage");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int sideInt, int meta) {
		return blockIcon;
	}

	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (TileEntityCage.active) {
			EntityFX fire = new EntityPurpleFlameFX(world, x - 0.5, y + 0.5, z + 0.5, 0, 0.1, 0, 68, 0, 152);
			Minecraft.getMinecraft().effectRenderer.addEffect(fire);
		}
	}

}
