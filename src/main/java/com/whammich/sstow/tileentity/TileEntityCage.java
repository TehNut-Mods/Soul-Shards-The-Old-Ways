package com.whammich.sstow.tileentity;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.EntityMapper;
import com.whammich.sstow.utils.ModLogger;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.TierHandler;
import com.whammich.sstow.utils.Utils;
import com.whammich.sstow.utils.Entitylist;

public class TileEntityCage extends TileEntity implements ISidedInventory {

	public static int INV_SIZE = 7;
	public ItemStack[] modules;
	private int counter;
	private int updateCounter;
	private int tier;
	private static final int[] slot = new int[] { 0 };

	private String entName;
	private String owner;
	private boolean redstoneActive;
	private boolean initChecks;
	public static boolean active;

	private String cageName;

	public TileEntityCage() {
		this.modules = new ItemStack[INV_SIZE];
		counter = 0;
		updateCounter = 0;
		redstoneActive = false;
		initChecks = false;
		active = false;
	}

	public void cageName(String string) {
		this.cageName = string;
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) {
			return;
		}

		if (Entitylist.bList.contains(entName)) {
			return;
		}

		if (!initChecks) {
			checkRedstone();
			initChecks = true;
		}

		if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 0 || tier <= 0) {
			updateCounter = 0;
			counter = 0;
			return;
		}

		if (updateCounter == 19) {
			EntityLiving ent = EntityMapper.getNewEntityInstance(this.worldObj, entName);

			if (canEntitySpawn(ent)) {
				setMetadata(2);
				active = true;

			} else {
				setMetadata(1);
				active = false;
			}
			updateCounter = 0;
		} else {
			updateCounter += 1;
		}

		if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) <= 1) {
			counter = 0;
			return;
		}

		if (counter >= TierHandler.getCooldown(tier - 1) * 20 - 1) {
			ModLogger.logDebug(Utils.localizeFormatted("chat.sstow.debug.successspawn", "" + entName));

			EntityLiving[] toSpawn = new EntityLiving[TierHandler.getNumSpawns(tier - 1)];

			ItemStack heldItem = Utils.getEntityHeldItem(modules[0]);
			for (int i = 0; i < toSpawn.length; i++) {
				toSpawn[i] = EntityMapper.getNewEntityInstance(this.worldObj, entName);

				if ((toSpawn[i] instanceof EntitySlime)) {
					toSpawn[i].getDataWatcher().updateObject(16, Byte.valueOf((byte) 1));
				}

				if (heldItem != null) {
					toSpawn[i].setCurrentItemOrArmor(0, heldItem);
				}

				for (int j = 1; j <= 4; j++) {
					toSpawn[i].setCurrentItemOrArmor(j, Utils.getEntityArmor(modules[0], j));
				}

				toSpawn[i].getEntityData().setBoolean("SSTOW", true);
				toSpawn[i].forceSpawn = true;
				toSpawn[i].func_110163_bv();
				toSpawn[i].spawnExplosionParticle();
			}
			spawnEntities(toSpawn);
			counter = 0;
		} else {
			counter += 1;
		}
	}

	private void setModule() {
		if (modules[1] != null) {
			ItemStack itemstack = modules[1];
			ItemStack itemstack1 = itemstack.copy();
			int var3 = itemstack.getItemDamage() + 2;
			if (modules[var3] == null) {
				this.modules[var3] = itemstack1;
				this.modules[1] = null;
			}
		}
	}

	public void checkRedstone() {
		if (worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {
			redstoneActive = true;
		} else {
			redstoneActive = false;
		}
	}

	private void setMetadata(int meta) {
		if (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) != meta) {
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, meta, 2);
		}
	}

	private boolean canEntitySpawn(EntityLiving ent) {
		if ((Config.floodPrevention) && (hasReachedSpawnLimit(ent))) {
			return false;
		}

		if (((Config.redstoneModule == false) 
				&& (TierHandler.getChecksRedstone(tier - 1)) && (redstoneActive == Config.invertRedstone))
				|| ((Config.redstoneModule == true) && (modules[2] != null) && (redstoneActive == Config.invertRedstone))) {
			return false;
		}

		if (((Config.playerModule == false)
				&& (TierHandler.getChecksPlayer(tier - 1)) && (!isPlayerClose(this.xCoord, this.yCoord, this.zCoord)))
				|| ((Config.playerModule == true) && (!isPlayerClose(this.xCoord, this.yCoord, this.zCoord)) 
				&& (modules[5] == null))) {
			return false;
		}

		if (((Config.lightModule == false)
				&& (TierHandler.getChecksLight(tier - 1)) && (!canSpawnInLight(ent)))
				|| ((Config.lightModule == true) && (!canSpawnInLight(ent)) && (modules[3] == null))) {
			return false;
		}

		if (((Config.dimensionModule == false)
				&& (TierHandler.getChecksWorld(tier - 1)) && (!canSpawnInWorld(ent)))
				|| ((Config.dimensionModule == true) && (!canSpawnInWorld(ent)) && (modules[4] == null))) {
			return false;
		}
		return true;
	}

	private boolean isPlayerClose(int x, int y, int z) {
		EntityPlayer entityPlayer = worldObj.getClosestPlayer(x, y, z, 16.0D);
		if ((Config.personalShard && entityPlayer != null && entityPlayer.getDisplayName().equals(owner))
				|| (!Config.personalShard && entityPlayer != null)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean canSpawnInWorld(EntityLiving ent) {
		int dimension = worldObj.provider.dimensionId;

		if ((ent instanceof EntitySkeleton)) {
			EntitySkeleton skele = (EntitySkeleton) ent;

			if ((skele.getSkeletonType() == 1) && (dimension == -1)) {
				return true;
			}
			if (dimension == 0) {
				return true;
			}
			return false;
		}

		if (((ent instanceof EntityBlaze))
				|| ((ent instanceof EntityPigZombie))
				|| ((ent instanceof EntityGhast))
				|| ((ent instanceof EntityMagmaCube))) {
			return dimension == -1;
		}
		if ((ent instanceof EntityEnderman)) {
			return dimension == 1;
		}
		return true;
	}

	private boolean canSpawnInLight(EntityLiving ent) {
		int light = worldObj.getBlockLightValue(xCoord, yCoord, zCoord);
		if (((ent instanceof EntityMob)) || ((ent instanceof IMob))) {
			return light <= 8;
		}
		if (((ent instanceof EntityAnimal)) || ((ent instanceof IAnimals))) {
			return light > 8;
		}
		return true;
	}

	private boolean canSpawnAtCoords(EntityLiving ent) {
		return worldObj.getCollidingBoundingBoxes(ent, ent.boundingBox).isEmpty();
	}

	@SuppressWarnings("unchecked")
	private boolean hasReachedSpawnLimit(EntityLiving ent) {
		AxisAlignedBB aabb = AxisAlignedBB
				.getBoundingBox(xCoord - 16, yCoord - 16, zCoord - 16,
						xCoord + 16, yCoord + 16, zCoord + 16);
		int mobCount = 0;

		for (EntityLiving entity : (List<EntityLiving>) worldObj
				.getEntitiesWithinAABB(ent.getClass(), aabb)) {
			if (entity.getEntityData().getBoolean("SSTOW")) {
				mobCount++;
			}
		}
		return mobCount >= Config.maxEntities;
	}

	private void spawnEntities(EntityLiving[] ents) {
		for (EntityLiving ent : ents) {
			int counter = 0;
			do {
				counter++;
				if (counter >= 5) {
					ent.setDead();
					break;
				}
				double x = xCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * 4.0D;
				double y = yCoord + worldObj.rand.nextInt(3) - 1;
				double z = zCoord + (worldObj.rand.nextDouble() - worldObj.rand.nextDouble()) * 4.0D;
				ent.setLocationAndAngles(x, y, z, worldObj.rand.nextFloat() * 360.0F, 0.0F);
			}
			while (!canSpawnAtCoords(ent) || counter >= 5);
			if (!ent.isDead) {
				worldObj.spawnEntityInWorld(ent);
			}
		}
	}

	public void invalidate() {
		super.invalidate();
		initChecks = false;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		int i;
		for (i = 0; i < 7; ++i) {
			modules[i] = ItemStack.loadItemStackFromNBT(nbt
					.getCompoundTag("Slot" + i));
		}
		if (modules[0] != null) {
			tier = Utils.getShardTier(modules[0]);
			entName = Utils.getShardBoundEnt(modules[0]);
			owner = Utils.getShardBoundPlayer(modules[0]);
		}
		if (nbt.hasKey("CustomName", 8)) {
			this.cageName = nbt.getString("CustomName");
		}
		active = nbt.getBoolean("active");
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		int i;
		for (i = 0; i < 7; ++i) {
			if (modules[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				modules[i].writeToNBT(tag);
				nbt.setTag("Slot" + i, tag);
			}
		}
		if (this.hasCustomInventoryName()) {
			nbt.setString("CustomName", this.cageName);
		}
		nbt.setBoolean("active", active);
	}

	public int getSizeInventory() {
		return 7;
	}

	public ItemStack getStackInSlot(int slot) {
		return modules[slot];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (this.modules[var1] != null) {
			ItemStack itemstack;
			if (this.modules[var1].stackSize <= var2) {
				itemstack = this.modules[var1];
				this.modules[var1] = null;
				this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
				this.tier = 0;
				this.entName = null;
				return itemstack;
			} else {
				itemstack = this.modules[var1].splitStack(var2);
				if (this.modules[var1].stackSize == 0) {
					this.modules[var1] = null;
				}
				this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord,
						zCoord, 0, 2);
				this.tier = 0;
				this.entName = null;
				return itemstack;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.modules[slot] = stack;
		if (this.modules[1] != null) {
			this.setModule();
		}
		if (this.modules[0] == null) {
			return;
		} else {
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord,
					this.zCoord, 1, 2);
			this.tier = Utils.getShardTier(this.modules[0]);
			this.entName = Utils.getShardBoundEnt(this.modules[0]);
			this.owner = Utils.getShardBoundPlayer(modules[0]);
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.modules != null) {
			ItemStack itemstack = this.modules[slot];
			this.modules[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.cageName
				: "container.soulcage";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.cageName != null && this.cageName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) != this ? false : player.getDistanceSq(
				(double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D,
				(double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	public int getTier() {
		return tier;
	}

	public String getEntityName() {
		return entName;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return stack != null && stack.getItem() == Register.ItemShardSoul
				&& Utils.isShardBound(stack) && Utils.getShardTier(stack) > 0;

	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {

		return var1 == 0 ? slot : (var1 == 1 ? slot : slot);
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack stack, int p_102007_3_) {
		return this.isItemValidForSlot(var1, stack);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {

		return true;
	}
}