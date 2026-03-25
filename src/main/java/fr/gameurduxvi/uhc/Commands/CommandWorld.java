package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Main;

public class CommandWorld implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			if(sender.isOp()) {
				Player p = (Player) sender;
				if(args.length >= 1 && Main.getInstance().argument(args, 1).equals("list")) {
					p.sendMessage("§7World §8>> §bListe des mondes:");
					for(World w: Bukkit.getWorlds()) {
						p.sendMessage(" §3> §b" + w.getName());
					}
				}
				else if(args.length >= 1) {
					for(World w: Bukkit.getWorlds()) {
						if(Main.getInstance().argument(args, 1).equalsIgnoreCase(w.getName())) {
							p.teleport(new Location(w, 0, 200, 0));
							return false;
						}
					}
					p.sendMessage("§7World §8>> §cLe monde n'a pas été trouvé");
				}
				else {
					p.sendMessage("§7World §8>> §3/world §b<world>");
					p.sendMessage("§7World §8>> §3/world §blist");
				}
			}
		}
		return false;
	}

}
