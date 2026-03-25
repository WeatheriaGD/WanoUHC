package fr.gameurduxvi.uhc.Listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;

public class BlockBreakListener implements Listener {
	
	public BlockBreakListener() {
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.DIAMOND_BLOCK);
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.DIAMOND_ORE);
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.GOLD_BLOCK);
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.GOLD_ORE);
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.IRON_BLOCK);
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.IRON_ORE);
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.BOOKSHELF);
		Main.getInstance().getWanoUnbrakebableBlocks().add(Material.ANVIL);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(Main.GAMESTATE.equals(GameState.Paused)) {
			e.setCancelled(true);
			return;
		}
		
		if(Main.GAMESTATE.equals(GameState.Ended)) {
			e.setCancelled(true);
			return;
		}
		
		if(e.getBlock().getLocation().getWorld().getName().equals(Main.getInstance().WanoWorldName)) {
			if(Main.getInstance().getWanoUnbrakebableBlocks().contains(e.getBlock().getType())) {
				if(Main.getInstance().getWanoPosedLocations().contains(e.getBlock().getLocation())) {
					Main.getInstance().getWanoPosedLocations().remove(e.getBlock().getLocation());
				}
				else {
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
				}			
			}
		}
		if(e.getBlock().getType().equals(Material.GRAVEL)) {
			Random rnd = new Random();
			int percent = rnd.nextInt(100);
			if(percent <= ConfigManager.FLINT_DROP) {
				e.setCancelled(true);
				Location loc = e.getBlock().getLocation();
				loc.getWorld().dropItem(loc, new ItemStack(Material.FLINT));
				e.getBlock().setType(Material.AIR);
			}
		}
	}
	
	/*@SuppressWarnings("unused")
	@EventHandler
    public void blockBreakEvent(final BlockBreakEvent event) {
        final Block block = event.getBlock();
        if (block.getType() != Material.LOG) {
            return;
        }
        final Location loc = block.getLocation();
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();
        final int range = 4;
        final int off = 5;
        Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), (Runnable)new Runnable() {
            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                for (int offX = -4; offX <= 4; ++offX) {
                    for (int offY = -4; offY <= 4; ++offY) {
                        for (int offZ = -4; offZ <= 4; ++offZ) {
                            if (world.getBlockTypeIdAt(x + offX, y + offY, z + offZ) == Material.LEAVES.getId()) {
                                breakLeaf(world, x + offX, y + offY, z + offZ);
                            }
                        }
                    }
                }
            }
        });
    }

	@SuppressWarnings({ "unused", "deprecation" })
	 private void breakLeaf(final World world, final int x, final int y, final int z) {
		 Random rand = new Random();
	        final Block block = world.getBlockAt(x, y, z);
	        final byte data = block.getData();
	        if ((data & 0x4) == 0x4) {
	            return;
	        }
	        final byte range = 4;
	        final byte max = 32;
	        final int[] blocks = new int[max * max * max];
	        final int off = range + 1;
	        final int mul = max * max;
	        final int div = max / 2;
	        
            for (int offX = -range; offX <= range; ++offX) {
                for (int offY = -range; offY <= range; ++offY) {
                    for (int offZ = -range; offZ <= range; ++offZ) {
                        final int type = world.getBlockTypeIdAt(x + offX, y + offY, z + offZ);
                        blocks[(offX + div) * mul + (offY + div) * max + offZ + div] = ((type == 17) ? 0 : ((type == 18) ? -2 : -1));
                    }
                }
            }
            for (int offX = 1; offX <= 4; ++offX) {
                for (int offY = -range; offY <= range; ++offY) {
                    for (int offZ = -range; offZ <= range; ++offZ) {
                        for (int type = -range; type <= range; ++type) {
                            if (blocks[(offY + div) * mul + (offZ + div) * max + type + div] == offX - 1) {
                                if (blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] == -2) {
                                    blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] == -2) {
                                    blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] == -2) {
                                    blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] == -2) {
                                    blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] == -2) {
                                    blocks[(offY + div) * mul + (offZ + div) * max + (type + div - 1)] = offX;
                                }
                                if (blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] == -2) {
                                    blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] = offX;
                                }
                            }
                        }
                    }
                }
	        }
            
	        if (blocks[div * mul + div * max + div] < 0) {
	            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						LeavesDecayEvent event = new LeavesDecayEvent(block);
			            Main.getInstance().getServer().getPluginManager().callEvent(event);
			            if (event.isCancelled()) {
			                return;
			            }
			            block.breakNaturally();
			            if (10 > rand.nextInt(100)) {
			                world.playEffect(block.getLocation(), Effect.STEP_SOUND, Material.LEAVES.getId());
			            }
					}
				}, 10 + new Random().nextInt(20));
	        }
	    }*/
}
