package fr.gameurduxvi.uhc.Personnages.Pirates;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Particles.DelayedParticle;
import fr.gameurduxvi.uhc.Particles.ParticleRectangle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Bartolomeo extends Personnage {
	
	public boolean takeMoreDamage = false;
	
	public Bartolomeo() {
		setId(3);
		setType(0);
		setPriority(9);
		setName("Bartolomeo");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 5);
		setUltimateTime(15);
		setDescription(" \n" + 
				" \n" + 
				"§f§l§nBarrier§r \n" + 
				" \n" + 
				"§7Bartolomeo créé une barrière résitante à tout choc. \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e5min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§l+1§e❤ §7§ld'absorbtion §a+ §7§lRésistance 1 §aà Bartolomeo. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §cReçoit §7§l1.5x + de dégats §cdurant §7§l30s §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.QUARTZ_ORE);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aBari Bari No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.bartolomeo");
		setAttributedSoundName("wano.roles.attribution.pirates.bartolomeo");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * getUltimateTime(), 0, true, false));
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() + 2);
		pd.setMalusHealth(2);
		
		new DelayedParticle(new ParticleRectangle(pd.getPlayer().getEyeLocation(), 4, 4, pd.getPlayer().getLocation().getYaw(), getUltimateTime() * 20, EnumParticle.CLOUD, null, 0, 0, 0, 0), 20);
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
		pd.setMalusHealth(0);
		takeMoreDamage = true;
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				takeMoreDamage = false;
			}
		}, 20 * 30);
	}
}
