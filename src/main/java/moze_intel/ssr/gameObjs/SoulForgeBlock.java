package moze_intel.ssr.gameObjs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulForgeBlock extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon[] forgeicons;

	public SoulForgeBlock() {
		super(Material.rock);
		this.setBlockName("ssr_forge_block");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.blockHardness = 3.0F;
		this.blockResistance = 3.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		forgeicons = new IIcon[2];
		for (int i = 0; i < 2; i++) {
			forgeicons[i] = iconRegister.registerIcon("ssr:forge_" + i);
		}
	}
}
