package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import fr.gameurduxvi.uhc.Config.ConfigManager;

public class PlayerExpChangeListener implements Listener {
	
	@EventHandler
	public void onExp(PlayerExpChangeEvent e) {
		e.setAmount(e.getAmount() * (ConfigManager.EXPERIENCE_AMPLIFIER / 100));
	}
}
