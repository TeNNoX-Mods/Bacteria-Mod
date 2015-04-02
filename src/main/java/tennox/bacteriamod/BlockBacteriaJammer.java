package tennox.bacteriamod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockBacteriaJammer extends Block {
	public BlockBacteriaJammer() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.5F);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("tennox_bacteria:jammer");
	}

	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer player) {
	}
}