package com.scarabcoder.domination.objects;

import org.bukkit.Location;

public class CapturePoint {
	
	private Location l1;
	
	private Location l2;
	
	private Team team = null;
	
	private double captureStatus = 0.0;
	
	private String name;
	
	
	public CapturePoint(Location l1, Location l2, String name){
		this.l1 = l1;
		this.l2 = l2;
		this.name = name;
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
		this.captureStatus = captureStatus;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
}
