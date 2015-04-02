package tennox.bacteriamod;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
	protected void onImpact(MovingObjectPosition pos) {
		if (pos.typeOfHit == MovingObjectType.BLOCK) { // RenderPotion
			Block block = worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
			int meta = worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);

			if (TileEntityBacteria.isValidFood(block, meta)) {
				worldObj.setBlock(pos.blockX, pos.blockY, pos.blockZ, Bacteria.bacteria, 0, 3);
				TileEntity t = worldObj.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);

				if (t != null && t instanceof TileEntityBacteria) {
					TileEntityBacteria tile = (TileEntityBacteria) t;
					tile.addFood(block, meta);
					if (tile.shouldStartInstantly())
						tile.startInstantly = true;
				}
			}

			this.worldObj.playAuxSFX(2002, (int) Math.round(this.posX), (int) Math.round(this.posY), (int) Math.round(this.posZ), this.getPotionDamage());
			this.setDead();
		}
	}

	@Override
	public int getPotionDamage() { // 6,8,12
		return 12;
	}
}