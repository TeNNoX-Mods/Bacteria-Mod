package tennox.bacteriamod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "tennox_bacteria", name = "Bacteria", version = "2.3.3")
public class Bacteria {
	static boolean achievements = false;

	@SidedProxy(clientSide = "tennox.bacteriamod.BacteriaClientProxy", serverSide = "tennox.bacteriamod.BacteriaCommonProxy")
	public static BacteriaCommonProxy proxy;
	public static Logger logger;
	public static BacteriaWorldGenerator worldGen = new BacteriaWorldGenerator();
	public static boolean randomize;
	public static String isolation;
	public static ArrayList<Food> blacklist;
	public static int speed;
	public static int jamAchievementID;
	public static Item bacteriaBunch;
	public static Item jammerItem;
	public static Item bacteriaPotion;
	public static Block bacteria;
	public static Block replacer;
	public static Block marker;
	public static Block jammer;
	public static Block must;
	public static Achievement mustAchievement;
	public static Achievement bacteriaAchievement;
	public static Achievement bacteriumAchievement;
	public static Achievement jamAchievement;
	public static ArrayList<Integer> jamcolonies = new ArrayList<Integer>();
	public static boolean jam_all;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		proxy.registerRenderInformation();
		logger = event.getModLog();

		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		achievements = config.get("General", "Enable achievements", true).getBoolean(true);

		isolation = config.get("General", "isolation block", "brick_block").getString();
		speed = config.get("General", "bacteria speed", 50).getInt();
		randomize = config.get("General", "randomize bacteria spread", true).getBoolean(true);
		String blacklist1 = config.get("General", "blacklist", "").getString();
		config.save();

		blacklist = new ArrayList<Food>();
		if (blacklist1.length() > 0) {
			for (String s : blacklist1.split(",")) {
				try {
					int meta = 0;
					if (s.contains(":")) {
						String[] s2 = s.split(":");
						s = s2[0];
						meta = Integer.parseInt(s2[1]);
					}

					int id = Integer.parseInt(s);
					Block block = Block.getBlockById(id);
					if (block == Blocks.air) {
						logger.error("Error while parsing blacklist: ID " + id + " is not a valid block!");
					} else {
						blacklist.add(new Food(block, meta));
					}
				} catch (NumberFormatException e) {
					logger.error("Error while parsing blacklist: '" + s + "' is not a valid number!");
					continue;
				}
			}
		}

		bacteriaBunch = new ItemBacteria().setUnlocalizedName("tennox_bacteriaitem");
		jammerItem = new ItemBacteriaJammer().setUnlocalizedName("tennox_jammeritem");
		bacteriaPotion = new ItemBacteriaPotion().setUnlocalizedName("tennox_bacteriapotion");

		bacteria = new BlockBacteria().setBlockName("tennox_bacteria");
		replacer = new BlockBacteriaReplace().setBlockName("tennox_replacer");
		jammer = new BlockBacteriaJammer().setBlockName("tennox_jammer");
		must = new BlockMust().setBlockName("tennox_must");

		GameRegistry.registerBlock(bacteria, "bacteria");
		GameRegistry.registerBlock(replacer, "replacer");
		GameRegistry.registerBlock(jammer, "jammer");
		GameRegistry.registerBlock(must, "must");
		GameRegistry.registerItem(bacteriaBunch, "bunch");
		GameRegistry.registerItem(jammerItem, "jammerItem");
		GameRegistry.registerItem(bacteriaPotion, "potion");
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {

		GameRegistry.addRecipe(new ItemStack(jammer, 1),
				new Object[] { "+#+", "#*#", "+-+", Character.valueOf('#'), bacteria, Character.valueOf('*'), Items.iron_ingot, Character.valueOf('-'), Blocks.redstone_torch,
						Character.valueOf('+'), Blocks.cobblestone });
		GameRegistry.addRecipe(new ItemStack(jammerItem, 1), new Object[] { " # ", "#*#", " - ", Character.valueOf('#'), bacteria, Character.valueOf('*'), Items.iron_ingot,
				Character.valueOf('-'), Blocks.redstone_torch });
		GameRegistry.addRecipe(new ItemStack(bacteria, 1),
				new Object[] { " # ", "#*#", " # ", Character.valueOf('#'), bacteriaBunch, Character.valueOf('*'), Blocks.redstone_torch });
		GameRegistry.addRecipe(new ItemStack(replacer, 1), new Object[] { " # ", "#*#", " # ", Character.valueOf('#'), bacteriaBunch, Character.valueOf('*'), Items.coal });
		GameRegistry.addRecipe(new ItemStack(must, 1),
				new Object[] { "+*+", " # ", Character.valueOf('+'), Items.bread, Character.valueOf('#'), Items.water_bucket, Character.valueOf('*'), Blocks.sponge });
		GameRegistry.addRecipe(new ItemStack(Blocks.sponge, 2), new Object[] { "+*+", "*+*", "+#+", Character.valueOf('+'), Blocks.wool, Character.valueOf('#'),
				Items.water_bucket, Character.valueOf('*'), Blocks.yellow_flower });

		GameRegistry.addRecipe(new ItemStack(Blocks.sponge, 2), new Object[] { "+*+", "*+*", "+#+", Character.valueOf('+'), Blocks.wool, Character.valueOf('#'),
				Items.water_bucket, Character.valueOf('*'), Blocks.yellow_flower });

		GameRegistry.addShapelessRecipe(new ItemStack(bacteriaPotion, 1), Items.potionitem, Items.nether_wart, bacteriaBunch);

		if (achievements) { // Achievements
			mustAchievement = new Achievement("bacteriamod.must", "must", 5, -2, must, AchievementList.buildWorkBench).registerStat();
			bacteriaAchievement = new Achievement("bacteriamod.bacteria", "bacteria", 5, -3, bacteriaBunch, mustAchievement).registerStat();
			bacteriumAchievement = new Achievement("bacteriamod.bacterium", "bacterium", 5, -4, bacteria, bacteriaAchievement).setSpecial().registerStat();
			jamAchievement = new Achievement("bacteriamod.jammer", "jammer", 5, -5, jammerItem, bacteriumAchievement).setSpecial().registerStat();
		}
		GameRegistry.registerTileEntity(TileEntityBacteria.class, "bacteria_tileentity");
		GameRegistry.registerTileEntity(TileEntityBacteriaReplacer.class, "replacer_tileentity");
		GameRegistry.registerWorldGenerator(worldGen, 0);
	}

	@EventHandler //TODO: This should be ForgeSubscribe, I think
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
		if (achievements && event.item.getEntityItem().getItem() == bacteriaBunch)
			event.entityPlayer.addStat(bacteriaAchievement, 1);
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) { // SlotCrafting
		if (achievements) {
			if (event.crafting.getItem() == Item.getItemFromBlock(must))
				event.player.addStat(mustAchievement, 1);
			if (event.crafting.getItem() == Item.getItemFromBlock(bacteria))
				event.player.addStat(bacteriumAchievement, 1);
		}
	}
}