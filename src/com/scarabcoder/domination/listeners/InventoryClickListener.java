package com.scarabcoder.domination.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.Game;
import com.scarabcoder.domination.objects.GamePlayer;

public class InventoryClickListener implements Listener{

	@EventHandler
	public void inventoryClick(InventoryClickEvent e){
		GamePlayer p = Main.getGamePlayer(((Player) e.getWhoClicked()).getUniqueId());
		if(Main.isGameRunning()){
			Game g = Main.game;
			if(e.getInventory().getName().equals("Select Kit")){
				e.setCancelled(true);
				if(e.getCurrentItem() != null){
					String name = e.getCurrentItem().getItemMeta().getDisplayName();
					name = name.substring(2, name.length());
					g.setKit(p, name.toLowerCase());
					p.getPlayer().closeInventory();
				}
			}
		}
	}
	
}
