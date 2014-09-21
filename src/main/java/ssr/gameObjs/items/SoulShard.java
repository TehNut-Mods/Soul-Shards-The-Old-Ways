package ssr.gameObjs.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ssr.gameObjs.ObjHandler;
import ssr.utils.TierHandling;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulShard extends Item
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] icons;
	public static String[] names = {"unboundSS", "tier0", "tier1", "tier2", "tier3", "tier4", "tier5"};
	
	public SoulShard()
	{
		this.canRepair = false;
		this.maxStackSize = 1;
		this.setMaxDamage(0);
		this.hasSubtypes = true;
		this.setCreativeTab(ObjHandler.sTab);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		if (!world.isRemote && stack.hasTagCompound())
		{
			int kills = stack.stackTagCompound.getInteger("KillCount");
			int tier = stack.stackTagCompound.getInteger("Tier");
			int damage = stack.getItemDamage();
			if (!TierHandling.isInBounds(tier, kills))
			{
				if (kills > TierHandling.getMax(5))
				{
					tier = 5;
					kills = TierHandling.getMax(5);
					stack.stackTagCompound.setInteger("KillCount", kills);
				}
				
				tier = TierHandling.updateTier(kills);
				damage = tier + 1;
				stack.stackTagCompound.setInteger("Tier", tier);
				stack.setItemDamage(damage);
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
		return names[stack.getItemDamage()];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs cTab, List list)
    {
		for (int i = 0; i < names.length; i++)
		{
			ItemStack stack = new ItemStack(item, 1, i);
			if (i != 0 && !stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
				stack.stackTagCompound.setString("EntityType", "empty");
				stack.stackTagCompound.setInteger("Tier", i - 1);
				stack.stackTagCompound.setInteger("KillCount", TierHandling.getMin(i - 1));
			}
			list.add(stack);
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return  stack.getItemDamage() == 6;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) 
	{
		if (stack.hasTagCompound())
		{
			String ent = stack.stackTagCompound.getString("EntityType");
			int kills = stack.stackTagCompound.getInteger("KillCount");
			int tier = stack.stackTagCompound.getInteger("Tier");
			if (!ent.equals("empty"))
			{
				if (ent.endsWith(".name"))
					ent = StatCollector.translateToLocal(ent);
				list.add("Bound to: "+ ent);
			}
			list.add("Kills: "+ kills);
			list.add("Tier: "+ tier);
		}
		else list.add("Kill a creature to trap its soul.");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon) 
	{
		icons = new IIcon[names.length];
		for(int i = 0; i < icons.length; i++) 
			icons[i] = icon.registerIcon("ssr" + ":" + names[i]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) 
	{
		return icons[meta];
	}
}
