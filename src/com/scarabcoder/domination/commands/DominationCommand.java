package com.scarabcoder.domination.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scarabcoder.domination.enums.Message;
import com.scarabcoder.domination.enums.PlayerMode;
import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.managers.DataManager;
import com.scarabcoder.domination.objects.GamePlayer;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class DominationCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if(sender instanceof Player){
			GamePlayer p = Main.getGamePlayer(((Player) sender).getUniqueId());
			
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("admin")){
					if(p.isAdmin()){
						boolean arena = p.getArena() != null;
						if(args.length > 2){
							if(args[1].equalsIgnoreCase("create")){
								if(!Main.arenas.contains(args[2])){
									DataManager.createArena(args[2]);
									p.sendMessage(ChatColor.GREEN + "Created arena \"" + args[2] + "\" successfully.");
									p.sendMessage(ChatColor.GREEN + "Entered edit mode for \"" + args[2] + "\".");
									p.setMode(PlayerMode.EDIT, args[2]);
								}else{
									p.sendMessage(Message.ARENAEXISTS.toString());
								}
							}else if(args[1].equalsIgnoreCase("remove")){
								if(Main.arenas.contains(args[2])){
									Main.arenas.set(args[2], null);
									p.sendMessage(ChatColor.GREEN + "Arena removed successfully.");
									if(p.getArena().equalsIgnoreCase(args[2])){
										p.setArena(null);
										p.setMode(PlayerMode.NONE);
										p.sendMessage(ChatColor.GREEN + "Exited edit mode.");
									}
								}else{
									p.sendMessage(ChatColor.RED + "Arena not found.");
								}
							}else if(args[1].equalsIgnoreCase("setspawn")){
								if(arena){
									if(args[2].equals("red") || args[2].equals("green")){
										DataManager.saveLocation(p.getArena() + ".team." + args[2] + ".spawn", p.getPlayer().getLocation().getBlock().getLocation());
									}else if(args[2].equalsIgnoreCase("lobby")){
										DataManager.saveLocation(p.getArena() + ".lobbyspawn", p.getPlayer().getLocation().getBlock().getLocation());
									}else{
										p.sendMessage(ChatColor.RED + "Invalid team (use green/red).");
									}
								}else{
									p.sendMessage(Message.NOARENA.toString());
								}
							}else if(args[1].equalsIgnoreCase("active")){
								if(arena){
									if(args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")){
										Main.arenas.set(p.getArena() + ".active", Boolean.valueOf(args[2]));
									}else{
										p.sendMessage(ChatColor.RED + "Invalid args, use true/false.");
									}
								}else{
									p.sendMessage(Message.NOARENA.toString());
								}
							}else if(args[1].equalsIgnoreCase("setpoint")){
								if(arena){
									Selection sel = Main.getWorldEdit().getSelection(p.getPlayer());
									if(sel != null){
										DataManager.saveLocation(p.getArena() + ".capturepoints." + args[2] + ".loc1", sel.getMinimumPoint());
										DataManager.saveLocation(p.getArena() + ".capturepoints." + args[2] + ".loc2", sel.getMaximumPoint());
										p.sendMessage(ChatColor.GREEN + "Set capture point \"" + args[2] + "\".");
									}else{
										p.sendMessage(ChatColor.RED + "Make a selection first!");
									}
								}else{
									p.sendMessage(Message.NOARENA.toString());
								}
							}else if (args[1].equalsIgnoreCase("minteamsize") || args[1].equalsIgnoreCase("maxteamsize")){
								if(arena){
									try{
										int size = Integer.valueOf(args[2]);
										Main.arenas.set(p.getArena() + "." + (args[1].equalsIgnoreCase("minteamsize") ? "minteamsize" : "maxteamsize"), size);
										p.sendMessage(ChatColor.GREEN + "Set " + args[1] + " to " + args[2] + ".");
									} catch (NumberFormatException e){
										p.sendMessage(ChatColor.RED + "Invalid value; integers expected.");
									}
								}else{
									p.sendMessage(Message.NOARENA.toString());
								}
							}else if(args[1].equalsIgnoreCase("lobbyserver")){
								Main.arenas.set("lobbyserver", args[2]);
								p.sendMessage(ChatColor.GREEN  + "Set lobby server to \"" + args[2] + "\".");
							}else{
								p.sendMessage(Message.INVALIDARGS.toString());
							}
						}else{
							p.sendMessage(Message.INVALIDARGS.toString());
						}
					}else{
						p.sendMessage(Message.NOPERMS.toString());
					}
					Main.saveArenas();
				}else{
					
				}
			}else{
				this.displayHelp(p);
			}
			
		}else{
			sender.sendMessage(ChatColor.RED + "Player-only command!");
		}
		
		return true;
	}
	
	private void displayHelp(GamePlayer p){
		
	}

}
