package fr.gameurduxvi.uhc.Personnages.Pirates;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Particles.ParticleCircle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Cavendish extends Personnage {	
	public Cavendish() {
		setId(1);
		setType(0);
		setPriority(9);
		setName("Cavendish");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 6);
		setUltimateTime(15);
		setDescription(" \n" + 
				" \n" + 
				"§5§l§nHakuba§r \n" + 
				" \n" + 
				"§7Cavendish laisse place à Hakuba et devient plus rapide et puissant. \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e6min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§l+2§4❤  §a+ §7§lSpeed 2 §aà Cavendish. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§lWeakness 1 §cdurant §7§l30s §cà la fin de l'ulti. \n" + 
				"");
		
		ItemStack it = new ItemStack(Material.DAYLIGHT_DETECTOR);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aRose");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.cavendish");
		setAttributedSoundName("wano.roles.attribution.pirates.cavendish");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * getUltimateTime(), 1, true, false));
		//pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * getUltimateTime(), 0, true, false));
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() + 4);
		pd.setMalusHealth(4);
		if(pd.getPlayer().getMaxHealth() < pd.getPlayer().getHealth() + 4) {
			pd.getPlayer().setHealth(pd.getPlayer().getMaxHealth());
		}
		else {
			pd.getPlayer().setHealth(pd.getPlayer().getHealth() + 4);
		}

		Location loc = pd.getPlayer().getLocation();
		Location locBlock = pd.getPlayer().getLocation();
		for(int i = 0; i <= 10; i++) {
			locBlock.setY(loc.getY() - i);
			if(!locBlock.getBlock().getType().equals(Material.AIR)) {
				loc.setY(locBlock.getBlockY() + 1);
				break;
			}
		}
		//new ParticleCircle(loc, 0.5, 2, EnumParticle.CLOUD, null, 0, 0, 0, 0, 60).run();
		//loc.add(new Vector(0, 0.1, 0));
		//new ParticleCircle(loc, 0.6, 2, EnumParticle.CLOUD, null, 0, 0, 0, 0, 60).run();
		//loc.add(new Vector(0, 0.1, 0));
		//new ParticleCircle(loc, 0.7, 2, EnumParticle.CLOUD, null, 0, 0, 0, 0, 60).run();
		for(int i=0; i<=getUltimateTime() * 4; i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Location loc = pd.getPlayer().getLocation();
					Location locBlock = pd.getPlayer().getLocation();
					for(int i = 0; i <= 10; i++) {
						locBlock.setY(loc.getY() - i);
						if(!locBlock.getBlock().getType().equals(Material.AIR)) {
							loc.setY(locBlock.getBlockY() + 1);
							break;
						}
					}
					new ParticleCircle(loc, 0.5, 1, EnumParticle.CLOUD, null, 0, 0, 0, 0, 90).run();
					//loc.add(new Vector(0, 0.1, 0));
					//new ParticleCircle(loc, 0.6, 2, EnumParticle.CLOUD, null, 0, 0, 0, 0, 60).run();
					//loc.add(new Vector(0, 0.1, 0));
					//new ParticleCircle(loc, 0.7, 2, EnumParticle.CLOUD, null, 0, 0, 0, 0, 60).run();
				}
			}, 5 * i);
		}	
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
		pd.setMalusHealth(0);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 30, 0, true, false));
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0, true, false));
	}
}
