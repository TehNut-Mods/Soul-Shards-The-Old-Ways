package com.whammich.sstow.entity.mob.hostile;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityZombieWitch extends EntityMob implements IRangedAttackMob {
	private static final UUID field_110184_bp = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier field_110185_bq = (new AttributeModifier(field_110184_bp, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
	private static final Item[] witchDrops = new Item[] {Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick, Items.rotten_flesh, Items.bone};
	private int witchAttackTimer;

	public EntityZombieWitch(World world) {
		super(world);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(21, Byte.valueOf((byte)0));
	}

	protected String getLivingSound() {
		return "mob.zombie.say";
	}

	protected String getHurtSound() {
		return "mob.zombie.hurt";
	}

	protected String getDeathSound() {
		return "mob.zombie.death";
	}

	public void setAggressive(boolean p_82197_1_) {
		this.getDataWatcher().updateObject(21, Byte.valueOf((byte)(p_82197_1_ ? 1 : 0)));
	}

	public boolean getAggressive() {
		return this.getDataWatcher().getWatchableObjectByte(21) == 1;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	public boolean isAIEnabled() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void onLivingUpdate() {
		if (!this.worldObj.isRemote) {
			if (this.getAggressive()) {
				if (this.witchAttackTimer-- <= 0) {
					this.setAggressive(false);
					ItemStack itemstack = this.getHeldItem();
					this.setCurrentItemOrArmor(0, (ItemStack)null);

					if (itemstack != null && itemstack.getItem() == Items.potionitem) {
						List list = Items.potionitem.getEffects(itemstack);

						if (list != null) {
							Iterator iterator = list.iterator();

							while (iterator.hasNext()) {
								PotionEffect potioneffect = (PotionEffect)iterator.next();
								this.addPotionEffect(new PotionEffect(potioneffect));
							}
						}
					}

					this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(field_110185_bq);
				}
			} else {
				short short1 = -1;

				if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing)) {
					short1 = 8237;
				} else if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance)) {
					short1 = 16307;
				} else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
					short1 = 16341;
				} else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					short1 = 16274;
				} else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
					short1 = 16274;
				}

				if (short1 > -1) {
					this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, short1));
					this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
					this.setAggressive(true);
					IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
					iattributeinstance.removeModifier(field_110185_bq);
					iattributeinstance.applyModifier(field_110185_bq);
				}
			}

			if (this.rand.nextFloat() < 7.5E-4F) {
				this.worldObj.setEntityState(this, (byte)15);
			}

			if (this.worldObj.isDaytime()) {
				float f = this.getBrightness(1.0F);

				if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))) {
					boolean flag = true;
					ItemStack itemstack = this.getEquipmentInSlot(4);

					if (itemstack != null) {
						if (itemstack.isItemStackDamageable()) {
							itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

							if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage()) {
								this.renderBrokenItemStack(itemstack);
								this.setCurrentItemOrArmor(4, (ItemStack)null);
							}
						}

						flag = false;
					}

					if (flag) {
						this.setFire(8);
					}
				}
			}

		}

		super.onLivingUpdate();
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 15) {
			for (int i = 0; i < this.rand.nextInt(35) + 10; ++i) {
				this.worldObj.spawnParticle("witchMagic", this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.boundingBox.maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	protected float applyPotionDamageCalculations(DamageSource dmgSrc, float dmgAmt) {
		dmgAmt = super.applyPotionDamageCalculations(dmgSrc, dmgAmt);

		if (dmgSrc.getEntity() == this) {
			dmgAmt = 0.0F;
		}

		if (dmgSrc.isMagicDamage()) {
			dmgAmt = (float)((double)dmgAmt * 0.15D);
		}

		return dmgAmt;
	}

	protected void dropFewItems(boolean var1, int var2) {
		int j = this.rand.nextInt(3) + 1;

		for (int k = 0; k < j; ++k) {
			int l = this.rand.nextInt(3);
			Item item = witchDrops[this.rand.nextInt(witchDrops.length)];

			if (var2 > 0) {
				l += this.rand.nextInt(var2 + 1);
			}

			for (int i1 = 0; i1 < l; ++i1) {
				this.dropItem(item, 1);
			}
		}
	}

	public void attackEntityWithRangedAttack(EntityLivingBase entity, float var2) {
		if (!this.getAggressive()) {
			EntityPotion entitypotion = new EntityPotion(this.worldObj, this, 32732);
			entitypotion.rotationPitch -= -20.0F;
			double d0 = entity.posX + entity.motionX - this.posX;
			double d1 = entity.posY + (double)entity.getEyeHeight() - 1.100000023841858D - this.posY;
			double d2 = entity.posZ + entity.motionZ - this.posZ;
			float f1 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);

			if (f1 >= 8.0F && !entity.isPotionActive(Potion.moveSlowdown)) {
				entitypotion.setPotionDamage(32698);
			} else if (entity.getHealth() >= 8.0F && !entity.isPotionActive(Potion.poison)) {
				entitypotion.setPotionDamage(32660);
			} else if (f1 <= 3.0F && !entity.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F) {
				entitypotion.setPotionDamage(32696);
			}

			entitypotion.setThrowableHeading(d0, d1 + (double)(f1 * 0.2F), d2, 0.75F, 8.0F);
			this.worldObj.spawnEntityInWorld(entitypotion);
		}
	}
}