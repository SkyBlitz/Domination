package com.scarabcoder.domination.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;

public class Team {
	
	private ChatColor color;
	
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	
	private String name;
	
	private List<Location> spawns = new ArrayList<Location>();
	
	private int points = 0;
	
	
	public Team(ChatColor color, String name, List<Location> spawns){
		this.color = color;
		this.name = name;
		this.spawns = spawns;
	}
	
	public void addPlayer(GamePlayer p){
		players.add(p);
	}
	
	public void removePlayer(GamePlayer p){
		players.remove(p);
	}
	
	public void addPoints(int amount){
		this.points += amount;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getSpawn() {
		return this.spawns.get(new Random().nextInt(this.spawns.size()));
	}


	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public List<GamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
	}
	
	
	
}
