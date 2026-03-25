package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUhc implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage("§3Commandes de l'uhc:");
			p.sendMessage("§3/§broles");
			p.sendMessage("§3/§binfo");
			p.sendMessage("§3/§btestpack");
			p.sendMessage("§3/§btexturepack");
			p.sendMessage("§3/§btrilog");
			if(p.isOp()) {
				p.sendMessage("§3/§bconfig");
				p.sendMessage("§3/§bstart");
				p.sendMessage("§3/§bpause");
				p.sendMessage("§3/§bend");
				p.sendMessage("§3/§bspectator [player]");
				p.sendMessage("§3/§btoggle [player]");
				p.sendMessage("§3/§bpregen");
				p.sendMessage("§3/§bworld <name>");
			}
		}
		return false;
	}

}
