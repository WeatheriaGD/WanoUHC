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

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Particles.DelayedParticle;
import fr.gameurduxvi.uhc.Particles.ParticleCircle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Kuzan extends Personnage {
	public Kuzan() {
		setId(8);
		setType(0);
		setPriority(9);
		setName("Kuzan");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 5);
		setUltimateTime(10);
		setDescription(" \n" + 
				" \n" + 
				"§b§l§nIce Age§r \n" + 
				" \n" + 
				"§7Une zone de glace apparaît autour de Kuzan réduisant ses adversaires casi immobiles. \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e5min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§lSlowness 2 §adans une zone de §7§l10 blocs §aautour de Kuzan. \n" + 
				"§a⇨ §7§lResitance 1 §aà Kuzan tant qu'il est dans sa zone. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§lWeakness 1 §cpendant §7§l30s §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.CAULDRON_ITEM);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aHie Hie No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.kuzan");
		setAttributedSoundName("wano.roles.attribution.pirates.kuzan");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(pd.getPlayer().getLocation(), 10, 10, 10)) {
			if(!(e instanceof Player)) continue;
			Player p = (Player) e;
			if(pd.getPlayer().getName().equals(p.getName())) continue;
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * getUltimateTime(), 1, true, false), true);
		}
		
		Location loc = pd.getPlayer().getLocation();
		for(int i=0; i <= getUltimateTime(); i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(loc, 10, 10, 10)) {
						if(!(e instanceof Player)) continue;
						Player p = (Player) e;
						if(!pd.getPlayer().getName().equals(p.getName())) continue;
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 2, 0, true, false), true);
					}
				}
			}, 20 * i);
		}
		//new ParticleCircle(loc, 11.5, getUltimateTime(), EnumParticle.SPELL_MOB, null, 0, 255, 255, 102, 1);
		new ParticleCircle(loc, 1, getUltimateTime(), EnumParticle.BLOCK_CRACK, Material.PACKED_ICE, 0, 0, 0, 0, 40);
		for(int i=0; i<=21; i++) {
			double time = 1.5 + (0.5 * i);
			new DelayedParticle(new ParticleCircle(loc, time, getUltimateTime(), EnumParticle.BLOCK_CRACK, Material.PACKED_ICE, 0, 0, 0, 0, 30 - i), (0.1 * i));
		}
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0, true, false), true);
	}
	
	@Override
	public void ultimateRecharge(PlayerData pd) {
		super.ultimateRecharge(pd);
	}
}
