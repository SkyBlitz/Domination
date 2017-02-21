package com.scarabcoder.domination.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.scarabcoder.domination.main.Main;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void playerLeave(PlayerQuitEvent e){
		Main.removeGamePlayer(e.getPlayer().getUniqueId());
	}
	
}
