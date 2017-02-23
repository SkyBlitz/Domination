package com.scarabcoder.domination.objects;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.DyeColor;
import org.bukkit.Location;

import com.scarabcoder.domination.main.Main;

public class CapturePoint {
	
	private Team team = null;
	
	private double captureStatus = 0.0;
	
	private Location center;
	
	private String name;
	
	private boolean captured = false;
	
	
	public CapturePoint(Location center, String name){
		this.center = center;
		this.name = name;
	}
	
	public List<GamePlayer> getGamePlayerInArea(Game game){
		List<GamePlayer> pls = new ArrayList<GamePlayer>();
		
		for(GamePlayer p : game.getPlayers()){
			if(Main.isInRect(p.getPlayer(), this.getL1(), this.getL2())){
				pls.add(p);
			}
		}
		
		return pls;
	}
	
	public ChatColor getEnemyChatColor(){
		return this.getTeam().getColor().equals(ChatColor.RED) ? ChatColor.GREEN : ChatColor.RED;
	}
	
	public ChatColor getChatColor(){
		return this.getTeam().getColor().equals(ChatColor.RED) ? ChatColor.RED : ChatColor.GREEN;
	}
	
	public DyeColor getColor(){
		return (this.getTeam().getColor().equals(ChatColor.RED) ? DyeColor.RED : DyeColor.GREEN);
	}
	
	public DyeColor getEnemyColor(){
		return (this.getTeam().getColor().equals(ChatColor.RED) ? DyeColor.GREEN : DyeColor.RED);
		
	}
	
	public boolean isCaptured(){
		return captured;
	}
	
	public void setCaptured(boolean captured){
		this.captured = captured;
	}
	
	


	public Location getL1() {
		Location l1 = this.center.clone();
		l1.setX(l1.getX() - 4);
		l1.setZ(l1.getZ() - 4);
		l1.setY(l1.getY() - 1);
		return l1;
	}



	public Location getL2() {
		Location l2 = this.center.clone();
		l2.setX(l2.getX() + 4);
		l2.setZ(l2.getZ() + 4);
		l2.setY(l2.getY() + 5);
		return l2;
	}
	
	public Location getCenter(){
		return center;
	}
	
	public void setCenter(Location l){
		this.center = l;
	}



	public Team getTeam() {
		return team;
	}


	public void setTeam(Team team) {
		this.team = team;
	}


	public double getCaptureStatus() {
		return captureStatus;
	}


	public void setCaptureStatus(double captureStatus) {
		if(captureStatus < 0.0){
			this.captureStatus = 0.0;
		}else if(captureStatus > 1.0){
			this.captureStatus = 1.0;
		}else{
			this.captureStatus = captureStatus;
		}
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
}
