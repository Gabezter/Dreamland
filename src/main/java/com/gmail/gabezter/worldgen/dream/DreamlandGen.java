package com.gmail.gabezter.worldgen.dream;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class DreamlandGen extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.01D);
		for (int X = 0; X < 16; X++)
			for (int Z = 0; Z < 16; Z++) {
				int currentHeight = (int) ((generator.noise(chunkX * 16 + X, chunkZ * 16 + Z, 0.5D, 0.5D, true) + 1)
						* 75D + 25D);
				chunk.setBlock(X, currentHeight, Z, Material.GRASS);
				chunk.setBlock(X, currentHeight-1, Z, Material.DIRT);
				for(int i = currentHeight -2 ; i >= 0; i--)
					chunk.setBlock(X, i, Z, Material.STONE);
				chunk.setBlock(X, 0, Z, Material.BEDROCK);
			}
		return chunk;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new DreamlandPopulator());
	}
}
