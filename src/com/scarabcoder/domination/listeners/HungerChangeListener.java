package com.scarabcoder.domination.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class HungerChangeListener implements Listener {
	
	@EventHandler
	public void blockHunger(FoodLevelChangeEvent e){
		Player p = (Player) e.getEntity();
		if(Main.isGameRunning()){
			GamePlayer gp = Main.getGamePlayer(p.getUniqueId());
			if(Main.game.getPlayers().contains(gp)){
				e.setCancelled(true);
				if(p.getFoodLevel() != 20){
					p.setFoodLevel(20);
				}
			}
		}
	}
	
}
