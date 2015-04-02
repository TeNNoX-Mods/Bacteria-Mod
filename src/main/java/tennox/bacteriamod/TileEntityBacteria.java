package tennox.bacteriamod;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBacteria extends TileEntity {
	Block block;
	ArrayList<Food> food;
	Random rand = new Random();
	int colony;
	boolean jammed;
	int tick = 0;
	boolean startInstantly;

	public TileEntityBacteria() {
		if (food == null)
			food = new ArrayList<Food>();
		block = Bacteria.bacteria;
		do
			colony = rand.nextInt();
		while (Bacteria.jamcolonies.contains(Integer.valueOf(colony)));
	}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote)
			return;
		if (Bacteria.jamcolonies.contains(Integer.valueOf(colony)) || Bacteria.jam_all) {
			jammed = true;
			die();
			return;
		}

		if (food.size() == 0) {
			if (!worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
				return;
			selectFood();
			if (food.size() == 0)
				return;
			if (shouldStartInstantly())
				startInstantly = true;
		}
		if (!startInstantly) {
			if (Bacteria.randomize)
				tick = rand.nextInt(Bacteria.speed + 1);
			if (tick < Bacteria.speed) {
				tick += 1;
				return;
			}
			tick = 0;
		}

		eatEverything();
	}

	public boolean shouldStartInstantly() {
		return true;
	}

	public void selectFood() {
		int i = xCoord; // xCoord, yCoord, zCoord
		int j = yCoord + 1;
		int k = zCoord;

		Block b;
		while ((b = worldObj.getBlock(i, j, k)) != Blocks.air) {
			addFood(b, worldObj.getBlockMetadata(i, j, k));
			j++;
		}
	}

	public void addFood(Block block, int meta) {
		if (isValidFood(block, meta))
			food.add(new Food(block, meta));
	}

	public static boolean isValidFood(Block block, int meta) {
		if (block == Blocks.bedrock || block == Bacteria.bacteria)
			return false;
		return true;
	}

	public void eatEverything() {
		int i = xCoord;
		int j = yCoord;
		int k = zCoord;
		maybeEat(i + 1, j, k);
		maybeEat(i, j + 1, k);
		maybeEat(i - 1, j, k);
		maybeEat(i, j - 1, k);
		maybeEat(i, j, k + 1);
		maybeEat(i, j, k - 1);

		die();
	}

	public void maybeEat(int i, int j, int k) {
		if (isAtBorder(i, j, k))
			return;
		if (isFood(worldObj.getBlock(i, j, k), worldObj.getBlockMetadata(i, j, k))) {
			worldObj.setBlock(i, j, k, block);
			((TileEntityBacteria) worldObj.getTileEntity(i, j, k)).food = food;
			((TileEntityBacteria) worldObj.getTileEntity(i, j, k)).colony = colony;
		}
	}

	public boolean isAtBorder(int i, int j, int k) { // Block
		while (worldObj.getBlock(i, j, k) != Block.getBlockFromName(Bacteria.isolation)) {
			if (j >= worldObj.getActualHeight())
				return false;
			j++;
		}
		return true;
	}

	Food grass = new Food(Blocks.grass, 0);
	Food dirt = new Food(Blocks.dirt, 0);
	Food water = new Food(Blocks.water, 0);
	Food flowing_water = new Food(Blocks.flowing_water, 0);

	public boolean isFood(Block block, int meta) {
		if (Bacteria.jamcolonies.contains(Integer.valueOf(colony)))
			return false;
		if (block == Bacteria.jammer) {
			Bacteria.jamcolonies.add(Integer.valueOf(colony));

			jammed = true;
			return false;
		}

		for (Food f : Bacteria.blacklist) {
			if (isFood2(f, block, meta))
				return false;
		}

		for (Food f : food) {
			if (isFood2(f, block, meta))
				return true;
		}

		if (block == Blocks.grass)
			return food.contains(dirt);
		if (block == Blocks.dirt)
			return food.contains(grass);
		if (block == Blocks.flowing_water || block == Blocks.water) {
			for (Food f : food) {
				if (f.block == Blocks.water || f.block == Blocks.flowing_water)
					return true;
			}
		}
		if (block == Blocks.flowing_lava || block == Blocks.lava) {
			for (Food f : food) {
				if (f.block == Blocks.lava || f.block == Blocks.flowing_lava)
					return true;
			}
		}
		return false;
	}

	private boolean isFood2(Food f, Block block, int meta) {
		if (!block.equals(f.block))
			return false;
		if (Item.getItemFromBlock(block) != null && !Item.getItemFromBlock(block).getHasSubtypes())
			return true;
		return meta == f.meta;
	}

	public void die() {
		worldObj.setBlockToAir(xCoord, yCoord, zCoord); // x,y,z
		if (jammed)
			ItemBacteriaJammer.num += 1L;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (food == null)
			food = new ArrayList<Food>();

		colony = nbt.getInteger("colony");
		int i = nbt.getInteger("numfood");

		for (int j = 0; j < i; j++) {
			int id = nbt.getInteger("food" + j);
			int meta = nbt.getInteger("food_meta" + j + "");
			food.add(new Food(Block.getBlockById(id), meta));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setInteger("colony", colony);
		nbt.setInteger("numfood", food.size());

		for (int j = 0; j < food.size(); j++) {
			int id = Block.getIdFromBlock(food.get(j).block);

			nbt.setInteger("food" + j, id);
			nbt.setInteger("food_meta" + j, food.get(j).meta);
		}
	}
}

class Food {
	Block block;
	int meta;

	public Food(Block block, int meta) {
		this.block = block;
		this.meta = meta;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Food))
			return false;
		Food f = (Food) o;
		return block.equals(f.block) && meta == f.meta;
	}

	public String toString() {
		return String.format("Food[id=%d, meta=%d]", Block.getIdFromBlock(block), meta);
	}
}