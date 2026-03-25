package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class ServerListPingListener implements Listener {
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		int max = 0;
		for(Personnage pers: Main.getInstance().getPersonages()) {
			max += pers.getAmount();
		}
		e.setMaxPlayers(max + PlayerData.getSpectatorPlayers().size());
		if(Main.GAMESTATE.equals(GameState.Idle)) {
			e.setMotd(Main.getInstance().pluginPrefix + " §eEn attente de joueurs...");
		}
		else if(Main.GAMESTATE.equals(GameState.In_Progress)) {
			e.setMotd(Main.getInstance().pluginPrefix + " §cPartie en cours");
		}
		else if(Main.GAMESTATE.equals(GameState.Paused)) {
			e.setMotd(Main.getInstance().pluginPrefix + " §cPartie en pause");
		}
		else if(Main.GAMESTATE.equals(GameState.Ended)) {
			e.setMotd(Main.getInstance().pluginPrefix + " §cPartie terminée");
		}
	}
}
