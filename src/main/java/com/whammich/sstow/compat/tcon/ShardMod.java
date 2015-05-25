package com.whammich.sstow.compat.tcon;

import java.util.*;

import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.Utils;

import net.minecraft.enchantment.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.StatCollector;
import tconstruct.modifiers.tools.ModBoolean;

public class ShardMod extends ModBoolean {
	
	 	String color;
	    String tooltipName;
	    
	public ShardMod(ItemStack[] items, int effect, String tag, String c, String tip) {
		super(items, effect, tag, c, tip);
		color = c;
        tooltipName = tip;

	}

	@Override
	protected boolean canModify(ItemStack tool, ItemStack[] input) {
		NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
		return tags.getInteger("Modifiers") > 0 && !tags.getBoolean(key);
	}

	@Override
	public void modify(ItemStack[] input, ItemStack tool) {
		if(!Utils.isShardBound(input[0])){
			return;
		}
		NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
		tags.setBoolean(key, true);
		addEnchantment(tool, Register.SOUL_STEALER, Utils.getShardTier(input[0]));
		int modifiers = tags.getInteger("Modifiers");
        modifiers -= 1;
        tags.setInteger("Modifiers", modifiers);

		addToolTip(tool, color + String.format(StatCollector.translateToLocal("modifier.tool.shard"),
				enchantlevels(Utils.getShardTier(input[0]))), color + key);
	}

	public static String enchantlevels(byte input) {
		if(input == 1){
			return "I";
		} else if(input == 2){
			return "II";
		} else if(input == 3){
			return "III";
		} else if(input == 4){
			return "IV";
		} else if(input == 5){
			return "V";
		}
		return ""; 
	}
	
	
	@SuppressWarnings("rawtypes")
	public void addEnchantment(ItemStack tool, Enchantment enchant, int level) {
		NBTTagList tags = new NBTTagList();
		Map enchantMap = EnchantmentHelper.getEnchantments(tool);
		Iterator iterator = enchantMap.keySet().iterator();
		int index;
		int lvl;
		boolean hasEnchant = false;
		while (iterator.hasNext()) {
			NBTTagCompound enchantTag = new NBTTagCompound();
			index = ((Integer) iterator.next()).intValue();
			lvl = (Integer) enchantMap.get(index);
			if (index == enchant.effectId) {
				hasEnchant = true;
				enchantTag.setShort("id", (short) index);
				enchantTag.setShort("lvl", (short) ((byte) level));
				tags.appendTag(enchantTag);
			} else {
				enchantTag.setShort("id", (short) index);
				enchantTag.setShort("lvl", (short) ((byte) lvl));
				tags.appendTag(enchantTag);
			}
		}
		if (!hasEnchant) {
			NBTTagCompound enchantTag = new NBTTagCompound();
			enchantTag.setShort("id", (short) enchant.effectId);
			enchantTag.setShort("lvl", (short) ((byte) level));
			tags.appendTag(enchantTag);
		}
		tool.stackTagCompound.setTag("ench", tags);
	}
}
