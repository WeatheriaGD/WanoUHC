package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Listeners.PlayerChatListener;

public class CommandChat implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			PlayerChatListener.chat = !PlayerChatListener.chat;
			
			if(PlayerChatListener.chat) {
				p.sendMessage("§aLe chat est activé");
			}
			else {
				p.sendMessage("§aLe chat est désactivé");
			}
		}
		else {
			sender.sendMessage("§cSeul les joueurs peuvent faire cette commande");
		}
		return false;
	}

}
