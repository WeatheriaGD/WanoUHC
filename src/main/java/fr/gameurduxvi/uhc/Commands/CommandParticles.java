package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Storage.PlayerData;

public class CommandParticles implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			PlayerData pd = PlayerData.getPlayerData((Player) sender);
			pd.particles = !pd.particles;
			
			if(!pd.particles) {
				sender.sendMessage("§aVous avez désactivé les particules");
			}
			else {
				sender.sendMessage("§aVous avez activé les particules");
			}
		}
		else {
			sender.sendMessage("§cSeul les joueurs peuvent faire cette commande");
		}
		return false;
	}

}
