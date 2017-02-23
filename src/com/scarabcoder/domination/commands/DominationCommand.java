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

public class DominationCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] args) {
		
		if(sender instanceof Player){
			GamePlayer p = Main.getGamePlayer(((Player) sender).getUniqueId());
			
			if(args.length > 0){
					if(p.isAdmin()){
					boolean arena = p.getArena() != null;
					if(args.length > 1){
						if(args[0].equalsIgnoreCase("create")){
							if(!Main.arenas.contains(args[1])){
								DataManager.createArena(args[1]);
								p.sendMessage(ChatColor.GREEN + "Created arena \"" + args[1] + "\" successfully.");
								p.sendMessage(ChatColor.GREEN + "Entered edit mode for \"" + args[1] + "\".");
								p.setMode(PlayerMode.EDIT, args[1]);
							}else{
								p.sendMessage(Message.ARENAEXISTS.toString());
							}
						}else if(args[0].equalsIgnoreCase("remove")){
							if(Main.arenas.contains(args[1])){
								Main.arenas.set(args[1], null);
								p.sendMessage(ChatColor.GREEN + "Arena removed successfully.");
								if(p.getArena().equalsIgnoreCase(args[1])){
									p.setArena(null);
									p.setMode(PlayerMode.NONE);
									p.sendMessage(ChatColor.GREEN + "Exited edit mode.");
								}
							}else{
								p.sendMessage(ChatColor.RED + "Arena not found.");
							}
						}else if(args[0].equalsIgnoreCase("setspawn")){
							if(arena){
								if(args[1].equals("red") || args[1].equals("green")){
									System.out.println(p.getArena() + ".team." + args[1] + ".spawn");
									System.out.println(Main.arenas.contains(p.getArena() + ".team." + args[1] + ".spawn"));
									DataManager.saveLocation(p.getArena() + ".team." + args[1] + ".spawn", p.getPlayer().getLocation());
									p.sendMessage(ChatColor.GREEN + "Set team " + args[1] + " spawnpoint.");
								}else if(args[1].equalsIgnoreCase("lobby")){
									
									DataManager.saveLocation(p.getArena() + ".lobbyspawn", p.getPlayer().getLocation());
									p.sendMessage(ChatColor.GREEN + "Set team lobby spawnpoint.");
								}else{
									p.sendMessage(ChatColor.RED + "Invalid team (use green/red).");
								}
							}else{
								p.sendMessage(Message.NOARENA.toString());
							}
						}else if(args[0].equalsIgnoreCase("active")){
							if(arena){
								if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")){
									Main.getPlugin().getConfig().set("game", (args[1].equalsIgnoreCase("true") ? p.getArena() : "none"));
									p.sendMessage(ChatColor.GREEN + "Game is now active!");
								}else{
									p.sendMessage(ChatColor.RED + "Invalid args, use true/false.");
								}
							}else{
								p.sendMessage(Message.NOARENA.toString());
							}
						}else if(args[0].equalsIgnoreCase("setpoint")){
							if(arena){
								
								DataManager.saveLocation(p.getArena() + ".capturepoints." + args[1] + ".location", p.getPlayer().getLocation().getBlock().getLocation());
								
								p.sendMessage(ChatColor.GREEN + "Set capture point \"" + args[1] + "\".");
								
							}else{
								p.sendMessage(Message.NOARENA.toString());
							}
						}else if (args[0].equalsIgnoreCase("minteamsize") || args[0].equalsIgnoreCase("maxteamsize")){
							if(arena){
								try{
									int size = Integer.valueOf(args[1]);
									Main.arenas.set(p.getArena() + "." + (args[0].equalsIgnoreCase("minteamsize") ? "minteamsize" : "maxteamsize"), size);
									p.sendMessage(ChatColor.GREEN + "Set " + args[0] + " to " + args[1] + ".");
								} catch (NumberFormatException e){
									p.sendMessage(ChatColor.RED + "Invalid value; integers expected.");
								}
							}else{
								p.sendMessage(Message.NOARENA.toString());
							}
						}else if(args[0].equalsIgnoreCase("lobbyserver")){
							Main.getPlugin().getConfig().set("lobby", args[1]);
							p.sendMessage(ChatColor.GREEN  + "Set lobby server to \"" + args[1] + "\".");
						}else if(args[0].equalsIgnoreCase("edit")){
							if(Main.arenas.contains(args[1])){
								p.setMode(PlayerMode.EDIT, args[1]);
								p.sendMessage(ChatColor.GREEN + "Entered edit mode for \"" + args[1] + "\".");
							}else{
								p.sendMessage(ChatColor.RED + "Arena not found!");
							}
						}else if(args[0].equalsIgnoreCase("savekit")){
							p.saveInventoryAsKit(args[1]);
							p.sendMessage(ChatColor.GREEN + "Saved current inventory as kit.");
						}else if(args[0].equalsIgnoreCase("loadkit")){
							if(Main.kits.contains(args[1])){
								p.loadKit(args[1]);
								p.sendMessage(ChatColor.GREEN + "Kit applied.");
							}else{
								p.sendMessage(ChatColor.RED + "Kit not found!");
							}
						}else if(args[0].equalsIgnoreCase("winscore")){
							if(p.getArena() != null){
								try{
									Main.arenas.set(p.getArena() + ".winscore", Integer.valueOf(args[1]));
									p.sendMessage(ChatColor.GREEN + "Set arena winning score to " + args[1]);
								} catch(NumberFormatException e){
									p.sendMessage(ChatColor.RED + "Expected number, got string.");
								}
							}else{
								p.sendMessage(Message.NOARENA.toString());
							}
						}else if(args[0].equalsIgnoreCase("defaultkit")){
							if(p.getArena() != null){
								Main.arenas.set(p.getArena() + ".defaultkit", args[1]);
								p.sendMessage(ChatColor.GREEN + "Set default kit.");
							}else{
								p.sendMessage(Message.NOARENA.toString());
							}
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
				Main.saveKits();
				Main.getPlugin().saveConfig();
			
			}else{
				p.showHelp();
			}
			
		}else{
			sender.sendMessage(ChatColor.RED + "Player-only command!");
		}
		
		return true;
	}
	

}
