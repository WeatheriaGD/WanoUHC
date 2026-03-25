package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCommandes implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		sender.sendMessage("");
		sender.sendMessage("");
		sender.sendMessage("§9/testpack §8- §7Permet de vérifier si le pack de ressource est chargé");
		sender.sendMessage("§9/texturepack §8- §7Permet d'accepter le pack de ressource");
		sender.sendMessage("§9/trilog §8- §7Permet de voir le craft du trilog");
		sender.sendMessage("§9/doc §8 §7Permet d'avoir le lien de la documentation Google");
		sender.sendMessage("§9/testparticles §8- §7Permet de vérifier si les particules sont bien activés");
		sender.sendMessage("§9/particules §8- §7Permet d'activer ou de désactiver les particules");
		return false;
	}

}
