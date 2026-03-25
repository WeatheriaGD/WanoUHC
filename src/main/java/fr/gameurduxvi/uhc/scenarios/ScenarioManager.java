package fr.gameurduxvi.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;
import fr.gameurduxvi.uhc.scenarios.anonymous.Anonymous;
import fr.gameurduxvi.uhc.scenarios.anonymous.ChangeSkin;
import fr.gameurduxvi.uhc.scenarios.diamondlimite.DiamondInstance;
import fr.gameurduxvi.uhc.scenarios.diamondlimite.DiamondLimite;
import fr.gameurduxvi.uhc.scenarios.diamondlimite.PlayerPickupItemListener;
import fr.gameurduxvi.uhc.scenarios.fastmelting.FastMelting;
import fr.gameurduxvi.uhc.scenarios.fastmelting.FurnaceBurnListener;
import fr.gameurduxvi.uhc.scenarios.hasteyboys.Hasteyboys;
import fr.gameurduxvi.uhc.scenarios.target.InventoryClickListener;
import fr.gameurduxvi.uhc.scenarios.target.Target;

public class ScenarioManager {
	public final String DIAMOND_LIMITE_NAME = "Diamond Limite";
	public final String ANONYMOUS_NAME = "Anonymous";
	public final String HASTEY_BOYS = "HasteyBoys";
	public final String FAST_MELTING = "Fastmelting";
	public final String TARGET_NAME = "Target";
	
	// Anonymous
	public ChangeSkin changeSkinInstance;
	public String SkinValue = "eyJ0aW1lc3RhbXAiOjE1ODYzNTM5OTAwMTAsInByb2ZpbGVJZCI6ImU3OTNiMmNhN2EyZjQxMjZhMDk4MDkyZDdjOTk0MTdiIiwicHJvZmlsZU5hbWUiOiJUaGVfSG9zdGVyX01hbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYxYzRiN2NmNmZjNDAwNTM4ZTY4ZThhZWZhMDc0MTYwYmZmZTNjNTAyMjc4NTM2MTczZGNmYjZhODVlOGU1NCJ9fX0=";
	public String SkinSignature = "Mi9WB+Z2iOv16EKGabupWoaXfK2buRtuBIuQG6PO/ioS9AtOFvfXtnTJGuAAxXGvK372avxnTkEulLAUFrjBxe1f3WHqMzLwu3bN7esGV+z4fKLsz5WA2kv2EgPt+bPKDX66OVajGyyBYMTnBd+46kRGcCIH/DXtZ5c3RwGpcuiHfT+U5lIXyW8xCx4m6uZcrivbA32msmJ+K7Vi291nAIRLPZiNMC5JzEsOO15Vh4/XCFv1mh/BlEGAK2Hl8mAKXm+/0JnvaB716hppcZ4Zn94+PQLAoyXh0Akt2VU8/pE5/qH0QOZzl8xd1RjOSKn1kRcUWtyYR8OUQ/mIWVO2sHL2tWfrWcNv5mbWONpwK6dy+ixFCyenHfjNma79N3uwk2pUCa0CwgcPIrvkTUm7120MZ2or+GbRWrpbRQJZFXDJynHTYHZnTiBjTxf18e+Dv5Ph2OZxsgqd1+DgDniAOw23cA3U04h5pgACQjHvVyQbsHBQv3e3qFoiukxymx3NDgxYWcBQ683cVXUgpRevnWTFGWQGTJZowY3ofO9/y+dtTZF3dVrd1R1hKLAphEtpMxb/Oud14GPoCdGFc+khOkW2u2qHZ+M08sbjDObbmvgp408ZiDPFDtrL8GZraNoB4j5RM5LodqrDFMgalWSZzcsnDZGohh2ZdPZ1rsICAJw=";
	
	// Diamond Limite
	public int maxDiamond;
	public ArrayList<DiamondInstance> diamond = new ArrayList<>();
	
	// Fastmelting
	public ArrayList<Furnace> furnaces = new ArrayList<>();
	
	// Target
	public Location targetLocation;
	
