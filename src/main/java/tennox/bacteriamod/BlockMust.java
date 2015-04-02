package tennox.bacteriamod;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMust extends Block {
	public int tick;
	public int growTime = 2;
	public boolean drop = true;

	protected BlockMust() {
		super(Material.sponge);
		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.6F);
		setTickRandomly(true);
		setStepSound(Block.soundTypeGrass);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("tennox_bacteria:must");
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		if (iblockaccess.getBlockMetadata(i, j, k) >= growTime)
			return 2411556;
		return 16777215;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (!world.isRemote) {
			int meta = world.getBlockMetadata(i, j, k);
			Block above = world.getBlock(i, j + 1, k);

			if (above.equals(Blocks.water))
				meta++;
			else if (above.equals(Blocks.flowing_water) && random.nextInt(3) >= 1)
				meta++;
			if (!above.equals(Blocks.flowing_water) && !above.equals(Blocks.water))
				meta = 0;
			world.setBlockMetadataWithNotify(i, j, k, meta, 3);
		}
	}

	@Override
	public int quantityDropped(Random random) {
		boolean prevDrop = drop;
		drop = true;
		return prevDrop ? 1 : 0;
	}

	// Block
	@Override
	public Item getItemDropped(int i, Random random, int j) {
		if (i >= growTime)
			return Bacteria.bacteriaBunch;
		return Item.getItemFromBlock(this);
	}
}