package fr.gameurduxvi.uhc.GameEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Commands.CommandInfo;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;

public class WorldBorderWanno extends GameEvent {
	public WorldBorderWanno() {
		setName("Border Wano");
		setMinute(135);
		setSecond(00);
		setInScoreboard(true);
		setScoreboardText("§7Border §f%m%§7:§f%s%");
	}
	public void run() {
		CommandInfo.broadcast("§fLa bordure commence à se déplacer dans le monde Wano");
		Bukkit.getWorld(Main.getInstance().WanoWorldName).getWorldBorder().setSize(300, (9 * 60 * 20 + 20 * 20) / 8);

		for(Player lp: Bukkit.getOnlinePlayers()) lp.playSound(lp.getLocation(), "wano.wano_border", 10, 1);
	}
}
