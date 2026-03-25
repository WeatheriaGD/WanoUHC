package fr.gameurduxvi.uhc.Personnages.Empereurs.Kaido;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Particles.DefaultParticle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Kaido2 extends Personnage {
	public Kaido2() {
		setId(14);
		setType(1);
		setPriority(3);
		setName("Kaido");
		setPrime(Main.getInstance().primeEmpereur);
		setAmount(0);
		setUltimateRecharge(60 * 7);
		setUltimateTime(10);
		setDescription(" \n" + 
				" \n" + 
				"§9§l§nBoro Breath§r \n" + 
				" \n" + 
				"§7Kaido se change en dragon et crache un feu brulant tout sur son passage. \n" + 
				" \n" + 
				"§6Durée §f: §e10s \n" + 
				"§6Cooldown §f: §e7min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ Les ennemis §7§lbrûlent durant §7§l5s §adans une zone de §7§l10 blocs §aautour de Kaido. \n" + 
				"§a⇨ §7§lRésitance 1 §a+ §7§lFire Resistance 2 §aà Kaido \n" + 
				"   \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-8§4❤ §cdurant §7§l7min §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.NETHER_BRICK_ITEM);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aZohan Mythique");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setShipFileName("ship");
		
		setFidelePrefix("§9Fidèle ");
		
		setUltimateSoundName("wano.roles.ultimates.empereurs.kaido.boro_breath");
	}
	
	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§9Kaido ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * getUltimateTime(), 0, true, false), true);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * getUltimateTime(), 1, true, false), true);
		for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(pd.getPlayer().getLocation(), 5, 5, 5)) {
			if(!(e instanceof Player)) continue;
			Player p = (Player) e;
			if(pd.getPlayer().getName().equals(p.getName())) continue;
			p.setFireTicks(10 * getUltimateTime());
		}
		
		
		
		Location loc = pd.getPlayer().getEyeLocation().clone().subtract(0, 0.2, 0);
		for(int i = 0; i < 2 * 10; i++) {
			int time = i;
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					int min = ((time - 2) * 5);
					int max = (time * 5);
					if(min < 0)
						min = 0;
					for(int j = min; j < max; j++) {
						new DefaultParticle(getFrontSideWithPitch(loc, j / 10.0), EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					}
					if(time == (2 * 10) - 1)
						new DefaultParticle(getFrontSideWithPitch(loc, max / 10.0), EnumParticle.EXPLOSION_HUGE, 0, 0, 0, null, 0).run();
				}
			}, i * 2);
		}
	}
	
	private Location getFrontSideWithPitch(Location loc, double distance) {
	    double angleYaw = Math.toRadians(loc.getYaw());
	    double anglePitch = Math.toRadians(loc.getPitch());
		return loc.clone().add(new Vector(-Math.sin(angleYaw) * Math.cos(anglePitch), -Math.sin(anglePitch), Math.cos(angleYaw) * Math.cos(anglePitch)).normalize().multiply(distance));
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() - 10);
		pd.setMalusHealth(-16);
	}
	
	@Override
	public void ultimateRecharge(PlayerData pd) {
		super.ultimateRecharge(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
		pd.setMalusHealth(0);
	}
}
