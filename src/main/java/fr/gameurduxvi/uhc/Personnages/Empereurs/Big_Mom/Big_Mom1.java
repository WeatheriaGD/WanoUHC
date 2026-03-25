package fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Particles.ParticleCircle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Big_Mom1 extends Personnage {
	
	public boolean weaknessOnFirstHit = false;
	
	public Big_Mom1() {
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
				"§d§l§nCognac§r \n" + 
				" \n" + 
				"§7Big Mom ultilise son épée Napoléon et \n" + 
				" \n" + 
				"§6Durée §f: §e10s \n" + 
				"§6Cooldown §f: §e7min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§lFire Aspect 2 §aapparait sur l'épée de Big Mom. \n" + 
				"§a⇨ §7§lThorns 3 §aapparait sur le plastron de Big Mom. \n" + 
				"§a⇨ §7§lWeakness §aapparait sur le premier enemi touché. \n" + 
				"   \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-8§4❤ §cdurant §7§l7min §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.ENDER_PORTAL_FRAME);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aSoru Soru No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setShipFileName("ship");
		
		setFidelePrefix("§5Fidèle ");
		
		setUltimateSoundName("wano.roles.ultimates.empereurs.big_mom.cognac");
	}
	
	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§5Big Mom ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		
		for(int i=0; i<=getUltimateTime()*20; i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Location loc = pd.getPlayer().getEyeLocation().add(0, .3, 0);
					new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()), 0.3, 0.5, EnumParticle.FLAME, null, 0, 0, 0, 0, 30).run();
					new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 0.2, loc.getZ()), 0.2, 0.5, EnumParticle.FLAME, null, 0, 0, 0, 0, 60).run();
					new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 0.4, loc.getZ()), 0.1, 0.5, EnumParticle.FLAME, null, 0, 0, 0, 0, 180).run();
				}
			}, i);
		}
		
		weaknessOnFirstHit = true;
		
		for(ItemStack it: pd.getPlayer().getInventory()) {
			if(Main.getInstance().isNull(it)) continue;
			if(it.getType().toString().contains("SWORD")) {
				/*if(it.containsEnchantment(Enchantment.FIRE_ASPECT)) {
					final int fireAspectEnchLvl = it.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							it.addEnchantment(Enchantment.FIRE_ASPECT, fireAspectEnchLvl);
						}
					}, 20 * getUltimateTime());
				}
				else {*/
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							it.removeEnchantment(Enchantment.FIRE_ASPECT);
						}
					}, 20 * getUltimateTime());
				//}
				it.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			}
		}
		
		
		ItemStack it = pd.getPlayer().getInventory().getChestplate();
		if(Main.getInstance().isNull(it)) return;
		if(it.getType().toString().contains("CHESTPLATE")) {
			if(it.containsEnchantment(Enchantment.THORNS)) {
				final int fireAspectEnchLvl = it.getEnchantmentLevel(Enchantment.THORNS);
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						it.addEnchantment(Enchantment.THORNS, fireAspectEnchLvl);
					}
				}, 20 * getUltimateTime());
			}
			else {
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						it.removeEnchantment(Enchantment.THORNS);
					}
				}, 20 * getUltimateTime());
			}
			it.addEnchantment(Enchantment.THORNS, 3);
		}
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		weaknessOnFirstHit = false;
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
