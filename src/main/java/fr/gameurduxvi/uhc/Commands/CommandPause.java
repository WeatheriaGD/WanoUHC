package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;

public class CommandPause implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!Main.GAMESTATE.equals(GameState.Paused)) {
				if(Main.getInstance().secondes <= 20 && Main.getInstance().minutes == 0) {
					player.sendMessage("§cOpération impossible, la partie vient de commencer.");
					player.sendMessage("§cVeuillez recommencer dans 30 secondes.");
				}
				else {
					Main.GAMESTATE = GameState.Paused;
					Bukkit.broadcastMessage("§7La partie a été mis en pause par §b" + player.getName());
				}
			}
			else {
				Main.GAMESTATE = GameState.In_Progress;
				Bukkit.broadcastMessage("§7La partie reprends son cours !");
			}
		}
		return false;
	}

}
