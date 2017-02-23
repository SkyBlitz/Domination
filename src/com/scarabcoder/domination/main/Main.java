package com.scarabcoder.domination.main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.scarabcoder.domination.commands.DominationCommand;
import com.scarabcoder.domination.commands.LeaveCommand;
import com.scarabcoder.domination.enums.MessageType;
import com.scarabcoder.domination.listeners.CommandListener;
import com.scarabcoder.domination.listeners.DeathListener;
import com.scarabcoder.domination.listeners.DropListener;
import com.scarabcoder.domination.listeners.HungerChangeListener;
import com.scarabcoder.domination.listeners.PingListener;
import com.scarabcoder.domination.listeners.PlayerJoinListener;
import com.scarabcoder.domination.listeners.PlayerQuitListener;
import com.scarabcoder.domination.listeners.SignListener;
import com.scarabcoder.domination.objects.Game;
import com.scarabcoder.domination.objects.GamePlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Main extends JavaPlugin implements PluginMessageListener{
	
	private static HashMap<UUID, GamePlayer> players = new HashMap<UUID, GamePlayer>();
	
	
	private static Connection connection;
	
	private static Plugin plugin;
	
	public static String prefix;
	
	public static FileConfiguration arenas;
	
	public static File arenasFile;
	
	public static FileConfiguration kits;
	
	public static File kitsFile;
	
	public static FileConfiguration signs;
	
	public static File signsFile;
	
	public static Game game;
	
	private static WorldEditPlugin we;
	
	
	
	public void onEnable(){
		
		
		Main.plugin = this;
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		
		this.initArenasConfig();
		this.initKitsConfig();
		this.getCommand("domination").setExecutor(new DominationCommand());
		this.getCommand("leave").setExecutor(new LeaveCommand());
		
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		this.initMySQL();
		
		this.registerListeners();
		
		
		for(Player p : Bukkit.getOnlinePlayers()){
			Main.setGamePlayer(p);
		}
		
		if(!this.getConfig().getString("game").equalsIgnoreCase("none")){
			game = new Game(this.getConfig().getString("game"));
		}
		
		prefix = this.getConfig().getString("mysql.prefix");
		
	}
	
	public static boolean isGameRunning(){
		return !(game == null);
	}
	
	public static WorldEditPlugin getWorldEdit(){
		return we;
	}
	
	public static void saveArenas(){
		try {
			arenas.save(arenasFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveKits(){
		try {
			kits.save(kitsFile);
			kits.load(kitsFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private void initArenasConfig(){
		arenasFile = new File(this.getDataFolder(), "arenas.yml");
		
		if(!arenasFile.exists()){
			try {
				arenasFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		arenas = new YamlConfiguration();
		try {
			arenas.load(arenasFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private void initKitsConfig(){
		kitsFile = new File(this.getDataFolder(), "kits.yml");
		
		if(!kitsFile.exists()){
			try {
				kitsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		kits = new YamlConfiguration();
		try {
			kits.load(kitsFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static Plugin getPlugin(){
		return plugin;
	}
	
	private void registerListeners(){
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
		Bukkit.getPluginManager().registerEvents(new PingListener(), this);
		Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
		Bukkit.getPluginManager().registerEvents(new SignListener(), this);
		Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
		Bukkit.getPluginManager().registerEvents(new DropListener(), this);
		Bukkit.getPluginManager().registerEvents(new HungerChangeListener(), this);
	}
	
	public static boolean isSign(Block block){
		Material m = block.getType();
		return (m.equals(Material.SIGN)) || (m.equals(Material.SIGN_POST)) || (m.equals(Material.WALL_SIGN));
	}
	
	public static boolean isInRect(Player p, Location l1, Location l2)
	{
	    Location pl = p.getLocation();
	    
	    double x1 = l1.getX();
	    double y1 = l1.getY();
	    double z1 = l1.getZ();
	    
	    double x2 = l2.getX();
	    double y2 = l2.getY();
	    double z2 = l2.getZ();
	    
	    if(l1.getX() > l2.getX()){
	    	x1 = l2.getX();
	    	x2 = l1.getX();
	    }
	    if(l1.getY() > l2.getY()){
	    	y1 = l2.getY();
	    	y2 = l1.getY();
	    }
	    if(l1.getZ() > l2.getZ()){
	    	z1 = l2.getZ();
	    	z2 = l1.getZ();
	    }
	    
	    if((pl.getX() >= x1) && (pl.getX() <= x2)){
		    if((pl.getY() >= y1) && (pl.getY() <= y2)){
			    if((pl.getZ() >= z1) && (pl.getZ() <= z2)){
			    	return true;
			    }
		    }
	    }
	    
	    
	    
	    return false;
	}
	
	public static void removeGamePlayer(UUID id){
		players.remove(id);
	}
	
	public static void setGamePlayer(Player p){
		players.put(p.getUniqueId(), new GamePlayer(p));
	}
	
	public static GamePlayer getGamePlayer(UUID id){
		return players.get(id);
	}
	
	public void initMySQL(){
		
		try {
			connection = DriverManager
			        .getConnection("jdbc:mysql://" + this.getConfig().getString("mysql.ip") + "/" + this.getConfig().getString("mysql.schema") + "?"
			                        + "user=" + this.getConfig().getString("mysql.user") + "&password=" + this.getConfig().getString("mysql.pass"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			String userSQL = "CREATE TABLE IF NOT EXISTS `" + this.getConfig().getString("mysql.schema") + "`.`" + this.getConfig().getString("mysql.prefix") + "_users` ( `uuid` VARCHAR(36) NOT NULL, `username` VARCHAR(16) NULL, `wins` INTEGER NULL, `deaths` INTEGER NULL, `kills` INTEGER NULL);";
			
			
			
			Main.getConnection().prepareStatement(userSQL).executeUpdate();
			
			MessageLogger.logMessage(MessageType.SUCCESS, "Made connection to database and created tables if not existing.");
			
		} catch (SQLException e){
			MessageLogger.logMessage(MessageType.ERROR, "There was a fatal error trying to create default databases!");
			e.printStackTrace();
		}
		
		MessageLogger.logMessage(MessageType.INFO, "MySQL Initialized.");
		
	}
	
	public static Connection getConnection(){
		return connection;
	}

	@Override
	public void onPluginMessageReceived(String arg0, Player arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		
	}
	
}
