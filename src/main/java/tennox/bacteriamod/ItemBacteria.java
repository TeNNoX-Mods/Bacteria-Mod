package tennox.bacteriamod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBacteria extends Item implements IItemWithName {

	private final String name = "bunch";

	public ItemBacteria() {
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(Bacteria.MODID + "-" + name);

		maxStackSize = 64;
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public String getName() {
		return name;
	}
}