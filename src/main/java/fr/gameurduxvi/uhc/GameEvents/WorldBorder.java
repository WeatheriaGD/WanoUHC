package fr.gameurduxvi.uhc.GameEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Commands.CommandInfo;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;

public class WorldBorder extends GameEvent {
	private int seconds = 0;
	
	public WorldBorder() {
		setName("Border");
		setDescription("Border du monde " + Main.getInstance().centerMaplocation.getWorld().getName());
		setMinute(90);
		setSecond(0);
		setInScoreboard(true);
		setScoreboardText("§7Border §f%m%§7:§f%s%");
	}
	
	public WorldBorder(int seconds) {
		this.seconds = seconds;
		setName("Border");
		setDescription("Message de la border du monde " + Main.getInstance().centerMaplocation.getWorld().getName());
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		if(seconds == 0) {
			CommandInfo.broadcast("§fLa bordure se déplace");
			Main.getInstance().centerMaplocation.getWorld().getWorldBorder().setSize(650 * 2, 15 * 60);
			
			for(Player lp: Bukkit.getOnlinePlayers()) lp.playSound(lp.getLocation(), "wano.world_border", 10, 1);
		}
		else {
			for(Player lp: Bukkit.getOnlinePlayers()) lp.sendTitle("§7Déplacement dans", "§b" + seconds);
		}
	}
}
