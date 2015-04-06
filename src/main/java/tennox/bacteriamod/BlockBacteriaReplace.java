package tennox.bacteriamod;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBacteriaReplace extends BlockContainer implements IBlockWithName {
	private static final String name = "tennox_replacer";

	protected BlockBacteriaReplace() {
		super(Material.rock);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		
		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.07F);
	}

	@Override
	public TileEntity createNewTileEntity(World w, int i) {
		return new TileEntityBacteriaReplacer();
	}

	@Override
	public String getName() {
		return name;
	}
}