package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class PlayerResourcePackStatusListener implements Listener {
	@EventHandler
	public void OnResourcePackStatus(PlayerResourcePackStatusEvent e) {
		if(e.getStatus().equals(Status.SUCCESSFULLY_LOADED)) {
			Bukkit.getConsoleSender().sendMessage("§aResourcePack loaded by " + e.getPlayer().getName());
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					//e.getPlayer().playSound(e.getPlayer().getLocation(), "opu.loaded", 10, 1);
					e.getPlayer().sendMessage("§aLe pack de ressource est chargé !");
					PlayerData.getPlayerData(e.getPlayer()).resourcePack = true;
				}
			}, 5);
		}
		else if(e.getStatus().equals(Status.FAILED_DOWNLOAD)) {
			Bukkit.getConsoleSender().sendMessage("§cResourcePack failed by " + e.getPlayer().getName());
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					//e.getPlayer().kickPlayer("�c�chec du t�l�chargement du resourcepack.");
					e.getPlayer().sendMessage("§céchec du téléchargement du pack de ressource.");
					e.getPlayer().sendMessage("§cEntrez la commande /texturepack");
				}
			}, 5);
		}
		else if (e.getStatus().equals(Status.DECLINED)){
			Bukkit.getConsoleSender().sendMessage("§cResourcePack declined by " + e.getPlayer().getName());
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					//e.getPlayer().kickPlayer("�cVeuillez accepter le t�l�chargement.");
					e.getPlayer().sendMessage("§cVeuillez accepter le téléchargement du pack de ressource.");
					e.getPlayer().sendMessage("§cPuis entrez la commande /texturepack");
				}
			}, 5);
		}
	}
}
