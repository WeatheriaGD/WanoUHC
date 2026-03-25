package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class CommandFinalHeal implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		for(PlayerData pd: Main.getInstance().getPlayersData()) {
			pd.heal();
		}
		//sender.sendMessage("�aFinal heal �xecut� avec succ�s");
		CommandInfo.broadcast("§cTous les joueurs ont été soigné !");
		return false;
	}

}
