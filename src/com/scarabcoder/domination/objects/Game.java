package com.scarabcoder.domination.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.scarabcoder.domination.enums.GameStatus;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.managers.DataManager;

public class Game {
	
	private String name;
	
	private GameStatus status;
	
	private Team red;
	
	private Team green;
	
	private int max;
	
	private int min;
	
	private int countdown;
	
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	
	private List<CapturePoint> points = new ArrayList<CapturePoint>();
	
	public Game(String nameID){
		
		this.name = nameID;
		
		max = Main.arenas.getInt(this.getName() + ".maxteamsize");
		min = Main.arenas.getInt(this.getName() + ".minteamsize");
		
		
		for(String key : Main.arenas.getConfigurationSection(this.getName() + ".capturepoints").getKeys(false)){
			String pre = this.getName() + ".capturepoints." + key;
			points.add(new CapturePoint(DataManager.getLocation(pre + ".loc1"), DataManager.getLocation(pre + ".loc2"), key));
		}
		
		countdown = Main.getPlugin().getConfig().getInt("lobbytime");
		
		
		this.status = GameStatus.WAITING;
		
		red = new Team(ChatColor.RED, "Red", DataManager.getLocation(this.getName() + ".team.red.spawn"));
		green = new Team(ChatColor.GREEN, "Green", DataManager.getLocation(this.getName() + ".team.green.spawn"));
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable(){

			@Override
			public void run() {
				
				if(getStatus().equals(GameStatus.WAITING)){
					if(players.size() >= min){
						if(countdown == Main.getPlugin().getConfig().getInt("lobbytime")){
							broadcastMessage(ChatColor.GREEN + "Game starting in " + ChatColor.BOLD + countdown + ChatColor.RESET + ChatColor.GREEN.toString() + " seconds!");
							countdown --;
						}
					}
					if(countdown != Main.getPlugin().getConfig().getInt("lobbytime")){
						if(players.size() >= min){
							if(countdown != 0){
								if((double)countdown % 10D == 0D){
									broadcastMessage(ChatColor.GREEN + "Game starting in " + ChatColor.BOLD + countdown + ChatColor.RESET + ChatColor.GREEN.toString() + " seconds!");
								}else if(countdown < 6){
									broadcastMessage(ChatColor.GREEN + "Game starting in " + ChatColor.BOLD + countdown + ChatColor.RESET + ChatColor.GREEN.toString() + " seconds!");
								}
								countdown --;
							}else{
								status = GameStatus.INGAME;
								for(GamePlayer p : red.getPlayers()){
									p.getPlayer().teleport(red.getSpawn());
								}
								for(GamePlayer p : green.getPlayers()){
									p.getPlayer().teleport(green.getSpawn());
								}
							}
						}else{
							countdown = Main.getPlugin().getConfig().getInt("lobbytime");
							broadcastMessage(ChatColor.RED + "Not enough players to start game, countdown cancelled.");
						}
					}
				}
				
			}
			
		}, 0, 20);
		
		
	}
	
	public void addPlayer(Player p){
		GamePlayer gm = Main.getGamePlayer(p.getUniqueId());
		
		this.players.add(gm);
		
		int r = red.getPlayers().size();
		int g = green.getPlayers().size();
		
		Team team;
		
		if(g > r){
			red.addPlayer(gm);
			team = red;
		}else if(g < r){
			green.addPlayer(gm);
			team = green;
		}else{
			double rand = new Random().nextDouble();
			if(rand > 0.49){
				team = red;
				red.addPlayer(gm);
			}else{
				team = green;
				green.addPlayer(gm);
			}
		}
		
		gm.sendMessage(ChatColor.GRAY + "You joined " + team.getColor().toString() + ChatColor.BOLD  + team.getName() + ChatColor.RESET + ".");
		if(this.getStatus().equals(GameStatus.WAITING)){
			this.broadcastMessage(ChatColor.GREEN + p.getName() + " joined the game (" + this.getPlayers().size() + "/" + max * 2 + ").");
		}else{
			this.broadcastMessage(ChatColor.GREEN + p.getName() + " joined " + team.getColor().toString() + ChatColor.BOLD + team.getName() + ChatColor.RESET + ".");
		}
	}
	
	public Team getRedTeam(){
		return red;
	}
	
	public Team getGreenTeam(){
		return green;
	}
	
	
	public void broadcastMessage(String msg){
		for(GamePlayer p : this.getPlayers()){
			p.sendMessage(msg);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public List<GamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
	}

	public List<CapturePoint> getPoints() {
		return points;
	}

	public void setPoints(List<CapturePoint> points) {
		this.points = points;
	}


	public String getStatusMessage() {
		switch(this.status){
		case INGAME:
			return ChatColor.RED + "Ingame";
		case RESTARTING:
			return ChatColor.RED + "Restarting";
		case WAITING:
			return ChatColor.GREEN + "Lobby";
		default:
			return "";
			
		}
	}
	
	
	
}
