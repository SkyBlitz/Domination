package com.scarabcoder.domination.main;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.scarabcoder.domination.commands.DominationCommand;
import com.scarabcoder.domination.enums.MessageType;
import com.scarabcoder.domination.listeners.PlayerJoinListener;
import com.scarabcoder.domination.objects.GamePlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Main extends JavaPlugin {
	
	private static HashMap<UUID, GamePlayer> players = new HashMap<UUID, GamePlayer>();
	
	
	private static Connection connection;
	
	private static Plugin plugin;
	
	public static FileConfiguration arenas;
	
	public static File arenasFile;
	
	private static WorldEditPlugin we;
	
	public void onEnable(){
		
		Main.plugin = this;
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		this.initArenasConfig();
		
		this.getCommand("domination").setExecutor(new DominationCommand());
		
		we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		this.initMySQL();
		
		this.registerListeners();
		
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
	
	public static Plugin getPlugin(){
		return plugin;
	}
	
	private void registerListeners(){
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
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
			String userSQL = "CREATE TABLE IF NOT EXISTS `" + this.getConfig().getString("mysql.schema") + "`.`" + this.getConfig().getString("mysql.prefix") + "-users` ( `uuid` VARCHAR(36) NOT NULL, `wins` INTEGER NULL, `deaths` INTEGER NULL, `kills` INTEGER NULL);";
			
			
			
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
	
}
