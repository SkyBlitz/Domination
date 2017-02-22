package com.scarabcoder.domination.listeners;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class CommandListener implements Listener {
	
	@EventHandler
	public void playerChat(PlayerCommandPreprocessEvent e){
		if(Main.isGameRunning()){
			GamePlayer gp = Main.getGamePlayer(e.getPlayer().getUniqueId());
			if(Main.game.getPlayers().contains(gp)){
				if(e.getMessage().startsWith("/")){
					if(!gp.isAdmin()){
						List<String> al = (List<String>) Main.getPlugin().getConfig().getStringList("whitelisted");
						boolean block = true;
						for(String a : al){
							if(e.getMessage().startsWith("/" + a)){
								block = false;
							}
						}
						if(block){
							gp.sendMessage(ChatColor.GREEN + "That command is not usable ingame!");
							e.setCancelled(true);
							
						}
					}
				}
			}
		}
		
	}
	
}
