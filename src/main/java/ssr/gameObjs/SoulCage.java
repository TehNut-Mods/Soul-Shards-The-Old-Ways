package ssr.gameObjs;

import java.util.List;
import java.util.Random;

import ssr.SSRCore;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ssr.config.SoulConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulCage extends Block implements ITileEntityProvider {
	@SideOnly(Side.CLIENT)
	public IIcon[] icons;

	public SoulCage() {
		super(Material.iron);
		this.setCreativeTab(ObjHandler.sTab);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if (!world.isRemote && world.getBlockMetadata(x, y, z) != 0)
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
	}

	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
		if (world.isRemote || meta == 0)
			return;
		CageTile tile = (CageTile) world.getTileEntity(x, y, z);
		if (tile == null || tile.inventory == null || tile.hasFailed)
			return;
		world.spawnEntityInWorld(new EntityItem(world, x, y, z, tile
				.decrStackSize(0, 1)));
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int direction, float par7, float par8,
			float par9) {
		if (world.isRemote)
			return false;

		CageTile tile = (CageTile) world.getTileEntity(x, y, z);

		if (tile == null)
			return false;

		if (player.getHeldItem() != null
				&& player.getHeldItem().getItem() == ObjHandler.sShard) {
			if (tile.inventory != null)
				return false;
			ItemStack stack = player.getHeldItem();
			if (stack.hasTagCompound()) {
				NBTTagCompound nbt = stack.stackTagCompound;
				int tier = nbt.getInteger("Tier");
				String entName = nbt.getString("EntityType");
				if (tier == 0 || entName.isEmpty() || entName.equals("empty"))
					return false;
				tile.setInventorySlotContents(0, stack);
				if (SoulConfig.requireOwnerOnline)
					tile.owner = player.getCommandSenderName();
				if (!player.capabilities.isCreativeMode)
					stack.stackSize--;
			}
		}

		if (player.isSneaking() && player.getHeldItem() == null) {
			if (tile.inventory == null)
				return false;
			world.spawnEntityInWorld(new EntityItem(world, GetOffset(direction,
					x, 'x'), GetOffset(direction, y, 'y'), GetOffset(direction,
					z, 'z'), tile.decrStackSize(0, 1)));
		}
		return false;
	}

	private double GetOffset(int direction, int coord, char coordChar) {
		double offset = 0;
		switch (coordChar) {
		case 'x':
			offset = Facing.offsetsXForSide[direction];
			break;
		case 'y':
			offset = Facing.offsetsYForSide[direction];
			break;
		case 'z':
			offset = Facing.offsetsZForSide[direction];
			break;
		}
		return coord + 0.5D + (0.48 * offset);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block) {
		if (!world.isRemote) {
			CageTile tile = (CageTile) world.getTileEntity(x, y, z);

			if (tile != null
					&& (tile.tier != 0 && SoulConfig.enableRS[tile.tier - 1]))
				tile.isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
		}
	}

	@Override
	public void dropXpOnBlockBreak(World par1World, int par2, int par3,
			int par4, int par5) {

	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
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
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int i = 0; i < 3; i++)
			par3List.add(new ItemStack(item, 1, i));
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return icons[meta];
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		String[] textureNames = { "soulCage", "cageUnlit", "cageLit" };
		icons = new IIcon[textureNames.length];

		for (int i = 0; i < this.icons.length; i++)
			icons[i] = iconRegister.registerIcon(String.format("%s:%s",
					new Object[] { "ssr", textureNames[i] }));
	}
}
