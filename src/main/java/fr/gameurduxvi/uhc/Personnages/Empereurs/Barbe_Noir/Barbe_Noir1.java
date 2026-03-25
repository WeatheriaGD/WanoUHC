package fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Particles.DelayedParticle;
import fr.gameurduxvi.uhc.Particles.ParticleCircle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

public class Barbe_Noir1 extends Personnage {
	
	private ArrayList<Player> list = new ArrayList<>();
	private boolean inZone = false;
	public boolean hidden = false;
	
	public Barbe_Noir1() {
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
				"§0§l§nBlack Hole§r\n" + 
				" \n" + 
				"§7Barbe Noire créer un trou noir dans lequel il est le maitre.\n" + 
				" \n" + 
				"§6Durée §f: §e10s\n" + 
				"§6Cooldown §f: §e7min\n" + 
				" \n" + 
				"§2✔ Effets bonus §f:\n" + 
				"§a⇨ §7§lBlindness 1 §adurant §7§l3s §adans une zone de §7§l10 blocs §aautour de Barbe Noire.\n" + 
				"§a⇨ §7§lInvisibilité §aà Barbe Noire tant qu'il est dans sa zone.\n" + 
				" \n" + 
				"§4✖ Effets malus §f:\n" + 
				"§c⇨ §7§l-8§4❤ §cdurant §7§l7min §cà la fin de l'ulti.");
		
		ItemStack it = new ItemStack(Material.BEDROCK);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aYami Yami No Mi");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setShipFileName("ship");
		
		setFidelePrefix("§8Fidèle ");
		
		setUltimateSoundName("wano.roles.ultimates.empereurs.barbe_noir.black_hole");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§8Barbe Noire ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		super.ultimate(pd);
		Location poseLoc = pd.getPlayer().getLocation();
		for(Entity e: pd.getPlayer().getWorld().getNearbyEntities(poseLoc, 10, 10, 10)) {
			if(!(e instanceof Player)) continue;
			Player p = (Player) e;
			if(pd.getPlayer().getName().equals(p.getName())) continue;
			if(PlayerData.hasFidèle(pd) && PlayerData.getFidèle(pd).getPlayer().getName().equals(p.getName())) continue;
			//list.add(p);
			//for(Team team: p.getScoreboard().getTeams()) team.setNameTagVisibility(NameTagVisibility.NEVER);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 1, false, false), true);
		}
		
		for(Player p: Bukkit.getOnlinePlayers()) {
			if(PlayerData.hasFidèle(pd) && PlayerData.getFidèle(pd).getPlayer().getName().equals(p.getName())) continue;
			list.add(p);
			for(Team team: p.getScoreboard().getTeams()) team.setNameTagVisibility(NameTagVisibility.NEVER);
		}
		
		for(int i=0; i<=getUltimateTime();i++) {
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Collection<Entity> entities = pd.getPlayer().getWorld().getNearbyEntities(poseLoc, 10, 10, 10);					
					for(Entity e: entities) {
						if(!(e instanceof Player)) continue;
						Player p = (Player) e;
						if(!pd.getPlayer().getName().equals(p.getName())) continue;
						pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 25, 1, false, false), true);
					}
				}
			}, 20 * i);
			hide(pd);
			for(int ticks = 0; ticks < 20; ticks++) {
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						Collection<Entity> entities = pd.getPlayer().getWorld().getNearbyEntities(poseLoc, 10, 10, 10);
						
						if(!inZone && !entities.contains(pd.getPlayer())) {
							show(pd);
						}
						else {
							hide(pd);
						}
					}
				}, ticks + (i * 20));
			}
		}
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				show(pd);
			}
		}, 20 * (getUltimateTime() + 1));
		
		/*new ParticleCircle(poseLoc, 1, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1);
		new ParticleCircle(poseLoc, 1.5, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1);
		for(int i=0; i<=21; i++) {
			double time = 2 + (0.5 * i);
			new DelayedParticle(new ParticleCircle(poseLoc, time, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1), (0.1 * i));
		}
		
		new ParticleCircle(poseLoc, 1, getUltimateTime(), EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 40);
		for(int i=0; i<=21; i++) {
			double time = 1.5 + (0.5 * i);
			new DelayedParticle(new ParticleCircle(poseLoc, time, getUltimateTime(), EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 10), (0.1 * i));
		}*/
		
		new ParticleCircle(poseLoc, 1, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1);
		//new ParticleCircle(poseLoc, 1.5, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1);
		for(int i=0; i<=21; i++) {
			double time = 2 + (0.5 * i);
			new DelayedParticle(new ParticleCircle(poseLoc, time, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1), (0.1 * i));
		}
		
		new ParticleCircle(poseLoc, 1, getUltimateTime(), EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 40);
		for(int i=0; i<=21; i++) {
			double time = 1.5 + (0.5 * i);
			new DelayedParticle(new ParticleCircle(poseLoc, time, getUltimateTime(), EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 30 - i), (0.1 * i));
		}
	}
	
	private void show(PlayerData pd) {
		PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 0, CraftItemStack.asNMSCopy(pd.getPlayer().getItemInHand()));
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 1, CraftItemStack.asNMSCopy(pd.getPlayer().getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 2, CraftItemStack.asNMSCopy(pd.getPlayer().getInventory().getChestplate()));
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 3, CraftItemStack.asNMSCopy(pd.getPlayer().getInventory().getLeggings()));
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 4, CraftItemStack.asNMSCopy(pd.getPlayer().getInventory().getBoots()));
        for(Player lp : Bukkit.getOnlinePlayers()) {
        	if(PlayerData.hasFidèle(pd) && PlayerData.getFidèle(pd).getPlayer().getName().equals(lp.getName())) continue;
            CraftPlayer craftPlayer = (CraftPlayer) lp;
            EntityPlayer handle = craftPlayer.getHandle();
            handle.playerConnection.sendPacket(handPacket);
            handle.playerConnection.sendPacket(helmetPacket);
            handle.playerConnection.sendPacket(chestPacket);
            handle.playerConnection.sendPacket(legPacket);
            handle.playerConnection.sendPacket(bootsPacket);
        }
	}
	
	private void hide(PlayerData pd) {
		PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 0, null);
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 1, null);
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 2, null);
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 3, null);
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 4, null);
        
        for(Player lp: Bukkit.getOnlinePlayers()) {
        	if(lp.equals(pd.getPlayer())) continue;
        	if(PlayerData.hasFidèle(pd) && PlayerData.getFidèle(pd).getPlayer().getName().equals(lp.getName())) continue;
            CraftPlayer craftPlayer = (CraftPlayer) lp;
            craftPlayer.getHandle().playerConnection.sendPacket(handPacket);
            craftPlayer.getHandle().playerConnection.sendPacket(helmetPacket);
            craftPlayer.getHandle().playerConnection.sendPacket(chestPacket);
            craftPlayer.getHandle().playerConnection.sendPacket(legPacket);
            craftPlayer.getHandle().playerConnection.sendPacket(bootsPacket);
        }
	}
	
	@Override
	public void ultimateEnd(PlayerData pd) {
		super.ultimateEnd(pd);
		
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth() - 10);
		pd.setMalusHealth(-16);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(Player lp: list) for(Team team: lp.getScoreboard().getTeams()) team.setNameTagVisibility(NameTagVisibility.ALWAYS);
				list.clear();
			}
		}, 30);
	}
	
	@Override
	public void ultimateRecharge(PlayerData pd) {
		super.ultimateRecharge(pd);
		//pd.getPlayer().setMaxHealth(pd.getMaxHealth());
		pd.setMalusHealth(0);
	}
}
