package fr.gameurduxvi.uhc.Listeners;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;

public class WorldInitListener implements Listener {
	
	@EventHandler
    public void onWorldInit(WorldInitEvent e) {
        e.getWorld().getPopulators().add(new CustomPopulator());
    }

    public class CustomPopulator extends BlockPopulator {    	
        public void populate(World world, Random random, Chunk chunk) {        	
        	if(random.nextInt(100) <= 10) {
    			for(int xChunk = random.nextInt(5); xChunk < 16; xChunk++) {
    				for(int zChunk = random.nextInt(5); zChunk < 16; zChunk++) {
    					int x = chunk.getX() * 16 + xChunk;
    					int z = chunk.getZ() * 16 + zChunk;
    					int y = world.getHighestBlockYAt(x, z) - 1;
    					
    					Block block = chunk.getWorld().getBlockAt(x, y, z);
    					if(block.getType() == Material.GRASS || block.getType() == Material.SAND) {
    						Location loc = block.getLocation();
    						Block a = loc.getWorld().getBlockAt((int) loc.getX() + 1, (int) loc.getY(), (int) loc.getZ());
    						Block b = loc.getWorld().getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ() + 1);
    						Block c = loc.getWorld().getBlockAt((int) loc.getX() - 1, (int) loc.getY(), (int) loc.getZ());
    						Block d = loc.getWorld().getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ() - 1);
    						
    						if(a.getType().toString().contains("WATER") || b.getType().toString().contains("WATER") || c.getType().toString().contains("WATER") || d.getType().toString().contains("WATER")) {
    							for(int i = 0; i < 3; i++) {
    								loc.getWorld().getBlockAt((int) loc.getX(), (int) loc.getY() + i + 1, (int) loc.getZ()).setType(Material.SUGAR_CANE_BLOCK);
    							}
    							return;
    						}
    					}
    				}        		
    			}
        	}          
		}
	}
}
