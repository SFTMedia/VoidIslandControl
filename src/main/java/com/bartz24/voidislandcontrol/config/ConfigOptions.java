package com.bartz24.voidislandcontrol.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigOptions {
	public static Configuration config;

	public static String worldSpawnType;

	public static boolean netherVoid;

	public static String commandName;

	public static int islandDistance;
	public static int islandSize;
	public static int islandResetDistance;
	public static boolean spawnChest;
	public static boolean oneChunk;
	public static boolean oneChunkCommandAllowed;
	public static boolean overworldGen;
	public static List<String> startingItems;
	public static int singleBiomeID;
	public static int islandYSpawn;
	public static String bottomBlock;
	public static String fillBlock;
	public static int cloudHeight;
	public static int horizonHeight;
	public static boolean netherPortalLink;

	// Grass
	public static boolean enableGrassIsland;
	public static boolean spawnTree;

	// Sand
	public static boolean enableSandIsland;
	public static boolean spawnCactus;
	public static boolean redSand;

	// Snow
	public static boolean enableSnowIsland;
	public static boolean spawnIgloo;
	public static boolean spawnPumpkins;

	// Wood
	public static boolean enableWoodIsland;
	public static boolean spawnString;
	public static boolean spawnWater;
	public static int woodMeta;

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();

		list.addAll(new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
		list.addAll(new ConfigElement(config.getCategory("islands")).getChildElements());

		return list;
	}

	public static void setConfigSettings() {
		startingItems = new ArrayList<String>();

		worldSpawnType = config.get(Configuration.CATEGORY_GENERAL, "WorldSpawnType", "random",
				"Valids are random, sand, snow, wood, grass, or others added by addons").getString();

		islandDistance = config.get("islands", "Island Gap Distance", 1000).getInt(1000);

		islandResetDistance = Math.min(
				config.get("islands", "Island Reset Radius", 500, "Max is half of Island Distance").getInt(500),
				islandDistance / 2);

		islandSize = config.get("islands", "Island Width/Length", 3, "Works best with odd values").getInt(3);

		spawnChest = config.get("islands", "Spawn Chest", false, "Spawn a chest on island").getBoolean(false);

		netherVoid = config.get("islands", "Nether Void World", true, "Nether generates with nothing").getBoolean(true);

		commandName = config.get("islands", "Name For Command", "island").getString();
		netherPortalLink = config
				.get("islands", "1 to 1 Nether Portal", true,
						"Nether Portal will link to the same position as in the overworld (Instead of 1/8 the position)")
				.getBoolean(true);

		oneChunk = config.get("islands", "One Chunk Challenge", false,
				"Required before world creation as it changes the spawn platform").getBoolean(false);

		oneChunkCommandAllowed = config
				.get("islands", "Allow One Chunk Mode", false, "Allows for one chunk mode to be turned on in-game")
				.getBoolean(false);
		singleBiomeID = config
				.get("islands", "Single Biome ID", -1, "Sets the biome used for generation in void world -1=default")
				.getInt(-1);
		islandYSpawn = config.get("islands", "Island Y Spawn", 88,
				"Y Coord for islands to spawn (Set to 2 above where you want the ground block)").getInt(88);
		cloudHeight = config.get("islands", "Cloud Height (Void)", 32, "Sets the cloud height in the void world type")
				.getInt(32);
		horizonHeight = config
				.get("islands", "Horizon Height (Void)", 40, "Sets the height where skybox is darker (Ex: in caves)")
				.getInt(40);
		overworldGen = config
				.get("islands", "Generate As Overworld", false,
						"Uses default generation instead of void and follows the Single Biome ID config")
				.getBoolean(false);

		bottomBlock = config
				.get("islands", "Bottom Block (Void)", "minecraft:air", "Sets the bottom layer in the void world")
				.getString();
		fillBlock = config
				.get("islands", "Fill Block (Void)", "minecraft:air",
						"Sets the fill layers from the island Y Spawn down to the the bottom layer in the void world")
				.getString();

		String[] array = new String[36];
		for (int i = 0; i < 36; i++)
			array[i] = "";

		startingItems = Arrays.asList(config.getStringList("Starting Inventory", "startingInv", array,
				"The starting inventory Format: modid:itemid:meta*amt (All parameters required)"));
		// Grass
		enableGrassIsland = config.get("islands", "Enable Grass Island", true, "Allows grass island to be generated")
				.getBoolean(true);
		spawnTree = config.get("islands", "Spawn Tree (Grass)", true, "Spawns a tree on the island").getBoolean(true);

		// Sand
		enableSandIsland = config.get("islands", "Enable Sand Island", true, "Allows sand island to be generated")
				.getBoolean(true);
		spawnCactus = config.get("islands", "Spawn Cactus (Sand)", true, "Spawns a cactus on the island")
				.getBoolean(true);
		redSand = config
				.get("islands", "Red Sand? (Sand)", true, "True sets sand type to red sand, false for normal sand")
				.getBoolean(true);

		// Snow
		enableSnowIsland = config.get("islands", "Enable Snow Island", true, "Allows snow island to be generated")
				.getBoolean(true);
		spawnIgloo = config.get("islands", "Spawn Igloo (Snow)", false, "Spawn an igloo covering on island")
				.getBoolean(false);
		spawnPumpkins = config.get("islands", "Spawn Pumpkins (Snow)", true, "Spawns 2 pumpkins on the island")
				.getBoolean(true);
		// Wood
		enableWoodIsland = config.get("islands", "Enable Wood Island", true, "Allows wood island to be generated")
				.getBoolean(true);
		spawnWater = config.get("islands", "Spawn Water (Wood)", true, "Spawns a water source in middle of the island")
				.getBoolean(true);
		spawnString = config.get("islands", "Spawn String (Wood)", true, "Spawns a piece of string on the island")
				.getBoolean(true);
		woodMeta = config.get("islands", "Wood Metadata (Wood)", 5, "Sets metadata of wood spawned on the island")
				.getInt();

		if (config.hasChanged())
			config.save();
	}

	public static void loadConfigThenSave(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());

		config.load();
		setConfigSettings();
		config.save();
	}

	public static void reloadConfigs() {
		setConfigSettings();
		if (config.hasChanged())
			config.save();
	}
}