package com.scarabcoder.domination.listeners;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.scarabcoder.domination.enums.GameStatus;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class SignListener implements Listener {
	
	@EventHandler
	public void signPlace(SignChangeEvent e){
		GamePlayer gp = Main.getGamePlayer(e.getPlayer().getUniqueId());
		if(gp.isAdmin()){
			if(e.getLine(0).equalsIgnoreCase("[kit]")){
				if(Main.kits.contains(e.getLine(1))){
					e.setLine(0, ChatColor.BLACK + "Select Kit");
					e.setLine(1, ChatColor.BOLD.toString() + StringUtils.capitalize(e.getLine(1)));
					
					gp.sendMessage(ChatColor.GREEN + "Kit sign created.");
				}else{
					e.getBlock().setType(Material.AIR);
					gp.sendMessage(ChatColor.RED + "Not a valid kit!");
				}
			}
		}
	}
	
	@EventHandler
	public void signInteract(PlayerInteractEvent e){
		if(Main.isGameRunning()){
			GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
			if(Main.game.getPlayers().contains(p)){
				if(Main.game.getStatus().equals(GameStatus.WAITING)){
					e.setCancelled(true);
					e.getPlayer().updateInventory();
				}
			}
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(Main.isSign(e.getClickedBlock())){
				GamePlayer gp = Main.getGamePlayer(e.getPlayer().getUniqueId());
				Sign sign = (Sign) e.getClickedBlock().getState();
				if(sign.getLine(0).contains("Select Kit")){
					gp.loadKit(sign.getLine(1).substring(2, sign.getLine(1).length()).toLowerCase());
					if(Main.isGameRunning()){
						GamePlayer p = Main.getGamePlayer(e.getPlayer().getUniqueId());
						Main.game.setKit(p, sign.getLine(1).substring(2, sign.getLine(1).length()).toLowerCase());
					}
				}
			}
		}
	}
	
}
