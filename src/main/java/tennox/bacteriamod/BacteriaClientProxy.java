package tennox.bacteriamod;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BacteriaClientProxy extends BacteriaCommonProxy {
	@Override
	public void registerRenderInformation() {
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		// ITEMS //
		registerItemModel(Bacteria.bacteriaBunch, 0);
		registerItemModel(Bacteria.bacteriaPotion, 0);
		registerItemModel(Bacteria.jammerItem, 0);

		// BLOCKS //
		registerBlockModel(Bacteria.bacteria, 0);
		registerBlockModel(Bacteria.replacer, 0);
		registerBlockModel(Bacteria.jammer, 0);
		registerBlockModel(Bacteria.must, 0);
	}

	private void registerBlockModel(IBlockWithName block, int defaultSubtype) {
		Item item = Item.getItemFromBlock((Block) block);
		ModelResourceLocation res = new ModelResourceLocation(Bacteria.prependModID(block.getName()), "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, defaultSubtype, res);
	}

	public void registerItemModel(IItemWithName item, int defaultSubtype) {
		ModelResourceLocation res = new ModelResourceLocation(Bacteria.prependModID(item.getName()), "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register((Item) item, defaultSubtype, res);
	}
}