package com.scarabcoder.domination.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.scarabcoder.domination.main.Main;

public class PlayerJoinListener implements Listener{
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e){
		Main.setGamePlayer(e.getPlayer());
		if(Main.isGameRunning()){
			Main.game.addPlayer(e.getPlayer());
		}
	}
	
}
