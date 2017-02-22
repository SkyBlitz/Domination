package com.scarabcoder.domination.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class DropListener implements Listener {
	
	@EventHandler
	public void playerDrop(PlayerDropItemEvent e){
		if(Main.isGameRunning()){
			GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
			if(Main.game.getPlayers().contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void playerPickup(PlayerPickupItemEvent e){
		if(Main.isGameRunning()){
			GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
			if(Main.game.getPlayers().contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
}
