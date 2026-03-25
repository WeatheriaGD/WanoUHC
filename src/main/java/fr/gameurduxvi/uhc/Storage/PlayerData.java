package fr.gameurduxvi.uhc.Storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Personnages.Empty;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;

public class PlayerData {
	protected Player player;
	protected int prime = 0;
	protected short kills = 0;
	protected Entity lastDamager = null;
	protected Date lastDamagerDate = new Date();
	
	protected double totalDamage;
	
	protected Personnage personnage;
	//private Player linkedTo = null;
	
	protected boolean canUltimate = true;	
	protected boolean imobilized;
	
	protected double maxHealth = 20;
	protected double malusHealth = 0;
	protected double topBonusHealth = 0;
	
	public boolean isDeath = false;
	public boolean isSpec = false;
	
	public boolean particles = true;
	public boolean resourcePack = false;
	
	private TeamData team = null;
	
	public boolean lateScatter = false;
	
	public Location spawnLocation = null;
	
	public int page = 0;
	public ArrayList<EntityArmorStand> armorstands = new ArrayList<>();
	
	public Location deathLocation = null;
	
	
	public PlayerData(Player player) {
		this.player = player;
		this.personnage = new Empty();
	}	
	/*******************************************************
	 * Get Functions
	 *******************************************************/
	public Player getPlayer() {
		return player;
	}
	
	public int getPrime() {
		return prime;
	}
	
	public short getKills() {
		return kills;
	}
	
	public Entity getLastDamager() {
		return lastDamager;
	}
	
	public Date getLastDamagerDate() {
		return lastDamagerDate;
	}
	
	public Personnage getPersonnage() {
		return personnage;
	}
	
	/*public Player getLinkedTo() {
		return linkedTo;
	}*/
	
	public boolean canUltimate() {
		return canUltimate;
	}
	
	public boolean isImobilized() {
		return imobilized;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}
	
	public double getMalusHealth() {
		return malusHealth;
	}
	
	public double getTopBonusHealth() {
		return topBonusHealth;
	}
	
	public TeamData getTeam() {
		return team;
	}
	
	
	/*******************************************************
	 * Set Functions
	 *******************************************************/
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setPrime(int prime) {
		this.prime = prime;
	}
	
	public void setKills(short kills) {
		this.kills = kills;
	}
	
	public void setLastDamager(Entity lastDamager) {
		this.lastDamager = lastDamager;
	}
	
	public void setLastDamagerDate(Date lastDamagerDate) {
		this.lastDamagerDate = lastDamagerDate;
	}
	
	public void setPersonnage(Personnage personnage) {
		this.personnage = personnage;
	}
	
	/*public void setLinkedTo(Player linkedTo) {
		this.linkedTo = linkedTo;
	}*/	
	
	public void setCanUltimate(boolean canUltimate) {
		this.canUltimate = canUltimate;
	}
	
