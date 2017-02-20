package com.scarabcoder.domination.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.scarabcoder.domination.enums.MessageType;

public class Main extends JavaPlugin {
	
	private static Connection connection;
	
	public void onEnable(){
		
		this.initMySQL();
		
		
		
		
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
