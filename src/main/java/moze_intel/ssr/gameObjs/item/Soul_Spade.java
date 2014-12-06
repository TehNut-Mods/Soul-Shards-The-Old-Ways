package moze_intel.ssr.gameObjs.item;

import moze_intel.ssr.gameObjs.ObjHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSpade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Soul_Spade extends ItemSpade {

	public Soul_Spade(ToolMaterial Material) {
		super(Material);
		this.setUnlocalizedName("soul_spade");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	public String getUnlocalizedName(ItemStack stack) {
		return "item.ssr.soul_spade";
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("ssr:soul_spade");
	}
}
