package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;

public class EntitySpawnListener implements Listener {
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if(Main.GAMESTATE.equals(GameState.Idle)) {
			if(e.getEntity().getLocation().getY() > 150)
				e.setCancelled(true);
			return;
		}
		
		if(!Main.GAMESTATE.equals(GameState.In_Progress) || e.getLocation().getWorld().getName().equals(Main.getInstance().WanoWorldName)) {
			if(e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof ArmorStand) && !(e.getEntity() instanceof Villager)) {
				e.setCancelled(true);
				return;
			}
		}
	}
}
