package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;

public class HangingBreakByEntityListener implements Listener {

	@EventHandler
	public void onPainting(HangingBreakByEntityEvent e) {
		if(Main.GAMESTATE.equals(GameState.Ended)) {
			e.setCancelled(true);
		}
	}
}
