package com.whammich.sstow.item.tools;

import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAxeShadow extends ItemAxe {

	public ItemAxeShadow(ToolMaterial Material) {
		super(Material);
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.sstow.shadowtool.axe";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.modID + ":axeShadow");
	}
	
	static Material[] materials = new Material[] {
		Material.gourd, Material.leaves, Material.wood, Material.vine
	};
	@SuppressWarnings("static-access")
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

					for (int i = 0; i < this.materials.length; ++i) {
						if (block.getMaterial() == materials[i]) {
							world.func_147480_a(x2, y2, z2, true);
							stack.damageItem(1, entityLiving);
						}
					}
				}
			}
		}

		return true;
	}
	
}
