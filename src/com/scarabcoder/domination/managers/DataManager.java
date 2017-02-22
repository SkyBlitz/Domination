package com.scarabcoder.domination.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.scarabcoder.domination.main.Main;

public class DataManager {
	
	/*
	 * Arena format:
	 * 	arena:
	 * 		active: false
	 * 		maxteamsize: 12
	 * 		minteamsize: 1
	 * 		team:
	 * 			red:
	 * 				spawn: location
	 * 			green:
	 * 				spawn: location
	 * 		lobbyspawn: location
	 * 		capturepoints:
	 * 			id1:
	 * 				loc1: location
	 * 				loc2: location
	 * 			id2:
	 * 				loc1: location
	 * 				loc2: location
	 * 		lobbyserver: server
	 * 
	 */
		

	
	public static void createArena(String arena){
		Main.arenas.set(arena + ".team.red.spawn", "NONE");
		Main.arenas.set(arena + ".team.green.spawn", "NONE");
		Main.arenas.set(arena + ".winscore", 150);
		Main.arenas.set(arena + ".minteamsize", 1);
		Main.arenas.set(arena + ".maxteamsize", 12);
	}
	
	public static void saveLocation(String prefix, Location loc){
		Main.arenas.set(prefix + ".x", loc.getX());
		Main.arenas.set(prefix + ".y", loc.getY());
		Main.arenas.set(prefix + ".z", loc.getZ());
		Main.arenas.set(prefix + ".pitch", loc.getPitch());
		Main.arenas.set(prefix + ".yaw", loc.getYaw());
		Main.arenas.set(prefix + ".world", loc.getWorld().getName());
	}
	
	public static Location getLocation(String prefix){
		return new Location(Bukkit.getWorld(Main.arenas.getString(prefix + ".world")), 
				Main.arenas.getDouble(prefix + ".x"), 
				Main.arenas.getDouble(prefix + ".y"), 
				Main.arenas.getDouble(prefix + ".z"),
				Main.arenas.getInt(prefix + ".yaw"),
				Main.arenas.getInt(prefix + ".pitch"));
	}
	
	public static ItemStack[] getKitInventory(String kit){
		ItemStack[] stack = new ItemStack[0];
		stack = Main.kits.getList(kit + ".inventory").toArray(stack);
		return stack;
	}
	
	public static ItemStack[] getKitArmor(String kit){
		ItemStack[] stack = new ItemStack[0];
		stack = Main.kits.getList(kit + ".armor").toArray(stack);
		return stack;
	}
	
}
