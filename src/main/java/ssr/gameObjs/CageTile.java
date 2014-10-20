package ssr.gameObjs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import ssr.SSRCore;
import ssr.config.Config;
import ssr.utils.Utils;

public class CageTile extends TileEntity implements IInventory {
	int maxMobs;
	int timer;
	int timer2;
	int timerEnd;
	public int tier;
	public String entName;
	public String entId;
	public String owner;
	public boolean isPowered;
	public boolean flag;
	public boolean hasFailed;
	private boolean hasLoaded;
	public ItemStack HeldItem;
	public ItemStack inventory;
	public static Random random;

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Tier", tier);
		if (entName != null && !entName.isEmpty())
			nbt.setString("Entity", entName);
		if (entId != null && !entId.isEmpty())
			nbt.setString("entId", entId);
		if (owner != null && !owner.isEmpty())
			nbt.setString("Owner", owner);
		nbt.setBoolean("Power", isPowered);
		if (HeldItem != null) {
			NBTTagCompound nbt2 = new NBTTagCompound();
			HeldItem.writeToNBT(nbt2);
			nbt.setTag("Item", nbt2);
		}
		if (inventory != null) {
			NBTTagCompound nbt3 = new NBTTagCompound();
			inventory.writeToNBT(nbt3);
			nbt.setTag("Inventory", nbt3);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tier = nbt.getInteger("Tier");
		entName = nbt.getString("Entity");
		entId = nbt.getString("entId");
		owner = nbt.getString("Owner");
		isPowered = nbt.getBoolean("Power");
		HeldItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Item"));
		inventory = ItemStack.loadItemStackFromNBT(nbt
				.getCompoundTag("Inventory"));
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}

	public void SendUpdate() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void updateEntity() {
		if (hasFailed || worldObj.isRemote)
			return;

		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (inventory == null) {
			if (hasLoaded) {
				ClearData();
				hasLoaded = false;
			}

			adjustMetadata(metadata, 0);
			return;
		}

		if (!hasLoaded) {
			LoadNbtData();

			if (tier == 0 || entName.equals("empty") || entName.isEmpty()) {
				SSRCore.SoulLog
						.fatal("Soul Cage failed to load NBT Data, invalidating tile entity.");
				hasFailed = true;
				return;
			} else {
				maxMobs = Config.numMobs[tier - 1];
				timerEnd = SecToTick(Config.coolDown[tier - 1]);
			}
			adjustMetadata(metadata, 1);
		}

		if (timer == 20) {
			timer = 0;

			if (Config.requireOwnerOnline && owner != null && !owner.isEmpty()
					&& !isOwnerOnline()) {
				flag = false;
				adjustMetadata(metadata, 1);
				return;
			}

			EntityLiving tempEnt = null;

			if (entName.equals("Wither Skeleton")) {
				EntitySkeleton skele = new EntitySkeleton(worldObj);
				skele.setSkeletonType(1);
				tempEnt = skele;
			} else
				tempEnt = (EntityLiving) EntityList.createEntityByName(entId,
						worldObj);

			if (tempEnt != null) {
				if (Config.enableRS[tier - 1])
					flag = isPowered;
				else
					flag = true;

				if (flag) {
					flag = (!hasReachedSpawnLimit(tempEnt) || Config.maxNumSpawns == 0);

					if (Config.needPlayer[tier - 1] && flag)
						flag = isPlayerClose(xCoord, yCoord, zCoord);

					if (Config.checkLight[tier - 1] && flag)
						flag = canSpawnInLight(tempEnt, xCoord, yCoord, zCoord);

					if (Config.otherWorlds[tier - 1] && flag)
						flag = canSpawnInWorld(tempEnt);
				}

				if (flag)
					adjustMetadata(1, 2);
				else
					adjustMetadata(2, 1);

				tempEnt = null; // destroy the object
			}
		} else {
			timer += 1;
		}

		if (timer2 == timerEnd) {
			timer2 = 0;

			if (flag) {
				EntityLiving[] entity = new EntityLiving[maxMobs];

				for (int i = 0; i < entity.length; i++) {
					if (entName.equals("Wither Skeleton")) {
						EntitySkeleton skele = new EntitySkeleton(worldObj);
						skele.setSkeletonType(1);
						entity[i] = skele;
					} else
						entity[i] = (EntityLiving) EntityList
								.createEntityByName(entId, worldObj);
				}

				SpawnAlgo(entity);

				random = new Random();
				for (int i = 0; i < entity.length; i++) {
					if (!entity[i].isDead) {
						if (entity[i] instanceof EntitySlime)
							entity[i].getDataWatcher().updateObject(16,
									new Byte((byte) 1));

						entity[i].getEntityData().setBoolean("fromSSR", true);
						entity[i].forceSpawn = true;
						entity[i].func_110163_bv();

						if (HeldItem != null)
							entity[i].setCurrentItemOrArmor(0, HeldItem);
						if (Config.enableDebug == true) {
							switch (Utils.getEntityID(entId)) {
							case 0:
								SSRCore.SoulLog.info("SSR: Horse spawned");
								EntityHorse entityhorse = new EntityHorse(
										worldObj);
								entityhorse.setHorseType(random.nextInt(3));
								entityhorse.setHorseVariant(random.nextInt(7)
										| random.nextInt(5) << 8);
								entity[i] = entityhorse;
								break;
							case 1:
								SSRCore.SoulLog.info("SSR: Sheep spawned");
								EntitySheep entitysheep = new EntitySheep(
										worldObj);
								// entitysheep.setFleeceColor(random.nextInt(16));
								entitysheep.setFleeceColor(6);
								entity[i] = entitysheep;
								break;
							case 2:
								SSRCore.SoulLog.info("SSR: Villager spawned");
								EntityVillager entityvillager = new EntityVillager(
										worldObj);
								entityvillager.setProfession(random.nextInt(5));
								entity[i] = entityvillager;
								break;
							case 3:
								SSRCore.SoulLog.info("SSR: Zombie spawned");
								EntityZombie entityzombie = new EntityZombie(
										worldObj);
								entityzombie
										.setVillager(random.nextInt(2) == 0 ? true
												: false);
								entity[i] = entityzombie;
								break;
							default:
								SSRCore.SoulLog.info("SSR: " + entId
										+ " spawned");
							}
						}
						worldObj.spawnEntityInWorld(entity[i]);
					}
				}
				random = null;
			}
		} else
			timer2 += 1;
	}

