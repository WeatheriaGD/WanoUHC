package fr.gameurduxvi.uhc.scenarios.target;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class InventoryClickListener implements Listener {
	Scenario sc = null;
	
	public InventoryClickListener(Scenario sc) {
		this.sc = sc;
	}
	
	@EventHandler
	public void OnClick(InventoryClickEvent e) {
		if(!fr.gameurduxvi.uhc.Main.getInstance().isNull(e.getClickedInventory())) {
			Player p = (Player) e.getWhoClicked();
			if(e.getClickedInventory().getName().equals(">>" + Main.getScenariosManager().TARGET_NAME + "<<")) {
				e.setCancelled(true);
				if(e.getSlot() == 31) {
					ConfigManager.MenuScenario(p, 1);
				}
				else if(e.getSlot() == 13) {
					Main.getScenariosManager().targetLocation = new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
					sc.setConfigInv(Main.getScenariosManager().getTargetInventory());
					p.openInventory(Main.getScenariosManager().getTargetInventory());
				}
				
				ConfigManager.set("scenario|" + Target.class.getName() + "|targetLocation", Main.getScenariosManager().targetLocation.getX() + "," + Main.getScenariosManager().targetLocation.getY() + "," + Main.getScenariosManager().targetLocation.getZ());
			}
		}
	}
}
