package com.gmail.gabezter.worldgen.dream;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class DreamlandPopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		if (random.nextBoolean()) {
			int amount = random.nextInt(5);
			for (int i = 0; i <= amount; i++) {
				int X = random.nextInt(15);
				int Z = random.nextInt(15);
				int Y;
				for (Y = world.getMaxHeight() - 1; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--)
					;
				switch (random.nextInt(TreeType.values().length)) {
				case 0:
				case 4:
				case 8:
				case 12:
					world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.TREE);
					break;
				case 1:
				case 5:
				case 9:
				case 13:
				case 16:
					world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.BIG_TREE);
					break;
				case 2:
				case 6:
				case 10:
				case 14:
					world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.TALL_BIRCH);
					break;
				case 3:
				case 7:
				case 11:
				case 15:
					world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.DARK_OAK);
					break;
				default:
					world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.MEGA_REDWOOD);
					break;
				}
			}
		}

	}

}
