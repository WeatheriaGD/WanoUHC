package fr.gameurduxvi.uhc.Personnages.Pirates;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Particles.ParticleVerticalCircleWithYaw;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Zoro extends Personnage {	
	
	public Zoro() {
		setId(4);
		setType(0);
		setPriority(9);
		setName("Zoro");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 5);
		setUltimateTime(15);
		setDescription(" \n" + 
				" \n" + 
				"§9§l§nSantoryu Ougi Sanzen Sekai§r \n" + 
				" \n" + 
				"§7Zoro fait tourner ses sabres à une vitesse fulgurante pour terrasser ses adversaires. \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e5min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§lSpeed 2 §a+ §7§lStrenght 1 §aà Zoro. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-3§4❤ §cpendant l'ulti. \n" + 
				"§c⇨ §7§lSlowness 1 §cet §7§lWeakness 1 §cdurant §7§l15s §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aShisui");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.zoro");
		setAttributedSoundName("wano.roles.attribution.pirates.zoro");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * getUltimateTime(), 0, true, false), true);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * getUltimateTime(), 1, true, false), true);
		pd.setMalusHealth(-6);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() - 6);
		
		for(int i=0; i<=getUltimateTime()*20; i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					new ParticleVerticalCircleWithYaw(pd.getPlayer().getEyeLocation(), 1.5, pd.getPlayer().getLocation().getYaw(), 0, EnumParticle.SPELL_MOB, null, 0, 255, 0, 255, 30).run();
					//new ParticleVerticalCircleWithYaw(pd.getPlayer().getEyeLocation(), 1.5, pd.getPlayer().getLocation().getYaw(), 0, EnumParticle.PORTAL, null, 0, 0, 0, 0, 10).run();
				}
			}, i);
		}
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
		pd.setMalusHealth(0);
		//pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 30, 0, true, false), true);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 15, 0, true, false), true);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 15, 0, true, false), true);
	}
}
