package com.scarabcoder.domination.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.scarabcoder.domination.enums.GameStatus;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class InteractListener implements Listener {
	
	
	@EventHandler
	public void playerInteract(PlayerInteractEvent e){
		if(Main.isGameRunning()){
			GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
			if(Main.game.getPlayers().contains(p)){
				if(Main.game.getStatus().equals(GameStatus.WAITING)){
					e.setCancelled(true);
					e.getPlayer().updateInventory();
				}
			}
			if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
				if(e.getPlayer().getItemInHand() != null){
					if(e.getPlayer().getItemInHand().getItemMeta() != null){
						if(e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null){
							String name = e.getPlayer().getItemInHand().getItemMeta().getDisplayName();
							if(name.equals(ChatColor.RED + "Leave")){
								Main.game.removePlayer(p, true, true);
							}else if(name.equalsIgnoreCase(ChatColor.GREEN + "Select Kit")){
								p.openKitGUI();
							}
						}
					}
				}
			}
		}
		
	}
	
}
