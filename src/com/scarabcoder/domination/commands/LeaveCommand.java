package com.scarabcoder.domination.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scarabcoder.domination.main.Main;
import com.scarabcoder.domination.objects.GamePlayer;

public class LeaveCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		if(arg0 instanceof Player){
			GamePlayer p = Main.getGamePlayer(((Player) arg0).getUniqueId());
			p.kickToLobby();
		}
		
		
		return true;
	}

}
