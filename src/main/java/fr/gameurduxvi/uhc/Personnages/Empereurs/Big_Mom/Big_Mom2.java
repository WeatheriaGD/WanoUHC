package fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom;

import java.util.Random;

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

public class Big_Mom2 extends Personnage {
	
	public boolean ignoreArmor = false;
	
	public Big_Mom2() {
		setId(13);
		setType(1);
		setPriority(3);
		setName("Big Mom");
		setPrime(Main.getInstance().primeEmpereur);
		setAmount(0);
		setUltimateRecharge(60 * 7);
		setUltimateTime(10);
		setDescription(" \n" + 
				" \n" + 
				"§d§l§nIkoku§r \n" + 
				" \n" + 
				"§7Kaido se transforme en dragon et crache  \n" + 
				" \n" + 
				"§6Durée §f: §e10s \n" + 
				"§6Cooldown §f: §e7min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §a⇨ §7§l-2§4❤ pour les adversaires dans une zone de §7§l10x10 §aautour de Big Mom. \n" + 
				"§a⇨ §7§lStrenght 1 §aà Big Mom. \n" + 
				"   \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-8§4❤ §cdurant §7§l7min §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.QUARTZ);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aNapoleon");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setShipFileName("ship");
		
		setFidelePrefix("§5Fidèle ");
		
		setUltimateSoundName("wano.roles.ultimates.empereurs.big_mom.ikoku");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§5Big Mom ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * getUltimateTime(), 0, true, false), true);
		
		ignoreArmor = true;
		for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(pd.getPlayer().getLocation(), 10, 10, 10)) {
			if(!(e instanceof Player)) continue;
			Player p = (Player) e;
			if(pd.getPlayer().getName().equals(p.getName())) continue;
			
			p.damage(4, pd.getPlayer());
		}
		ignoreArmor = false;		
		
		
		double beforeHorizontalDistance = 0;
		double horizontalDistance = 0;
		double diff = 0;
		for(int i = 0; i < 50; i++) {
			int times = i % 10;
			if(times == 0) {
				beforeHorizontalDistance = horizontalDistance;
				horizontalDistance = (new Random().nextInt(20) / 10.0) - 1;
				if(horizontalDistance > beforeHorizontalDistance)
					diff = (horizontalDistance - beforeHorizontalDistance) / 10;
				else 
					diff = -(beforeHorizontalDistance - horizontalDistance) / 10;
			}
			//Bukkit.broadcastMessage(horizontalDistance + "(" + diff + ")" + i + "(" + times + ")");
			
			Location loc = getRightSide(getFrontSide(pd.getPlayer().getLocation(), i / 10.0), beforeHorizontalDistance + (times * diff));
			/*Location locBlock = getRightSide(getFrontSide(pd.getPlayer().getLocation(), i / 10.0), beforeHorizontalDistance + (times * diff));
			for(int height = 0; height <= 10; height++) {
				locBlock.setY(loc.getY() - height);
				if(!locBlock.getBlock().getType().equals(Material.AIR)) {
					loc.setY(locBlock.getBlockY() + 1);
					break;
				}
			}*/
			
			for(int j = 0; j < 40; j++) {
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						new DefaultParticle(loc.clone().subtract(0, 0.5, 0), EnumParticle.PORTAL, 0, 0, 0, null, 0).run();
					}
				}, j);
				
			}
		}
		
	}
	
	private Location getRightSide(Location location, double distance) {
	    double angle = Math.toRadians(location.getYaw());
	    return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
	}
	
	private Location getFrontSide(Location loc, double distance) {
		double angle = Math.toRadians(loc.getYaw());
		return loc.clone().add(new Vector(-Math.sin(angle), 0, Math.cos(angle)).normalize().multiply(distance));
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
