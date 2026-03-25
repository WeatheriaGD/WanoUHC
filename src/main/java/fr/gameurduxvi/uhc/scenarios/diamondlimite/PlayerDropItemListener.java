package fr.gameurduxvi.uhc.scenarios.diamondlimite;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.gameurduxvi.uhc.Main;

public class PlayerDropItemListener implements Listener {
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(e.getItemDrop().getItemStack().getType().equals(Material.DIAMOND)) {
			for(DiamondInstance d: Main.getScenariosManager().diamond) {
				if(e.getPlayer().equals(d.player)) {
					if((d.amountDiamond - e.getItemDrop().getItemStack().getAmount()) < 0) {
						d.amountDiamond = 0;
					}
					else {
						d.amountDiamond = d.amountDiamond - e.getItemDrop().getItemStack().getAmount();
					}
				}
			}
		}
	}
}