	public void setImobilized(boolean imobilized) {
		this.imobilized = imobilized;
	}
	
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
		updateMaxHealth();		
	}
	
	public void setMalusHealth(double malusHealth) {
		this.malusHealth = malusHealth;
		updateMaxHealth();
	}
	
	public void setTopBonusHealth(double topBonusHealth) {
		this.topBonusHealth = topBonusHealth;
		updateMaxHealth();
	}
	
	public void setTeam(TeamData team) {
		this.team = team;
	}
	
	public void addTotalDamage(double damage) {
		this.totalDamage += damage;
	}
	
	/*******************************************************
	 * Other Functions
	 *******************************************************/
	
	public void updateMaxHealth() {
		if(maxHealth + malusHealth + topBonusHealth > 0) {
			player.setMaxHealth(maxHealth + malusHealth + topBonusHealth);
		}
		else
		{
			Bukkit.getConsoleSender().sendMessage("§cCan't set the MaxHealth value for " + player.getName() + " at updateMaxHealth() in PlayerData.java");
		}
	}
	
	public void heal() {
		updateMaxHealth();
		player.setHealth(player.getMaxHealth());
	}
	
	public static boolean hasOfflinePlayerData(Player p) {
		for(PlayerData data: Main.getInstance().getOfflinePlayersData()) {
			if(data.getPlayer().getName().equals(p.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static PlayerData getOfflinePlayerData(String name) {
		for(PlayerData data: Main.getInstance().getOfflinePlayersData()) {
			if(data.getPlayer().getName().equals(name)) {
				return data;
			}
		}
		return null;
	}
	
	public static boolean hasPlayerData(Player p) {
		for(PlayerData data: Main.getInstance().getPlayersData()) {
			if(data.getPlayer().equals(p)) {
				return true;
			}
		}
		return false;
	}
	
	public static PlayerData getPlayerData(Player p) {
		for(PlayerData data: Main.getInstance().getPlayersData()) {
			if(data.getPlayer().equals(p)) {
				return data;
			}
		}
		return null;
	}
	
	public static boolean hasFidèle(PlayerData pd) {
		if(pd.getPersonnage().getType() != 1) return false;
		
		for(PlayerData data: Main.getInstance().getPlayersData()) {
			if(data.getPersonnage().getId() == (pd.getPersonnage().getId() + 100)) {
				return true;
			}
		}		
		return false;
	}
	
	public static PlayerData getFidèle(PlayerData pd) {
		if(pd.getPersonnage().getType() != 1) return null;
		
		for(PlayerData data: Main.getInstance().getPlayersData()) {
			if(data.getPersonnage().getId() == (pd.getPersonnage().getId() + 100)) {
				return data;
			}
		}		
		return null;
	}
	
	public static String getPrefix(Player QuiVoit, Player QuelPrefix) {		
		if(hasPlayerData(QuiVoit) && hasPlayerData(QuelPrefix)) {
			PlayerData pd1 = getPlayerData(QuiVoit);
			PlayerData pd2 = getPlayerData(QuelPrefix);
			String prefix = "";
			for(PrefixData prf1: pd2.getPersonnage().getPrefixData()) {
				for(int type: prf1.getWhoCanSee()) {
					if(type == -1 || type == pd1.getPersonnage().getId()) {
						//prefix = prf1.getPrefix();			    
					    String anonymousPrefix = "";
						if(Scenario.isActive(Main.getScenariosManager().ANONYMOUS_NAME)) {
							anonymousPrefix = "§k";
						}
						prefix = prf1.getPrefix() + anonymousPrefix;
					}
				}
			}
			return prefix;
		}
		return "";
	}
	
	public static String getSuffix(Player QuiVoit, Player QuelPrefix) {		
		if(hasPlayerData(QuiVoit) && hasPlayerData(QuelPrefix)) {
			PlayerData pd1 = getPlayerData(QuiVoit);
			PlayerData pd2 = getPlayerData(QuelPrefix);
			String suffix = "";
			for(PrefixData prf1: pd2.getPersonnage().getPrefixData()) {
				for(int type: prf1.getWhoCanSee()) {
					if(type == -1 || type == pd1.getPersonnage().getId()) {
						//prefix = prf1.getPrefix();
						String anonymousSuffix = "";
						if(Scenario.isActive(Main.getScenariosManager().ANONYMOUS_NAME)) {
							for(int i = pd2.getPlayer().getName().length(); i<16; i++) {
								anonymousSuffix = anonymousSuffix + "a";
							}
						}
						suffix = anonymousSuffix;
					}
				}
			}
			return suffix;
		}
		return "";
	}
	
	public static ArrayList<PlayerData> getAlivePlayers() {
		ArrayList<PlayerData> list = new ArrayList<>();
		for(PlayerData d: Main.getInstance().getPlayersData()) {
			if(!d.isDeath && !d.isSpec) list.add(d);
		}
		return list;
	}
	
	public static ArrayList<PlayerData> getSpectatorPlayers() {
		ArrayList<PlayerData> list = new ArrayList<>();
		for(PlayerData d: Main.getInstance().getPlayersData()) {
			if(d.isSpec) list.add(d);
		}
		return list;
	}
	
	public static ArrayList<PlayerData> getDeathPlayers() {
		ArrayList<PlayerData> list = new ArrayList<>();
		for(PlayerData d: Main.getInstance().getPlayersData()) {
			if(d.isSpec) list.add(d);
		}
		return list;
	}
	
	public static void toggleDeath(Player p, boolean dead) {
		if(!hasPlayerData(p)){
			Bukkit.getConsoleSender().sendMessage("§c" + p.getName() + " has no PlayerData (PlayerDataFunctions.java:toggleDeath)");
			return;
		}
		else {
			PlayerData pd = getPlayerData(p);
			
			pd.isDeath = dead;
			
			if(!pd.isDeath && !pd.isSpec) {
				pd.getPlayer().setGameMode(GameMode.SURVIVAL);
			}
			else {
				pd.getPlayer().setGameMode(GameMode.SPECTATOR);
			}
			
			refreshPlayersVieuw();
		}
	}
	
	public static void toggleSpectator(Player p, boolean spectator) {
		if(!hasPlayerData(p)){
			Bukkit.getConsoleSender().sendMessage("§c" + p.getName() + " has no PlayerData (PlayerDataFunctions.java:toggleSpectator)");
			return;
		}
		else {
			PlayerData pd = getPlayerData(p);
			
			pd.isSpec = spectator;
			
			if(!pd.isDeath && !pd.isSpec) {
				pd.getPlayer().setGameMode(GameMode.SURVIVAL);
			}
			else {
				pd.getPlayer().setGameMode(GameMode.SPECTATOR);
			}
			
			refreshPlayersVieuw();
		}
	}
	
	public static void refreshPlayersVieuw() {
		for(PlayerData pd: Main.getInstance().getPlayersData()) {
			if(!pd.isDeath && !pd.isSpec) {
				// pd.getPlayer() is alive
				
				for(PlayerData d: getAlivePlayers()) {
					
					// Show all alive players for player
					pd.getPlayer().showPlayer(d.getPlayer());
					
					// Show player for alive players
					d.getPlayer().showPlayer(pd.getPlayer());
				}
				
				for(PlayerData d: getSpectatorPlayers()) {
					
					// Hide all spectators players for player
					pd.getPlayer().hidePlayer(d.getPlayer());
					
					// Show player for spectators players
					d.getPlayer().showPlayer(pd.getPlayer());
				}
				
				for(PlayerData d: getDeathPlayers()) {
					
					// Hide all death players for player
					pd.getPlayer().hidePlayer(d.getPlayer());
					
					// Show player for death players
					d.getPlayer().showPlayer(pd.getPlayer());
				}
			}
			else {
				// pd.getPlayer() is death
				
				for(PlayerData d: getAlivePlayers()) {
					
					// Show all alive players for player
					pd.getPlayer().showPlayer(d.getPlayer());
					
					// Hide player for allive players
					d.getPlayer().hidePlayer(pd.getPlayer());
				}
				
				for(PlayerData d: getSpectatorPlayers()) {
					
					// Show all spectators players for player
					pd.getPlayer().showPlayer(d.getPlayer());
					
					// Show player for spectators players
					d.getPlayer().showPlayer(pd.getPlayer());
				}
				
				for(PlayerData d: getDeathPlayers()) {
					
					// Show all death players for player
					pd.getPlayer().showPlayer(d.getPlayer());
					
					// Show player for death players
					d.getPlayer().showPlayer(pd.getPlayer());
				}
			}
		}
	}
	
	public static void givePrime(PlayerData pd1, PlayerData pd2) {		
		/*int prime = 0;
		
		prime = pd1.getPersonnage().getPrime() + (pd1.getKills() * Main.getInstance().primeParKill);
		
		// Adding a kill and prime to attacker
		pd2.setKills((short) (pd2.getKills() + 1));
		pd2.setPrime(pd2.getPrime() + prime);*/
		pd2.setKills((short) (pd2.getKills() + 1));
		/*if(pd2.getPersonnage().getType() != 1)
			pd2.setPrime(pd1.getPrime() + pd2.getPrime());*/
		
		if(pd1.getPersonnage().getType() == 1)
			pd2.setPrime(pd2.getPrime() + Main.getInstance().primeEmpereur + (pd1.getKills() * 4000));
		else if(pd1.getPersonnage().getType() == 2)
			pd2.setPrime(pd2.getPrime() + Main.getInstance().primeFidele + (pd1.getKills() * 4000));
		else
			pd2.setPrime(pd2.getPrime() + Main.getInstance().primePirate + (pd1.getKills() * 4000));
	}
	
	public void nextPage() {
		if(page >= 2) {
			page = 0;
		}
		else {
			page++;
		}
		changePage(page);
	}
	
	public void PreviousPage() {
		if(page <= 0) {
			page = 2;
		}
		else {
			page--;
		}
		changePage(page);
	}
	
	public void changePage(int page) {
		int max = 0;
		ArrayList<PlayerData> completeList = new ArrayList<>();
		for(PlayerData pd: Main.getInstance().getPlayersData()) {
			if(!pd.isSpec) {
				completeList.add(pd);				
			}
		}
		for(PlayerData pd: Main.getInstance().getOfflinePlayersData()) {
			if(!pd.isSpec) {
				completeList.add(pd);				
			}
		}
		
		for(EntityArmorStand e: armorstands) {
			PacketPlayOutEntityDestroy destroy2 = new PacketPlayOutEntityDestroy(e.getId());
            ((CraftPlayer)getPlayer()).getHandle().playerConnection.sendPacket(destroy2);
		}
		
		if(page == 0) {			
			HashMap<String, Short> hm = new HashMap<>();
			for(PlayerData pd: completeList) {
				hm.put(pd.getPlayer().getName(), pd.getKills());
				max++;
			}
			
			// Create a list from elements of HashMap
			List<Map.Entry<String, Short>> list = new LinkedList<Map.Entry<String, Short>>(hm.entrySet());
			
			// Sort the list
			Collections.sort(list, new Comparator<Map.Entry<String, Short>>() {
				public int compare(Map.Entry<String, Short> o2, Map.Entry<String, Short> o1)
				{
					return (o1.getValue()).compareTo(o2.getValue());
				}
			});
			
			
			Location loc = Main.getInstance().WanoScoreboardLocation.clone();
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 4)));
			
			armorstand(getPlayer(), loc, "§3>> §b§lScoreboard §r§3<<");
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 3)));
			armorstand(getPlayer(), loc, "§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌");
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 2)));
			armorstand(getPlayer(), loc, "§7Kills");
			
			
			
			for(Entry<String, Short> a : list) {
				loc = Main.getInstance().WanoScoreboardLocation.clone();
				loc.setY(loc.getY() + (0.25 * max));
				
				int lenght = 20 - a.getKey().length() - ("" + a.getValue()).length();
				String text = "§9【§f" + a.getKey() + "§9】§8";

				
				for(int i = 0; i <= lenght; i++) {
					text += "-";
				}
				
				text += "§f " + a.getValue();
				
				armorstand(getPlayer(), loc, text);
				max--;
			}
			
			loc.setY(loc.getY() + (0.25 * (-1.5)));
			armorstand(getPlayer(), loc, "§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌");
			
			loc.setY(loc.getY() - 0.25);
			armorstand(getPlayer(), loc, "§8【§b<-- Click Gauche§8】 §8【§bClick Droit -->§8】");
		}
		else if(page == 1) {			
			HashMap<String, Integer> hm = new HashMap<>();
			for(PlayerData pd: completeList) {
				hm.put(pd.getPlayer().getName(), pd.getPrime());
				max++;
			}
			
			// Create a list from elements of HashMap
			List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());
			
			// Sort the list
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o2, Map.Entry<String, Integer> o1)
				{
					return (o1.getValue()).compareTo(o2.getValue());
				}
			});
			
			
			Location loc = Main.getInstance().WanoScoreboardLocation.clone();
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 4)));
			
			armorstand(getPlayer(), loc, "§3>> §b§lScoreboard §r§3<<");
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 3)));
			armorstand(getPlayer(), loc, "§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌");
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 2)));
			armorstand(getPlayer(), loc, "§6Prime");
			
			
			
			for(Entry<String, Integer> a : list) {
				loc = Main.getInstance().WanoScoreboardLocation.clone();
				loc.setY(loc.getY() + (0.25 * max));
				
				int lenght = 20 - a.getKey().length() - ((a.getValue()/1000) + "k").length();
				String text = "§9【§f" + a.getKey() + "§9】§8";
				
				for(int i = 0; i <= lenght; i++) {
					text += "-";
				}
				
				text += "§f " + (a.getValue()/1000) + "k";
				
				armorstand(getPlayer(), loc, text);
				max--;
			}
			
			loc.setY(loc.getY() + (0.25 * (-1.5)));
			armorstand(getPlayer(), loc, "§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌");
			
			loc.setY(loc.getY() - 0.25);
			armorstand(getPlayer(), loc, "§8【§b<-- Click Gauche§8】 §8【§bClick Droit -->§8】");
		}
		else if(page == 2) {			
			HashMap<String, Integer> hm = new HashMap<>();
			for(PlayerData pd: completeList) {
				hm.put(pd.getPlayer().getName(), (int) pd.totalDamage);
				max++;
			}
			
			// Create a list from elements of HashMap
			List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());
			
			// Sort the list
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> o2, Map.Entry<String, Integer> o1)
				{
					return (o1.getValue()).compareTo(o2.getValue());
				}
			});
			
			
			Location loc = Main.getInstance().WanoScoreboardLocation.clone();
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 4)));
			
			armorstand(getPlayer(), loc, "§3>> §b§lScoreboard §r§3<<");
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 3)));
			armorstand(getPlayer(), loc, "§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌");
			
			loc.setY(Main.getInstance().WanoScoreboardLocation.clone().getY() + (0.25 * (max + 2)));
			armorstand(getPlayer(), loc, "§cDégats totals");
			
			
			
			for(Entry<String, Integer> a : list) {
				loc = Main.getInstance().WanoScoreboardLocation.clone();
				loc.setY(loc.getY() + (0.25 * max));
				
				int lenght = 20 - a.getKey().length() - ("" + a.getValue()).length();
				String text = "§9【§f" + a.getKey() + "§9】§8";
				
				for(int i = 0; i <= lenght; i++) {
					text += "-";
				}
				
				text += "§f " + a.getValue();
				
				armorstand(getPlayer(), loc, text);
				max--;
			}
			
			loc.setY(loc.getY() + (0.25 * (-1.5)));
			armorstand(getPlayer(), loc, "§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌");
			
			loc.setY(loc.getY() - 0.25);
			armorstand(getPlayer(), loc, "§8【§b<-- Click Gauche§8】 §8【§bClick Droit -->§8】");
		}
	}
	
	private void armorstand(Player p, Location loc, String text) {
		WorldServer s = ((CraftWorld)p.getLocation().getWorld()).getHandle();
        EntityArmorStand stand = new EntityArmorStand(s);
       
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName(text);
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setInvisible(true);
        
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        armorstands.add(stand);
	}
}
