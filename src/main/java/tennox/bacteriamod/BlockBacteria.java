package tennox.bacteriamod;

import java.util.ArrayList;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBacteria extends BlockContainer implements IBlockWithName {
	
	public static final String name = "tennox_bacteria";
	
	ArrayList<Integer> food = new ArrayList<Integer>();

	protected BlockBacteria() {
		super(Material.rock);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		
		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.07F);
	}

	@Override
	public TileEntity createNewTileEntity(World w, int i) {
		return new TileEntityBacteria();
	}

	@Override
	public String getName() {
		return name;
	}
}