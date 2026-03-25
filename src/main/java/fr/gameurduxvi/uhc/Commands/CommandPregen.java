package fr.gameurduxvi.uhc.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.LoadingChunkTask;
import fr.gameurduxvi.uhc.Main;
import net.warvale.WorldBorder.WorldFillTask;

public class CommandPregen implements CommandExecutor {
	public static WorldFillTask worldFillTask;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		CommandSender p = sender;
		if(p instanceof Player) {
			if(((Player)p).isOp()) {
				new LoadingChunkTask(Main.getInstance().centerMaplocation.getWorld());
				/*final int chunksPerRun = 20;
			    if(worldFillTask == null) {
			    	(worldFillTask = new WorldFillTask(Main.getInstance().centerMaplocation.getWorld().getName(), chunksPerRun, 2100)).setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), (Runnable)worldFillTask, 1L, 1L));
			    	sender.sendMessage("§7Pregen §8>> §b WorldBorder map generation task for world \"" + Main.getInstance().centerMaplocation.getWorld().getName() + "\" started.");
			    }*/
			}			
		}
		return false;
	}
		
	public String argument(String[] args, int i) {
		if(args.length >= i) {
			return args[i-1];
		}
		return "";
	}
}
