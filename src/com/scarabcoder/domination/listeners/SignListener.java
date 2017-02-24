package com.scarabcoder.domination.listeners;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class SignListener implements Listener{
	
	@EventHandler
	public void signPlace(SignChangeEvent e){
		if(e.getLine(0).equalsIgnoreCase("[selectkit]")){
			e.setLine(0, ChatColor.BOLD + "Right-click to");
			e.setLine(1, ChatColor.BOLD + "select a kit.");
		}
	}
	
	@EventHandler
	public void signInteract(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(Main.isSign(e.getClickedBlock())){
				Sign sign = (Sign) e.getClickedBlock().getState();
				if(sign.getLine(0).equals(ChatColor.BOLD + "Right-click to")){
					GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
					p.openKitGUI();
				}
			}
		}
	}
	
}
