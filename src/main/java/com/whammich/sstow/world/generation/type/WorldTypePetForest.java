package com.whammich.sstow.world.generation.type;

import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerZoom;

public class WorldTypePetForest extends WorldType {

	public WorldTypePetForest(int par1, String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates the GenLayerBiome used for generating the world
	 *
	 * @param worldSeed
	 *            The world seed
	 * @param parentLayer
	 *            The parent layer to feed into any layer you return
	 * @return A GenLayer that will return ints representing the Biomes to be
	 *         generated, see GenLayerBiome
	 */
	public GenLayer getBiomeLayer(long worldSeed, GenLayer parentLayer) {
		GenLayer ret = new PetForestGenLayerBiome(200L, parentLayer, this);
		ret = GenLayerZoom.magnify(1000L, ret, 2);
		ret = new GenLayerBiomeEdge(1000L, ret);
		return ret;
	}

}
