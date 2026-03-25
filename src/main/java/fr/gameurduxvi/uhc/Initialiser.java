package fr.gameurduxvi.uhc;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.boydti.fawe.Fawe;

import fr.gameurduxvi.uhc.Commands.CommandChat;
import fr.gameurduxvi.uhc.Commands.CommandCommandes;
import fr.gameurduxvi.uhc.Commands.CommandConfig;
import fr.gameurduxvi.uhc.Commands.CommandDebug;
import fr.gameurduxvi.uhc.Commands.CommandEnd;
import fr.gameurduxvi.uhc.Commands.CommandFinalHeal;
import fr.gameurduxvi.uhc.Commands.CommandGDoc;
import fr.gameurduxvi.uhc.Commands.CommandInfo;
import fr.gameurduxvi.uhc.Commands.CommandListTexturePack;
import fr.gameurduxvi.uhc.Commands.CommandParticles;
import fr.gameurduxvi.uhc.Commands.CommandPause;
import fr.gameurduxvi.uhc.Commands.CommandRevive;
import fr.gameurduxvi.uhc.Commands.CommandRoles;
import fr.gameurduxvi.uhc.Commands.CommandSpectator;
import fr.gameurduxvi.uhc.Commands.CommandStart;
import fr.gameurduxvi.uhc.Commands.CommandTestPack;
import fr.gameurduxvi.uhc.Commands.CommandTestParticles;
import fr.gameurduxvi.uhc.Commands.CommandTexturePack;
import fr.gameurduxvi.uhc.Commands.CommandTrilog;
import fr.gameurduxvi.uhc.Commands.CommandUhc;
import fr.gameurduxvi.uhc.Commands.CommandWhitelist;
import fr.gameurduxvi.uhc.Commands.CommandWorld;
import fr.gameurduxvi.uhc.GameEvents.AntiDamageGameEvent;
import fr.gameurduxvi.uhc.GameEvents.FideleDistributionGameEvent;
import fr.gameurduxvi.uhc.GameEvents.RoleDistributionGameEvent;
import fr.gameurduxvi.uhc.GameEvents.TeamDistributionGameEvent;
import fr.gameurduxvi.uhc.GameEvents.WorldBorder;
import fr.gameurduxvi.uhc.GameEvents.WorldBorderWanno;
import fr.gameurduxvi.uhc.Listeners.BlockBreakListener;
import fr.gameurduxvi.uhc.Listeners.BlockPlaceListener;
import fr.gameurduxvi.uhc.Listeners.CraftItemListener;
import fr.gameurduxvi.uhc.Listeners.EnchantmentListener;
import fr.gameurduxvi.uhc.Listeners.EntityDamageByEntityListener;
import fr.gameurduxvi.uhc.Listeners.EntityDamageListener;
import fr.gameurduxvi.uhc.Listeners.EntityDeathListener;
import fr.gameurduxvi.uhc.Listeners.EntityExplodeListener;
import fr.gameurduxvi.uhc.Listeners.EntitySpawnListener;
import fr.gameurduxvi.uhc.Listeners.FoodLevelChangeListener;
import fr.gameurduxvi.uhc.Listeners.HangingBreakByEntityListener;
import fr.gameurduxvi.uhc.Listeners.InventoryClickListener;
import fr.gameurduxvi.uhc.Listeners.InventoryDragListener;
import fr.gameurduxvi.uhc.Listeners.LeavesDecayListener;
import fr.gameurduxvi.uhc.Listeners.PlayerAchievementAwardedListener;
import fr.gameurduxvi.uhc.Listeners.PlayerChatListener;
import fr.gameurduxvi.uhc.Listeners.PlayerDeathListener;
import fr.gameurduxvi.uhc.Listeners.PlayerDropItemListener;
import fr.gameurduxvi.uhc.Listeners.PlayerExpChangeListener;
import fr.gameurduxvi.uhc.Listeners.PlayerInteractAtEntityListener;
import fr.gameurduxvi.uhc.Listeners.PlayerInteractEntityListener;
import fr.gameurduxvi.uhc.Listeners.PlayerInteractListener;
import fr.gameurduxvi.uhc.Listeners.PlayerItemConsumeEvenListener;
import fr.gameurduxvi.uhc.Listeners.PlayerItemConsumeListener;
import fr.gameurduxvi.uhc.Listeners.PlayerJoinListener;
import fr.gameurduxvi.uhc.Listeners.PlayerMoveListener;
import fr.gameurduxvi.uhc.Listeners.PlayerQuitListener;
import fr.gameurduxvi.uhc.Listeners.PlayerResourcePackStatusListener;
import fr.gameurduxvi.uhc.Listeners.ServerListPingListener;
import fr.gameurduxvi.uhc.Listeners.VehicleDestroyListener;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir.Barbe_NoirP;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom.Big_MomP;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Kaido.KaidoP;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Shanks.ShanksP;
import fr.gameurduxvi.uhc.Personnages.Pirates.Bartolomeo;
import fr.gameurduxvi.uhc.Personnages.Pirates.Cavendish;
import fr.gameurduxvi.uhc.Personnages.Pirates.Kuzan;
import fr.gameurduxvi.uhc.Personnages.Pirates.Law;
import fr.gameurduxvi.uhc.Personnages.Pirates.Luffy;
import fr.gameurduxvi.uhc.Personnages.Pirates.Nami;
import fr.gameurduxvi.uhc.Personnages.Pirates.Sabo;
import fr.gameurduxvi.uhc.Personnages.Pirates.Zoro;
import fr.gameurduxvi.uhc.Tasks.GameTask;
import fr.gameurduxvi.uhc.Tasks.HeaderAndFooterTask;
import fr.gameurduxvi.uhc.Tasks.ScoreboardTask;
import fr.gameurduxvi.uhc.Tasks.ShipsTask;

