package com.scarabcoder.domination.objects;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;

public class Team {
	
	private ChatColor color;
	
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	
	private String name;
	
	private Location spawn;
	
	private int points = 0;
	
	public Team(ChatColor color, String name, Location spawn){
		this.color = color;
		this.name = name;
		this.spawn = spawn;
	}
	
	public void addPlayer(GamePlayer p){
		players.add(p);
	}
	
	public int addPoints(int amount){
		this.points += amount;
		return points;
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
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
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
