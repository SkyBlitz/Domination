package com.scarabcoder.domination.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.material.Wool;

import com.scarabcoder.domination.enums.GameStatus;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.managers.DataManager;
import com.scarabcoder.domination.managers.ItemNames;

public class Game {
	
	private String name;
	
	private GameStatus status;
	
	private Team red;
	
	private Team green;
	
	private int max;
	
	private int min;
	
	private int countdown;
	
	private Game game;
	
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	
	private List<CapturePoint> points = new ArrayList<CapturePoint>();
	
	public Game(String nameID){
		
		game = this;
		
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

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				
				if(getStatus().equals(GameStatus.WAITING)){
					String msg = ChatColor.GREEN + "Game starting in " + ChatColor.BOLD + countdown + ChatColor.RESET + ChatColor.GREEN.toString() + " seconds!";
					if(players.size() >= min){
						if(countdown == Main.getPlugin().getConfig().getInt("lobbytime")){
							
							broadcastMessage(msg);
							countdown --;
						}
					}
					if(countdown != Main.getPlugin().getConfig().getInt("lobbytime")){
						if(players.size() >= min){
							if(countdown != 0){
								if((double)countdown % 10D == 0D){
									broadcastMessage(msg);
								}else if(countdown < 6){
									broadcastMessage(msg);
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
				}else{
					for(CapturePoint p : points){
						Location l1 = p.getL1().clone();
						Location l2 = p.getL2().clone();
						
						if(l1.getY() > l2.getY()){
							l1.setY(l2.getY());
						}else{
							l2.setY(l1.getY());
						}
						
						Location current = l1.clone();
						int x = 0;
						int z = 0;
						for(x = (int) l1.getX(); x != l2.getX(); x += (l1.getX() > l2.getX() ? -1 : 1)){
							for(z = (int) l1.getZ(); z != l2.getZ(); z += (l1.getZ() > l2.getZ() ? -1 : 1)){
								current.setZ(z);
								double r = new Random().nextDouble();
								current.getBlock().setType(Material.WOOL);
								if(p.getTeam() != null)
								current.getBlock().setData(p.getColor().getData());
								if(p.getCaptureStatus() != 0.0){
									if(r <= p.getCaptureStatus()){
										if(!p.isCaptured() && (p.getTeam() != null)){
											current.getBlock().setData(p.getEnemyColor().getData());
										}
									}else{
										if(p.isCaptured()){
											current.getBlock().setData(p.getColor().getData());
										}
									}
								}else{
									if(p.getTeam() != null)
									current.getBlock().setData(p.getColor().getData());
								}
							}
							current.setX(x);
						}
						
						

						for(GamePlayer pl : p.getGamePlayerInArea(game)){
							//System.out.println(p.getCaptureStatus());
							System.out.println(p.getCaptureStatus());
							Team tm = game.getTeam(pl);
							if(p.getTeam() == null){ 	
								p.setTeam(tm);
							}
							if(!game.getTeam(pl).equals(p.getTeam())){
								if(p.getCaptureStatus() == 0.0) game.broadcastMessage(tm.getColor() + ChatColor.BOLD.toString() + tm.getName() + ChatColor.RESET + " is capturing point " + ChatColor.BOLD.toString() + p.getName());
								p.setCaptureStatus(p.getCaptureStatus() + 0.1);
								
							}else{
								if(p.getCaptureStatus() != 0.0){
									p.setCaptureStatus(p.getCaptureStatus() - 0.1);
								}
							}
							if(p.getCaptureStatus() == 1.0){
								p.setTeam((p.getTeam().equals(red) ? green : red));
								p.setCaptureStatus(0.0);
								game.broadcastMessage(p.getTeam().getColor() + ChatColor.BOLD.toString() + p.getTeam().getName() + ChatColor.WHITE + " has captured point " + p.getName());
							}
							
							
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
	

	public void handleDeath(EntityDamageByEntityEvent e) {
		
		GamePlayer d = this.playerToGamePlayer((Player) e.getEntity());
		
		GamePlayer h;
		
		if(e.getDamager() instanceof Arrow){
			h = this.playerToGamePlayer((Player) ((Arrow) e.getDamager()).getShooter());
		}else{
			h = this.playerToGamePlayer((Player) e.getDamager());
		}
		
		int distance = (int) Math.abs(h.getPlayer().getLocation().distance(d.getPlayer().getLocation()));
		
		String msg;
		if(e.getCause().equals(DamageCause.PROJECTILE)){
			msg = getTeam(d).getColor() + d.getPlayer().getName() + ChatColor.GRAY + " was shot by " + getTeam(h).getColor() + h.getPlayer().getName() + ChatColor.GRAY + " from " + ChatColor.DARK_GRAY + distance + "m";
		}else{
			msg = getTeam(d).getColor() + d.getPlayer().getName() + ChatColor.GRAY + " was slain by " + getTeam(h).getColor() + h.getPlayer().getName() + ChatColor.GRAY;
			if(h.getPlayer().getItemInHand() != null){
				msg += " wielding his " + ChatColor.DARK_GRAY + ItemNames.lookup(h.getPlayer().getItemInHand());;
			}else{
				msg += " with his bare fist!";
			}
		}
		
		
		
		this.broadcastMessage(msg);
		
		
		
	}
	
	public Team getTeam(Player p){
		GamePlayer gp = this.playerToGamePlayer(p);
		return (red.getPlayers().contains(gp) ? red : green);
		
	}
	
	private GamePlayer playerToGamePlayer(Player p){
		for(GamePlayer p2 : this.getPlayers()){
			if(p2.getPlayer().getUniqueId().equals(p.getUniqueId())){
				return p2;
			}
		}
		return null;
	}
	
	private Team getTeam(GamePlayer p){
		return this.getTeam(p.getPlayer());
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
