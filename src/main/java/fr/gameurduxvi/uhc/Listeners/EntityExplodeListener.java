package fr.gameurduxvi.uhc.Listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.gameurduxvi.uhc.Main;

public class EntityExplodeListener implements Listener {
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		ArrayList<Block> toRemove = new ArrayList<>();
		for(Block b: e.blockList()) {
			if(b.getLocation().getWorld().getName().equals(Main.getInstance().WanoWorldName)) {
				if(Main.getInstance().getWanoUnbrakebableBlocks().contains(b.getType())) {
					if(Main.getInstance().getWanoPosedLocations().contains(b.getLocation())) {
						Main.getInstance().getWanoPosedLocations().remove(b.getLocation());
					}
					else {
						toRemove.add(b);
						b.setType(Material.AIR);
					}			
				}
			}
		}
		for(Block b: toRemove) {
			e.blockList().remove(b);
		}
	}
}
