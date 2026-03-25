package fr.gameurduxvi.uhc.Commands;

import fr.gameurduxvi.uhc.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandWhitelist implements CommandExecutor {
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (p.isOp()){
                Main.getInstance().getServer().setWhitelist(!Main.getInstance().getServer().hasWhitelist());
                if (Main.getInstance().getServer().hasWhitelist()) {
                    p.sendMessage("§aLa whitelist a été activée");
                }
                else {
                    p.sendMessage("§aLa whitelist a été désactivée");
                }
            }
        }
        return false;
    }
}
