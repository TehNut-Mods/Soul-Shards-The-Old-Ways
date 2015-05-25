package com.whammich.sstow.entity.mob.hostile.render;

import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.ResourceLocation;

import com.whammich.sstow.utils.Reference;

public class RenderZombieWitch extends RenderWitch {
	private static final ResourceLocation witchTextures = new ResourceLocation(Reference.modID + ":textures/entity/zombieWitch.png");
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityWitch p_110775_1_) {
		return witchTextures;
	}
}
