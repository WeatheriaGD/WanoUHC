package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class PlayerDropItemListener implements Listener {	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(!PlayerData.hasPlayerData(p)) return;
		PlayerData pd = PlayerData.getPlayerData(p);
		if(e.getItemDrop().getItemStack().getType().equals(pd.getPersonnage().ultimateItem().getType())){
			if(!Main.getInstance().isNull(e.getItemDrop().getItemStack().getItemMeta().getDisplayName())) {
				if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().contains(pd.getPersonnage().ultimateItem().getItemMeta().getDisplayName())) {
					pd.getPlayer().sendMessage("§6" + pd.getPersonnage().getDescription());
					e.setCancelled(true);
					if(p.getInventory().firstEmpty() == -1) {
						p.getWorld().dropItem(p.getLocation(), p.getInventory().getItem(8));
						Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								p.getInventory().setItem(8, pd.getPersonnage().ultimateItem());
							}
						}, 1);
					}
				}
			}
		}
	}
}
