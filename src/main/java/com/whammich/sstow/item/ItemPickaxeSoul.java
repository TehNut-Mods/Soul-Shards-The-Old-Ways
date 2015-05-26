package com.whammich.sstow.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPickaxeSoul extends ItemPickaxe {

	public ItemPickaxeSoul(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.soultool.pickaxe";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":pickaxeSoul");
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entityLiving) {
		if (entityLiving.isSneaking()) {
			return super.onBlockDestroyed(stack, world, block, x, y, z, entityLiving);
		}
		for (int y2 = y - 1; y2 <= y + 1; ++y2) {
			for (int x2 = x - 1; x2 <= x + 1; ++x2) {
				for (int z2 = z - 1; z2 <= z + 1; ++z2) {
					Block blk = world.getBlock(x2, y2, z2);
					if (blk == null) {
						continue;
					}
					
					if (block.getMaterial() == Material.rock) {
						world.func_147480_a(x2, y2, z2, true);
						stack.damageItem(1, entityLiving);
					}

				}
			}
		}
		return true;
	}
}
