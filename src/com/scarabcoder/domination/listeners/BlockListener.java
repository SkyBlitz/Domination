package com.scarabcoder.domination.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class BlockListener implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e){
		if(Main.isGameRunning()){
			GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
			if(Main.game.getPlayers().contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e){
		if(Main.isGameRunning()){
			GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
			if(Main.game.getPlayers().contains(p)){
				e.setCancelled(true);
			}
		}
	}
	
}
