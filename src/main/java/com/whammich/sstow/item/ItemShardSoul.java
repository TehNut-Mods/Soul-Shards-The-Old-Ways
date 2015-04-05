package com.whammich.sstow.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.whammich.sstow.utils.Config;
import com.whammich.sstow.utils.EntityMapper;
import com.whammich.sstow.utils.Register;
import com.whammich.sstow.utils.TierHandler;
import com.whammich.sstow.utils.Utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemShardSoul extends Item {
	
	@SideOnly(Side.CLIENT)
	private IIcon unbound;
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	private int tier;
	
	public ItemShardSoul() {
		this.setCreativeTab(Register.CREATIVE_TAB);
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld) {
		if (world.isRemote) {
			return;
		}

		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}

		Utils.checkAndFixShard(stack);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		byte level = Utils.getShardTier(stack);
		tier = Utils.getShardTier(stack);

		if (world.isRemote || (Utils.hasMaxedKills(stack)) || !Config.ALLOW_SPAWNER_ABSORB) {
			return stack;
		}
		
		if(!Utils.isShardBound(stack) && level == 0) {
			player.addChatComponentMessage(new ChatComponentText(Utils.localize("sstow.shard.unbound")));
		} else if(level == 0) {
			player.addChatComponentMessage(new ChatComponentText("Soul Shard Tier " + level));
			player.addChatComponentMessage(new ChatComponentText("Bound to: " + Utils.getShardBoundEnt(stack)));
			player.addChatComponentMessage(new ChatComponentText("This shard cannot be used in a soul cage, you must level it up"));
		} else if(level > 0) {
			player.addChatComponentMessage(new ChatComponentText("Tier " + level));
			player.addChatComponentMessage(new ChatComponentText("Bound to: " + Utils.getShardBoundEnt(stack)));
			player.addChatComponentMessage(new ChatComponentText("Requires Player: " + TierHandler.getChecksPlayer((int) tier - 1)));
			player.addChatComponentMessage(new ChatComponentText("Requires Darkness: " + TierHandler.getChecksLight((int) tier - 1)));
			player.addChatComponentMessage(new ChatComponentText("Requires Worlds: " + TierHandler.getChecksWorld((int) tier - 1)));
			player.addChatComponentMessage(new ChatComponentText("Uses Redstone: " + TierHandler.getChecksRedstone((int) tier - 1)));
			player.addChatComponentMessage(new ChatComponentText("Spawn Amount: " + TierHandler.getNumSpawns((int) tier - 1)));
			player.addChatComponentMessage(new ChatComponentText("Spawn Rate: " + TierHandler.getCooldown((int) tier - 1)));
		}
		
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

		if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
			return stack;
		}

		TileEntity tile = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);

		if (tile instanceof TileEntityMobSpawner) {
			String name = ((TileEntityMobSpawner) tile).func_145881_a().getEntityNameToSpawn();

			Entity ent = EntityMapper.getNewEntityInstance(world, name);

			if (ent == null) {
				return stack;
			}

			ent = ((TileEntityMobSpawner) tile).func_145881_a().func_98265_a(
					ent);

			if (ent instanceof EntitySkeleton
					&& ((EntitySkeleton) ent).getSkeletonType() == 1) {
				name = "Wither Skeleton";
			}

			if (!EntityMapper.isEntityValid(name)) {
				return stack;
			}

			if (Utils.isShardBound(stack)) {
				if (Utils.getShardBoundEnt(stack).equals(name)) {
					Utils.increaseShardKillCount(stack,
							(short) Config.SPAWNER_ABSORB_BONUS);
//					Utils.checkForAchievements(player, stack);
					world.func_147480_a(mop.blockX, mop.blockY, mop.blockZ,
							false);
				}
			} else if (EntityMapper.isEntityValid(name)) {
				if (stack.stackSize > 1) {
					stack.stackSize--;
					ItemStack newStack = new ItemStack(Register.ItemShardSoul, 1);

					Utils.setShardBoundEnt(newStack, name);
					Utils.writeEntityHeldItem(newStack, (EntityLiving) ent);
					Utils.increaseShardKillCount(newStack,
							(short) Config.SPAWNER_ABSORB_BONUS);

					boolean emptySpot = false;
					int counter = 0;

					while (!emptySpot && counter < 36) {
						ItemStack inventoryStack = player.inventory
								.getStackInSlot(counter);
						if (inventoryStack == null) {
							player.inventory.addItemStackToInventory(newStack);
							emptySpot = true;
						}
						counter++;
					}

					if (!emptySpot) {
						player.worldObj.spawnEntityInWorld(new EntityItem(
								player.worldObj, player.posX, player.posY,
								player.posZ, newStack));
					}
				} else {
					Utils.setShardBoundEnt(stack, name);
					Utils.writeEntityHeldItem(stack, (EntityLiving) ent);
					Utils.increaseShardKillCount(stack,
							(short) Config.SPAWNER_ABSORB_BONUS);
				}
				world.func_147480_a(mop.blockX, mop.blockY, mop.blockZ, true);
			}
		}

		return stack;
	}

	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10) {
		if (world.isRemote) {

		}
		return true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (Utils.isShardBound(stack)) {
			return "item.sstow.shard";
		}

		return "item.sstow.shard.unbound";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		return Utils.hasMaxedKills(stack);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (int i = 0; i <= 5; i++) {
			ItemStack stack = new ItemStack(item, 1);

			Utils.setShardKillCount(stack, TierHandler.getMinKills(i));
			Utils.setShardTier(stack, (byte) i);

			list.add(stack);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list,
			boolean bool) {
		if (Utils.isShardBound(stack)) {
			list.add("Bound to: "
					+ Utils.getEntityNameTransltated(Utils
							.getShardBoundEnt(stack)));
		}

		if (Utils.getShardKillCount(stack) >= 0) {
			list.add("Kills: " + Utils.getShardKillCount(stack));
		}

		list.add("Tier: " + Utils.getShardTier(stack));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		if (!Utils.isShardBound(stack)) {
			return unbound;
		}

		return icons[Utils.getShardTier(stack)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		unbound = register.registerIcon("sstow:unbound");
		icons = new IIcon[6];
		for (int i = 0; i <= 5; i++) {
			icons[i] = register.registerIcon("sstow:tier" + i);
		}
	}
}
