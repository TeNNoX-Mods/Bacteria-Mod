package tennox.bacteriamod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

class EntityBacteriaPotion extends EntityPotion {

	public EntityBacteriaPotion(World world) {
		super(world);
	}

	public EntityBacteriaPotion(World world, EntityPlayer player, ItemStack itemstack) {
		super(world, player, itemstack);
	}

	@Override
	protected void onImpact(MovingObjectPosition objpos) {
		BlockPos pos = objpos.getBlockPos();
		if (objpos.typeOfHit == MovingObjectType.BLOCK) { // RenderPotion
			IBlockState state = worldObj.getBlockState(pos);

			if (TileEntityBacteria.isValidFood(state)) {
				worldObj.setBlockState(pos, Bacteria.bacteria.getDefaultState());
				TileEntity t = worldObj.getTileEntity(pos);

				if (t != null && t instanceof TileEntityBacteria) {
					TileEntityBacteria tile = (TileEntityBacteria) t;
					tile.addFood(state);
					if (tile.shouldStartInstantly())
						tile.startInstantly = true;
				}
			}

			this.worldObj.playAuxSFX(2002, this.getPosition(), this.getPotionDamage());
			this.setDead();
		}
	}

	@Override
	public int getPotionDamage() { // 6,8,12 //TODO what is this? comment this please. (magic numbers :P)
		return 12;
	}
}