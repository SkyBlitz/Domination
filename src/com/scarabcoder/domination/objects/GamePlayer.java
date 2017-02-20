package com.scarabcoder.domination.objects;

import org.bukkit.entity.Player;

import com.scarabcoder.domination.enums.Message;
import com.scarabcoder.domination.enums.PlayerMode;
import com.scarabcoder.domination.enums.PlayerPerms;

public class GamePlayer {
	
	private PlayerMode mode = PlayerMode.NONE;
	
	private Player p;
	
	private String arena;
	
	public GamePlayer(Player p){
		
		this.p = p;
		
	}
	
	public void sendMessage(String message){
		p.sendMessage(Message.PREFIX.toString() + " " + message);
	}
	
	public Player getPlayer(){
		return this.p;
	}
	
	
	
	public boolean isAdmin(){
		return p.hasPermission(PlayerPerms.ADMIN.toString());
	}
	
	
	public boolean isPlayer(){
		return p.hasPermission(PlayerPerms.PLAYER.toString());
	}

	public PlayerMode getMode() {
		return mode;
	}

	public void setMode(PlayerMode mode) {
		this.mode = mode;
	}
	
	public void setMode(PlayerMode mode, String arena){
		this.setMode(mode);
		this.arena = arena;
	}

	public String getArena() {
		return arena;
	}

	public void setArena(String arena) {
		this.arena = arena;
	}
	
}
