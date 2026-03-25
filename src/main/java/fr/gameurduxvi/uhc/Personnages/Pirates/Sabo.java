package fr.gameurduxvi.uhc.Personnages.Pirates;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

public class Sabo extends Personnage {
	
	public Map<ItemStack,Integer> fireEnchantChange = new HashMap<>();
	
	public Sabo() {
		setId(6);
		setType(0);
		setPriority(9);
		setName("Sabo");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 6);
		setUltimateTime(15);
		setDescription(" \n" + 
				" \n" + 
				"§6§l§nHiken§r \n" + 
				" \n" + 
				"§7Le poing de Sabo s'enflamme et brûle tout sur son passage.  \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e6min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§lFire Aspect 2 §aapparait sur l'épée de Sabo. \n" + 
				"§a⇨ §7§lResitance 1 §a+ §7§lFire Resistance 2 §aà Sabo. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-2§4❤ §cpendant l'ulti. \n" + 
				"§c⇨ §7§l-1§4❤ §c+ §7§lWeakness 1 §cdurant §7§l30s §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.BREWING_STAND_ITEM);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aMera Mera No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.sabo");
		setAttributedSoundName("wano.roles.attribution.pirates.sabo");
	}
	
	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * getUltimateTime(), 1, true, false));
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * getUltimateTime(), 0, true, false));
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() - 4);
		pd.setMalusHealth(-4);
		
		/*
		 *  Fire aspect stuff
		 */
		fireEnchantChange.clear();
		for(ItemStack it: pd.getPlayer().getInventory()) {
			if(Main.getInstance().isNull(it)) continue;
			if(it.getType().toString().contains("SWORD")) {
				int fireAspectEnchLvl = 0;
				/*if(it.containsEnchantment(Enchantment.FIRE_ASPECT)) {
					int fireAspectEnchLvl2 = it.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
					fireAspectEnchLvl = fireAspectEnchLvl2;
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							it.addEnchantment(Enchantment.FIRE_ASPECT, fireAspectEnchLvl2);
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
				fireEnchantChange.put(it, fireAspectEnchLvl);
			}
		} // end Fire Aspect
		
		for(int i=0; i<=getUltimateTime()*20; i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Location rightHand = getRightSide(pd.getPlayer().getEyeLocation(), 0.45).subtract(0, .7, 0);
					new DefaultParticle(rightHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					new DefaultParticle(rightHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					//new DefaultParticle(rightHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					//new DefaultParticle(rightHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					
					Location leftHand = getRightSide(pd.getPlayer().getEyeLocation(), -0.45).subtract(0, .7, 0);
					new DefaultParticle(leftHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					new DefaultParticle(leftHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					//new DefaultParticle(leftHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
					//new DefaultParticle(leftHand, EnumParticle.FLAME, 0, 0, 0, null, 0).run();
				}
			}, i);
			//new DelayedParticle(new ParticleSphere(pd.getPlayer().getEyeLocation(), 0.3, 20 * 1, EnumParticle.SPELL_MOB, null, 0, 255, 255, 255, 180), i);
		}
	}
		
	private Location getRightSide(Location location, double distance) {
	    float angle = location.getYaw() / 60;
	    return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		fireEnchantChange.clear();
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0, true, false));
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() - 2);
		pd.setMalusHealth(-2);
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
				pd.setMalusHealth(0);
			}
		}, 20 * 30);
	}
}
