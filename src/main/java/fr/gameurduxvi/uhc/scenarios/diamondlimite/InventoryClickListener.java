package fr.gameurduxvi.uhc.scenarios.diamondlimite;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.json.simple.JSONObject;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class InventoryClickListener implements Listener {
	Scenario sc = null;
	
	public InventoryClickListener(Scenario sc) {
		this.sc = sc;
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void OnClick(InventoryClickEvent e) {
		if(!fr.gameurduxvi.uhc.Main.getInstance().isNull(e.getClickedInventory())) {
			Player p = (Player) e.getWhoClicked();
			if(e.getClickedInventory().getName().equals(">>" + Main.getScenariosManager().DIAMOND_LIMITE_NAME + "<<")) {
				e.setCancelled(true);
				if(e.getSlot() == 31) {
					ConfigManager.MenuScenario(p, 1);
				}
				else if(e.getSlot() == 12) {
					if((Main.getScenariosManager().maxDiamond - 1) <= 0) {
						return;
					}
					Main.getScenariosManager().maxDiamond--;
					sc.setConfigInv(Main.getScenariosManager().getDiamondLimiteInventory());
					p.openInventory(Main.getScenariosManager().getDiamondLimiteInventory());
				}
				else if(e.getSlot() == 14) {
					Main.getScenariosManager().maxDiamond++;
					sc.setConfigInv(Main.getScenariosManager().getDiamondLimiteInventory());
					p.openInventory(Main.getScenariosManager().getDiamondLimiteInventory());
				}
				
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joScenario = ConfigManager.validJSONObject((JSONObject) jo.get("scenario"));
				JSONObject joSigleScenario = ConfigManager.validJSONObject((JSONObject) joScenario.get(sc.getClass().getName()));
				joSigleScenario.put("maxDiamond", Main.getScenariosManager().maxDiamond);
				joScenario.put(sc.getClass().getName(), joSigleScenario);
				jo.put("scenario", joScenario);
				
				ConfigManager.saveToFile(jo);
			}
		}
	}
}
