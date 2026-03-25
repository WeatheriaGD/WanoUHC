package fr.gameurduxvi.uhc.Personnages.Pirates;

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

public class Luffy extends Personnage {	
	public Luffy() {
		setId(2);
		setType(0);
		setPriority(9);
		setName("Luffy");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 6);
		setUltimateTime(15);
		setDescription(" \n" + 
				" \n" + 
				"§4§l§nGear 4§r \n" + 
				" \n" + 
				"§7Luffy se transforme en Snake-Man, il devient plus agile et plus fort. \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e6min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§lSpeed 1 §a+ §7§lStrenght 1 §aà Luffy. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-2§4❤ §cpendant l'ulti. \n" +
				"§c⇨ §7§l9§4❤ §cmax après l'ulti. \n" +
				"§c⇨ §7§lSlowness 1 §cdurant §7§l30s §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.BEACON);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aGomu Gomu No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.luffy");
		setAttributedSoundName("wano.roles.attribution.pirates.luffy");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.setMalusHealth(-4);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * getUltimateTime(), 0, true, false));
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * getUltimateTime(), 0, true, false));
		Location loc = pd.getPlayer().getLocation();
		double time = getUltimateTime();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 0.5, loc.getZ()), 3, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1.0, loc.getZ()), 3.2, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run(); // -
		//new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 1.5, loc.getZ()), 2.8, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 2.0, loc.getZ()), 2.6, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 2.5, loc.getZ()), 2.4, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 3.0, loc.getZ()), 2.6, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run(); // -
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 3.5, loc.getZ()), 2.4, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 4.0, loc.getZ()), 2.2, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 4.5, loc.getZ()), 2.0, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 5.0, loc.getZ()), 2.2, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run(); // -
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 5.5, loc.getZ()), 2.0, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
		new ParticleCircle(new Location(loc.getWorld(), loc.getX(), loc.getY() + 6.0, loc.getZ()), 1.8, time, EnumParticle.CLOUD, null, 0, 0, 0, 0, 20).run();
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 30, 0, true, false));
		pd.setMalusHealth(-2);
	}
	
	@Override
	public void ultimateRecharge(PlayerData pd) {
		super.ultimateRecharge(pd);
		pd.setMalusHealth(0);
	}
}
