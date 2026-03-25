package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInfo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			String message = "";
			for(String str: args) {
				if(!message.equals("")) {
					message += " " + str;
				}
				else {
					message += str;
				}
			}
			
			String check = message.replace(" ", "");
			if(check.length() > 0) {
				message = message.replace("&", "§");
				broadcast(message);
			}
			else {
				p.sendMessage("§cVous devez au moins mettre un argument");
			}
		}
		return false;
	}
	
	public static void broadcast(String message) {
		Bukkit.broadcastMessage("§bInfo §7>> §f" + message);
	}
}
