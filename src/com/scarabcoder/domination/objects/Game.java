package com.scarabcoder.domination.objects;

import java.util.List;

import com.scarabcoder.domination.enums.GameStatus;

public class Game {
	
	private String name;
	
	private GameStatus status;
	
	private List<Team> teams;
	
	private List<GamePlayer> players;
	
	private List<CapturePoint> points;
	
	public Game(String nameID){
		
		
		this.name = nameID;
		this.status = GameStatus.RESTARTING;
		
		
		
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public List<GamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
	}

	public List<CapturePoint> getPoints() {
		return points;
	}

	public void setPoints(List<CapturePoint> points) {
		this.points = points;
	}
	
	
	
}
