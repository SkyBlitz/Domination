package com.scarabcoder.domination.enums;

import net.md_5.bungee.api.ChatColor;

public enum Message {
	
	PREFIX(ChatColor.RESET + "[" + ChatColor.DARK_PURPLE + "Domination" + ChatColor.RESET + "]"),
	NOPERMS(ChatColor.RED + "You don't have permission to use this command!"),
	ARENAEXISTS(ChatColor.RED + "Arena already exists!"), 
	INVALIDARGS(ChatColor.RED + "Invalid arguments!"),
	NOARENA(ChatColor.RED + "You must be in arena edit mode to use this command.");
	
	private String msg;
	
	Message(String msg){
		this.msg = msg;
	}
	
	public String toString(){
		return this.msg;
	}
}
