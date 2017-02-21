package com.scarabcoder.domination.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.scarabcoder.domination.main.Main;

public class DeathListener implements Listener {
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent e){
		if((e.getEntity() instanceof Player) && ((e.getDamager() instanceof Player) || (e.getDamager() instanceof Arrow))){
			Player p = (Player) e.getEntity();
			Player p2;
			if(e.getDamager() instanceof Arrow){
				p2 = (Player)((Arrow) e.getDamager()).getShooter();
			}else{
				p2 = (Player) e.getDamager();
			}
			if(Main.isGameRunning()){
				if(Main.game.getTeam(p).getName().equals(Main.game.getTeam(p2).getName())){
					e.setCancelled(true);
				}else{
					if(p.getHealth() - e.getDamage() <= 0d){
						Main.game.handleDeath(e);
					}
				}
			}
			
		}
	}
	
	
	@EventHandler
	public void entityDeath(PlayerDeathEvent e){
		if(Main.isGameRunning()){
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable(){

			@Override
			public void run() {
					e.getEntity().spigot().respawn();
					e.getEntity().teleport(Main.game.getTeam(e.getEntity()).getSpawn());
				
				}
				
			}, 5);
		e.setDeathMessage(null);
		}
	}
	
}
