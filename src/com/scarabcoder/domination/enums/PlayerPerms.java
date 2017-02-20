package com.scarabcoder.domination.enums;

import com.scarabcoder.domination.main.Main;

public enum PlayerPerms {
	ADMIN(Main.getPlugin().getConfig().getString("permissions.admin")),
	PLAYER(Main.getPlugin().getConfig().getString("permissions.player"));
	
	private String perm;
	
	PlayerPerms(String perm){
		this.perm = perm;
	}
	
	public String toString(){
		return this.perm;
	}
}
