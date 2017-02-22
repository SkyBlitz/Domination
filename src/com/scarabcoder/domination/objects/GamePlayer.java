package com.scarabcoder.domination.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.scarabcoder.domination.enums.Message;
import com.scarabcoder.domination.enums.PlayerMode;
import com.scarabcoder.domination.enums.PlayerPerms;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.managers.DataManager;

public class GamePlayer {
	
	private PlayerMode mode = PlayerMode.NONE;
	
	private Player p;
	
	private String arena;
	
	public GamePlayer(Player p){
		
		this.p = p;
		
	}
	
	public void addKill(int amount){
		this.addInt("kills", amount);
	}
	
	public void addWin(int amount){
		this.addInt("wins", amount);
	}
	
	public void addDeath(int amount){
		this.addInt("deaths", amount);
	}
	
	public void saveInventoryAsKit(String kitname){
		ItemStack[] inv = this.getPlayer().getInventory().getContents();
		Main.kits.set(kitname + ".inventory", inv);
		inv = this.getPlayer().getInventory().getArmorContents();
		Main.kits.set(kitname + ".armor", inv);
		Main.kits.set(kitname + ".default", false);
		Main.saveKits();
	}
	
	public void showHelp(){
		this.sendMessage(ChatColor.GOLD + "------ " + ChatColor.LIGHT_PURPLE + "Domination" + ChatColor.GOLD + " ------");
		this.sendMessage(ChatColor.GOLD + "/domination admin create <arena>" + ChatColor.RESET + ": Creates a Domination arena.");
		this.sendMessage(ChatColor.GOLD + "/domination admin remove <arena>" + ChatColor.RESET + ": Removes a Domination arena.");
		this.sendMessage(ChatColor.GOLD + "/domination admin edit <arena>" + ChatColor.RESET + ": Enters edit mode for arena.");
		this.sendMessage(ChatColor.GOLD + "/domination admin setspawn <red/green/lobby>" + ChatColor.RESET + ": Sets the spawn for lobby or team.");
		this.sendMessage(ChatColor.GOLD + "/domination admin active <true/false>" + ChatColor.RESET + ": Makes current arena active.");
		this.sendMessage(ChatColor.GOLD + "/domination admin setpoint <name>" + ChatColor.RESET + ": Sets a capture point with WorldEdit selection.");
		this.sendMessage(ChatColor.GOLD + "/domination admin minteamsize/maxteamsize <amount>" + ChatColor.RESET + ": Sets the minimum and maximum team sizes.");
		this.sendMessage(ChatColor.GOLD + "/domination admin lobbyserver <server>" + ChatColor.RESET + ": Sets the lobby BungeeCord server.");
		this.sendMessage(ChatColor.GOLD + "/domination admin savekit <kit>" + ChatColor.RESET + ": Creates a kit with current inventory.");
		this.sendMessage(ChatColor.GOLD + "/domination admin loadkit <kit>" + ChatColor.RESET + ": Loads kit into inventory.");
		this.sendMessage(ChatColor.GOLD + "/domination admin winscore <kit>" + ChatColor.RESET + ": Sets the score goal for teams to win.");
	}
	
	public void loadKit(String kitname){
		this.getPlayer().getInventory().setContents(DataManager.getKitInventory(kitname));
		this.getPlayer().getInventory().setArmorContents(DataManager.getKitArmor(kitname));
		this.getPlayer().updateInventory(); 
	}
	
	public void kickToLobby(){
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("Connect");
		out.writeUTF(Main.getPlugin().getConfig().getString("lobby"));
		this.getPlayer().sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
	}
	
	private void addInt(String column, int amount){
		try {
			String uuid = this.getPlayer().getUniqueId().toString();
			PreparedStatement sq = Main.getConnection().prepareStatement("SELECT " + column + " FROM " + Main.prefix + "-users WHERE uuid=?");
			
			sq.setString(1, uuid);
			sq.executeQuery();
			ResultSet st = sq.executeQuery();
			if(st.next()){
				sq = Main.getConnection().prepareStatement("UPDATE " + Main.prefix + "-users SET " + column + "=? WHERE uuid=?");
				sq.setInt(1, st.getInt("kills") + amount);
				sq.setString(2, uuid);
				sq.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
