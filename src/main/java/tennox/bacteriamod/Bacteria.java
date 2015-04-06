package tennox.bacteriamod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

import org.apache.logging.log4j.Logger;

@Mod(modid = Bacteria.MODID, version = Bacteria.VERSION)
public class Bacteria {
	public static final String MODID = "tennox_bacteria";
	public static final String VERSION = "2.4";

	@Mod.Instance
	public static Bacteria instance;

	@SidedProxy(clientSide = "tennox.bacteriamod.BacteriaClientProxy", serverSide = "tennox.bacteriamod.BacteriaCommonProxy")
	public static BacteriaCommonProxy proxy;

	public static Logger logger;
	public static BacteriaWorldGenerator worldGen = new BacteriaWorldGenerator();
	public static boolean randomize;
	public static String isolation;
	public static ArrayList<Food> blacklist;
	public static int speed;
	public static int jamAchievementID;
	public static ItemBacteria bacteriaBunch;
	public static ItemBacteriaJammer jammerItem;
	public static ItemBacteriaPotion bacteriaPotion;
	public static BlockBacteria bacteria;
	public static BlockBacteriaReplace replacer;
	public static BlockBacteriaJammer jammer;
	public static BlockMust must;
	public static Achievement mustAchievement;
	public static Achievement bacteriaAchievement;
	public static Achievement bacteriumAchievement;
	public static Achievement jamAchievement;
	public static ArrayList<Integer> jamcolonies = new ArrayList<Integer>();
	public static boolean jam_all;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@SubscribeEvent
	// TODO: This should be ForgeSubscribe, I think
	public void onMapMissing(FMLMissingMappingsEvent event) {
		List<MissingMapping> list = event.get();

		for (MissingMapping m : list) {
			System.out.println("missing: " + m.name);
			if (m.name.equals("tennox_bacteria:Bunch of Bacteria")) {
				m.remap(bacteriaBunch);
			} else if (m.name.equals("tennox_bacteria:Bacteria Jammer")) {
				m.remap(jammerItem);
			} else if (m.name.equals("tennox_bacteria:Bacteria Potion")) {
				m.remap(bacteriaPotion);
			} else
				System.out.println("STILL MISSING: " + m.name);
		}
	}

	@SubscribeEvent
	public void onPickup(EntityItemPickupEvent event) {
		if (event.item.getEntityItem().getItem() == bacteriaBunch)
			event.entityPlayer.addStat(bacteriaAchievement, 1);
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) { // SlotCrafting
		if (event.crafting.getItem() == Item.getItemFromBlock(must))
			event.player.addStat(mustAchievement, 1);
		if (event.crafting.getItem() == Item.getItemFromBlock(bacteria))
			event.player.addStat(bacteriumAchievement, 1);
	}

	/**
	 * Prepend the name with the mod ID, suitable for ResourceLocations such as textures.
	 * 
	 * @param name
	 * @return eg "minecraftbyexample:myblockname"
	 */
	public static String prependModID(String name) {
		return MODID + ":" + name;
	}
}