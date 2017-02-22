package com.scarabcoder.domination.objects;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.DyeColor;
import org.bukkit.Location;

import com.scarabcoder.domination.main.Main;

public class CapturePoint {
	
	private Location l1;
	
	private Location l2;
	
	private Team team = null;
	
	private double captureStatus = 0.0;
	
	private String name;
	
	private boolean captured = false;
	
	
	public CapturePoint(Location l1, Location l2, String name){
		this.l1 = l1;
		this.l2 = l2;
		this.name = name;
	}
	
	public List<GamePlayer> getGamePlayerInArea(Game game){
		List<GamePlayer> pls = new ArrayList<GamePlayer>();
		
		for(GamePlayer p : game.getPlayers()){
			if(Main.isInRect(p.getPlayer(), l1, l2)){
				pls.add(p);
			}
		}
		
		return pls;
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
		return l1;
	}


	public void setL1(Location l1) {
		this.l1 = l1;
	}


	public Location getL2() {
		return l2;
	}


	public void setL2(Location l2) {
		this.l2 = l2;
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
