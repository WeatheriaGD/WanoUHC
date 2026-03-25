package fr.gameurduxvi.uhc.scenarios.diamondlimite;

import java.util.Date;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class PlayerPickupItemListener implements Listener {
	@EventHandler
	public void onPickUpItem(PlayerPickupItemEvent e) {
		if(Scenario.isActive(Main.getScenariosManager().DIAMOND_LIMITE_NAME)) {
			if(e.getItem().getItemStack().getType().equals(Material.DIAMOND)) {
				boolean found = false;
				for(DiamondInstance d: Main.getScenariosManager().diamond) {
					if(e.getPlayer().equals(d.player)) {
						found = true;
					}
				}
				
				if(!found) {
					Main.getScenariosManager().diamond.add(new DiamondInstance(e.getPlayer()));
				}
				for(DiamondInstance d: Main.getScenariosManager().diamond) {
					if(e.getPlayer().equals(d.player)) {
						if(d.amountDiamond < Main.getScenariosManager().maxDiamond) {
							int maxCanPickUp = Main.getScenariosManager().maxDiamond - d.amountDiamond;
							if(e.getItem().getItemStack().getAmount() > maxCanPickUp) {
								d.amountDiamond = Main.getScenariosManager().maxDiamond;
								int removeDiamond = e.getItem().getItemStack().getAmount() - maxCanPickUp;
								e.getItem().setItemStack(new ItemStack(Material.DIAMOND, removeDiamond));							
								//e.getItem().getItemStack().setAmount(removeDiamond);
								e.setCancelled(true);
								e.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND, maxCanPickUp));
								long diff = new Date().getTime() - d.date.getTime();
								long diffSeconds = diff / 1000;
								if(diffSeconds > 5) {
									d.date = new Date();
									e.getPlayer().sendMessage("§7Vous avez atteint la limite de diamant !");
								}							
							}
							else {
								d.amountDiamond = d.amountDiamond + e.getItem().getItemStack().getAmount();
							}
						}
						else {
							e.setCancelled(true);
							long diff = new Date().getTime() - d.date.getTime();
							long diffSeconds = diff / 1000;
							if(diffSeconds > 5) {
								d.date = new Date();
								e.getPlayer().sendMessage("§7Vous avez atteint la limite de diamant !");
							}
						}					
					}
				}
			}
		}
	}
}
