package ssr.creativeTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ssr.gameObjs.ObjHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulTab extends CreativeTabs 
{

	public SoulTab() 
	{
		super("Soul Shards Reborn");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() 
	{
		return new ItemStack(ObjHandler.sShard, 1, 6);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() 
	{
		return null;
	}
}
