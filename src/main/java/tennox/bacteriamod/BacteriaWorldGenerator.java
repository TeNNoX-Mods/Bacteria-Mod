package tennox.bacteriamod;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class BacteriaWorldGenerator implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (int x = 0; x < 5; x++) {
			int i = chunkX * 16 + random.nextInt(16);
			int j = random.nextInt(100) + 50;
			int k = chunkZ * 16 + random.nextInt(16);
			Block above = world.getBlock(i, j + 1, k);
			Block current = world.getBlock(i, j, k);
			Block below = world.getBlock(i, j - 1, k);

			if (!below.equals(Blocks.water) && current.equals(Blocks.water) && above.equals(Blocks.water)) {
				world.setBlock(i, j, k, Blocks.sponge);
				i = -10;
			}
		}
	}
}