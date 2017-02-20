package com.scarabcoder.domination.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import com.scarabcoder.domination.main.Main;

public class DataManager {
	
	/*
	 * Arena format:
	 * 	arena:
	 * 		active: false
	 * 		lobbytime: 35
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
		Main.arenas.set(arena + ".minteamsize", 1);
		Main.arenas.set(arena + ".maxteamsize", 12);
		Main.arenas.set(arena + ".lobbytime", 35);
		Main.arenas.set(arena + ".active", false);
	}
	
	public static void saveLocation(String prefix, Location loc){
		ConfigurationSection sec = Main.arenas.getConfigurationSection(prefix);
		sec.set("x", loc.getX());
		sec.set("y", loc.getY());
		sec.set("z", loc.getZ());
		sec.set("world", loc.getWorld().getName());
	}
	
	public static Location getLocation(String prefix){
		ConfigurationSection sec = Main.arenas.getConfigurationSection(prefix);
		return new Location(Bukkit.getWorld(sec.getString("world")), sec.getDouble("x"), sec.getDouble("y"), sec.getDouble("z"));
	}
	
}