	private void LoadNbtData() {
		NBTTagCompound nbt = inventory.stackTagCompound;
		entName = nbt.getString("EntityType");
		entId = nbt.getString("entId");
		tier = nbt.getInteger("Tier");
		if (nbt.getBoolean("HasItem"))
			HeldItem = ItemStack.loadItemStackFromNBT(nbt
					.getCompoundTag("Item"));
		hasLoaded = true;
		SendUpdate();
	}

	private void ClearData() {
		tier = 0;
		timer = 0;
		timer2 = 0;
		entName = "";
		entId = "";
		HeldItem = null;
		flag = false;
		hasFailed = false;
		isPowered = false;
		SendUpdate();
	}

	private void adjustMetadata(int currentMeta, int correctMeta) {
		if (worldObj.isRemote)
			return;

		if (currentMeta != correctMeta)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,
					correctMeta, 2);
	}

	private boolean isOwnerOnline() {
		if (!worldObj.isRemote) {
			Iterator<EntityPlayer> iter = worldObj.playerEntities.iterator();
			String names[] = MinecraftServer.getServer().getAllUsernames();
			if (names == null || names.length == 0)
				return false;
			return Arrays.asList(names).contains(owner);
		}
		return false;
	}

	private boolean isPlayerClose(int x, int y, int z) {
		return (worldObj.getClosestPlayer(x, y, z, 16) != null);
	}

	private boolean canSpawnInLight(EntityLiving ent, int x, int y, int z) {
		if (ent instanceof EntitySlime || ent instanceof EntityGhast
				|| ent instanceof EntityPigZombie)
			return true;
		else if (ent instanceof EntityMob) {
			float light = worldObj.getChunkFromBlockCoords(x >> 4, z >> 4)
					.getBlockLightValue(x & 0xF, y, z & 0xF,
							worldObj.skylightSubtracted);

			return light <= 8;
		} else
			return true;
	}

	private boolean canSpawnInWorld(EntityLiving ent) {
		int dimension = worldObj.provider.dimensionId; // -1 Nether, 0 OW, 1 End

		if (ent instanceof EntitySkeleton) {
			EntitySkeleton skele = (EntitySkeleton) ent;

			if (skele.getSkeletonType() == 1 && dimension == -1)
				return true;
			else if (dimension == 0)
				return true;
			else
				return false;
		} else if (ent instanceof EntityBlaze || ent instanceof EntityPigZombie
				|| ent instanceof EntityGhast || ent instanceof EntityMagmaCube)
			return (dimension == -1);
		else if (ent instanceof EntityEnderman)
			return (dimension == 1);
		else if (ent instanceof EntityMob || ent instanceof EntitySlime)
			return (dimension == 0);
		else
			return true;
	}

	private boolean canSpawnAtCoords(EntityLiving ent) {
		return (worldObj.getCollidingBoundingBoxes(ent, ent.boundingBox)
				.isEmpty());
	}

	private boolean hasReachedSpawnLimit(EntityLiving ent) {
		AxisAlignedBB aabb = AxisAlignedBB
				.getBoundingBox(xCoord - 16, yCoord - 16, zCoord - 16,
						xCoord + 16, yCoord + 16, zCoord + 16);
		Iterator<EntityLiving> entIter = worldObj.getEntitiesWithinAABB(
				ent.getClass(), aabb).iterator();
		int mobCount = 0;

		while (entIter.hasNext()) {
			EntityLiving entity = entIter.next();

			if (entity.getEntityData().getBoolean("fromSSR"))
				mobCount += 1;
		}

		return (mobCount > Config.maxNumSpawns);
	}

	private int SecToTick(int seconds) {
		return seconds * 20;
	}

	private void SpawnAlgo(EntityLiving[] ents) {
		for (int i = 0; i < ents.length; i++) {
			int counter = 0;

			do {
				counter += 1;

				if (counter >= 5) {
					ents[i].setDead();
					break;
				}

				double x = xCoord
						+ (worldObj.rand.nextDouble() - worldObj.rand
								.nextDouble()) * 4.0D;
				double y = yCoord + worldObj.rand.nextInt(3) - 1;
				double z = zCoord
						+ (worldObj.rand.nextDouble() - worldObj.rand
								.nextDouble()) * 4.0D;
				ents[i].setLocationAndAngles(x, y, z,
						worldObj.rand.nextFloat() * 360.0F, 0.0F);
			} while (!canSpawnAtCoords(ents[i]) || counter >= 5);
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (j == 0)
			return null;

		ItemStack stack = new ItemStack(ObjHandler.sShard, 1,
				inventory.getItemDamage());
		stack.stackTagCompound = inventory.getTagCompound();
		setInventorySlotContents(i, null);
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		inventory = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack) {
		if (stack.getItem() != ObjHandler.sShard || !stack.hasTagCompound())
			return false;
		NBTTagCompound nbt = stack.stackTagCompound;
		return (inventory == null && nbt.getInteger("Tier") > 0 && !nbt
				.getString("EntityType").equals("empty"));
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {
		//
	}

	@Override
	public void closeInventory() {
		//
	}
}
