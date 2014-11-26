package moze_intel.ssr.gameObjs.block;

import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SouliumBlock extends Block {

	public SouliumBlock() {
		super(Material.iron);
		this.setBlockName("ssr_soulium_block");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon("ssr:soulium_block");
	}
}
