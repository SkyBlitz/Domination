package com.scarabcoder.domination.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.scarabcoder.domination.main.Main;

public class PingListener implements Listener{
	
	@EventHandler
    public void onPing(ServerListPingEvent e){
		if(Main.isGameRunning()){
			e.setMotd(Main.game.getStatusMessage());
			
		
		
		}
	}
	
}
