package ssr.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import ssr.SSRCore;
import ssr.config.Config;
import ssr.gameObjs.ObjHandler;
import ssr.utils.TierHandling;
import ssr.utils.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AbsorbSpawner {
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		World world = player.worldObj;

		if (!world.isRemote && Config.canAbsorbSpawners && player != null)
			if (player.getHeldItem() != null
					&& player.getHeldItem().getItem() == ObjHandler.sShard) {
				ItemStack stack = player.getHeldItem();
				TileEntity tile = world
						.getTileEntity(event.x, event.y, event.z);
				TileEntityMobSpawner spawner = null;
				if (tile instanceof TileEntityMobSpawner)
					spawner = (TileEntityMobSpawner) tile;
				if (spawner != null) {
					String entName = spawner.func_145881_a()
							.getEntityNameToSpawn(); // func_ is
														// MobSpawnerBaseLogic
					Entity ent = EntityList.createEntityByName(entName, world);
					String translate = ent.getCommandSenderName();

					if (Utils.isEntityAccepted(translate)
							&& !Config.easyVanillaAbsorb) {
						if (!stack.hasTagCompound()) {
							stack.setTagCompound(new NBTTagCompound());
							stack.stackTagCompound.setString("EntityType",
									"empty");
						}

						NBTTagCompound nbt = stack.stackTagCompound;
						String nbtName = nbt.getString("EntityType");
						int nbtKills = nbt.getInteger("KillCount");
						int tier = nbt.getInteger("Tier");

						if ((nbtName.equals("empty") || translate
								.equals(nbtName))
								&& (nbtKills != TierHandling.getMax(5) || nbtName
										.equals("empty"))) {
							int totalKills = nbtKills + 200;
							totalKills = totalKills > TierHandling.getMax(5) ? TierHandling
									.getMax(5) : totalKills;
							nbt.setInteger("KillCount", totalKills);
							if (nbtName.equals("empty")) {
								nbt.setString("EntityType", translate);
								nbt.setString("EntityId", entName);
								EntityLiving entLiv = (EntityLiving) spawner
										.func_145881_a().func_98281_h();
								ItemStack heldItem = entLiv
										.getEquipmentInSlot(0);
								if (heldItem != null
										&& !nbt.getBoolean("HasItem")) {
									nbt.setBoolean("HasItem", true);
									NBTTagCompound nbt2 = new NBTTagCompound();
									heldItem.writeToNBT(nbt2);
									nbt.setTag("Item", nbt2);
								}
							}
							world.setBlockToAir(event.x, event.y, event.z);
						}
					} else if (Config.easyVanillaAbsorb) {
						if (!stack.hasTagCompound()) {
							stack.setTagCompound(new NBTTagCompound());
							stack.stackTagCompound.setString("EntityType",
									"empty");
						}

						NBTTagCompound nbt = stack.stackTagCompound;
						String nbtName = nbt.getString("EntityType");
						int nbtKills = nbt.getInteger("KillCount");

						if (nbtName.equals("empty")) {
							nbt.setString("EntityType", translate);
							nbt.setString("EntityId", entName);
							EntityLiving entLiv = (EntityLiving) spawner
									.func_145881_a().func_98281_h();
							ItemStack heldItem = entLiv.getEquipmentInSlot(0);
							if (heldItem != null && !nbt.getBoolean("HasItem")) {
								nbt.setBoolean("HasItem", true);
								NBTTagCompound nbt2 = new NBTTagCompound();
								heldItem.writeToNBT(nbt2);
								nbt.setTag("Item", nbt2);
							}
						}
						if (nbtKills < TierHandling.getMax(5)) {
							nbtKills += Config.vanillaBonus;
							nbtKills = nbtKills > TierHandling.getMax(5) ? TierHandling
									.getMax(5) : nbtKills;
							nbt.setInteger("KillCount", nbtKills);
							world.setBlockToAir(event.x, event.y, event.z);
						}
					}
				}
			}
	}
}
