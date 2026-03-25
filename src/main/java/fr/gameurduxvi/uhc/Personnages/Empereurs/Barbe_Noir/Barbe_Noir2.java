package fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir;

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

public class Barbe_Noir2 extends Personnage {
	
	public boolean ignoreArmor = false;
	
	public Barbe_Noir2() {
		setId(11);
		setType(1);
		setPriority(3);
		setName("Barbe Noire");
		setPrime(Main.getInstance().primeEmpereur);
		setAmount(0);
		setUltimateRecharge(60 * 7);
		setUltimateTime(10);
		setDescription(" \n" + 
				" \n" + 
				"§0§l§nShima Yurashi§r\n" + 
				" \n" + 
				"§7Barbe Noire manipule les vagues sismiques et génère des tremblements de terre. \n" + 
				" \n" + 
				"§6Durée §f: §e10s\n" + 
				"§6Cooldown §f: §e7min\n" + 
				" \n" + 
				"§2✔ Effets bonus §f:\n" + 
				"§a⇨ §7§l-3§4❤ pour les adversaires dans une zone de §7§l10x10 §aautour de Barbe Noire.\n" + 
				"§a⇨ §7§lRésistance 1 §aà Barbe Noire.\n" + 
				" \n" + 
				"§4✖ Effets malus §f:\n" + 
				"§c⇨ §7§l-8§4❤ §cdurant §7§l7min §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.PACKED_ICE);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aGura Gura No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setShipFileName("ship");
		
		setFidelePrefix("§8Fidèle ");
		
		setUltimateSoundName("wano.roles.ultimates.empereurs.barbe_noir.shima_yurashi");
	}
	
	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§8Barbe Noire ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 0, true, false));
		
		ignoreArmor = true;
		for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(pd.getPlayer().getLocation(), 10, 10, 10)) {
			if(!(e instanceof Player)) continue;
			Player p = (Player) e;
			if(pd.getPlayer().getName().equals(p.getName())) continue;
			p.damage(6, pd.getPlayer());
		}
		ignoreArmor = false;
		//new DefaultParticle(loc, particleType, mat, data, red, green, blue)
		//new DefaultParticle(pd.getPlayer().getEyeLocation(), EnumParticle.SPELL_MOB, null, 0, 255, 255, 255).run();
		//new DelayedParticle(new ParticleSphere(pd.getPlayer().getEyeLocation(), 0.3, 20 * 10, EnumParticle.SPELL_MOB, null, 0, 255, 255, 255, 90), 1);
		for(int i=0; i<=getUltimateTime()*20; i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Location rightHand = getRightSide(pd.getPlayer().getEyeLocation(), 0.45).subtract(0, .7, 0);
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
					new DefaultParticle(rightHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
					
					Location leftHand = getRightSide(pd.getPlayer().getEyeLocation(), -0.45).subtract(0, .7, 0);
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
					new DefaultParticle(leftHand, EnumParticle.SPELL_MOB, 255, 255, 255, null, 0).run();
				}
			}, i);
			//new DelayedParticle(new ParticleSphere(pd.getPlayer().getEyeLocation(), 0.3, 20 * 1, EnumParticle.SPELL_MOB, null, 0, 255, 255, 255, 180), i);
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
