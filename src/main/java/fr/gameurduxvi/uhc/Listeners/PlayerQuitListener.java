package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class PlayerQuitListener implements Listener {
	
	@EventHandler
	public void playQuit(PlayerQuitEvent e) {
		if(PlayerData.hasPlayerData(e.getPlayer())) {
			PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
			pd.resourcePack = false;
			if(!pd.isSpec && !pd.isDeath) {
				int max = 0;
				for(Personnage pers: Main.getInstance().getPersonages()) {
					max += pers.getAmount();
				}
				if(!Main.GAMESTATE.equals(GameState.Idle) && !Main.GAMESTATE.equals(GameState.Ended)){
					Bukkit.broadcastMessage("§7Un joueur a quitté la partie! (b" + (PlayerData.getAlivePlayers().size() - 1) + "§7/" + max + ")");
				}
				else {
					Bukkit.broadcastMessage("§7" + e.getPlayer().getName() + " a quitté la partie! (§b" + (PlayerData.getAlivePlayers().size() - 1) + "§7/" + max + ")");
				}
			}
		}
		
		e.setQuitMessage(null);
		if(PlayerData.hasPlayerData(e.getPlayer())) {
			Main.getInstance().getOfflinePlayersData().add(PlayerData.getPlayerData(e.getPlayer()));
			Main.getInstance().getPlayersData().remove(PlayerData.getPlayerData(e.getPlayer()));
		}
		
		/*if(Main.GAMESTATE.equals(GameState.In_Progress)) {
			GameTask.checkEndFight();
		}*/
	}
}
