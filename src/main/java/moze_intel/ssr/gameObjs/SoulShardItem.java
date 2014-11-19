package moze_intel.ssr.gameObjs;

import java.util.List;

import moze_intel.ssr.utils.EntityMapper;
import moze_intel.ssr.utils.SSRConfig;
import moze_intel.ssr.utils.TierHandler;
import moze_intel.ssr.utils.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SoulShardItem extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon unbound;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public SoulShardItem() {
		this.setUnlocalizedName("soul_shard");
		this.setCreativeTab(ObjHandler.CREATIVE_TAB);
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
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player) {
		if (world.isRemote || (Utils.hasMaxedKills(stack))
				|| !SSRConfig.ALLOW_SPAWNER_ABSORB) {
			return stack;
		}

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(
				world, player, false);

		if (mop == null
				|| mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
			return stack;
		}

		TileEntity tile = world.getTileEntity(mop.blockX, mop.blockY,
				mop.blockZ);

		if (tile instanceof TileEntityMobSpawner) {
			String name = ((TileEntityMobSpawner) tile).func_145881_a()
					.getEntityNameToSpawn();

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
							(short) SSRConfig.SPAWNER_ABSORB_BONUS);
					Utils.checkForAchievements(player, stack);
					world.func_147480_a(mop.blockX, mop.blockY, mop.blockZ,
							false);
				}
			} else if (EntityMapper.isEntityValid(name)) {
				Utils.setShardBoundEnt(stack, name);
				Utils.writeEntityHeldItem(stack, (EntityLiving) ent);
				Utils.increaseShardKillCount(stack,
						(short) SSRConfig.SPAWNER_ABSORB_BONUS);
				world.func_147480_a(mop.blockX, mop.blockY, mop.blockZ, true);
			}
		}

		return stack;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (Utils.isShardBound(stack)) {
			return "item.ssr.shard";
		}

		return "item.ssr.shard_unbound";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack, int pass) {
		return Utils.hasMaxedKills(stack);
	}

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
		unbound = register.registerIcon("ssr:unbound");
		icons = new IIcon[6];
		for (int i = 0; i <= 5; i++) {
			icons[i] = register.registerIcon("ssr:tier" + i);
		}
	}
}
