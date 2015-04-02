package tennox.bacteriamod;

import java.util.ArrayList;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBacteria extends BlockContainer {
	ArrayList<Integer> food = new ArrayList<Integer>();

	protected BlockBacteria() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.07F);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("tennox_bacteria:normal");
	}

	@Override
	public TileEntity createNewTileEntity(World w, int i) {
		return new TileEntityBacteria();
	}
}