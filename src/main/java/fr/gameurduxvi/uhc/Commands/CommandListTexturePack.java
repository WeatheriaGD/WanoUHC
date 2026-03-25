package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.gameurduxvi.uhc.Storage.PlayerData;

public class CommandListTexturePack implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		sender.sendMessage("§bListe des joueurs ayant le texturepack: ");
		for(PlayerData pd: PlayerData.getAlivePlayers()) {
			if(pd.resourcePack) sender.sendMessage("§a- " + pd.getPlayer().getName());
		}
		sender.sendMessage("§bListe des joueurs n'ayant §cpas §ble texturepack: ");
		for(PlayerData pd: PlayerData.getAlivePlayers()) {
			if(!pd.resourcePack) sender.sendMessage("§c- " + pd.getPlayer().getName());
		}
		return false;
	}

}
