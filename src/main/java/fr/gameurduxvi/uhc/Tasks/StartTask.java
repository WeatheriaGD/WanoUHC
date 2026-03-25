package fr.gameurduxvi.uhc.Tasks;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Commands.CommandStart;

public class StartTask extends BukkitRunnable {
	private Date lastResponse = null;
	public static boolean canStart = false;
	private int stableTimes = 0;
	
	@Override
	public void run() {
		if(lastResponse != null) {
			long diff = new Date().getTime() - lastResponse.getTime();
			long diffTicks = diff / 50;
			lastResponse = new Date();
			if(diffTicks > 20) {
				Bukkit.getConsoleSender().sendMessage("§cThe game waited " + diffTicks + " ticks before starting (server loading)");
			}
			else {
				stableTimes++;
				if(stableTimes >= 4) {
					Bukkit.getConsoleSender().sendMessage("§aThe server loaded all needed. Starting game in 1 second");
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {	
							canStart = true;
							CommandStart.start();
						}
					}, 20);
					Bukkit.getScheduler().cancelTask(CommandStart.startTask.getTaskId());
				}
			}
		}
		else {
			lastResponse = new Date();
		}
	}
}
