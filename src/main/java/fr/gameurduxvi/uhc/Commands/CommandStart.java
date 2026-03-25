package fr.gameurduxvi.uhc.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.GameEvents.WorldBorder;
import fr.gameurduxvi.uhc.Refractor.SchematicLoad;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;
import fr.gameurduxvi.uhc.Tasks.StartTask;
import net.warvale.WorldBorder.WorldFillTask;

public class CommandStart implements CommandExecutor {
	BukkitTask task;
	public static BukkitTask startTask;
	boolean started = false;


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			/*******************************************************
			 *
			 * Verification before start
			 *
			 *******************************************************/
			if(Main.getInstance().getWanoMapLocations().size() == 0) {
				sender.sendMessage("§7Start §8>> §cAucune position joueurs n'a été mise dans le monde " + Main.getInstance().WanoWorldName);
				sender.sendMessage("§7Start §8>> §cReplissez le fichier §elocations.json §c dans le dossier du monde");
				sender.sendMessage("§7Start §8>> §cPuis recharger le plugin (/reload)");
				return false;
			}

			if(Main.getInstance().getWanoChestsLocations().size() == 0) {
				sender.sendMessage("§7Start §8>> §cAucune position des coffres n'a été mise dans le monde " + Main.getInstance().WanoWorldName);
				sender.sendMessage("§7Start §8>> §cReplissez le fichier §elocations.json §c dans le dossier du monde");
				sender.sendMessage("§7Start §8>> §cPuis recharger le plugin (/reload)");
				return false;
			}

			int max = 0;
			for(Personnage pers: Main.getInstance().getPersonages()) {
				max += pers.getAmount();
			}
			if(PlayerData.getAlivePlayers().size() != max) {
				sender.sendMessage("§7Start §8>> §cIl y a plus de joueurs que le nombre de roles possible");
				sender.sendMessage("§7Start §8>> §cPour changer cela, faites /config");
				return false;
			}

			int maxTeams = 0;
			for(TeamData td: Main.getInstance().getTeamData()) {
				maxTeams += td.getMax();
			}

			if(PlayerData.getAlivePlayers().size() != maxTeams) {
				sender.sendMessage("§7Start §8>> §cLes équipes ne sont pas adapté selon le nombre de joueurs present");
				sender.sendMessage("§7Start §8>> §cPour changer cela, faites /config");
				return false;
			}

