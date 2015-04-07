package tennox.bacteriamod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBacteriaPotion extends Item implements IItemWithName {

	private final String name = "potion";

	public ItemBacteriaPotion() {
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(Bacteria.MODID + "-" + name);
		
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	// EntityPotion
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode) {
			--itemstack.stackSize;
		}

		world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote) {
			world.spawnEntityInWorld(new EntityBacteriaPotion(world, player, itemstack));
		}

		return itemstack;
	}

	@Override
	public String getName() {
		return name;
	}
}