package fr.gameurduxvi.uhc.Personnages.Empereurs.Kaido;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class Kaido1 extends Personnage {
	public Kaido1() {
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
				"§9§l§nRamei Hakke§r\n" + 
				" \n" + 
				"§7Kaido prend de l'élant et donne un coup de massu dévastateur. \n" + 
				" \n" + 
				"§6Durée §f: §e10s \n" + 
				"§6Cooldown §f: §e7min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ §7§lSpeed 2 §apendant §7§l3s §aà Kaido \n" + 
				"§a⇨ §7§lStrenght 1 §aà Kaido \n" + 
				"   \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-8§4❤ §cdurant §7§l7min §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.BLAZE_ROD);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aKanabo");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setShipFileName("ship");
		
		setFidelePrefix("§9Fidèle ");
		
		setUltimateSoundName("wano.roles.ultimates.empereurs.kaido.ramei_hakke");
	}	

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§9Kaido ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1, true, false), true);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * getUltimateTime(), 0, true, false), true);
		for(int i=0; i<=getUltimateTime()*20; i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Location rightHand = getRightSide(pd.getPlayer().getEyeLocation(), 0.45).subtract(0, .7, 0);
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
					
					Location leftHand = getRightSide(pd.getPlayer().getEyeLocation(), -0.45).subtract(0, .7, 0);
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 0, 255, null, 0).run();
				}
			}, i);
		}
	}
	

	private Location getRightSide(Location location, double distance) {
	    double angle = Math.toRadians(location.getYaw());
	    return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
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
