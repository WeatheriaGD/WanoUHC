package fr.gameurduxvi.uhc.Tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.GameEvents.RoleDistributionGameEvent;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.scenarios.target.Target;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class GameTask extends BukkitRunnable {
	private ArrayList<PlayerData> topList = new ArrayList<>();
	
	short times = 0;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Bukkit.getWorlds().get(0).setStorm(false);
		Bukkit.getWorlds().get(0).setThundering(false);
		
		/*******************************************************
		 * Top Bonuses
		 *******************************************************/		
		if(!Main.GAMESTATE.equals(GameState.Idle)) {
			times++;
			if(times >= 5) {
				times = 0;
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					if(Target.hasTarget(pd.getPlayer()) && Target.getTarget(pd.getPlayer()) != null) {
						pd.getPlayer().setCompassTarget(Target.getTarget(pd.getPlayer()).getLocation());
						pd.getPlayer().updateInventory();
					}
				}
			}
			for(PlayerData pd: PlayerData.getAlivePlayers()) {
				if(Target.hasTarget(pd.getPlayer()) && Target.getTarget(pd.getPlayer()) != null) {
					if(pd.canUltimate()) {
						String message = "§7Distance de votre cible: §6" + (int)(pd.getPlayer().getLocation().distance(Target.getTarget(pd.getPlayer()).getLocation())) + " §7blocks";
						PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
						((CraftPlayer) pd.getPlayer()).getHandle().playerConnection.sendPacket(packet);
					}
				}
			}
			
			short emperorsAlive = 0;
			for(PlayerData pd: PlayerData.getAlivePlayers()) {
				if(pd.getPersonnage().getType() == 1) {
					if(PlayerData.hasFidèle(pd)) {
						PlayerData pd2 = PlayerData.getFidèle(pd);
						if(pd.getPlayer().getWorld().equals(pd2.getPlayer().getWorld()) && pd.getPlayer().getLocation().distance(pd2.getPlayer().getLocation()) <= 15) {
							pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 0, true, false));
							pd2.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 3, 0, true, false));
						}
					}
					emperorsAlive++;
				}
			}
			
			if(emperorsAlive == 0 && Main.getInstance().showPrimes) {				
				ArrayList<PlayerData> bcp = (ArrayList<PlayerData>) topList.clone();
				
				topList.clear();
				
				if(HeaderAndFooterTask.topList != null && HeaderAndFooterTask.topList.size() > 0) {
					int i = 0;
					for(Entry<PlayerData, Integer> entry: HeaderAndFooterTask.topList) {
						topList.add(entry.getKey());
						i++;
						if(i == 1) {
							//System.out.println("1 " + entry.getKey().getPlayer().getName());
							if(bcp.size() > 0 && entry.getKey() != bcp.get(0)) {
								//bcp.get(0).setMaxHealth(bcp.get(0).getMaxHealth() - 4);
								//entry.getKey().setMaxHealth(entry.getKey().getMaxHealth() + 4);
								bcp.get(0).setTopBonusHealth(0);
								entry.getKey().setTopBonusHealth(4);
							}
							else if(bcp.size() < 1) {
								//entry.getKey().setMaxHealth(entry.getKey().getMaxHealth() + 4);
								entry.getKey().setTopBonusHealth(4);
							}
							if(!entry.getKey().getPlayer().hasPotionEffect(PotionEffectType.SPEED))
								entry.getKey().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 0, true, false));
						}
						else if(i == 2) {
							//System.out.println("2 " + entry.getKey().getPlayer().getName());
							if(bcp.size() > 1 && entry.getKey() != bcp.get(1)) {
								//bcp.get(1).setMaxHealth(bcp.get(1).getMaxHealth() - 4);
								//entry.getKey().setMaxHealth(entry.getKey().getMaxHealth() + 4);
								bcp.get(1).setTopBonusHealth(0);
								entry.getKey().setTopBonusHealth(4);
							}
							else if(bcp.size() < 2) {
								//entry.getKey().setMaxHealth(entry.getKey().getMaxHealth() + 4);
								entry.getKey().setTopBonusHealth(4);
							}
						}
						else if(i == 3) {
							//System.out.println("3 " + entry.getKey().getPlayer().getName());
							if(bcp.size() > 2 && entry.getKey() != bcp.get(2)) {
								//bcp.get(2).setMaxHealth(bcp.get(2).getMaxHealth() - 2);
								//entry.getKey().setMaxHealth(entry.getKey().getMaxHealth() + 2);
								bcp.get(2).setTopBonusHealth(0);
								entry.getKey().setTopBonusHealth(2);
							}
							else if(bcp.size() < 3) {
								//entry.getKey().setMaxHealth(entry.getKey().getMaxHealth() + 2);
								entry.getKey().setTopBonusHealth(2);
							}
						}
					}
				}				
			}
		}
		/*******************************************************
		 * Lobby Instance
		 *******************************************************/	
		else {
			for(PlayerData pd: Main.getInstance().getPlayersData()) {
				/*******************************************************
				 * When falling from lobby
				 *******************************************************/
				if(pd.getPlayer().getLocation().getY() <= 120 && pd.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
					pd.getPlayer().teleport(new Location(Main.getInstance().centerMaplocation.getWorld(), 0.5, 203, 0.5));
				}				
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public static void checkEndFight() {
		if(Main.GAMESTATE.equals(GameState.Ended)) return;
		for(GameEvent ge: Main.getInstance().getGameEvents()) {
			int geSec = (ge.getMinute() * 60) + ge.getSecond();
			int sec = (Main.getInstance().minutes * 60) + Main.getInstance().secondes;
			if(ge.getClass().equals(RoleDistributionGameEvent.class) && geSec < sec) {
				int sizeEmereur = 0;
			    int sizePirates = 0;
			    int sizeFidele = 0;
			    for(PlayerData pd: PlayerData.getAlivePlayers()) {
			    	if(pd.getPersonnage().getType() == 0) {
			    		sizePirates++;
			    	}
			    	else if(pd.getPersonnage().getType() == 1) {
			    		sizeEmereur++;
			    	}
			    	else if(pd.getPersonnage().getType() == 2) {
			    		sizeFidele++;
			    	}
			    }
			    
			    if(sizeEmereur == 0) {
			    	if((sizePirates + sizeFidele) == 1) {
			    		endGame();
			    	}
			    	else if(!Main.getInstance().fightPirates) {
			    		Main.getInstance().fightPirates = true;
			    		// Fight between pirates
			    		Bukkit.broadcastMessage("§bInfo §7>> §fTous les empereurs sont morts");
			    		
						for(Player lp: Bukkit.getOnlinePlayers()) lp.playSound(lp.getLocation(), "wano.all_emperors_dead", 10, 1);
						for(TeamData td: Main.getInstance().getTeamData()) td.setTeamSuffix("");
						
			    		
			    		
			    		for(PlayerData pd: PlayerData.getAlivePlayers()) pd.getPlayer().sendTitle("§a", "§bTous les empereurs sont morts");
			    		
			    		/*Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								for(PlayerData pd: PlayerData.getAlivePlayers()) pd.getPlayer().sendTitle("§a", "§bTéléporation dans 1 min");
							}
						}, 2 * 20);*/
			    		
			    		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(Main.GAMESTATE.equals(GameState.Ended)) return;
					    		Bukkit.broadcastMessage("§bInfo §7>> §fTéléporation dans 10 secondes");
							}
						}, 5 * 20);
			    		
			    		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(Main.GAMESTATE.equals(GameState.Ended)) return;
					    		Bukkit.broadcastMessage("§bInfo §7>> §fTéléporation dans 5 secondes");
							}
						}, 10 * 20);
			    		
			    		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(Main.GAMESTATE.equals(GameState.Ended)) return;
					    		Bukkit.broadcastMessage("§bInfo §7>> §fTéléporation dans 3 secondes");
							}
						}, 12 * 20);
			    		
			    		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(Main.GAMESTATE.equals(GameState.Ended)) return;
					    		Bukkit.broadcastMessage("§bInfo §7>> §fTéléporation dans 2 secondes");
							}
						}, 13 * 20);
			    		
			    		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(Main.GAMESTATE.equals(GameState.Ended)) return;
					    		Bukkit.broadcastMessage("§bInfo §7>> §fTéléporation dans 1 secondes");
							}
						}, 14 * 20);
			    		
			    		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(Main.GAMESTATE.equals(GameState.Ended)) return;
								teleportPiratesFight();
								Bukkit.getWorld(Main.getInstance().WanoWorldName).getWorldBorder().setCenter(Main.getInstance().WanoOnigashimaCenterLocation);
					    		Bukkit.getWorld(Main.getInstance().WanoWorldName).getWorldBorder().setSize(Main.getInstance().WanoOnigashimaBeforeBorderSize);
					    		Bukkit.getWorld(Main.getInstance().WanoWorldName).getWorldBorder().setSize(Main.getInstance().WanoOnigashimaAfterBorderSize, 60 * 10);
							}
						}, 15 * 20);
					}
			    }
			    else if(sizePirates == 0) {
			    	if(sizeEmereur == 1) {
			    		endGame();
			    	}
			    	else if(!Main.getInstance().fightPirates) {
			    		Main.getInstance().fightPirates = true;
			    		// Fight between emperors
			    		Bukkit.broadcastMessage("§bInfo §7>> §fTous les pirates sont morts");
			    		
						for(Player lp: Bukkit.getOnlinePlayers()) lp.playSound(lp.getLocation(), "wano.all_pirates_dead", 10, 1);
			    		
			    		for(PlayerData pd: PlayerData.getAlivePlayers()) pd.getPlayer().sendTitle("§a", "§bTous les pirates sont morts");
					}
			    }
			}
		}		
	}
	
	@SuppressWarnings("deprecation")
	public static void teleportPiratesFight() {
		ArrayList<PlayerData> list = PlayerData.getAlivePlayers();
		Collections.shuffle(list);
		for(Location loc: Main.getInstance().getWanoOnigashimaLocations()) {
			if(list.size() != 0) {
    			list.get(0).getPlayer().teleport(loc);
    			list.remove(0);
			}
		}
		
		Main.getInstance().playersCanMove = false;
		
		Sound sound = Sound.NOTE_PLING;
		//String sound = "OPU.BLING";
		
		for(PlayerData pd: PlayerData.getAlivePlayers()) {
			
			pd.getPlayer().setWalkSpeed(0);
			pd.setImobilized(true);
			
			pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
			pd.getPlayer().sendTitle("§a", "§b> §7§l10 §r§b<");
		}
			
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().sendTitle("§a", "§a");
				}
			}
		}, 40);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l5 §r§b<");
				}
			}
		}, 100);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l4 §r§b<");
				}
			}
		}, 120);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l3 §r§b<");
				}
			}
		}, 140);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l2 §r§b<");
				}				
			}
		}, 160);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l1 §r§b<");
				}
			}
		}, 180);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().setWalkSpeed((float) 0.2);
					pd.setImobilized(false);
					
					pd.getPlayer().sendTitle("§a", "§b> §7§lGo §r§b<");
				}
				Main.getInstance().playersCanMove = true;
			}
		}, 200);

		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					pd.getPlayer().sendTitle("§a", "§a");
				}
			}
		}, 240);
	}
	
	public static void endGame() {
		Main.GAMESTATE = GameState.Ended;
		
		PlayerData pd = null;
		
		if(PlayerData.getAlivePlayers().size() == 1) {
			pd = PlayerData.getAlivePlayers().get(0);
		}
		else {
			for(PlayerData pd1: PlayerData.getAlivePlayers()) {
				if(pd1.getPersonnage().getType() == 1) {
					pd = pd1;
				}
			}
			if(pd == null) {
				pd = PlayerData.getAlivePlayers().get(0);
				Bukkit.getConsoleSender().sendMessage("§cThe selection of the emperor failed !");
			}
		}
		
		
		if(pd.getPersonnage().getType() == 1 && PlayerData.hasFidèle(pd)) {
			PlayerData fid = PlayerData.getFidèle(pd);
			Bukkit.broadcastMessage("§b" + pd.getPlayer().getName() + " §aet §b" + fid.getPlayer().getName() + " §aont conquis le Nouveau Monde ! Bravo à vous !");
		}
		else {
			Bukkit.broadcastMessage("§b" + pd.getPlayer().getName() + " §aa conquis le Nouveau Monde ! Bravo à toi !");
		}
		
		
		for(PlayerData pd1: Main.getInstance().getPlayersData()) {
			pd1.getPlayer().teleport(Main.getInstance().WanoEndGameLocation);
			pd1.changePage(0);
			Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					pd1.changePage(pd1.page);
				}
			}, 20 * 5, 20 * 5);
			for(PlayerData pd2: Main.getInstance().getPlayersData()) {
				pd1.getPlayer().showPlayer(pd2.getPlayer());
			}
			pd1.getPlayer().setGameMode(GameMode.CREATIVE);
			pd1.getPlayer().getInventory().clear();
		}
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(Player lp: Bukkit.getOnlinePlayers()) lp.playSound(Main.getInstance().WanoEndGameLocation, "wano.game_end", 10, 1);
			}
		}, 30);
		

		Bukkit.getWorld(Main.getInstance().WanoWorldName).getWorldBorder().setCenter(Main.getInstance().centerMaplocation);
		Bukkit.getWorld(Main.getInstance().WanoWorldName).getWorldBorder().setSize(3000);
		
		
		Location loc = Main.getInstance().WanoScoreboardLocation.clone();
		ArmorStand armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setBasePlate(false);
		armor.setVisible(false);
		armor.setGravity(false);
		
		loc.setY(loc.getY() - 1);
		armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setBasePlate(false);
		armor.setVisible(false);
		armor.setGravity(false);
		
		loc.setY(loc.getY() - 1);
		armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setBasePlate(false);
		armor.setVisible(false);
		armor.setGravity(false);
		
		loc.setY(loc.getY() - 1);
		armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setBasePlate(false);
		armor.setVisible(false);
		armor.setGravity(false);
		
		loc = Main.getInstance().WanoScoreboardLocation.clone();
		loc.setY(loc.getY() + 1);
		armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setBasePlate(false);
		armor.setVisible(false);
		
		loc.setY(loc.getY() + 1);
		armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setBasePlate(false);
		armor.setVisible(false);
		armor.setGravity(false);
		
		loc.setY(loc.getY() + 1);
		armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armor.setBasePlate(false);
		armor.setVisible(false);
		armor.setGravity(false);
	}
}
