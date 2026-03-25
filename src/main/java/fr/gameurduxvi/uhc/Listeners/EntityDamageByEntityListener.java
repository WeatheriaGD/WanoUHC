package fr.gameurduxvi.uhc.Listeners;

import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir.Barbe_Noir2;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom.Big_Mom1;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom.Big_Mom2;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Shanks.Shanks1;
import fr.gameurduxvi.uhc.Personnages.Pirates.Bartolomeo;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;

public class EntityDamageByEntityListener implements Listener {
	@EventHandler
	public void OnDamage(EntityDamageByEntityEvent e) {
		if(Main.GAMESTATE.equals(GameState.Ended)) {	
			if(e.getEntity() instanceof ArmorStand) {
				if(e.getDamager() instanceof Player) {
					PlayerData pd = PlayerData.getPlayerData((Player) e.getDamager());
					pd.PreviousPage();
				}
			}
			e.setCancelled(true);
			return;
		}
		
		if(e.getEntity() instanceof Player) {
			
			Player victim = (Player) e.getEntity();
			if(!PlayerData.hasPlayerData(victim)) {
				return;
			}
			
			PlayerData pd = PlayerData.getPlayerData(victim);
			
			Entity attacker = e.getDamager();
			if(e.getDamager() instanceof Arrow) {
				attacker = (Entity) ((Arrow) e.getDamager()).getShooter();
			}
			
			pd.setLastDamager(attacker);
			pd.setLastDamagerDate(new Date());
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					long diff = new Date().getTime() - pd.getLastDamagerDate().getTime();
					long diffSeconds = diff / 1000;
					if(diffSeconds > 4) {
						pd.setLastDamager(null);
					}
				}
			}, 6 * 20);
			
			// Damage Amplifier
			e.setDamage(e.getDamage() * (ConfigManager.DAMAGE_AMPLIFIER / 100));
			
			if(pd.getPersonnage() instanceof Bartolomeo) {
				Bartolomeo pers = (Bartolomeo) pd.getPersonnage();
				if(pers.takeMoreDamage) {
					e.setDamage(e.getDamage() * 1.3);
				}
			}
			
			if(attacker instanceof Player) {
				
				Player attackerP = (Player) attacker;
				PlayerData pd2 = PlayerData.getPlayerData(attackerP);
				
				if(victim.getWorld().getName().equals(Main.getInstance().centerMaplocation.getWorld().getName()) && Main.GAMESTATE.equals(GameState.In_Progress)) {
					e.setCancelled(true);
					return;
				}					
				
				if(pd2.getPersonnage() instanceof Big_Mom1) {
					Big_Mom1 bigMom1 = (Big_Mom1) pd2.getPersonnage();
					if(bigMom1.weaknessOnFirstHit) {
						victim.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 15, 0, true, false), true);
						bigMom1.weaknessOnFirstHit = false;
					}
				}
				
				if(pd2.getPersonnage() instanceof Big_Mom2) {
					Big_Mom2 bigMom2 = (Big_Mom2) pd2.getPersonnage();
					if(bigMom2.ignoreArmor) {
						e.setDamage(DamageModifier.ARMOR, 0);
						e.setDamage(DamageModifier.BLOCKING, 0);
						e.setDamage(DamageModifier.MAGIC, 0);
						e.setDamage(DamageModifier.RESISTANCE, 0);
					}
				}
				else if(pd2.getPersonnage() instanceof Shanks1) {
					Shanks1 shanks1 = (Shanks1) pd2.getPersonnage();
					if(shanks1.ignoreArmor) {
						e.setDamage(DamageModifier.ARMOR, 0);
						e.setDamage(DamageModifier.BLOCKING, 0);
						e.setDamage(DamageModifier.MAGIC, 0);
						e.setDamage(DamageModifier.RESISTANCE, 0);
					}
				}
				else if(pd2.getPersonnage() instanceof Barbe_Noir2) {
					Barbe_Noir2 barbeNoir2 = (Barbe_Noir2) pd2.getPersonnage();
					if(barbeNoir2.ignoreArmor) {
						e.setDamage(DamageModifier.ARMOR, 0);
						e.setDamage(DamageModifier.BLOCKING, 0);
						e.setDamage(DamageModifier.MAGIC, 0);
						e.setDamage(DamageModifier.RESISTANCE, 0);
					}
				}
				
				if(attackerP.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
					e.setDamage(e.getDamage() / 1.4);
				}
				
				if(!e.isCancelled()) {
					Random rand = new Random();
					
					double toAdd = 0;
					if(rand.nextBoolean()) toAdd = 180;
					
					double sin = Math.sin(Math.toRadians(attackerP.getLocation().getYaw() + toAdd));
					double cos = Math.cos(Math.toRadians(attackerP.getLocation().getYaw() + toAdd));
					
					double x = victim.getLocation().getX() + cos;
					double y = victim.getLocation().getY() - (1.5 * rand.nextDouble());
					double z = victim.getLocation().getZ() + sin;
					
					Location loc = new Location(victim.getWorld(), x, y, z);
					
					WorldServer s = ((CraftWorld)victim.getWorld()).getHandle();
			        EntityArmorStand stand = new EntityArmorStand(s);
			       
			        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
			        double finalDamage = e.getFinalDamage() * 100;
			        finalDamage = (int) finalDamage;
			        finalDamage = finalDamage / 100;
			        stand.setCustomName("§c" + finalDamage);
			        stand.setCustomNameVisible(true);
			        stand.setGravity(false);
			        stand.setInvisible(true);
			        
			        if(finalDamage > victim.getHealth()) {
			        	pd2.addTotalDamage(victim.getHealth());
			        }
			        else {
				        pd2.addTotalDamage(finalDamage);
			        }
			       
			        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
			        ((CraftPlayer)attackerP).getHandle().playerConnection.sendPacket(packet);
			        
			        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							PacketPlayOutEntityDestroy destroy2 = new PacketPlayOutEntityDestroy(stand.getId());
				            ((CraftPlayer)attackerP).getHandle().playerConnection.sendPacket(destroy2);
						}
					}, 10);
				}				
			}
		}
	}
}
