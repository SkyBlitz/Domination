package com.scarabcoder.domination.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class InventoryClickListener implements Listener{

	@EventHandler
	public void inventoryClick(InventoryClickEvent e){
		GamePlayer p = Main.getGamePlayer(((Player) e.getWhoClicked()).getUniqueId());
	}
	
}
