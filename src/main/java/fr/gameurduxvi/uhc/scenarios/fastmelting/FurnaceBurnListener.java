package fr.gameurduxvi.uhc.scenarios.fastmelting;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class FurnaceBurnListener implements Listener {
	@EventHandler
	public void onBurn(FurnaceBurnEvent e) {
		Furnace furnace = (Furnace) e.getBlock().getState();		
		if(Scenario.isActive(Main.getScenariosManager().FAST_MELTING)) {
			boolean exist = false;
			for(Furnace f: Main.getScenariosManager().furnaces) {
				if(f.equals(furnace)) {
					exist = true;
				}
			}
			if(!exist) {
				Main.getScenariosManager().furnaces.add(furnace);
				test1(furnace);
			}
		}
	}
	
	public void test1(Furnace furnace) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				test2(furnace);
			}
		}, 3);
	}
	
	public void test2(Furnace furnace) {
		boolean blockExist = false;
		if(furnace.getBlock().getType().equals(Material.FURNACE) || furnace.getBlock().getType().equals(Material.BURNING_FURNACE)) {
			blockExist = true;
		}
		if(!Scenario.isActive(Main.getScenariosManager().FAST_MELTING) || furnace.getBurnTime() <= 0 || Main.getInstance().isNull(furnace.getInventory().getItem(0)) || !blockExist) {
			//Main.instance.furnaces.remove(furnace);
			Main.getScenariosManager().furnaces.remove(furnace);
		}
		else {
			furnace.setCookTime((short) (furnace.getCookTime() + 21));
			furnace.setBurnTime((short) (furnace.getBurnTime() - 3));
			test1(furnace);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Set<Material> nullSet = null;
		if(Scenario.isActive(Main.getScenariosManager().FAST_MELTING)) {
			Material mat = e.getWhoClicked().getTargetBlock(nullSet, 10).getType();
			if(mat.equals(Material.FURNACE) || mat.equals(Material.BURNING_FURNACE)) {
				Furnace furnace = (Furnace) e.getWhoClicked().getTargetBlock(nullSet, 10).getState();
				boolean exist = false;
				for(Furnace f: Main.getScenariosManager().furnaces) {
					if(f.equals(furnace)) {
						exist = true;
					}
				}
				if(!exist) {
					Main.getScenariosManager().furnaces.add(furnace);
					test1(furnace);
				}
			}
		}
	}
}
