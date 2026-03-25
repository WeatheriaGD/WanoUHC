package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.entity.Boat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

public class VehicleDestroyListener implements Listener {
	
	@EventHandler
	public void onDestroy(VehicleDestroyEvent e) {
		if(e.getAttacker() == null && e.getVehicle() instanceof Boat) {
			e.setCancelled(true);
		}
	}
}
