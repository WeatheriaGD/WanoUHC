package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;

public class PlayerItemConsumeEvenListener implements Listener {
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if(Main.GAMESTATE.equals(GameState.Paused)) {
			e.setCancelled(true);
		}
	}
}
