package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;

public class FoodLevelChangeListener implements Listener {
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		if(Main.GAMESTATE.equals(GameState.Paused)) {
			e.setCancelled(true);
			return;
		}
		if(!Main.GAMESTATE.equals(GameState.In_Progress)) {
			Player player = (Player) e.getEntity();
			player.setFoodLevel(20);
			player.setHealth(player.getMaxHealth());
			e.setCancelled(true);
		}
	}
}
