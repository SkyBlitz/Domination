package com.scarabcoder.domination.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void playerLeave(PlayerQuitEvent e){
		Main.removeGamePlayer(e.getPlayer().getUniqueId());
	}
	
}
