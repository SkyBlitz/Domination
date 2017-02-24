package com.scarabcoder.domination.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.scarabcoder.domination.enums.GameStatus;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.managers.DataManager;
import com.scarabcoder.domination.managers.ItemNames;

public class Game {
	
	private String name;
	
	private GameStatus status;
	
	private Team red;
	
	private Team green;
	
	private HashMap<GamePlayer, Integer> pScores = new HashMap<GamePlayer, Integer>();
	
	private int max;
	
	private int min;
	
	private Location lobby;
	
	private HashMap<GamePlayer, String> kits = new HashMap<GamePlayer, String>();
	
	private int countdown;
	
	private String defaultKit;
	
	private int wincount;
	
	private Game game;
	
	private boolean ended = false;
	
	private List<GamePlayer> players = new ArrayList<GamePlayer>();
	
	
	private List<CapturePoint> points = new ArrayList<CapturePoint>();

	private HashMap<GamePlayer, Integer> pDeaths = new HashMap<GamePlayer, Integer>();
	
	public Game(String nameID){
		
		game = this;
		
		
		this.name = nameID;
		
		this.defaultKit = Main.arenas.getString(this.getName() + ".defaultkit");
		
		this.lobby = DataManager.getLocation(this.getName() + ".lobbyspawn");
		
		this.wincount = Main.arenas.getInt(this.getName() + ".winscore");
		
		max = Main.arenas.getInt(this.getName() + ".maxteamsize");
		min = Main.arenas.getInt(this.getName() + ".minteamsize");
		
		
		for(String key : Main.arenas.getConfigurationSection(this.getName() + ".capturepoints").getKeys(false)){
			String pre = this.getName() + ".capturepoints." + key;
			points.add(new CapturePoint(DataManager.getLocation(pre + ".location"), key));
		}
		
		countdown = Main.getPlugin().getConfig().getInt("lobbytime");
		
		
		this.status = GameStatus.WAITING;
		
		
		
		red = new Team(ChatColor.RED, "Red", DataManager.getSpawnList(this.getName() + ".team.red.spawns"));
		green = new Team(ChatColor.GREEN, "Green", DataManager.getSpawnList(this.getName() + ".team.green.spawns"));
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable(){

			@Override
			public void run() {
				for(GamePlayer p : game.getPlayers()){
					if(!game.ended)
					game.updateScoreboard(p);
					
				}
				if(getStatus().equals(GameStatus.WAITING)){
					String msg = ChatColor.GREEN + "Game starting in " + ChatColor.BOLD + countdown + ChatColor.RESET + ChatColor.GREEN.toString() + " seconds!";
					if(players.size() >= min * 2){
						if(countdown == Main.getPlugin().getConfig().getInt("lobbytime")){
							
							broadcastMessage(msg);
							countdown --;
						}
					}
					if(countdown != Main.getPlugin().getConfig().getInt("lobbytime")){
						if(players.size() >= min * 2){
							if(countdown != 0){
								if((double)countdown % 10D == 0D){
									broadcastMessage(msg);
								}else if(countdown < 6){
									broadcastMessage(msg);
								}
								countdown --;
							}else{
								status = GameStatus.INGAME;
								for(GamePlayer p : game.getPlayers()){
									p.loadKit(game.kits.get(p));
								}
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
					
					if(!ended){
						if((red.getPoints() >= game.wincount) || (green.getPoints() >= game.wincount)){
							Team win = (red.getPoints() >= wincount ? red : green);
							ended = true;
							game.broadcastMessage(win.getColor().toString() + ChatColor.BOLD + win.getName() + ChatColor.GOLD + " wins the game!");
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable(){
	
								@Override
								public void run() {
									game.endGame();
								}
								
							}, 8 * 20);
						}
					}
					for(CapturePoint p : points){
						List<GamePlayer> pls = p.getGamePlayerInArea(game);
						int redCount = 0;
						int greenCount = 0;
						for(GamePlayer pl : pls){
							if(game.getTeam(pl).equals(red)){
								redCount++;
							}else{
								greenCount++;
							}
						}
						if(p.isCaptured() && !game.ended){
							if(p.getCaptureStatus() == 0.0){
								p.getTeam().addPoints(1);
							}
						}
						if(pls.size() == 0){
							if(p.getCaptureStatus() != 0){
								p.setCaptureStatus(p.getCaptureStatus() - 0.2);
							}
						}
						if(redCount != greenCount){
							for(GamePlayer pl : pls){
								
								
								
								Team tm = game.getTeam(pl);
								
								if(p.getTeam() == null){
									p.setTeam(tm);
									p.setCaptureStatus(0.99);
									game.broadcastMessage(tm.getColor() + ChatColor.BOLD.toString() + tm.getName() + ChatColor.RESET + " is capturing point " + ChatColor.BOLD.toString() + p.getName());
	
								}else if(!p.isCaptured()){
									if(!p.getTeam().equals(game.getTeam(pl))){
										p.setTeam(tm);
										p.setCaptureStatus(0.99);
										
									}
								}
								
								if(!game.getTeam(pl).equals(p.getTeam())){
									if(p.getCaptureStatus() == 0.0) game.broadcastMessage(tm.getColor() + ChatColor.BOLD.toString() + tm.getName() + ChatColor.RESET + " is capturing point " + ChatColor.BOLD.toString() + p.getName());
									p.setCaptureStatus(p.getCaptureStatus() + 0.1);
									
								}else{
									if(p.getCaptureStatus() != 0.0){
										p.setCaptureStatus(p.getCaptureStatus() - 0.1);
									}else{
										if(!p.isCaptured()){
											p.setCaptured(true);
											game.broadcastMessage(p.getTeam().getColor() + ChatColor.BOLD.toString() + p.getTeam().getName() + ChatColor.WHITE + " has captured point " + ChatColor.BOLD + p.getName());
										}
									}
								}
								
								
								
								if(p.getCaptureStatus() == 1.0){
									
									p.setTeam((p.getTeam().equals(red) ? green : red));
									p.setCaptureStatus(0.0);
									game.broadcastMessage(p.getTeam().getColor() + ChatColor.BOLD.toString() + p.getTeam().getName() + ChatColor.WHITE + " has captured point " + ChatColor.BOLD + p.getName());
								}
								
								ChatColor enemyColor;
								ChatColor teamColor;
								if(p.isCaptured()){
									enemyColor = p.getEnemyChatColor();
									teamColor = p.getChatColor();
								}else{
									if(p.getTeam() != null){
										teamColor = ChatColor.WHITE;
										enemyColor = p.getChatColor();
									}else{
										teamColor = ChatColor.WHITE;
										enemyColor = teamColor;
									}
								}
								
								String s = "⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛";
								int point;
								if(p.isCaptured()){
									point = (int) Math.round(p.getCaptureStatus() * s.length());
								}else{
									point = (int) Math.round((1d - p.getCaptureStatus()) * s.length());
								}
								
								s = new StringBuilder(s).insert(point, teamColor).toString();
								s = enemyColor + s;
								
								ActionBarAPI.sendActionBar(pl.getPlayer(), s);
								
							}
							
							
							
							
						}
					}
				}
				
			}
			
		}, 0, 20);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable(){

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				
				for(CapturePoint p : game.getPoints()){
					Material enemyColor;
					Material teamColor;
					
					
					if(p.isCaptured()){
						teamColor = (p.getTeam().getName().equalsIgnoreCase("red") ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK);
						enemyColor = (teamColor.equals(Material.REDSTONE_BLOCK) ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK);
					}else{
						if(p.getTeam() == null){
							enemyColor = Material.IRON_BLOCK;
						}else{
							enemyColor = (p.getTeam().getName().equalsIgnoreCase("red") ? Material.REDSTONE_BLOCK : Material.EMERALD_BLOCK);
						}
						teamColor = Material.IRON_BLOCK;
					}
					
					Location current = p.getCenter().clone();
					int cStatus = (int) Math.round(p.getCaptureStatus() * 4);
					if(!p.isCaptured()) cStatus = 4 - cStatus;
					int cur = 0;
					current.setZ(current.getZ() + 0.5);
					current.setX(current.getX() + 0.5);
					for(int y = p.getCenter().getBlockY() + 4; y != p.getCenter().getBlockY() + 7; y++){
						cur++;
						current.setY(y);
						current.getWorld().spigot().playEffect(current, Effect.TILE_BREAK, (cur < cStatus ? enemyColor : teamColor).getId(), 0, 0.4f, 0.4f, 0.4f, 0.1f, 75, 50);
					}
				}
				
			}
			
		}, 0, 3);
		
	}
	
	public void setKit(GamePlayer p, String kit){
		this.kits.put(p, kit);
		if(this.getStatus().equals(GameStatus.INGAME)){
			p.loadKit(kit);
		}
	}
	
	public String getKit(GamePlayer p){
		return this.kits.get(p);
	}
	
	public void endGame(){
		List<GamePlayer> kick = new ArrayList<GamePlayer>(this.getPlayers());
		Iterator<GamePlayer> it = kick.iterator();
		while(it.hasNext()){
			GamePlayer pl = it.next();
			this.removePlayer(pl, true, false);
			System.out.println(pl.getPlayer().getName());
		}
		Bukkit.getScheduler().cancelTasks(Main.getPlugin())	;
		Main.game = new Game(this.getName());
	}
	
	
	@SuppressWarnings("deprecation")
	public void addPlayer(Player p){
		
		p.getInventory().clear();
		p.getInventory().setArmorContents(new ItemStack[] {});
		p.updateInventory();
		GamePlayer gm = Main.getGamePlayer(p.getUniqueId());
		this.pScores.put(gm, 0);
		this.pDeaths.put(gm, 0);
		this.players.add(gm);
		
		this.setKit(gm, this.defaultKit);
		
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
		
		p.setPlayerListName(team.getColor() + p.getName());
		p.setDisplayName(team.getColor() + p.getName() + ChatColor.RESET);
		
		ItemStack leave = new ItemStack(Material.BED);
		ItemMeta m = leave.getItemMeta();
		m.setDisplayName(ChatColor.RED + "Leave");
		leave.setItemMeta(m);
		
		ItemStack kit = new ItemStack(Material.HOPPER_MINECART);
		m.setDisplayName(ChatColor.GREEN + "Select Kit");
		kit.setItemMeta(m);
		
		p.getInventory().setItem(0, kit);
		p.getInventory().setItem(8, leave);
		
		p.updateInventory();
		
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = scoreboard.registerNewObjective("main", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "DOMINATION");
		p.setScoreboard(scoreboard);
		org.bukkit.scoreboard.Team redT = scoreboard.registerNewTeam(red.getName());
		redT.setPrefix(red.getColor().toString());
		org.bukkit.scoreboard.Team greenT = scoreboard.registerNewTeam(green.getName());
		greenT.setPrefix(green.getColor().toString());
		for(GamePlayer p2 : game.getPlayers()){
			
			if(this.getTeam(p2).equals(red)){
				redT.addPlayer(p2.getPlayer());
			}else{
				greenT.addPlayer(p2.getPlayer());
			}
			
			Scoreboard sb = p2.getPlayer().getScoreboard();
			sb.getTeam(team.getName()).addPlayer(gm.getPlayer());
			
		}
		
		
		this.updateScoreboard(gm);
		
		gm.sendMessage(ChatColor.GRAY + "You joined " + team.getColor().toString() + ChatColor.BOLD  + team.getName() + ChatColor.RESET + ".");
		if(this.getStatus().equals(GameStatus.WAITING)){
			this.broadcastMessage(team.getColor().toString() + p.getName() + ChatColor.GRAY + " joined the game (" + this.getPlayers().size() + "/" + max * 2 + ").");
			p.teleport(lobby);
		}else{
			this.broadcastMessage(ChatColor.GREEN + p.getName() + " joined " + team.getColor().toString() + ChatColor.BOLD + team.getName() + ChatColor.RESET + ".");
			p.teleport(team.getSpawn());
		}
	}
	
	public void updateScoreboard(GamePlayer p){
		/*
		 * Scoreboard format:
		 * 
		 * Domination
		 * You are on Red
		 * Kills: 12
		 * Kit: Heavy
		 * 
		 * A: ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
		 * B: ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
		 * C: ⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛
		 * 
		 * Red: 124/150
		 * Green: 98/150
		 */
		
		
		
		int scores = 0;
		Scoreboard s = p.getPlayer().getScoreboard();
		
		
		s.getObjective("main").unregister();
		
		Objective o = s.registerNewObjective("main", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.GREEN + ChatColor.MAGIC + "!" + ChatColor.RESET + ChatColor.LIGHT_PURPLE.toString() + "Domination" + ChatColor.GREEN + ChatColor.MAGIC + "!" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "");
		o.getScore(ChatColor.DARK_GREEN + ChatColor.STRIKETHROUGH.toString() + ChatColor.BOLD + "-------------------").setScore(scores);
		scores--;
		o.getScore(ChatColor.GOLD + "You are on the " + this.getTeam(p).getColor() + ChatColor.BOLD.toString() + this.getTeam(p).getName() + ChatColor.RESET + ChatColor.GOLD + " team!").setScore(scores);;
		scores--;
		o.getScore(ChatColor.AQUA + "Stats:").setScore(scores);
		scores--;
		o.getScore(ChatColor.GOLD + "    Kills: " + ChatColor.RED + this.pScores.get(p)).setScore(scores);
		scores--;
		o.getScore(ChatColor.GOLD + "    Deaths: " + ChatColor.RED + this.pDeaths.get(p)).setScore(scores);
		scores--;
		o.getScore(ChatColor.YELLOW + "Kit: " + ChatColor.RESET + ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + StringUtils.capitalize(this.kits.get(p))).setScore(scores);
		scores--;
		o.getScore(ChatColor.RESET.toString() + ChatColor.DARK_GREEN + ChatColor.STRIKETHROUGH.toString() + ChatColor.BOLD + "-------------------").setScore(scores);
		scores--;
		o.getScore(ChatColor.RED + "Rank: " + ChatColor.YELLOW + "Player");
		for(CapturePoint cp : this.getPoints()){
			scores--;
			ChatColor enemyColor;
			ChatColor teamColor;
			if(cp.isCaptured()){
				enemyColor = cp.getEnemyChatColor();
				teamColor = cp.getChatColor();
			}else{
				if(cp.getTeam() != null){
					teamColor = ChatColor.WHITE;
					enemyColor = cp.getChatColor();
				}else{
					teamColor = ChatColor.WHITE;
					enemyColor = teamColor;
				}
			}
			
			String st = "⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛";
			int point;
			if(cp.isCaptured()){
				point = (int) Math.round(cp.getCaptureStatus() * st.length());
			}else{
				point = (int) Math.round((1d - cp.getCaptureStatus()) * st.length());
			}
			
			st = new StringBuilder(st).insert(point, teamColor).toString();
			st = enemyColor + st;
			scores--;
			o.getScore(teamColor + ChatColor.BOLD.toString() + cp.getName() + ChatColor.RESET + " " + st).setScore(scores);
		}
		scores--;
		o.getScore("").setScore(scores);
		scores--;
		o.getScore(red.getColor().toString() + red.getName() + ChatColor.RESET + ":    " + red.getPoints() + "/" + game.wincount).setScore(scores);
		scores--;
		o.getScore(green.getColor().toString() + green.getName() + ChatColor.RESET + ": " + green.getPoints() + "/" + game.wincount).setScore(scores);
		
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
		
		this.pScores.put(h, this.pScores.get(h) + 1);
		
		this.pDeaths.put(d, this.pDeaths.get(d) + 1);
		
		
		this.broadcastMessage(msg);
		
		
		
	}
	
	public void removePlayer(GamePlayer gp, boolean kick, boolean msg) {
		Team tm = this.getTeam(gp);
		if(msg) this.broadcastMessage(tm.getColor() + gp.getPlayer().getName() + ChatColor.GRAY + " quit the game.");
		tm.removePlayer(gp);
		this.players.remove(gp);
		if(kick) gp.kickToLobby();
		gp.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		gp.addKill(this.pScores.get(gp));
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
