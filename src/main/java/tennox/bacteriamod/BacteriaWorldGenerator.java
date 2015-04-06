package tennox.bacteriamod;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BacteriaWorldGenerator implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (int x = 0; x < 5; x++) {
			int i = chunkX * 16 + random.nextInt(16);
			int j = random.nextInt(100) + 50;
			int k = chunkZ * 16 + random.nextInt(16);
			BlockPos pos = new BlockPos(i, j, k);
			Block above = world.getBlockState(pos.up()).getBlock();
			Block current = world.getBlockState(pos).getBlock();
			Block below = world.getBlockState(pos.down()).getBlock();

			if (below != Blocks.water && current == Blocks.water && above == Blocks.water) {
				world.setBlockState(pos, Blocks.sponge.getDefaultState());
			}
		}
	}
}