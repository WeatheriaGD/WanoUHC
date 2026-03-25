package fr.gameurduxvi.uhc.Personnages.Pirates;

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
import fr.gameurduxvi.uhc.Particles.DelayedParticle;
import fr.gameurduxvi.uhc.Particles.ParticleCircle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Nami extends Personnage {	
	
	public boolean particles = false;
	public Location lastParticleLocation;
	
	public Nami() {
		setId(5);
		setType(0);
		setPriority(9);
		setName("Nami");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 5);
		setUltimateTime(15);
		setDescription(" \n" + 
				" \n" + 
				"§e§l§nZeus§r \n" + 
				" \n" + 
				"§7La foudre s'abat brutalement tout autour de Nami.  \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e5min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§l-1§4❤ §a+ §7§lWeakness 1 §apendant §7§l10s §adans une zone de §7§l10 blocs §aautour de Nami. \n" + 
				"§a⇨ §7§l+1§4❤ §asupplémentaire à Nami. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§lWeakness 1 §cdurant §7§l30s. \n" + 
				"§c⇨ §7§lTraces de pas §cderrière Nami durant §7§l30s.");
		
		ItemStack it = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aClima-Tact");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.nami");
		setAttributedSoundName("wano.roles.attribution.pirates.nami");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		
		for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(pd.getPlayer().getLocation(), 10, 10, 10)) {
			if(e instanceof Player) {
				if(!e.equals(pd.getPlayer())) {
					e.getWorld().strikeLightning(e.getLocation());
					((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 0, true, false));
				}
			}
		}
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() + 2);
		pd.setMalusHealth(2);
		
		for(double y = 0; y <= 2; y += 0.5) {
			Location loc = pd.getPlayer().getLocation().add(new Vector(0, y + 6, 0));
			new ParticleCircle(loc, 1, getUltimateTime(), EnumParticle.CLOUD, null, 0, 0, 0, 0, 40);
			for(int i=0; i<=24; i++) {
				double range = (0.5 * i);
				new DelayedParticle(new ParticleCircle(loc, range, 2, EnumParticle.CLOUD, null, 0, 0, 0, 0, 180/((i / ((i % 2) + 1)) + 1)), (0.1 * i));
			}
		}
		
		for(double y = 0; y <= 2; y += 0.5) {
			Location loc = pd.getPlayer().getLocation().add(new Vector(0, y + 6, 0));
			new ParticleCircle(loc, 1, getUltimateTime(), EnumParticle.SMOKE_LARGE, null, 0, 0, 0, 0, 40);
			for(int i=0; i<=24; i++) {
				double range = (0.5 * i);
				new DelayedParticle(new ParticleCircle(loc, range, 2, EnumParticle.SMOKE_LARGE, null, 0, 0, 0, 0, 180/((i / ((i % 2) + 1)) + 1)), (0.1 * i) + 2);
			}
		}
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
		pd.setMalusHealth(0);
		
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0, true, false));
		
		particles = true;
		lastParticleLocation = pd.getPlayer().getLocation();
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				particles = false;
			}
		}, 20 * 30);
	}
}
