package moze_intel.ssr.gameObjs;

import moze_intel.ssr.utils.EntityMapper;
import moze_intel.ssr.utils.SSRConfig;
import moze_intel.ssr.utils.TierHandler;
import moze_intel.ssr.utils.Utils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class SoulCageTile extends TileEntity implements IInventory {
	private ItemStack inventory;
	private int counter;
	private int updateCounter;
	private int tier;
	private String entName;
	private boolean redstoneActive;
	private boolean initChecks;

	public SoulCageTile() {
		counter = 0;
		updateCounter = 0;
		redstoneActive = false;
		initChecks = false;
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}

		if (!initChecks) {
			checkRedstone();
			initChecks = true;
		}

		if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 0
				|| tier <= 0) {
			updateCounter = 0;
			counter = 0;
			return;
		}

		if (updateCounter == 19) {
			EntityLiving ent = EntityMapper.getNewEntityInstance(this.worldObj,
					entName);

			if (canEntitySpawn(ent)) {
				setMetadata(2);
			} else {
				setMetadata(1);
			}

			updateCounter = 0;
		} else {
			updateCounter++;
		}

		if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) <= 1) {
			counter = 0;
			return;
		}

		if (counter >= ((TierHandler.getCooldown(tier - 1) * 20) - 1)) {
			if (SSRConfig.ENABLE_DEBUG) {
				System.out.println("SPAWNING!");
			}
			EntityLiving[] toSpawn = new EntityLiving[TierHandler
					.getNumSpawns(tier - 1)];

			ItemStack heldItem = Utils.getEntityHeldItem(inventory);

			for (int i = 0; i < toSpawn.length; i++) {
				toSpawn[i] = EntityMapper.getNewEntityInstance(this.worldObj,
						entName);

				if (toSpawn[i] instanceof EntitySlime) {
					toSpawn[i].getDataWatcher().updateObject(16, (byte) 1);
				}

				if (heldItem != null) {
					toSpawn[i].setCurrentItemOrArmor(0, heldItem);
				}

				toSpawn[i].getEntityData().setBoolean("SSR", true);

				// Spawn when force chunk-loaded
				toSpawn[i].forceSpawn = true;

				// Set persistence
				toSpawn[i].func_110163_bv();
			}

			spawnEntities(toSpawn);
			counter = 0;
		} else {
			counter++;
		}
	}

	public void checkRedstone() {
			if (worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)){
				redstoneActive = true;
			}else{
				redstoneActive = false;
			}	
	}

	private void setMetadata(int meta) {
		if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord,
				this.zCoord) != meta) {
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord,
					this.zCoord, meta, 2);
		}
	}

	private boolean canEntitySpawn(EntityLiving ent) {
		if (SSRConfig.ENABLE_FLOOD_PREVENTION && hasReachedSpawnLimit(ent)) {
			return false;
		}

		if (TierHandler.getChecksRedstone(tier - 1)
				&& redstoneActive == SSRConfig.INVERT_REDSTONE) {
			return false;
		}

		if (TierHandler.getChecksPlayer(tier - 1)
				&& !isPlayerClose(this.xCoord, this.yCoord, this.zCoord)) {
			return false;
		}

		if (TierHandler.getChecksLight(tier - 1) && !canSpawnInLight(ent)) {
			return false;
		}

		if (TierHandler.getChecksWorld(tier - 1) && !canSpawnInWorld(ent)) {
			return false;
		}

		return true;
	}

	private boolean isPlayerClose(int x, int y, int z) {
		return (worldObj.getClosestPlayer(x, y, z, 16) != null);
	}

	private boolean canSpawnInWorld(EntityLiving ent) {
		int dimension = worldObj.provider.dimensionId;

		if (ent instanceof EntitySkeleton) {
			EntitySkeleton skele = (EntitySkeleton) ent;

			if (skele.getSkeletonType() == 1 && dimension == -1) {
				return true;
			} else if (dimension == 0) {
				return true;
			} else {
				return false;
			}
		} else if (ent instanceof EntityBlaze || ent instanceof EntityPigZombie
				|| ent instanceof EntityGhast || ent instanceof EntityMagmaCube) {
			return (dimension == -1);
		} else if (ent instanceof EntityEnderman) {
			return (dimension == 1);
		} else {
			return true;
		}
	}

	private boolean canSpawnInLight(EntityLiving ent) {
		int light = worldObj.getBlockLightValue(xCoord, yCoord, zCoord);

		if (ent instanceof EntityMob || ent instanceof IMob) {
			return light <= 8;
		} else if (ent instanceof EntityAnimal || ent instanceof IAnimals) {
			return light > 8;
		}

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
		int mobCount = 0;

		for (EntityLiving entity : (List<EntityLiving>) worldObj
				.getEntitiesWithinAABB(ent.getClass(), aabb)) {
			if (entity.getEntityData().getBoolean("SSR")) {
				mobCount += 1;
			}
		}

		return mobCount >= SSRConfig.MAX_NUM_ENTITIES;
	}

	private void spawnEntities(EntityLiving[] ents) {
		for (EntityLiving ent : ents) {
			int counter = 0;

			do {
				counter += 1;

				if (counter >= 5) {
					ent.setDead();
					break;
				}

				double x = xCoord
						+ (worldObj.rand.nextDouble() - worldObj.rand
								.nextDouble()) * 4.0D;
				double y = yCoord + worldObj.rand.nextInt(3) - 1;
				double z = zCoord
						+ (worldObj.rand.nextDouble() - worldObj.rand
								.nextDouble()) * 4.0D;
				ent.setLocationAndAngles(x, y, z,
						worldObj.rand.nextFloat() * 360.0F, 0.0F);
			} while (!canSpawnAtCoords(ent) || counter >= 5);

			if (!ent.isDead) {
				worldObj.spawnEntityInWorld(ent);
			}
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();

		initChecks = false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		inventory = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Shard"));

		if (inventory != null) {
			tier = Utils.getShardTier(inventory);
			entName = Utils.getShardBoundEnt(inventory);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		if (inventory != null) {
			NBTTagCompound tag = new NBTTagCompound();
			inventory.writeToNBT(tag);
			nbt.setTag("Shard", tag);
		}
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory;
	}

	@Override
	public ItemStack decrStackSize(int slot, int qty) {
		if (qty == 0) {
			return null;
		}

		ItemStack stack = inventory.copy();
		inventory = null;

		this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
		this.tier = 0;
		this.entName = null;

		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory = stack;

		this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);
		this.tier = Utils.getShardTier(inventory);
		this.entName = Utils.getShardBoundEnt(inventory);
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
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack != null && stack.getItem() == ObjHandler.SOUL_SHARD
				&& Utils.isShardBound(stack) && Utils.getShardTier(stack) > 0;
	}
}
