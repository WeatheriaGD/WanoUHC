package fr.gameurduxvi.uhc.Personnages.Pirates;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
import fr.gameurduxvi.uhc.Particles.ParticleSphere;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Law extends Personnage {
	
	Date lastSwitch = new Date();
	
	public Law() {
		setId(7);
		setType(0);
		setPriority(9);
		setName("Law");
		setPrime(Main.getInstance().primePirate);
		setAmount(3);
		setUltimateRecharge(60 * 5);
		setUltimateTime(15);
		setDescription(" \n" + 
				" \n" + 
				"§d§l§nRoom Shambles§r \n" + 
				" \n" + 
				"§7Law créé une zone dans laquelle il a tout le contrôle. \n" + 
				" \n" + 
				"§6Durée §f: §e15s \n" + 
				"§6Cooldown §f: §e5min \n" + 
				" \n" + 
				"§2✔ Effets bonus §f: \n" + 
				"§a⇨ Dans une zone de §7§l10x10 blocs§a, Law peut §7§léchanger sa place aléatoirement avec un joueur §aprésent dans celle-ci. \n" + 
				"§a⇨ §7§lStrenght 1 §aà Law. \n" + 
				" \n" + 
				"§4✖ Effets malus §f: \n" + 
				"§c⇨ §7§l-1.5§4❤ §cpendant l'ulti. \n" + 
				"§c⇨ §7§l-2§4❤ §cdurant §7§l30s §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.NETHER_STAR);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aOpe Ope No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setUltimateSoundName("wano.roles.ultimates.pirates.law");
		setAttributedSoundName("wano.roles.attribution.pirates.law");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7Pirate ", "", new int[]{-1}));
	}
	
	@Override
	public void preUltimate(PlayerData pd) {
		if(pd.getPersonnage().isInUltimate() && !pd.canUltimate()) {
			swtich(pd);
		}
		else {
			super.preUltimate(pd);
		}
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() - 3);
		pd.setMalusHealth(-1.5);

		pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 15, 0, true, false), true);
		
		Location loc = pd.getPlayer().getLocation();
		
		/*new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 2, 0, 25, null, 0).run();
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 2.5, 0, 25, null, 0), 2);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 3.5, 0, 20, null, 0), 4);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 4.5, 0, 20, null, 0), 6);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 5.5, 0, 15, null, 0), 8);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 6.5, 0, 15, null, 0), 10);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 7.5, 0, 10, null, 0), 12);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 8.5, 0, 10, null, 0), 14);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 9.5, 0, 5, null, 0), 16);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 10.5, 0, 5, null, 0), 18);*/
		
		new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 2, 0, 25, null, 0).run();
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 2.5, 0, 45, null, 0), 2);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 3.5, 0, 20, null, 0), 4);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 4.5, 0, 40, null, 0), 6);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 5.5, 0, 15, null, 0), 8);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 6.5, 0, 35, null, 0), 10);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 7.5, 0, 10, null, 0), 12);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 8.5, 0, 30, null, 0), 14);
		//new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 9.5, 0, 5, null, 0), 16);
		new DelayedParticle(new ParticleSphere(loc, EnumParticle.CLOUD, 0, 0, 0, 0, 10.5, 0, 25, null, 0), 18);
	}
	
	public void swtich(PlayerData pd) {
		TimeUnit timeUnit = TimeUnit.SECONDS;
		long diffInMillies = new Date().getTime() - lastSwitch.getTime();
		long diff = timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
		
		if(diff < 1) {
			pd.getPlayer().sendMessage("§cVous devez attendre au moins 1 seconde.");
		}
		else {
			lastSwitch = new Date();
			ArrayList<Player> players = new ArrayList<>();
			for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(pd.getPlayer().getLocation(), 10, 10, 10)) {
				if(!(e instanceof Player)) continue;
				Player p = (Player) e;
				if(pd.getPlayer().getName().equals(p.getName())) continue;
				players.add(p);
			}
			
			if(players.size() > 0) {
				Random rand = new Random();
				Player randomP = players.get(rand.nextInt(players.size()));
				Location loc1 = pd.getPlayer().getLocation();
				Location loc2 = randomP.getLocation();
				pd.getPlayer().teleport(loc2);
				randomP.teleport(loc1);
			}
		}		
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() - 4);
		pd.setMalusHealth(-4);
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
				pd.setMalusHealth(0);
			}
		}, 20 * 30);
	}
}