	public void onLoad() {
		Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName + " §bEnabling " + Main.getInstance().pluginName + " Scenario");
		
		// Anonymous
		Anonymous anonymous = new Anonymous(ANONYMOUS_NAME, "Tous les joueurs dans la partie possède \ndes pseudos soit identiques soit \nbrouillés ainsi que des skins identiques", new ItemStack(Material.JACK_O_LANTERN));
		fr.gameurduxvi.uhc.Main.getInstance().getScenarios().add(anonymous);
		changeSkinInstance = new ChangeSkin();
		Main.getInstance().getServer().getPluginManager().registerEvents(new fr.gameurduxvi.uhc.scenarios.anonymous.PlayerJoinListener(), Main.getInstance());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player pl: Bukkit.getOnlinePlayers()) {
					changeSkinInstance.changeSkin(pl);
				}
			}
		}.runTaskLater(Main.getInstance(), 20);
		
		
		
		// Diamond Limite
		DiamondLimite diamondLimite = new DiamondLimite(DIAMOND_LIMITE_NAME, "Cela ajouterait une limite de diamant que les joueurs pouvent avoir dans la partie (Par defaut 17)", new ItemStack(Material.DIAMOND));
		//diamondLimite.setActive(true);
		diamondLimite.setConfigInv(getDiamondLimiteInventory());
		fr.gameurduxvi.uhc.Main.getInstance().getScenarios().add(diamondLimite);
		Main.getInstance().getServer().getPluginManager().registerEvents(new fr.gameurduxvi.uhc.scenarios.diamondlimite.InventoryClickListener(diamondLimite), Main.getInstance());
		Main.getInstance().getServer().getPluginManager().registerEvents(new PlayerPickupItemListener(), Main.getInstance());
		Main.getInstance().getServer().getPluginManager().registerEvents(new fr.gameurduxvi.uhc.scenarios.diamondlimite.PlayerDropItemListener(), Main.getInstance());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Object obj1 = ConfigManager.get("scenario|" + DiamondLimite.class.getName() + "|maxDiamond");
				if(obj1 != null) {
					maxDiamond = Integer.parseInt("" + (long) obj1);
					for(Scenario sc: Main.getInstance().getScenarios()) {
						if(sc.getClass().getName().equals(DiamondLimite.class.getName())) {
							sc.setConfigInv(getDiamondLimiteInventory());
						}
					}
				}
				else {
					maxDiamond = 17;
					ConfigManager.set("scenario|" + DiamondLimite.class.getName() + "|maxDiamond", maxDiamond);
				}					
			}
		}.runTaskLater(Main.getInstance(), 20);
		
		
		
		// HasteyBoys
		fr.gameurduxvi.uhc.Main.getInstance().getScenarios().add(new Hasteyboys(HASTEY_BOYS, "Ce scénario permet d'obtenir des outils \n(pioches, hâches et pelles) directement \nenchantées avec Efficiency 3 et \nUnbreaking 3 lorsqu'on les craft.", new ItemStack(Material.DIAMOND_PICKAXE)));
       
		
		
		// Fastmelting
		fr.gameurduxvi.uhc.Main.getInstance().getScenarios().add(new FastMelting(FAST_MELTING, "Ce scénario accélère simplement la cuisson.", new ItemStack(Material.IRON_INGOT)));
		Main.getInstance().getServer().getPluginManager().registerEvents(new FurnaceBurnListener(), Main.getInstance());
		
		
		
		// Target
		Target target = new Target(TARGET_NAME, "Ce scénario ...", new ItemStack(Material.BOOK_AND_QUILL));
		fr.gameurduxvi.uhc.Main.getInstance().getScenarios().add(target);
		Main.getInstance().getServer().getPluginManager().registerEvents(new InventoryClickListener(target), Main.getInstance());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Object obj2 = ConfigManager.get("scenario|" + Target.class.getName() + "|targetLocation");
				if(obj2 != null) {
					try {
						String[] splitLoc = ((String)obj2).split(",");
						double x = Double.parseDouble(splitLoc[0]);
						double y = Double.parseDouble(splitLoc[1]);
						double z = Double.parseDouble(splitLoc[2]);
						targetLocation = new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), x, y, z);
						for(Scenario sc: Main.getInstance().getScenarios())
							if(sc.getClass().getName().equals(Target.class.getName()))
								sc.setConfigInv(getTargetInventory());
					} 
					catch (Exception e) {
						Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §cThe targetLocation isn't placed correctly. Due to an error, a default location has been set at 0, 140, 0");
						targetLocation = new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), 0, 140, 0);
						ConfigManager.set("scenario|" + Target.class.getName() + "|targetLocation", targetLocation.getX() + "," + targetLocation.getY() + "," + targetLocation.getZ());
						for(Scenario sc: Main.getInstance().getScenarios())
							if(sc.getClass().getName().equals(Target.class.getName()))
								sc.setConfigInv(getTargetInventory());
					}
				}
				else {
					targetLocation = new Location(Bukkit.getWorld(Main.getInstance().WanoWorldName), 0, 140, 0);
					ConfigManager.set("scenario|" + Target.class.getName() + "|targetLocation", targetLocation.getX() + "," + targetLocation.getY() + "," + targetLocation.getZ());
					for(Scenario sc: Main.getInstance().getScenarios())
						if(sc.getClass().getName().equals(Target.class.getName()))
							sc.setConfigInv(getTargetInventory());
				}					
			}
		}.runTaskLater(Main.getInstance(), 20);
	}
	
	public Inventory getDiamondLimiteInventory() {
		Inventory inv = Bukkit.createInventory(null, 36, ">>" + DIAMOND_LIMITE_NAME + "<<");
		
		ItemStack bg = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta bgM = bg.getItemMeta();
		bgM.setDisplayName("");
		bg.setItemMeta(bgM);
		
		for(int i = 0; i <= 35; i++) {
			inv.setItem(i, bg);
		}
		
		ItemStack d = new ItemStack(Material.DIAMOND, maxDiamond);
		ItemMeta dM = d.getItemMeta();
		dM.setDisplayName("§bMaximum de diamant par joueur");
		dM.setLore(Arrays.asList("§b" + maxDiamond + " diamants"));
		d.setItemMeta(dM);
		inv.setItem(13, d);
		
		ItemStack p = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
		ItemMeta pM = p.getItemMeta();
		pM.setDisplayName("§aRetirer");
		p.setItemMeta(pM);
		inv.setItem(12, p);
		
		ItemStack a = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		ItemMeta aM = a.getItemMeta();
		aM.setDisplayName("§aAjouter");
		a.setItemMeta(aM);
		inv.setItem(14, a);
		
		ItemStack b = new ItemStack(Material.ARROW);
		ItemMeta bM = b.getItemMeta();
		bM.setDisplayName("§7Retour");
		b.setItemMeta(bM);
		inv.setItem(31, b);
		
		return inv;		
	}
	
	public Inventory getTargetInventory() {
		Inventory inv = Bukkit.createInventory(null, 36, ">>" + TARGET_NAME + "<<");
		
		ItemStack bg = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta bgM = bg.getItemMeta();
		bgM.setDisplayName("");
		bg.setItemMeta(bgM);
		
		for(int i = 0; i <= 35; i++) {
			inv.setItem(i, bg);
		}
		
		ItemStack d = new ItemStack(Material.MONSTER_EGG, 1);
		ItemMeta dM = d.getItemMeta();
		dM.setDisplayName("§bDéfinir l'emplacement du pnj");
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§b" + targetLocation.getBlockX() + " " + targetLocation.getBlockY() + " " + targetLocation.getBlockZ() + " §7dans le monde de Wano");
		lore.add("§bCliquez ici pour le redéfinir");
		dM.setLore(lore);
		d.setItemMeta(dM);
		inv.setItem(13, d);
		
		ItemStack b = new ItemStack(Material.ARROW);
		ItemMeta bM = b.getItemMeta();
		bM.setDisplayName("§7Retour");
		b.setItemMeta(bM);
		inv.setItem(31, b);
		
		return inv;		
	}
}
