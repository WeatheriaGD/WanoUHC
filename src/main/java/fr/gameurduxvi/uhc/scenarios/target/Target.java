package fr.gameurduxvi.uhc.scenarios.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class Target extends Scenario {
	
	public static final String VILLAGER_NAME = "Quotes";
	public static Map<Player, Player> targets = new HashMap<Player, Player>();

	public Target(String sencarioName, String senarioDescription) {
		super(sencarioName, senarioDescription);
	}

	public Target(String sencarioName, String senarioDescription, ItemStack senarioItem) {
		super(sencarioName, senarioDescription, senarioItem);
	}

	@Override
	public void stateChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		Main.getScenariosManager().targetLocation.getChunk().load();
		
		Collection<Entity> entities = Main.getScenariosManager().targetLocation.getWorld().getEntities();
		for(Entity e: entities) {
			if((e instanceof Villager || e instanceof ArmorStand) && e.getLocation().distance(Main.getScenariosManager().targetLocation) <= 30)
				e.remove();
		}
		
		Location finalLoc = Main.getScenariosManager().targetLocation.subtract(0, 1.5, 0);
		ArmorStand a = (ArmorStand) Main.getScenariosManager().targetLocation.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
		Villager v = (Villager) Main.getScenariosManager().targetLocation.getWorld().spawnEntity(Main.getScenariosManager().targetLocation, EntityType.VILLAGER);
		
		Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §aPnj Target a spawn at " + finalLoc.getX() + " " + finalLoc.getY() + " " + finalLoc.getZ() + " in world " + finalLoc.getWorld().getName());
		
		a.setGravity(false);
		a.setVisible(false);
		a.setBasePlate(false);
		
		a.setPassenger(v);
		
		v.setCustomName(VILLAGER_NAME);
		v.setCustomNameVisible(true);
		
		Main.getScenariosManager().targetLocation.getChunk().unload();
		
	}
	
	public static boolean hasTarget(Player p) {
		for(Entry<Player, Player> entry: Target.targets.entrySet())
			if(entry.getKey().getName().equals(p.getName()))
				return true;
		return false;
	}
	
	public static boolean isTarget(Player p1, Player p2) {
		for(Entry<Player, Player> entry: Target.targets.entrySet())
			if(entry.getKey().getName().equals(p1.getName()) && entry.getValue() != null && entry.getValue().getName().equals(p2.getName()))
				return true;
		return false;
	}
	
	public static boolean isATarget(Player p2) {
		for(Entry<Player, Player> entry: Target.targets.entrySet())
			if(entry.getValue() != null && entry.getValue().getName().equals(p2.getName()))
				return true;
		return false;
	}
	
	public static void targetKilled(Player attacker, Player victim) {
		for(Entry<Player, Player> entry: Target.targets.entrySet())
			if(entry.getKey().getName().equals(attacker.getName()) && entry.getValue().getName().equals(victim.getName()))
				entry.setValue(null);
		
		attacker.getInventory().remove(Material.COMPASS);
		attacker.sendMessage("§7[§8Marin Dodousse§7] §aBien joué moussaillon ! Viens me voir !");
		teminateTarget(victim);
	}
	
	public static void teminateTarget(Player victim) {		
		ArrayList<Player> toDelete = new ArrayList<>();
		
		for(Entry<Player, Player> entry: Target.targets.entrySet())
			if(entry.getValue() != null && entry.getValue().getName().equals(victim.getName()))
				toDelete.add(entry.getKey());
		
		for(Player playerDelete: toDelete) {
			Target.targets.remove(playerDelete);
			playerDelete.getInventory().remove(Material.COMPASS);
			playerDelete.sendMessage("§7[§8Marin Dodousse§7] §cVotre contrat a été annul�.");
		}
				
	}
	
	public static Player getTarget(Player p) {
		for(Entry<Player, Player> entry: Target.targets.entrySet())
			if(entry.getKey().getName().equals(p.getName()))
				return entry.getValue();
		return null;
	}
}
