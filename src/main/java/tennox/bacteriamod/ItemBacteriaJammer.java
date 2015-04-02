package tennox.bacteriamod;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemBacteriaJammer extends Item {
	int tick;
	static long num;

	public ItemBacteriaJammer() {
		super();
		maxStackSize = 64;
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("tennox_bacteria:jammeritem");
	}

	@Override
	public void onUpdate(ItemStack item, World world, Entity entity, int i, boolean flag) {
		if (tick > 0) {
			tick -= 1;
			if (tick == 0) {
				Bacteria.jam_all = false;
				((EntityPlayer) entity).addChatMessage(new ChatComponentText("Jammed " + num + " bacteria!"));
				num = 0L;
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		if (world.isRemote)
			return item;
		Bacteria.jam_all = true;
		tick = 30;
		if (Bacteria.achievements)
			player.addStat(Bacteria.jamAchievement, 1);
		player.addChatMessage(new ChatComponentText("Jamming bacteria..."));
		return item;
	}
}