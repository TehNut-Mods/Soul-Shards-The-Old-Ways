package com.whammich.sstow.world.generation.biome;

import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.biome.BiomeGenBase;

import com.whammich.sstow.entity.mob.hostile.EntityZombieWitch;
import com.whammich.sstow.utils.Register;

public class BiomeGenPetForest extends BiomeGenBase {

	@SuppressWarnings("unchecked")
	public BiomeGenPetForest(int id) {
		super(id);
		//this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 5, 2, 10));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 5, 2, 10));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 5, 2, 10));
		this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityZombieWitch.class, 5, 2, 10));
		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 0;
		this.topBlock = Register.BlockXenolith;
		this.fillerBlock = Register.BlockXenolith;
		this.enableRain = false;
		this.enableSnow = false;
	}
}
