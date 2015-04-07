package tennox.bacteriamod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBacteriaJammer extends Block implements IBlockWithName {

	private final String name = "jammer";

	public BlockBacteriaJammer() {
		super(Material.rock);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(Bacteria.MODID + "-" + name);

		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.5F);
	}

	@Override
	public String getName() {
		return name;
	}
}
