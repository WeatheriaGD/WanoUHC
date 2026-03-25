package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class PlayerInteractAtEntityListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractAtEntityEvent e) {
		if(Main.GAMESTATE.equals(GameState.Ended)) {
			if(e.getRightClicked() instanceof ArmorStand) {			
				PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
				pd.nextPage();
				//e.setCancelled(true);
			}
			
			e.setCancelled(true);
		}
	}
}
