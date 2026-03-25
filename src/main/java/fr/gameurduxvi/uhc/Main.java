package fr.gameurduxvi.uhc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.gameurduxvi.uhc.Config.ConfigMain;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.Listeners.WorldInitListener;
import fr.gameurduxvi.uhc.Refractor.SchematicLoad;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;
import fr.gameurduxvi.uhc.scenarios.ScenarioManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class Main extends JavaPlugin{
	
	private static Main instance;
	private static ScenarioManager scenariosManager;
	public static ConfigMain configMain;
	public static ConfigManager configManager;
	
	public String pluginName = "Wano UHC";
	public String pluginPrefix = "§r[§b" + pluginName + "§r]";
	public String pluginDir = "plugins/WanoUHC/";
	
	public ScoreboardManager manager;
	
	private ArrayList<PlayerData> playersData = new ArrayList<>();
	private ArrayList<PlayerData> offlinePlayersData = new ArrayList<>();
	private ArrayList<Scenario> scenarios = new ArrayList<>();
	private ArrayList<Personnage> personages = new ArrayList<>();
	private ArrayList<GameEvent> gameEvents = new ArrayList<>();
	private ArrayList<TeamData> teamData = new ArrayList<>();

	public short secondes = 0;
	public short minutes = 0;
	public short episode = 1;
	public boolean playersCanMove = true;
	public Location centerMaplocation;
	
	public boolean showPrimes = false;
	
	public boolean anyDamage = false;
	
	public static GameState GAMESTATE = GameState.Idle;
	//public boolean gameStarted = false;
	//public boolean gamePaused = false;
	
	public boolean fightPirates = false;
	//public boolean gameEnd = false;
	
	public int primePirate = 10000;
	public int primeFidele= 20000;
	public int primeEmpereur = 30000;
	public int primeParKill = 4000;
	
	public String nextEvent = "";
	
	public String WanoWorldName = "";
	public Location WanoEndGameLocation = null;
	public Location WanoScoreboardLocation = null;
	private ArrayList<Location> WanoMapLocations = new ArrayList<>();
	private ArrayList<Location> WanoOnigashimaLocations = new ArrayList<>();
	private ArrayList<Location> WanoChestsLocations = new ArrayList<>();
	private ArrayList<Material> WanoUnbrakebableBlocks = new ArrayList<>();
	private ArrayList<Location> WanoPosedLocations = new ArrayList<>();
	public Location WanoOnigashimaCenterLocation = null;
	public int WanoOnigashimaBeforeBorderSize = 300;
	public int WanoOnigashimaAfterBorderSize = 50;	
	
	@SuppressWarnings({ "static-access" })
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§bEnabling Wanno UHC");
				
		/*******************************************************
		 * Instances
		 *******************************************************/
		instance = this;
		scenariosManager = new ScenarioManager();
		
		
		/*******************************************************
		 * Config Instances
		 *******************************************************/
		File pluginDirectory = new File(pluginDir);
		if(!pluginDirectory.exists()) {
			pluginDirectory.mkdir();
		}
		
		configMain = new ConfigMain();
		
		configManager = new ConfigManager();
		
		
		
		/*******************************************************
		 * Scoreboard Manager
		 *******************************************************/
		manager = Bukkit.getScoreboardManager();
		
		
		
		/*******************************************************
		 * Map Options
		 *******************************************************/
		Bukkit.getServer().getPluginManager().registerEvents(new WorldInitListener(), Main.getInstance());
		
		for(Player p1: Bukkit.getOnlinePlayers()) {
			p1.setGameMode(GameMode.SPECTATOR);
		}
		
		// Monde minage
		String worldName = "Mining";
		
		File worldFile = new File(worldName + "/");
		
		if(Bukkit.getWorld(worldName) != null) {
			Bukkit.unloadWorld(worldName, false);
		}
		
		if(worldFile.exists()) {
			try {
				FileUtils.deleteDirectory(worldFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
		if(Bukkit.getWorld(worldName) != null) {
			centerMaplocation = new Location(Bukkit.getWorld(worldName), 0, 200, 0);
		}
		else {
			ArrayList<Long> seeds = new ArrayList<>();
			seeds.add(Long.parseLong("-715187289534188764"));
			seeds.add(Long.parseLong("9087590210243401434"));
			seeds.add(Long.parseLong("1937454849482155110"));
			seeds.add(Long.parseLong("5766480477522264527"));
			seeds.add(Long.parseLong("5152874708707397847"));
			seeds.add(Long.parseLong("8597993869422556363"));
			seeds.add(Long.parseLong("7709308338800066628"));
			seeds.add(Long.parseLong("3488936712824092210"));
			seeds.add(Long.parseLong("-9092199965578595820"));
			seeds.add(Long.parseLong("7291115122033936734"));
			Collections.shuffle(seeds);
			World world = new WorldCreator(worldName).seed(seeds.get(0)).createWorld();
			//World world = new WorldCreator(worldName).seed(Bukkit.getWorlds().get(0).getSeed()).createWorld();
			centerMaplocation = new Location(world, 0, 200, 0);
		}
		
		
		// Wano world
		WanoWorldName = configMain.getFileConfig().getString("WanoWorldName");
		new WorldCreator(WanoWorldName).createWorld();
		
		
		Bukkit.setSpawnRadius(0);
		
		// Worlds settings
		centerMaplocation.getWorld().setGameRuleValue("naturalRegeneration", "false");
		centerMaplocation.getWorld().setGameRuleValue("doDaylightCycle", "false");
		centerMaplocation.getWorld().setGameRuleValue("doFireTick", "false");
		centerMaplocation.getWorld().setTime(6000);
		centerMaplocation.getWorld().getWorldBorder().setCenter(centerMaplocation);
		centerMaplocation.getWorld().getWorldBorder().setSize(3000);
		
		Bukkit.getWorld(WanoWorldName).setGameRuleValue("naturalRegeneration", "false");
		Bukkit.getWorld(WanoWorldName).setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld(WanoWorldName).setTime(6000);
		Bukkit.getWorld(WanoWorldName).getWorldBorder().setCenter(centerMaplocation);
		Bukkit.getWorld(WanoWorldName).getWorldBorder().setSize(3000);
		
		
		/*******************************************************
		 * Shematics
		 *******************************************************/
		File dir = new File(pluginDir + "schematics");
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		if (!new File(pluginDir + "schematics/lobby.schematic").exists()) {
			//saveResource("lobby.schematic", false);
			try {
				FileUtils.copyInputStreamToFile(this.getResource("lobby.schematic"), new File(pluginDir + "schematics/lobby.schematic"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §6Placing lobby");
		new Location(centerMaplocation.getWorld(), centerMaplocation.getX(), centerMaplocation.getY(), centerMaplocation.getZ()).getBlock().setType(Material.STONE);
		new SchematicLoad(centerMaplocation, pluginDir + "schematics/lobby.schematic", false, 0);
		

		
		/*******************************************************
		 * initialise plugin
		 *******************************************************/
		Initialiser.init();
		
		
		/*******************************************************
		 * Make join the players
		 *******************************************************/
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				for(Player lp: Bukkit.getOnlinePlayers()) {
					PlayerJoinEvent event = new PlayerJoinEvent(lp, "");
					Bukkit.getPluginManager().callEvent(event);
				}
			}
		}, 2);
		
		for(Player p1: Bukkit.getOnlinePlayers()) {
			for(Player p2: Bukkit.getOnlinePlayers()) {
				p1.showPlayer(p2);
			}
		}

		/*Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				int chunksPerRun = 20;
				int radius = 2100;
				(CommandPregen.worldFillTask = new WorldFillTask(Main.getInstance().centerMaplocation.getWorld().getName(), chunksPerRun, radius)).setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), (Runnable)CommandPregen.worldFillTask, 1L, 1L));
		    	Bukkit.getConsoleSender().sendMessage("�7Pregen �8>> �b WorldBorder map generation task for world \"" + Main.getInstance().centerMaplocation.getWorld().getName() + "\" started.");
				
			}
		}, 3 * 20);*/
		
		/*******************************************************
		 * Scenario load
		 *******************************************************/
		scenariosManager.onLoad();
		
		
		/*******************************************************
		 * Purge unused classes
		 *******************************************************/
		System.gc();
	}
	
	@Override
	public void onDisable() {
		for(Player lp: Bukkit.getOnlinePlayers()) {
			lp.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			lp.setHealth(1);
			lp.setMaxHealth(20);
			lp.setHealth(20);
			
			lp.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			for(Objective ob: lp.getScoreboard().getObjectives()) {
				ob.unregister();
			}
			lp.sendMessage("§7Veuillez patienter, chargement de la map...");
		}
		Bukkit.getConsoleSender().sendMessage("§bDisabling Wanno UHC");
	}
	
	public String argument(String[] args, int i) {
		if(args.length >= i) {
			return args[i-1];
		}
		return "";
	}
	
	public void setHeaderFooter(Player p, String header, String footer) {
		PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
		IChatBaseComponent top = ChatSerializer.a("{\"text\":\"" + header + "\"}");
		IChatBaseComponent bottom = ChatSerializer.a("{\"text\":\"" + footer + "\"}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		 
	    try {
	        Field headerField = packet.getClass().getDeclaredField("a");
	        headerField.setAccessible(true);
	        headerField.set(packet, top);
	        headerField.setAccessible(!headerField.isAccessible());
	     
	        Field footerField = packet.getClass().getDeclaredField("b");
	        footerField.setAccessible(true);
	        footerField.set(packet, bottom);
	        footerField.setAccessible(!footerField.isAccessible());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	 
	    con.sendPacket(packet);
	}
	
	public void sendActionBarMessage(Player p, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	public boolean isNull(Object o) {
		try {
			if(!o.equals(null)) {
				return false;
			}
		} catch (Exception e) {
		}
		return true;
	}
	
	
	
	/*@SuppressWarnings("unchecked")
	public void spawnShips() {
		String[] schematics = {"ship", "big_mom", "barbe_noir", "shanks", "kaido", "land", "emptyShip"};
		
		for(String schematicName: schematics) {
			if(!new File(pluginDir + "schematics/" + schematicName + ".schematic").exists()) {
				//saveResource("ship.schematic", false);
				try {
					FileUtils.copyInputStreamToFile(this.getResource(schematicName + ".schematic"), new File(pluginDir + "schematics/" + schematicName + ".schematic"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		File verifFile = new File(centerMaplocation.getWorld().getName() + "/shipSpawn.json");
		
		ArrayList<Personnage> emperors = new ArrayList<>();
		for(Personnage pers: getPersonages()) {
			if(pers.getType() == 1) {
				emperors.add(pers);
			}
		}
		
		int pirates = 0;
		for(Personnage pers: getPersonages()) {
			if(pers.getType() != 1) {
				pirates += pers.getAmount();
			}
		}
			
		int amountBoats = (int) (emperors.size() + Math.ceil((pirates / 4)));		
		
		if(!verifFile.exists()) {
			Bukkit.getConsoleSender().sendMessage("�b" + Main.getInstance().pluginName +  " �8>>  �8>> �6Placing ships on the map");
			JSONArray ja = new JSONArray();
			
			for(int i = 0; i < amountBoats; i++) {
				int angle = i * (360 / amountBoats);
				double x = centerMaplocation.getX() + (Math.sin(Math.toRadians(angle)) * 600);
				double z = centerMaplocation.getZ() + (Math.cos(Math.toRadians(angle)) * 600);
				
				int y = 0;
				for(int tempY = 255; tempY >= 0; tempY--) {
					Location loc = new Location(centerMaplocation.getWorld(), x, tempY, z);
					if(!loc.getBlock().getType().equals(Material.AIR) &&
							!loc.getBlock().getType().equals(Material.LEAVES) &&
							!loc.getBlock().getType().equals(Material.LOG)) {
						y = tempY + 5;
						break;
					}
				}
				
				final Location loc = new Location(centerMaplocation.getWorld(), x, y, z);
				
				TeamData bd = null;
				
				String shipName;
				if(i < emperors.size()) {
					bd = new TeamData(loc, 1);
					bd.setLinkedId(emperors.get(i).getId());
					shipName = emperors.get(i).getShipFileName();
				}
				else {
					bd = new TeamData(loc, 4);
					shipName = "ship";
				}
				
				JSONObject jo = new JSONObject();
				
				jo.put("location", (int) loc.getX() + "," + (int) loc.getY() + "," + (int) loc.getZ());
				jo.put("max", bd.getMax());
				jo.put("linkedId", bd.getLinkedId());
				
				ja.add(jo);
				
				getTeamData().add(bd);
				
				new SchematicLoad(loc, pluginDir + "schematics/emptyShip.schematic", false, Material.AIR);
				
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						new SchematicLoad(loc, pluginDir + "schematics/land.schematic", false, 0);
					}
				}, 5);
				
				final String finalShipName = shipName;
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						new SchematicLoad(loc, pluginDir + "schematics/" + finalShipName + ".schematic", false, 0);
					}
				}, 10);
			}
			try (FileWriter fw = new FileWriter(centerMaplocation.getWorld().getName() + "/shipSpawn.json")) {
	            fw.write(ja.toJSONString());
	            fw.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			Bukkit.getConsoleSender().sendMessage("�b" + pluginName +  " �8>>  �8>> �aShips placed");
		}
		else {
			try {
				Bukkit.getConsoleSender().sendMessage("�b" + pluginName +  " �8>> �6Loading existing ships data");
				JSONArray ja = (JSONArray) new JSONParser().parse(new FileReader(centerMaplocation.getWorld().getName() + "/shipSpawn.json"));
				
				for(Object obj: ja) {
					JSONObject jo = (JSONObject) obj;
					
					String[] splitLoc = ((String)jo.get("location")).split(",");
					int x = Integer.parseInt(splitLoc[0]);
					int y = Integer.parseInt(splitLoc[1]);
					int z = Integer.parseInt(splitLoc[2]);
					Location location = new Location(centerMaplocation.getWorld(), x, y, z);
					
					int max = Integer.parseInt("" + (Long) jo.get("max"));
					
					int linkedId = Integer.parseInt("" + (Long) jo.get("linkedId"));
					
					TeamData bd = new TeamData(location, max);
					bd.setLinkedId(linkedId);
					
					getTeamData().add(bd);
				}
				Bukkit.getConsoleSender().sendMessage("�b" + pluginName +  " �8>> �aExisting ships data loaded");
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	public static Main getInstance() {
		return instance;
	}
	
	public static ScenarioManager getScenariosManager() {
		return scenariosManager;
	}
	
	public ArrayList<PlayerData> getPlayersData() {
		return playersData;
	}
	public ArrayList<Personnage> getPersonages() {
		return personages;
	}
	public ArrayList<Scenario> getScenarios() {
		return scenarios;
	}
	public ArrayList<PlayerData> getOfflinePlayersData() {
		return offlinePlayersData;
	}
	public ArrayList<GameEvent> getGameEvents() {
		return gameEvents;
	}
	public ArrayList<TeamData> getTeamData() {
		return teamData;
	}
	public ArrayList<Material> getWanoUnbrakebableBlocks() {
		return WanoUnbrakebableBlocks;
	}
	public ArrayList<Location> getWanoPosedLocations() {
		return WanoPosedLocations;
	}
	public ArrayList<Location> getWanoChestsLocations() {
		return WanoChestsLocations;
	}
	public ArrayList<Location> getWanoOnigashimaLocations() {
		return WanoOnigashimaLocations;
	}
	public ArrayList<Location> getWanoMapLocations() {
		return WanoMapLocations;
	}
	
}
