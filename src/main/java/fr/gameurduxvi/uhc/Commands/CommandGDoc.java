package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandGDoc implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		sender.sendMessage("https://docs.google.com/document/d/1yHNbwsHiXPwzgOmYGxsf_rGKfIp2JdgEgxkwbpiCFJA/edit?usp=sharing");
		return false;
	}

}
