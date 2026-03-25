package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;

public class CommandEnd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!Main.GAMESTATE.equals(GameState.Idle)) {
				String arg1 = Main.getInstance().argument(args, 1);
				if(arg1.equalsIgnoreCase("confirm")) {
					Bukkit.broadcastMessage("§7{§c!§7}§c" + player.getName() + " a arrêté la partie !");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
				}
				else {
					player.sendMessage("§cVeuillez confirmer avec la commande suivante: §e/end confirm");
				}
			}
			else {
				player.sendMessage("§cLa partie n'est pas en cours !");
			}
		}
		return false;
	}

}
