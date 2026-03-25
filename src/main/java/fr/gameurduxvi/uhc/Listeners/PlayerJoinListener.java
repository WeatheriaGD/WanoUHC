package fr.gameurduxvi.uhc.Listeners;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class PlayerJoinListener implements Listener {
	
	@SuppressWarnings("resource")
	@EventHandler
	public void playerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(e.getPlayer().isOp()) {
			try {
				URL url = new URL("https://raw.githubusercontent.com/GameurDuXVI/WanoUHC/master/version.txt");
				Scanner s = new Scanner(url.openStream());
				String lastVersion = s.next();
				String currentVersion = Main.getInstance().getDescription().getVersion() + "";
				if(!lastVersion.equalsIgnoreCase(currentVersion)) {
					e.getPlayer().sendMessage("§b§l" + Main.getInstance().pluginName + " §8>> §aUne nouvelle mise à jour est disponible ! (" + lastVersion + ")");
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		
		if(!PlayerData.hasPlayerData(p)) {
			int max = 0;
			for(Personnage pers: Main.getInstance().getPersonages()) {
				max += pers.getAmount();
			}
			if(PlayerData.hasOfflinePlayerData(p)) {
				PlayerData pdo = PlayerData.getOfflinePlayerData(p.getName());
				pdo.setPlayer(e.getPlayer());
				Main.getInstance().getPlayersData().add(pdo);
				Main.getInstance().getOfflinePlayersData().remove(PlayerData.getOfflinePlayerData(p.getName()));
				if(!pdo.isDeath && !pdo.isSpec) {
					if(!Main.GAMESTATE.equals(GameState.Idle) && !Main.GAMESTATE.equals(GameState.Ended)){
						Bukkit.broadcastMessage("§7Un joueur s'est reconnecté (§b" + PlayerData.getAlivePlayers().size() + "§7/" + max + ")");
						if(pdo.lateScatter) {
							pdo.lateScatter = false;
							if(!(Main.getInstance().secondes < 20 && Main.getInstance().minutes == 0)) {
								Location loc = pdo.getPlayer().getWorld().getHighestBlockAt(pdo.spawnLocation).getLocation().add(0, 1, 0);
								pdo.getPlayer().teleport(loc);
								for(ItemStack it: ConfigManager.StuffDepart) {
									pdo.getPlayer().getInventory().addItem(it);
								}
							}
							else {
								Location loc = pdo.spawnLocation.add(0, 1, 0);
								pdo.getPlayer().teleport(loc);
							}
						}
					}
					else {
						Bukkit.broadcastMessage("§7" + p.getName() + " s'est reconnecté (§b" + PlayerData.getAlivePlayers().size() + "§7/" + max + ")");
					}					
				}
				
				try {
					if(pdo.getTeam() != null) {
						TeamData td = pdo.getTeam();
						pdo.getPlayer().setCompassTarget(td.getShipLocation());
						pdo.getPlayer().updateInventory();
						int i = 0;
						boolean found = false;
						for(Player lp: td.getPlayers()) {
							if(p.getUniqueId().equals(lp.getUniqueId())) {
								found = true;
								break;
							}
							i++;
						}
						if(found) {
							td.getPlayers().remove(td.getPlayers().get(i));
							td.getPlayers().add(p);
						}
						
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
				//PlayerData pd = PlayerData.getPlayerData(p);
				
				/*for(GameEvent ge: Main.getInstance().getGameEvents()) {
					int geSec = (ge.getMinute() * 60) + ge.getSecond();
					int sec = (Main.getInstance().minutes * 60) + Main.getInstance().secondes;
					if(ge.getClass().equals(RoleDistributionGameEvent.class) && geSec < sec) {
						if(pd.getPersonnage() instanceof Empty) {
							RoleDistributionGameEvent.attributeLateRole(pd);					
						}
					}
				}
				
				for(GameEvent ge: Main.getInstance().getGameEvents()) {
					int geSec = (ge.getMinute() * 60) + ge.getSecond();
					int sec = (Main.getInstance().minutes * 60) + Main.getInstance().secondes;
					if(ge.getClass().equals(FideleDistributionGameEvent.class) && geSec < sec) {
						FideleDistributionGameEvent.attributeLateFidèle(pd);
					}
				}*/
				
				/*for(GameEvent ge: Main.getInstance().getGameEvents()) {
					int geSec = (ge.getMinute() * 60) + ge.getSecond();
					int sec = (Main.getInstance().minutes * 60) + Main.getInstance().secondes;
					if(ge.getClass().equals(TeamDistributionGameEvent.class) && geSec < sec) {
						if(Main.getInstance().isNull(pd.getTeam())) {
							TeamDistributionGameEvent.attributeLateTeam(pd, true);
						}
					}
				}*/
			}
			else {
				Main.getInstance().getPlayersData().add(new PlayerData(p));
				if(!Main.GAMESTATE.equals(GameState.Idle)) {
					PlayerData.toggleSpectator(p, true);
				}
				else {
					if(!Main.GAMESTATE.equals(GameState.Idle) && !Main.GAMESTATE.equals(GameState.Ended)){
						Bukkit.broadcastMessage("§7Un joueur a rejoint la partie! (§b" + PlayerData.getAlivePlayers().size() + "§7/" + max + ")");
					}
					else {
						Bukkit.broadcastMessage("§7" + p.getName() + " a rejoint la partie! (§b" + PlayerData.getAlivePlayers().size() + "§7/" + max + ")");
					}
				}
			}
			
		}
		
		PlayerData pd = PlayerData.getPlayerData(p);
		
		PlayerData.refreshPlayersVieuw();
		
		for(Team team: e.getPlayer().getScoreboard().getTeams()) {
			team.unregister();
		}
		for(Objective objective: e.getPlayer().getScoreboard().getObjectives()) {
			objective.unregister();
		}
		
		e.setJoinMessage(null);
		
		p.setResourcePack("https://github.com/GameurDuXVI/WanoUHC/raw/master/WanoUHC.zip");	
		
		if(Main.GAMESTATE.equals(GameState.Idle) && !pd.isSpec) {
			p.teleport(new Location(Main.getInstance().centerMaplocation.getWorld(), 0.5, 203, 0.5));
			p.setGameMode(GameMode.ADVENTURE);
			for(PotionEffect pe: p.getActivePotionEffects()) {
				p.removePotionEffect(pe.getType());
			}
			p.setWalkSpeed(0.2f);
			
			p.getInventory().clear();
			p.getInventory().setHelmet(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setBoots(null);
			
			p.setHealth(1);
			p.setMaxHealth(20);
			p.setHealth(20);
			p.setFoodLevel(20);
			
			p.getInventory().setMaxStackSize(127);
		}
		else if(Main.GAMESTATE.equals(GameState.Ended)) {
			pd.getPlayer().teleport(Main.getInstance().WanoEndGameLocation);
			pd.changePage(0);
			Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					pd.changePage(pd.page);
				}
			}, 20 * 5, 20 * 5);
			for(PlayerData pd2: Main.getInstance().getPlayersData()) {
				pd.getPlayer().showPlayer(pd2.getPlayer());
			}
			pd.getPlayer().setGameMode(GameMode.CREATIVE);
			pd.getPlayer().getInventory().clear();
		}
		else {
			if(pd.isImobilized() && Main.getInstance().playersCanMove) {
				pd.getPlayer().setWalkSpeed((float) 0.2);
			}
		}
	}
	
}
