package com.scarabcoder.domination.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void playerLeave(PlayerQuitEvent e){
		GamePlayer gp = Main.getGamePlayer(e.getPlayer().getUniqueId());
		if(Main.isGameRunning()){
			if(Main.game.getPlayers().contains(gp)){
				Main.game.removePlayer(gp);
				e.setQuitMessage(null);
			}
		}
		Main.removeGamePlayer(e.getPlayer().getUniqueId());
		
	}
	
}
