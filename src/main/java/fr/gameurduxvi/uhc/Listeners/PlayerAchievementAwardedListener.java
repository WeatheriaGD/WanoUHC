package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

import fr.gameurduxvi.uhc.Storage.PlayerData;

public class PlayerAchievementAwardedListener implements Listener {
	
	@EventHandler
	public void onAdvancement(PlayerAchievementAwardedEvent e) {
		if(PlayerData.hasPlayerData(e.getPlayer())) {
			PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
			if(pd.isDeath || pd.isSpec) {
				e.setCancelled(true);
			}
		}
	}
}
