package fr.gameurduxvi.uhc.Tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ShipsTask extends BukkitRunnable {
	private boolean firstTeleport = false;
	public static int chestSpawnInterval = 60 * 5;
	public static ArrayList<ItemStack> chestItems = new ArrayList<>();
	int remind = 0;
	short times = 0;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void run() {		
		times++;
		remind++;
		if(remind >= 21) {
			remind = 0;
		}
		for(TeamData sd: Main.getInstance().getTeamData()) {
			if(times >= 30) {
				times = 0;
				for(Player p: alivePlayers(sd)) {
					if(p.getWorld().equals(Main.getInstance().centerMaplocation.getWorld())) {
						p.setCompassTarget(sd.getShipLocation());
						p.updateInventory();
					}
				}
			}
			
			
			
			
			Collection<Entity> entities = sd.getShipLocation().getWorld().getNearbyEntities(sd.getShipLocation(), 10, 10, 10);
			
			
			
			/*******************************************************
			 * Join Zone
			 *******************************************************/
			for(Entity e: entities) {
				if(e instanceof Player) {
					if(!sd.getPlayersInZone().contains(e)) {
						sd.getPlayersInZone().add((Player) e);
						((Player) e).sendTitle("§a", "§aVous entrez dans la zone du bateau");
					}
				}
			}
			
			/*******************************************************
			 * Wait Message
			 *******************************************************/			
			if(remind >= 20) {
				for(Player lp: sd.getPlayersInZone()) {
					String message = "";
					if(alivePlayers(sd).contains(lp)) {
						if(count(sd) != alivePlayers(sd).size()) {
							message = "§bEn attente des autres membres de ce bateau";
						}
					}
					else {
						message = "§cVous ne faites pas partie de ce bateau";
					}
					lp.sendTitle("§a", message);
					/*PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
					((CraftPlayer) lp).getHandle().playerConnection.sendPacket(packet);*/
				}
				
				if(sd.getMax() > 1 && sd.getPlayersInZone().size() >= 1) {
					for(Player lp: alivePlayers(sd)) {
						if(!sd.getPlayersInZone().contains(lp)) {
							lp.sendTitle("§a", "§bVos coéquipiers vous attendent dans le bateau!");
						}
					}
				}
			}						
			
			/*******************************************************
			 * Quit Zone
			 *******************************************************/
			ArrayList<Player> quitList = new ArrayList<>();
			for(Player lp: sd.getPlayersInZone()) {
				if(!entities.contains(lp)) {
					String message = "§6Vous quittez la zone du bateau";
					lp.sendTitle("§a", message);
					/*PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
					((CraftPlayer) lp).getHandle().playerConnection.sendPacket(packet);*/
					quitList.add(lp);
				}
			}
			for(Player lp: quitList) sd.getPlayersInZone().remove(lp);
			
			/*******************************************************
			 * All players at zone
			 *******************************************************/
			sd.setTimer(sd.getShipTimer() - 1);
			if(alivePlayers(sd).size() == count(sd) && sd.canTeleport()) {				
				if(sd.getShipTimer() <= 0) {
					if(alivePlayers(sd).size() > 0) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName + " §8>> §aTeleportation of the ship " + sd.getShipLocation().getBlockX() + " " + sd.getShipLocation().getBlockY() + " " + sd.getShipLocation().getBlockZ());
						sd.setTeleport(false);
						Location loc = null;
						ArrayList<Location> locs = Main.getInstance().getWanoMapLocations();
						for(Location l: locs) {
							boolean foundPlayer = false;
							for(Entity e: l.getWorld().getNearbyEntities(l, 100, 100, 100)) {
								if(e instanceof Player){
									foundPlayer = true;
									break;
								}
							}
							if(!foundPlayer) {
								loc = l;
								break;
							}
						}
						
						if(Main.getInstance().isNull(loc)) {
							loc = locs.get(0);
						}
						
						
						for(Player lp: alivePlayers(sd)) {
							lp.teleport(loc);
							lp.playSound(lp.getLocation(), "wano.bonus_chest", 10, 1);
							lp.getInventory().remove(Material.COMPASS);
						}
						
						if(!firstTeleport) {
							firstTeleport = true;
							
							ArrayList<Location> chestLocs = (ArrayList<Location>) Main.getInstance().getWanoChestsLocations().clone();
							Collections.shuffle(chestLocs);
							
							int i = 0;
							for(Location chestLoc: chestLocs) {
								Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
									
									@SuppressWarnings({ })
									@Override
									public void run() {
										if(!Main.GAMESTATE.equals(GameState.Ended) && !Main.getInstance().fightPirates) {
											Block b = chestLoc.getBlock();
											b.setType(Material.CHEST);
											Chest chest = (Chest) b.getState();
	
											Random rnd = new Random();
											int num = rnd.nextInt(chestItems.size());
											ItemStack it = chestItems.get(num);
											chestItems.remove(num);
											
											if(!Main.getInstance().isNull(it)) {
												chest.getBlockInventory().setItem(13, it);
											}

											for(Player lp: Bukkit.getOnlinePlayers()) lp.playSound(lp.getLocation(), "wano.bonus_chest", 10, 1);
											
											Bukkit.broadcastMessage("§bInfo §7>> §fUn coffre est apparu aux coordonnées X:" + chestLoc.getBlockX() + " Y:" + chestLoc.getBlockY() + " Z:" + chestLoc.getBlockZ() + " dans le monde Wano");
										}
									}
								}, 20 * chestSpawnInterval * i);
								i++;
							}
						}
					}					
				}
				
				if(sd.getShipTimer() == 10 || sd.getShipTimer() == 5 || sd.getShipTimer() == 4 || sd.getShipTimer() == 3 || sd.getShipTimer() == 2 || sd.getShipTimer() == 1) {
					for(Player lp: sd.getPlayersInZone()) {
						if(alivePlayers(sd).contains(lp)) {
							lp.sendTitle("§7Téléportation dans", "§b" + sd.getShipTimer() + " secondes");
						}
					}
				}
				
				for(Player lp: sd.getPlayersInZone()) {
					if(alivePlayers(sd).contains(lp)) {
						String message = "§bEn attente de la téléporation (" + sd.getShipTimer() + " sec)";
						PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
						((CraftPlayer) lp).getHandle().playerConnection.sendPacket(packet);
					}
				}
			}
			else {
				sd.setTimer(30);
			}
		}
	}
	
	public ArrayList<Player> alivePlayers(TeamData sd) {
		/*******************************************************
		 * Check allive players
		 *******************************************************/
		ArrayList<Player> alivePlayers = new ArrayList<>();
		for(Player lp: sd.getPlayers()) {
			if(PlayerData.hasPlayerData(lp)) {
				PlayerData pd = PlayerData.getPlayerData(lp);
				if(!pd.isDeath && !pd.isSpec) {
					alivePlayers.add(lp);
				}
			}
		}
		return alivePlayers;
	}
	
	public int count(TeamData sd) {
		/*******************************************************
		 * Count players that has to be in the zone
		 *******************************************************/
		int i = 0;
		for(Player lp: alivePlayers(sd)) {
			boolean isIn = false;
			for(Player lp2: sd.getPlayersInZone()) {
				if(lp.equals(lp2)) {
					isIn = true;
					break;
				}
			}
			if(isIn) i++;
		}
		return i;
	}
}