			/*******************************************************
			 *
			 * World pregeneration
			 *
			 *******************************************************/
			if(Main.GAMESTATE.equals(GameState.Idle)) {
				if(started) {
					sender.sendMessage("§7Start §8>> §cLa partie est en train de ce lancer !");
					return false;
				}
				started = true;

				Main.getInstance().getOfflinePlayersData().clear();

				short size = (short) PlayerData.getAlivePlayers().size();

				ArrayList<WorldFillTask> tasks = new ArrayList<>();
				ArrayList<Location> locs = new ArrayList<>();

				Double angle = 360.0 / size;
				double radians = Math.toRadians(angle);

				Bukkit.getConsoleSender().sendMessage("§7Pregen §8>> §b WorldBorder map generation task for world \"" + Main.getInstance().centerMaplocation.getWorld().getName() + "\" started.");

				int i = 0;
				for(@SuppressWarnings("unused") PlayerData pd: PlayerData.getAlivePlayers()) {
					i++;

					int rndX = (int) ((int) Math.round(Math.sin(radians * i) * 1400) + Main.getInstance().centerMaplocation.getX());
					int rndZ = (int) ((int) Math.round(Math.cos(radians * i) * 1400) + Main.getInstance().centerMaplocation.getZ());

					Location loc = new Location(Bukkit.getWorlds().get(0), rndX, 150, rndZ);

					locs.add(loc);
				}

				task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {

					WorldFillTask lastTask = null;
					int i = 0;
					@Override
					public void run() {

						if(lastTask != null) {
							if(lastTask.getPercentageCompleted() >= 100) {
								i++;
								if(locs.size() > i) {
									int chunksPerRun = 20;
									int radius = 200;
									(lastTask = new WorldFillTask(Main.getInstance().centerMaplocation.getWorld().getName(), chunksPerRun, radius, locs.get(i))).setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), (Runnable)lastTask, 1L, 1L));
									tasks.add(lastTask);
								}
							}
						}
						else {
							int chunksPerRun = 20;
							int radius = 150;
							(lastTask = new WorldFillTask(Main.getInstance().centerMaplocation.getWorld().getName(), chunksPerRun, radius, locs.get(0))).setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), (Runnable)lastTask, 1L, 1L));
							tasks.add(lastTask);
						}

						int finished = 0;
						for(WorldFillTask wft: tasks) {
							if(wft.getPercentageCompleted() >= 100.0) {
								finished++;
							}
						}
						for(PlayerData pd: Main.getInstance().getPlayersData()) Main.getInstance().sendActionBarMessage(pd.getPlayer(), "§7Spawns g§n§r§s §b" + finished + "§7/§b" + size);
						if(finished == size) {
							Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

								@SuppressWarnings("deprecation")
								@Override
								public void run() {
									startTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new StartTask(), 0, 20);
									teleport();
								}
							}, 20);
							Bukkit.getScheduler().cancelTask(task.getTaskId());
						}
					}
				}, 10, 10);
			}
		}
		return false;
	}


	private void teleport() {
		/*******************************************************
		 *
		 * Teleport players
		 *
		 *******************************************************/
		Main.GAMESTATE = GameState.In_Progress;

		Bukkit.getWorlds().get(0).setTime(6000);
		Main.getInstance().playersCanMove = false;

		ArrayList<PlayerData> playerList = new ArrayList<>();
		for(PlayerData pd: PlayerData.getAlivePlayers()) playerList.add(pd);
		for(PlayerData pd: Main.getInstance().getOfflinePlayersData()) {
			playerList.add(pd);
			pd.lateScatter = true;
		}

		new SchematicLoad(Main.getInstance().centerMaplocation, Main.getInstance().pluginDir + "schematics/lobby.schematic", false, Material.AIR);

		Double angle = 360.0 / playerList.size();
		//Double angle = 360.0 / 28;
		double radians = Math.toRadians(angle);

		/*******************************************************
		 * Couldown + Count + Teleport
		 *******************************************************/
		int i = 0;

		for(PlayerData pd: playerList) {
			i++;

			int rndX = (int) ((int) Math.round(Math.sin(radians * i) * 1400) + Main.getInstance().centerMaplocation.getX());
			int rndZ = (int) ((int) Math.round(Math.cos(radians * i) * 1400) + Main.getInstance().centerMaplocation.getZ());

			Location loca = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX, 150, rndZ);
			Location locb = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX, 150, rndZ + 1);
			Location locc = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX, 150, rndZ + 2);

			Location locd = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 1, 150, rndZ);
			Location loce = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 1, 150, rndZ + 1);
			Location locf = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 1, 150, rndZ + 2);

			Location locg = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 2, 150, rndZ);
			Location loch = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 2, 150, rndZ + 1);
			Location loci = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 2, 150, rndZ + 2);

			loca.getBlock().setType(Material.GLASS);
			locb.getBlock().setType(Material.GLASS);
			locc.getBlock().setType(Material.GLASS);

			locd.getBlock().setType(Material.GLASS);
			loce.getBlock().setType(Material.GLASS);
			locf.getBlock().setType(Material.GLASS);

			locg.getBlock().setType(Material.GLASS);
			loch.getBlock().setType(Material.GLASS);
			loci.getBlock().setType(Material.GLASS);

			pd.spawnLocation = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 1.5, 151, rndZ + 1.5);
			pd.getPlayer().teleport(pd.spawnLocation);

			pd.getPlayer().setWalkSpeed(0);
			pd.setImobilized(true);

			pd.getPlayer().setGameMode(GameMode.ADVENTURE);
			pd.getPlayer().getInventory().clear();
			pd.getPlayer().setHealth(pd.getPlayer().getMaxHealth());
			pd.getPlayer().setFoodLevel(20);
			pd.getPlayer().setExp(0);
			pd.getPlayer().setLevel(0);
			pd.getPlayer().getEnderChest().clear();

			pd.getPlayer().sendMessage("§7Chat:");
			pd.getPlayer().sendMessage(" §e!§7<message> §b- Pour parler à tout le monde");
			pd.getPlayer().sendMessage(" §e%§7<message> §b- Pour parler à son empereur ou fidèle");

		}
	}

	@SuppressWarnings("deprecation")
	public static void start() {
		/*******************************************************
		 *
		 * Starting timer
		 *
		 *******************************************************/
		ArrayList<PlayerData> playerList = new ArrayList<>();
		for(PlayerData pd: PlayerData.getAlivePlayers()) playerList.add(pd);
		for(PlayerData pd: Main.getInstance().getOfflinePlayersData()) playerList.add(pd);

		Double angle = 360.0 / playerList.size();
		//Double angle = 360.0 / 28;
		double radians = Math.toRadians(angle);

		int i = 0;
		for(PlayerData pd: playerList) {
			i++;

			int rndX = (int) ((int) Math.round(Math.sin(radians * i) * 1400) + Main.getInstance().centerMaplocation.getX());
			int rndZ = (int) ((int) Math.round(Math.cos(radians * i) * 1400) + Main.getInstance().centerMaplocation.getZ());

			Location loca = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX, 150, rndZ);
			Location locb = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX, 150, rndZ + 1);
			Location locc = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX, 150, rndZ + 2);

			Location locd = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 1, 150, rndZ);
			Location loce = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 1, 150, rndZ + 1);
			Location locf = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 1, 150, rndZ + 2);

			Location locg = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 2, 150, rndZ);
			Location loch = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 2, 150, rndZ + 1);
			Location loci = new Location(Main.getInstance().centerMaplocation.getWorld(), rndX + 2, 150, rndZ + 2);

			//Sound sound = Sound.NOTE_PLING;
			//String sound = "opu.bling";


			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					//pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l10 §r§b<");
				}
			}, 200);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					pd.getPlayer().sendTitle("§a", "§a");
				}
			}, 240);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					//pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l5 §r§b<");
				}
			}, 300);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					//pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), "wano.game_start.4", 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l4 §r§b<");
				}
			}, 320);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					//pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), "wano.game_start.3", 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l3 §r§b<");
				}
			}, 340);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					//pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), "wano.game_start.2", 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l2 §r§b<");
				}
			}, 360);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					//pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 1);
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), "wano.game_start.1", 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§l1 §r§b<");
				}
			}, 380);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					// remove platform
					loca.getBlock().setType(Material.AIR);
					locb.getBlock().setType(Material.AIR);
					locc.getBlock().setType(Material.AIR);

					locd.getBlock().setType(Material.AIR);
					loce.getBlock().setType(Material.AIR);
					locf.getBlock().setType(Material.AIR);

					locg.getBlock().setType(Material.AIR);
					loch.getBlock().setType(Material.AIR);
					loci.getBlock().setType(Material.AIR);

					// set up player
					if(pd.getPlayer().isOnline()) {
						pd.getPlayer().setWalkSpeed((float) 0.2);
						pd.setImobilized(false);
					}
					Main.getInstance().playersCanMove = true;
					pd.getPlayer().playSound(pd.getPlayer().getLocation(), "wano.game_start.0", 10, 1);
					pd.getPlayer().sendTitle("§a", "§b> §7§lGo §r§b<");
					//pd.getPlayer().playSound(pd.getPlayer().getLocation(), sound, 10, 2);
					pd.getPlayer().setGameMode(GameMode.SURVIVAL);

					// spawn boats
					Boat boat = (Boat) pd.getPlayer().getWorld().spawnEntity(pd.getPlayer().getLocation(), EntityType.BOAT);
					boat.setPassenger(pd.getPlayer());

					// give start stuff
					for(ItemStack it: ConfigManager.StuffDepart) {
						pd.getPlayer().getInventory().addItem(it);
					}
				}
			}, 400);

			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					pd.getPlayer().sendTitle("§a", "§a");
				}
			}, 440);
		}

		/*******************************************************
		 *
		 * Spawning ships
		 *
		 *******************************************************/
		try {
			int i2 = 0;
			for(TeamData td: Main.getInstance().getTeamData()) {
				int angle2 = i2 * (360 / Main.getInstance().getTeamData().size());
				double x = Main.getInstance().centerMaplocation.getX() + (Math.sin(Math.toRadians(angle2)) * 600);
				double z = Main.getInstance().centerMaplocation.getZ() + (Math.cos(Math.toRadians(angle2)) * 600);

				int y = 0;
				for(int tempY = 255; tempY >= 0; tempY--) {
					Location loc = new Location(Main.getInstance().centerMaplocation.getWorld(), x, tempY, z);
					if(!loc.getBlock().getType().equals(Material.AIR) &&
							!loc.getBlock().getType().equals(Material.LEAVES) &&
							!loc.getBlock().getType().equals(Material.LOG)) {
						y = tempY + 5;
						break;
					}
				}

				Location loc = new Location(Main.getInstance().centerMaplocation.getWorld(), x, y, z);

				td.setLocation(loc);

				new SchematicLoad(loc, Main.getInstance().pluginDir + "schematics/emptyShip.schematic", false, Material.AIR);

				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

					@Override
					public void run() {
						new SchematicLoad(loc, Main.getInstance().pluginDir + "schematics/land.schematic", false, 0);
					}
				}, 5);

				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

					@Override
					public void run() {
						new SchematicLoad(loc, Main.getInstance().pluginDir + "schematics/" + td.getSchematicName() + ".schematic", false, 0);
					}
				}, 10);

				i2++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*******************************************************
		 *
		 * Border title message
		 *
		 *******************************************************/
		{
			int secondes = 0;
			for(GameEvent ge: Main.getInstance().getGameEvents()) {
				if(ge instanceof WorldBorder) {
					secondes = (ge.getMinute() * 60) + ge.getSecond();
				}
			}
			if(secondes != 0) {
				secondes -= 1;
				Main.getInstance().getGameEvents().add(new WorldBorder(1).setMinute(secondes / 60).setSecond(secondes - (secondes / 60)));
				secondes -= 1;
				Main.getInstance().getGameEvents().add(new WorldBorder(2).setMinute(secondes / 60).setSecond(secondes - (secondes / 60)));
				secondes -= 1;
				Main.getInstance().getGameEvents().add(new WorldBorder(3).setMinute(secondes / 60).setSecond(secondes - (secondes / 60)));
				secondes -= 1;
				Main.getInstance().getGameEvents().add(new WorldBorder(4).setMinute(secondes / 60).setSecond(secondes - (secondes / 60)));
				secondes -= 1;
				Main.getInstance().getGameEvents().add(new WorldBorder(5).setMinute(secondes / 60).setSecond(secondes - (secondes / 60)));
				secondes -= 5;
				Main.getInstance().getGameEvents().add(new WorldBorder(10).setMinute(secondes / 60).setSecond(secondes - (secondes / 60)));
			}
		}
		/*******************************************************
		 *
		 * Starting start scenarion option
		 *
		 *******************************************************/
		for(Scenario sc: Main.getInstance().getScenarios()) sc.onStart();


		/*******************************************************
		 *
		 * Organising Game Event List
		 *
		 *******************************************************/
		HashMap<GameEvent, Integer> hm = new HashMap<>();
		for(GameEvent ge: Main.getInstance().getGameEvents()) {
			hm.put(ge, (ge.getMinute() * 60) + ge.getSecond());
		}

		List<Map.Entry<GameEvent, Integer> > list =
				new LinkedList<Map.Entry<GameEvent, Integer> >(hm.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<GameEvent, Integer> >() {
			public int compare(Map.Entry<GameEvent, Integer> o1,
							   Map.Entry<GameEvent, Integer> o2)
			{
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Main.getInstance().getGameEvents().clear();
		for (Map.Entry<GameEvent, Integer> aa : list) {
			Main.getInstance().getGameEvents().add(aa.getKey());
		}
	}

}
