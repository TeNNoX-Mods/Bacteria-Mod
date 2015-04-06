package tennox.bacteriamod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class TileEntityBacteriaReplacer extends TileEntityBacteria {
	Food replace;

	public TileEntityBacteriaReplacer() {
		bacteriaBlock = Bacteria.replacer.getDefaultState();
		do
			colony = rand.nextInt();
		while (Bacteria.jamcolonies.contains(Integer.valueOf(colony)));
	}

	@Override
	public void selectFood() {
		if (worldObj.isBlockIndirectlyGettingPowered(getPos()) > 0) {
			IBlockState above = worldObj.getBlockState(getPos().up());
			IBlockState below = worldObj.getBlockState(getPos().down());
			if (above.getBlock() == Blocks.air || below.getBlock() == Blocks.air)
				return;
			if (above.getBlock() == Bacteria.replacer || below.getBlock() == Bacteria.replacer)
				return;
			if (above == below)
				return;
			addFood(below);
			replace = new Food(above);
			worldObj.setBlockToAir(getPos().up());
		}
	}

	@Override
	public boolean shouldStartInstantly() {
		return false;
	}

	@Override
	public void maybeEat(BlockPos pos) {
		if (isAtBorder(pos))
			return;
		if (isFood(worldObj.getBlockState(pos))) {
			worldObj.setBlockState(pos, bacteriaBlock);
			TileEntity newtile = worldObj.getTileEntity(pos);
			TileEntityBacteriaReplacer newtile2 = (TileEntityBacteriaReplacer) newtile;
			newtile2.food = food;
			newtile2.colony = colony;
			newtile2.replace = replace;
		}
	}

	@Override
	public void die() {
		if (replace != null)
			worldObj.setBlockState(getPos(), replace.state);
		else
			worldObj.setBlockToAir(getPos());
		if (jammed)
			ItemBacteriaJammer.num += 1L;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		Block r = Block.getBlockById(nbt.getInteger("replace"));
		replace = new Food(r.getStateFromMeta(nbt.getInteger("replace_meta")));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		if (replace != null) {
			nbt.setInteger("replace", Block.getIdFromBlock(replace.state.getBlock()));
			nbt.setInteger("replace_meta", replace.state.getBlock().getMetaFromState(replace.state));
		}
	}
}