public class Initialiser {
	
	public static void init() {
		initListeners();
		initTasks();
		initCommands();
		initWanoLocations();
		initWanoChests();
		initRoles();
		initGameEvents();
		initCrafts();
		killMobs();
		configureWE();
		initShips();
	}

	private static void configureWE() {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Fawe.get().getWorldEdit().getConfiguration().wandItem = 166;
				Fawe.get().getWorldEdit().getConfiguration().navigationWand = 166;
			}
		}, 10);		
	}

	private static void initListeners() {
		Bukkit.getServer().getPluginManager().registerEvents(new EntityDamageListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new BlockBreakListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerResourcePackStatusListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new CraftItemListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new EntitySpawnListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new VehicleDestroyListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerAchievementAwardedListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new LeavesDecayListener(), Main.getInstance()); 
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryDragListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new EntityDeathListener(), Main.getInstance()); 
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractAtEntityListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new ServerListPingListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new HangingBreakByEntityListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerExpChangeListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new EnchantmentListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerItemConsumeEvenListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerItemConsumeListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractEntityListener(), Main.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new EntityExplodeListener(), Main.getInstance());
	}
	
	@SuppressWarnings("deprecation")
	private static void initTasks() {
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new GameTask(), 20, 20);
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new HeaderAndFooterTask(), 20, 20);
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new ScoreboardTask(), 20, 20);
	}
	
	private static void initCommands() {
		Main.getInstance().getCommand("final_heal").setExecutor(new CommandFinalHeal());
		Main.getInstance().getCommand("fh").setExecutor(new CommandFinalHeal());
		Main.getInstance().getCommand("doc").setExecutor(new CommandGDoc());
		Main.getInstance().getCommand("gdoc").setExecutor(new CommandGDoc());
		Main.getInstance().getCommand("commandes").setExecutor(new CommandCommandes());
		Main.getInstance().getCommand("testpack").setExecutor(new CommandTestPack());
		Main.getInstance().getCommand("texturepack").setExecutor(new CommandTexturePack());
		Main.getInstance().getCommand("uhc").setExecutor(new CommandUhc());
		Main.getInstance().getCommand("start").setExecutor(new CommandStart());
		Main.getInstance().getCommand("end").setExecutor(new CommandEnd());
		Main.getInstance().getCommand("pause").setExecutor(new CommandPause());
		Main.getInstance().getCommand("revive").setExecutor(new CommandRevive());
		Main.getInstance().getCommand("roles").setExecutor(new CommandRoles());
		Main.getInstance().getCommand("r").setExecutor(new CommandRoles());
		Main.getInstance().getCommand("spectator").setExecutor(new CommandSpectator());
		Main.getInstance().getCommand("debug").setExecutor(new CommandDebug());
		//Main.getInstance().getCommand("pregen").setExecutor(new CommandPregen());
		Main.getInstance().getCommand("info").setExecutor(new CommandInfo());
		Main.getInstance().getCommand("world").setExecutor(new CommandWorld());
		Main.getInstance().getCommand("config").setExecutor(new CommandConfig());
		Main.getInstance().getCommand("trilog").setExecutor(new CommandTrilog());
		Main.getInstance().getCommand("particles").setExecutor(new CommandParticles());
		Main.getInstance().getCommand("testparticles").setExecutor(new CommandTestParticles());
		Main.getInstance().getCommand("chat").setExecutor(new CommandChat());
		Main.getInstance().getCommand("listtexturepack").setExecutor(new CommandListTexturePack());
		Main.getInstance().getCommand("cwhitelist").setExecutor(new CommandWhitelist());
	}
	
	private static void initWanoLocations() {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {				
				File file = new File(Main.getInstance().WanoWorldName + "/locations.json");
				if(!file.exists()) {
					Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cLes possitions du monde Wano ne sont pas remplies");
					try {
						file.createNewFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try (FileWriter fw = new FileWriter(Main.getInstance().WanoWorldName + "/locations.json")) {
			            fw.write("{}");
			            fw.flush();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
				}
				try {
					Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §6Loading locations of the world §e" + Main.getInstance().WanoWorldName);
					JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(Main.getInstance().WanoWorldName + "/locations.json"));
					
					JSONArray jaLocations = null;
					try {
						jaLocations = (JSONArray)jo.get("locations");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cTeleport locations not found");
					}
					try {
						for(Object obj: jaLocations) {
							String[] splitLoc = ((String)obj).split(",");
							int x = Integer.parseInt(splitLoc[0]);
							int y = Integer.parseInt(splitLoc[1]);
							int z = Integer.parseInt(splitLoc[2]);
							Main.getInstance().getWanoMapLocations().add(new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), x, y, z));
						}
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cTeleport locations not correctly writed, needs to be: \"x,y,z\"");
					}
					
					JSONArray jaChestLocations = null;
					try {
						jaChestLocations = (JSONArray)jo.get("chestsLocations");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cChests locations not found");
					}
					try {
						for(Object obj: jaChestLocations) {
							String[] splitLoc = ((String)obj).split(",");
							int x = Integer.parseInt(splitLoc[0]);
							int y = Integer.parseInt(splitLoc[1]);
							int z = Integer.parseInt(splitLoc[2]);
							Main.getInstance().getWanoChestsLocations().add(new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), x, y, z));
						}
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cChests locations not correctly writed, needs to be: \"x,y,z\"");
					}
					
					JSONArray JaPirateFightLocations = null;
					try {
						JaPirateFightLocations = (JSONArray)jo.get("pirateFightLocations");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cPirate Fight locations not found");
					}
					try {
						for(Object obj: JaPirateFightLocations) {
							String[] splitLoc = ((String)obj).split(",");
							int x = Integer.parseInt(splitLoc[0]);
							int y = Integer.parseInt(splitLoc[1]);
							int z = Integer.parseInt(splitLoc[2]);
							Main.getInstance().getWanoOnigashimaLocations().add(new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), x, y, z));
						}
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cPirate Fight locations not correctly writed, needs to be: \"x,y,z\"");
					}
					Collections.shuffle(Main.getInstance().getWanoOnigashimaLocations());
					
					String endGameLocation = null;
					try {
						endGameLocation = (String)jo.get("endGameLocation");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cEnd Game location not found");
					}
					try {
						String[] splitLoc = endGameLocation.split(",");
						int x = Integer.parseInt(splitLoc[0]);
						int y = Integer.parseInt(splitLoc[1]);
						int z = Integer.parseInt(splitLoc[2]);
						Main.getInstance().WanoEndGameLocation = new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), x, y, z);
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cEnd Game location not correctly writed, needs to be: \"x,y,z\"");
					}
					
					String scoreboardLocation = null;
					try {
						scoreboardLocation = (String)jo.get("scoreboardLocation");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cScoreboard location not found");
					}
					try {
						String[] splitLoc = scoreboardLocation.split(",");
						int x = Integer.parseInt(splitLoc[0]);
						int y = Integer.parseInt(splitLoc[1]);
						int z = Integer.parseInt(splitLoc[2]);
						Main.getInstance().WanoScoreboardLocation = new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), x, y, z);
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cScoreboard location not correctly writed, needs to be: \"x,y,z\"");
					}
					
					String centerLocation = null;
					try {
						centerLocation = (String)jo.get("pirateFightCenterLocation");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cOnigashima center location not found");
					}
					try {
						String[] splitLoc = centerLocation.split(",");
						int x = Integer.parseInt(splitLoc[0]);
						int y = Integer.parseInt(splitLoc[1]);
						int z = Integer.parseInt(splitLoc[2]);
						Main.getInstance().WanoOnigashimaCenterLocation = new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), x, y, z);
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cOnigashima center location not correctly writed, needs to be: \"x,y,z\"");
					}
					
					int beforeBorderSize = 300;
					try {
						beforeBorderSize = Integer.parseInt(jo.get("pirateFightBorderBeforeSize") + "");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cOnigashima before border size location not found");
					}
					Main.getInstance().WanoOnigashimaBeforeBorderSize = beforeBorderSize;
					
					int afterBorderSize = 50;
					try {
						afterBorderSize = Integer.parseInt(jo.get("pirateFightBorderAfterSize") + "");
					} catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cOnigashima after border size location not found");
					}
					Main.getInstance().WanoOnigashimaAfterBorderSize = afterBorderSize;
					
					
					Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §aLocations loaded");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 20);
	}
	
	private static void initWanoChests() {
		ItemStack it1 = new ItemStack(Material.DAYLIGHT_DETECTOR);
		ItemMeta it1M = it1.getItemMeta();
		it1M.setDisplayName("Hana Hana No Mi");
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§7> §34coeurs d'absorption");
		lore.add("§7> §3Regen d'une pomme d'or");
		it1M.setLore(lore);
		it1.setItemMeta(it1M);
		ShipsTask.chestItems.add(it1);
		ShipsTask.chestItems.add(it1);
		
		ItemStack it2 = new ItemStack(Material.DISPENSER);
		ItemMeta it2M = it2.getItemMeta();
		it2M.setDisplayName("Horu Horu No Mi");
		lore = new ArrayList<>();
		lore.add("§7> §3Reset cooldown d'ulti");
		it2M.setLore(lore);
		it2.setItemMeta(it2M);
		ShipsTask.chestItems.add(it2);
		ShipsTask.chestItems.add(it2);
		
		ItemStack it3 = new ItemStack(Material.GOLDEN_APPLE, 2);
		ShipsTask.chestItems.add(it3);
		ShipsTask.chestItems.add(it3);
		ShipsTask.chestItems.add(it3);
		
		ItemStack it4 = new ItemStack(Material.ENDER_PEARL, 2);
		ShipsTask.chestItems.add(it4);
		ShipsTask.chestItems.add(it4);
	}
	
	private static void initRoles() {
		// Pirates
		Main.getInstance().getPersonages().add(new Bartolomeo());
		Main.getInstance().getPersonages().add(new Cavendish());
		Main.getInstance().getPersonages().add(new Kuzan());
		Main.getInstance().getPersonages().add(new Law());
		Main.getInstance().getPersonages().add(new Luffy());
		Main.getInstance().getPersonages().add(new Nami());
		Main.getInstance().getPersonages().add(new Sabo());
		Main.getInstance().getPersonages().add(new Zoro());		
		
		// Empereurs
		Main.getInstance().getPersonages().add(new Barbe_NoirP());
		Main.getInstance().getPersonages().add(new Big_MomP());
		Main.getInstance().getPersonages().add(new KaidoP());
		Main.getInstance().getPersonages().add(new ShanksP());
	}
	
	private static void initGameEvents() {
		Main.getInstance().getGameEvents().add(new WorldBorder());
		Main.getInstance().getGameEvents().add(new WorldBorderWanno());
		Main.getInstance().getGameEvents().add(new AntiDamageGameEvent());
		Main.getInstance().getGameEvents().add(new RoleDistributionGameEvent());
		Main.getInstance().getGameEvents().add(new FideleDistributionGameEvent());
		Main.getInstance().getGameEvents().add(new TeamDistributionGameEvent());
	}
	
	private static void initCrafts() {
		List<Recipe> backup = new ArrayList<Recipe>();
		{
			// Idk why you change scope, but why not
			Iterator<Recipe> a = Bukkit.getServer().recipeIterator();
			while(a.hasNext()){
				Recipe recipe = a.next();
				ItemStack result = recipe.getResult();
				if(!result.getType().equals(Material.WATCH) && !result.getType().equals(Material.COMPASS) && !result.isSimilar(new ItemStack(Material.GOLDEN_APPLE, 1, (short)1))) {
					backup.add(recipe);
				}
			}
		}
		Bukkit.getServer().clearRecipes();
		for (Recipe r : backup)Bukkit.getServer().addRecipe(r);
		
		ItemStack it = new ItemStack(Material.COMPASS);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§4Trilog Pose");
		it.setItemMeta(itM);
		ShapedRecipe craft = new ShapedRecipe(it);
		craft.shape("L R", " W ", "R L");
		craft.setIngredient('W', Material.WATCH);
		craft.setIngredient('R', Material.REDSTONE_BLOCK);
		craft.setIngredient('L', Material.LAPIS_BLOCK);
		Bukkit.getServer().addRecipe(craft);
		
		ItemStack w = new ItemStack(Material.WATCH);
		ItemMeta wM = it.getItemMeta();
		wM.setDisplayName("Boussole Endommagée");
		w.setItemMeta(wM);
		ShapedRecipe craftw = new ShapedRecipe(w);
		craftw.shape(" I ", "IRI", " I ");
		craftw.setIngredient('I', Material.IRON_INGOT);
		craftw.setIngredient('R', Material.REDSTONE);
		Bukkit.getServer().addRecipe(craftw);
	}
	
	private static void killMobs() {
		Collection<Entity> entities = Main.getInstance().centerMaplocation.getWorld().getNearbyEntities(Main.getInstance().centerMaplocation, 100, 100, 100);
		for(Entity e: entities) {
			if(e instanceof Monster) {
				e.remove();
			}
		}
	}
	
	private static void initShips() {
		String[] schematics = {"ship", "big_mom", "barbe_noir", "shanks", "kaido", "land", "emptyShip"};
		
		for(String schematicName: schematics) {
			if(!new File(Main.getInstance().pluginDir + "schematics/" + schematicName + ".schematic").exists()) {
				//saveResource("ship.schematic", false);
				try {
					FileUtils.copyInputStreamToFile(Main.getInstance().getResource(schematicName + ".schematic"), new File(Main.getInstance().pluginDir + "schematics/" + schematicName + ".schematic"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
