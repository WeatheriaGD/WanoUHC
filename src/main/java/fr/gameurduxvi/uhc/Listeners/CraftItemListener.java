package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class CraftItemListener implements Listener {
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerData pd = PlayerData.getPlayerData(p);
		if(e.getCurrentItem().getType().equals(Material.COMPASS)) {
			if(Main.getInstance().isNull(pd.getTeam())) {
				e.setCancelled(true);
				e.setResult(Result.DENY);
				p.sendMessage("§cVous ne pouvez pas encore fabriquer le Trilog Pose");
				p.sendMessage("§cCette action ne sera possible qu'après l'attribution des équipages");
				p.closeInventory();
			}
			else {
				pd.getPlayer().setCompassTarget(pd.getTeam().getShipLocation());
				pd.getPlayer().updateInventory();
			}			
		}
		for(ItemStack it: e.getClickedInventory()) {
			if(Main.getInstance().isNull(it)) continue;
			if(Main.getInstance().isNull(it.getItemMeta())) continue;
			if(Main.getInstance().isNull(it.getItemMeta().getDisplayName())) continue;
			if(it.getType().equals(pd.getPersonnage().ultimateItem().getType())){
				if(it.getItemMeta().getDisplayName().contains(pd.getPersonnage().ultimateItem().getItemMeta().getDisplayName())) {
					e.setCancelled(true);
					e.setResult(Result.DENY);
				}
			}
		}
	}
}
