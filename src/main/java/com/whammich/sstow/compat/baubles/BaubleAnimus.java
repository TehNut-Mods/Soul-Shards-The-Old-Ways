package com.whammich.sstow.compat.baubles;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;

import com.mojang.util.UUIDTypeAdapter;
import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.Reference;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaubleAnimus extends Item implements IBauble {

	public BaubleAnimus() {
		super();
		this.maxStackSize = 1;
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setUnlocalizedName("sstow.bauble.animus");
		this.setTextureName(Reference.modID + ":gemAnimusBauble");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote) {
			InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
			for(int i = 0; i < baubles.getSizeInventory(); i++) {
				if(baubles.getStackInSlot(i) == null && baubles.isItemValidForSlot(i, stack)) {
					baubles.setInventorySlotContents(i, stack.copy());
					if(!player.capabilities.isCreativeMode){
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}
					onEquipped(stack, player);
					break;
				}
			}
		}

		return stack;	
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held) {
		if(stack.stackTagCompound != null){
			if (Utils.displayShiftForDetail && !Utils.isShiftKeyDown())
				list.add(Utils.shiftForDetails());

			if (Utils.isShiftKeyDown()) {
				list.add(Utils.localizeFormatted("chat.sstow.shard.bound", 
						UsernameCache.getLastKnownUsername(UUIDTypeAdapter.fromString(stack.stackTagCompound.getString("master")))));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		return true;
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		if (!player.worldObj.isRemote) {
			player.worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 1.3f);
			if(player instanceof EntityPlayer){
				EntityPlayer entPlayer = (EntityPlayer) player;
				if(stack.stackTagCompound == null){
					stack.setTagCompound(new NBTTagCompound());
				}
				if(!stack.stackTagCompound.getString("master").equals(entPlayer.getUniqueID().toString())){
					if(Config.hurtOnBind){
						player.attackEntityFrom(DamageSource.generic, 1F);
						player.setHealth(0.5F);
					}
					stack.stackTagCompound.setString("master", entPlayer.getUniqueID().toString());
				}
			}
		}
	}
	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
		return true;
	}
}
