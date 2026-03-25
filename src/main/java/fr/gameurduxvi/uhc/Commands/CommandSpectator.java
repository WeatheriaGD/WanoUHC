package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class CommandSpectator implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			Player target = player;
			if(args.length >=1) {
				String arg1 = Main.getInstance().argument(args, 1);
				
				boolean found = false;
				for(Player p: Bukkit.getOnlinePlayers()) {
					if(p.getName().equalsIgnoreCase(arg1)) {
						found = true;
						target = p;
					}
				}
				
				if(!found) {
					player.sendMessage("§cCe joueur est introuvable !");
					return false;
				}
			}
			
			if(!PlayerData.hasPlayerData(player)) {
				Bukkit.getConsoleSender().sendMessage("§c");
				return false;
			}
			
			PlayerData targetData = PlayerData.getPlayerData(target);
			
			PlayerData.toggleSpectator(targetData.getPlayer(), !targetData.isSpec);
			if(targetData.isSpec) {
				targetData.getPlayer().sendMessage("§7Spectator Mode §8>> §bVous êtes désormais en spectateur");
			}
			else {
				targetData.getPlayer().sendMessage("§7Spectator Mode §8>> §bVous n'êtes plus un spectateur");
			}
			
			if(targetData.isDeath) {
				targetData.getPlayer().sendMessage("§6Warning §eVous êtes toujours compté comme mort !");
				targetData.getPlayer().sendMessage("§eSeul les spectateurs peuvent vous voir");
			}
		}
		return false;
	}

}
