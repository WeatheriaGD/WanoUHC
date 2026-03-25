package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTexturePack implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.setResourcePack("https://github.com/GameurDuXVI/WanoUHC/raw/master/WanoUHC.zip");
		}
		else
			sender.sendMessage("§cSeul un joueur peut faire cette commande !");
		return false;
	}

}
