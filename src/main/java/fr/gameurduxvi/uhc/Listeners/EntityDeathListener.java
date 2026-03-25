package fr.gameurduxvi.uhc.Listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Config.ConfigManager;

public class EntityDeathListener implements Listener {
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity() instanceof Enderman) {
			e.getDrops().clear();
			Random rnd = new Random();
			int percent = rnd.nextInt(100);
			if(percent <= ConfigManager.PEARL_DROP) {
				Location loc = e.getEntity().getLocation();
				loc.getWorld().dropItem(loc, new ItemStack(Material.ENDER_PEARL));
			}
		}
	}
}
