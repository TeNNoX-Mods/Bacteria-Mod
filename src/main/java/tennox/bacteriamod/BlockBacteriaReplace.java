package tennox.bacteriamod;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBacteriaReplace extends BlockContainer implements IBlockWithName {
	private static final String name = "replacer";

	protected BlockBacteriaReplace() {
		super(Material.rock);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(Bacteria.MODID + "-" + name);

		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.07F);
	}

	// super method in BlockContainer returns -1
	@Override
	public int getRenderType() {
		return 3;
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