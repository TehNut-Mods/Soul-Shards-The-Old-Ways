package ssr.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import ssr.gameObjs.ObjHandler;
import ssr.utils.InvUtils;
import ssr.utils.TierHandling;
import ssr.utils.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerKill {
	@SubscribeEvent
	public void killEvent(LivingDeathEvent event) {
		if (event.source.getEntity() instanceof EntityPlayerMP
				&& event.entityLiving instanceof EntityLiving) {
			EntityLiving ent = (EntityLiving) event.entityLiving;
			EntityPlayerMP player = (EntityPlayerMP) event.source.getEntity();
			String mobName = ent.getCommandSenderName();

			if (mobName.equals("Skeleton") && ent instanceof EntitySkeleton) {
				EntitySkeleton skele = (EntitySkeleton) ent;

				if (skele.getSkeletonType() == 1)
					mobName = "Wither Skeleton";
			}

			String mobId = EntityList.getEntityString(ent);

			if ((Utils.isEntityAccepted(mobName))
					&& !ent.getEntityData().getBoolean("fromSSR")) {
				if (!InvUtils.HasShard(player, mobName))
					return;

				ItemStack stack = InvUtils.getStack(player, mobName);

				if (stack != null && !stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
					stack.stackTagCompound.setString("EntityType", "empty");
					stack.stackTagCompound.setInteger("EntityID", 0);
					stack.stackTagCompound.setInteger("KillCount", 0);
					stack.stackTagCompound.setInteger("Tier", 0);
					stack.stackTagCompound.setString("entId", "empty");
				}

				if (stack.stackTagCompound.getString("EntityType").equals(
						"empty")) {
					stack.stackTagCompound.setString("EntityType", mobName);
					stack.stackTagCompound.setString("entId", mobId);
					ItemStack heldItem = ent.getEquipmentInSlot(0);

					if ((!mobName.equals("Zombie") || !mobName
							.equals("Enderman")) && heldItem != null) {
						stack.stackTagCompound.setBoolean("HasItem", true);
						NBTTagCompound nbt2 = new NBTTagCompound();
						heldItem.writeToNBT(nbt2);
						stack.stackTagCompound.setTag("Item", nbt2);
					}
				}

				String name = stack.stackTagCompound.getString("EntityType");
				int kills = stack.stackTagCompound.getInteger("KillCount");

				if (mobName.equals(name) && kills < TierHandling.getMax(5)) {
					if (stack.getItemDamage() == 0)
						stack.setItemDamage(1);
					kills += getSoulBonus(player.getHeldItem()) + 1;
					kills = kills > TierHandling.getMax(5) ? TierHandling
							.getMax(5) : kills;
					stack.stackTagCompound.setInteger("KillCount", kills);
				}
			}
		}
	}

	private int getSoulBonus(ItemStack stack) {
		return (stack == null ? 0 : EnchantmentHelper.getEnchantmentLevel(
				ObjHandler.sStealer.effectId, stack));
	}
}
