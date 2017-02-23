package com.scarabcoder.domination.listeners;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.scarabcoder.domination.enums.MessageType;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.main.MessageLogger;

public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e){
		
		
		
		Main.setGamePlayer(e.getPlayer());
		if(Main.isGameRunning()){
			Main.game.addPlayer(e.getPlayer());
			e.setJoinMessage(null);
		}
		
		PreparedStatement st;
		try {
			st = Main.getConnection().prepareStatement("SELECT 1 FROM " + Main.prefix + "_users WHERE uuid=?");
			st.setString(1, e.getPlayer().getUniqueId().toString());
			if(!st.executeQuery().next()){
				st = Main.getConnection().prepareStatement("INSERT INTO " + Main.prefix + "_users (uuid, username, wins, deaths, kills) VALUES (?, ?, 0, 0, 0)");
				st.setString(1, e.getPlayer().getUniqueId().toString());
				st.setString(2, e.getPlayer().getName());
				st.executeUpdate();
				MessageLogger.logMessage(MessageType.INFO, "Added new player " + e.getPlayer().getName() + " to database.");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
	
}
