package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;

public class EntityDamageListener implements Listener {
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(Main.GAMESTATE.equals(GameState.Paused)) {
			e.setCancelled(true);
			return;
		}		
		
		if(e.getEntity() instanceof Villager && e.getEntity().getLocation().getWorld().equals(Bukkit.getWorld(Main.getInstance().WanoWorldName))) {
			e.setCancelled(true);
		}		
		
		if(e.getEntity() instanceof Player) {
			//Player player = (Player) e.getEntity();			
			
			if(!Main.GAMESTATE.equals(GameState.In_Progress)) {
				//e.setDamage(0);
				e.setCancelled(true);
			}
			
			if(e.getCause().equals(DamageCause.LIGHTNING)) {
				e.setDamage(2);
			}
			
			if(Main.GAMESTATE.equals(GameState.In_Progress) && !Main.getInstance().anyDamage) {
				e.setCancelled(true);
			}
			
			World world = e.getEntity().getWorld();
			double size =  e.getEntity().getWorld().getWorldBorder().getSize();
			
			Location loca = new Location(world, Main.getInstance().centerMaplocation.getX() + (size / 2), 0, Main.getInstance().centerMaplocation.getZ() + (size / 2));
			Location locb = new Location(world, Main.getInstance().centerMaplocation.getX() - (size / 2), 1000, Main.getInstance().centerMaplocation.getZ() - (size / 2));
			if(!isInZone(loca, locb, e.getEntity().getLocation()) && e.getEntity().getLocation().getWorld().equals(Main.getInstance().centerMaplocation.getWorld())) {
				e.setCancelled(true);
				Location tpLoc = e.getEntity().getLocation().clone();
				if(e.getEntity().getLocation().getX() > (size / 2)) {
					tpLoc.setX(((size / 2) - 20));
				}
				if(e.getEntity().getLocation().getX() < -(size / 2)) {
					tpLoc.setX(((size / 2) + 20));
				}
				
				if(e.getEntity().getLocation().getZ() > (size / 2)) {
					tpLoc.setZ(((size / 2) - 20));
				}
				if(e.getEntity().getLocation().getZ() < -(size / 2)) {
					tpLoc.setZ(((size / 2) + 20));
				}
				
				for(int y = 150; y >= 62; y--) {
					Location loc1 = new Location(world, tpLoc.getX(), y, tpLoc.getZ());
					Location loc2 = new Location(world, tpLoc.getX(), y + 1, tpLoc.getZ());
					
					if(!loc1.getBlock().getType().equals(Material.AIR) && loc2.getBlock().getType().equals(Material.AIR)) {
						tpLoc.setX(tpLoc.getBlockX() + 0.5);
						tpLoc.setY(y + 1);
						tpLoc.setZ(tpLoc.getBlockZ() + 0.5);
						e.getEntity().teleport(tpLoc);
						break;
					}
				}
			}
		}
	}
	
	public boolean isInZone(Location loca, Location locb, Location mainLoc) {
		if(isBetween(loca.getX(), locb.getX(), mainLoc.getX()) && isBetween(loca.getY(), locb.getY(), mainLoc.getY()) && isBetween(loca.getZ(), locb.getZ(), mainLoc.getZ())) {
			return true;
		}
		return false;
	}
	
	public boolean isBetween(double numa, double numb, double mainNum) {
		if(numa >= numb) {
			if(mainNum < numa && mainNum > numb) {
				return true;
			}
		}
		else {
			if(mainNum > numa && mainNum < numb) {
				return true;
			}
		}
		return false;
	}
}
