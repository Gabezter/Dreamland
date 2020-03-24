package com.gmail.gabezter.worldgen.nightmare;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class NightmareGen extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		generator.setScale(0.005D);
		for (int X = 0; X < 16; X++)
			for (int Z = 0; Z < 16; Z++) {
				int currentHeight = (int) ((generator.noise(chunkX * 16 + X, chunkZ * 16 + Z, 0.5D, 0.5D, true) + 1)
						* 15D + 50D);
				Random rand = new Random();
				placeBlock(X, Z, currentHeight, chunk, rand);
				placeBlock(X, Z, currentHeight - 1, chunk, rand);
				placeBlock(X, Z, currentHeight - 2, chunk, rand);
				placeBlock(X, Z, currentHeight - 3, chunk, rand);
				placeBlock(X, Z, currentHeight - 4, chunk, rand);
				for (int Y = currentHeight - 5; Y > 0; Y--)
					chunk.setBlock(X, Y, Z, Material.getMaterial("NETHERRACK"));
				chunk.setBlock(X, 0, Z, Material.getMaterial("BEDROCK"));
			}
		return chunk;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList((BlockPopulator) new NightmarePopulator());
	}

	private void placeBlock(int X, int Z, int currentHeight, ChunkData chunk, Random rand) {
		switch (rand.nextInt(15)) {
		case 0:
		case 7:
			chunk.setBlock(X, currentHeight, Z, Material.getMaterial("GRASS"));
			break;
		case 1:
		case 8:
			chunk.setBlock(X, currentHeight, Z, Material.getMaterial("DIRT"));
			break;
		case 2:
		case 9:
			chunk.setBlock(X, currentHeight, Z, Material.getMaterial("STONE"));
			break;
		case 3:
		case 10:
			 chunk.setBlock(X, currentHeight, Z, Material.getMaterial("END_STONE"));
			 break;
		case 4:
		case 11:
			chunk.setBlock(X, currentHeight, Z, Material.getMaterial("OBSIDIAN"));
			break;
		case 5:
		case 12:
			chunk.setBlock(X, currentHeight, Z, Material.getMaterial("COBBLESTONE"));
			break;
		case 6:
		case 13:
		case 14:
		default:
			chunk.setBlock(X, currentHeight, Z, Material.getMaterial("NETHERRACK"));
			break;
		}
	}

}
