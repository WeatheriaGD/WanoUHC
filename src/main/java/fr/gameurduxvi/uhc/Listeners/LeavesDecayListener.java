package fr.gameurduxvi.uhc.Listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Config.ConfigManager;

public class LeavesDecayListener implements Listener {
	
	@EventHandler
	public void OnEvent(LeavesDecayEvent e) {
		/*e.getBlock().setType(Material.AIR);
	    e.getBlock().getState().update(true);*/
		Random rnd = new Random();
		int percent = rnd.nextInt(100);
		if(percent <= ConfigManager.APPLE_DROP) {
			Location loc = e.getBlock().getLocation();
			loc.getWorld().dropItem(loc, new ItemStack(Material.APPLE));
		}
	}
}
