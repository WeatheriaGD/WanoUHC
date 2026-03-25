package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTestPack implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.playSound(p.getLocation(), "wano.roles.ultimates.empereurs.barbe_noir.black_hole", 1, 1);
		}
		else
			sender.sendMessage("§cSeul un joueur peut faire cette commande !");
		return false;
	}

}
