package moze_intel.ssr.gameObjs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moze_intel.ssr.utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class VileDustItem extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon unbound;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public VileDustItem() {
		this.setUnlocalizedName("vile_dust");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
	}
	public String getUnlocalizedName(ItemStack stack) {
		return "item.ssr.vile_dust";
	}
}
