package com.bartz24.voidislandcontrol.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bartz24.voidislandcontrol.config.ConfigOptions;
import com.google.common.base.Strings;

import net.minecraft.command.CommandGive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class IslandManager {
	public static ArrayList<IslandGen> IslandGenerations = new ArrayList<IslandGen>();

	public static ArrayList<IslandPos> CurrentIslandsList = new ArrayList<IslandPos>();

	public static ArrayList<String> spawnedPlayers = new ArrayList<String>();

	public static boolean worldOneChunk = false;
	public static int initialIslandDistance = ConfigOptions.islandDistance;

	public static void registerIsland(IslandGen gen) {
		IslandGenerations.add(gen);
	}

	public static List<String> getIslandGenTypes() {
		List<String> types = new ArrayList<String>();
		for (IslandGen g : IslandGenerations)
			types.add(g.Identifier);

		return types;
	}

	public static int getIndexOfIslandType(String type) {
		for (int i = 0; i < IslandGenerations.size(); i++)
			if (IslandGenerations.get(i).Identifier.equals(type))
				return i;
		return -1;
	}

	public static IslandPos getNextIsland() {
		int size = (int) Math.floor(Math.sqrt(CurrentIslandsList.size()));
		if (size % 2 == 0 && size > 0)
			size--;

		size = (size + 1) / 2;
		for (int x = -size; x <= size; x++) {
			for (int z = -size; z <= size; z++) {
				if (!hasPosition(x, z)) {
					return new IslandPos(x, z);
				}
			}
		}
		return null;
	}

	public static IslandPos getPlayerIsland(UUID playerUUID) {
		for (IslandPos pos : CurrentIslandsList) {
			if (pos.getPlayerUUIDs().contains(playerUUID.toString()))
				return pos;
		}
		return null;
	}

	public static List<String> getPlayerNames(World world) {
		List<String> names = new ArrayList();
		for (IslandPos pos : CurrentIslandsList) {
			for (String s : pos.getPlayerUUIDs())

				names.add(world.getPlayerEntityByUUID(UUID.fromString(s)).getName());
		}
		return names;
	}

	public static boolean hasPosition(int x, int y) {
		for (IslandPos pos : CurrentIslandsList) {
			if (pos.getX() == x && pos.getY() == y)
				return true;
		}

		return false;
	}

	public static boolean playerHasIsland(UUID playerUUID) {
		for (IslandPos pos : CurrentIslandsList) {
			if (pos.getPlayerUUIDs().contains(playerUUID.toString()))
				return true;
		}

		return false;
	}

	public static void addPlayer(UUID playerUUID, IslandPos posAdd) {
		for (IslandPos pos : CurrentIslandsList) {
			if (pos.getX() == posAdd.getX() && pos.getY() == posAdd.getY()) {
				pos.addNewPlayer(playerUUID);
				return;
			}
		}
	}

	public static void removePlayer(UUID playerUUID) {
		IslandPos pos = getPlayerIsland(playerUUID);
		pos.removePlayer(playerUUID);
	}

	public static boolean hasPlayerSpawned(UUID playerUUID) {
		return spawnedPlayers.contains(playerUUID.toString());
	}

	public static void setStartingInv(EntityPlayerMP player) {
		player.inventory.clear();

		try {
			for (int i = 0; i < ConfigOptions.startingItems.size(); i++) {
				String s = ConfigOptions.startingItems.get(i);
				if (!Strings.isNullOrEmpty(s) && s.contains(":") && s.contains("*")) {
					String trimmed = s.replaceAll(" ", "");
					String itemName = trimmed.split(":")[0] + ":" + trimmed.split(":")[1];
					int meta = Integer.parseInt(trimmed.split(":")[2].split("\\*")[0]);
					int amt = Integer.parseInt(trimmed.split(":")[2].split("\\*")[1]);

					Item item = CommandGive.getItemByText(player, itemName);

					ItemStack stack = new ItemStack(item, amt, meta);

					player.inventory.setInventorySlotContents(i, stack);
				}
			}
		} catch (Exception e) {
			player.inventory.clear();
			player.sendMessage(
					new TextComponentString(TextFormatting.RED + "Error getting starting inventory.\n" + e.toString()));
		}
	}

	public static void tpPlayerToPos(EntityPlayer player, BlockPos pos) {
		if (!player.world.isAirBlock(pos) && !player.world.isAirBlock(pos.up())) {
			pos = player.world.getTopSolidOrLiquidBlock(pos);

			player.sendMessage(new TextComponentString("Failed to spawn. Sent to top block of platform spawn."));
		}
		player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 60, 20, false, false));

		player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 2.6, pos.getZ() + 0.5);
	}

	public static void tpPlayerToPosSpawn(EntityPlayer player, BlockPos pos) {
		tpPlayerToPos(player, pos);
		player.setSpawnPoint(pos, true);
	}
}