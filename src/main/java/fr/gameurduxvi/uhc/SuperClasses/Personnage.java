package fr.gameurduxvi.uhc.SuperClasses;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.scenarios.target.Target;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public abstract class Personnage {
	private int id;
	private int priority;
	private String name;
	private int prime;
	private int amount;
	private int ultimateTime;
	private int ultimateRecharge;
	protected ArrayList<PrefixData> prefixData = new ArrayList<>();
	private boolean inUltimate = false;
	private String description = "§7Aucun détail de " + name + " n'a été noté";
	private ItemStack itemUltimate;
	private int type;	
	private String fidelePrefix = "";
	private String shipFileName = "";
	private String ultimateSoundName = "";
	private String attributedSoundName = "";
	
	
	
	public Personnage() {
		setPrefixData();
	}
	/*
	 * ============================================================================
	 * ========================== Get Functions ===================================
	 * ============================================================================
	 * */		
	public int getId() {
		return id;
	}
	public int getPriority() {
		return priority;
	}
	public String getName() {
		return name;
	}
	public int getPrime() {
		return prime;
	}
	public int getAmount() {
		return amount;
	}
	public int getUltimateTime() {
		return ultimateTime;
	}
	public int getUltimateRecharge() {
		return ultimateRecharge;
	}
	public String getDescription() {
		return description;
	}
	public ItemStack ultimateItem() {
		return itemUltimate;		
	}
	public int getType() {
		return type;
	}
	public String getFidelePrefix() {
		return fidelePrefix;
	}
	
	public abstract void setPrefixData();
	
	public ArrayList<PrefixData> getPrefixData() {
		return prefixData;
	}
	
	public boolean isInUltimate() {
		return inUltimate;
	}
	
	public String getShipFileName() {
		return shipFileName;
	}
	
	public String getUltimateSoundName() {
		return ultimateSoundName;
	}
	
	public String getAttributedSoundName() {
		return attributedSoundName;
	}
	
	
	
	/*
	 * ============================================================================
	 * ========================== Set Functions ===================================
	 * ============================================================================
	 * */
	
	public void setId(int id) {
		this.id = id;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrime(int prime) {
		this.prime = prime;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setUltimateRecharge(int ultimateRecharge) {
		this.ultimateRecharge = ultimateRecharge;
	}
	public void setUltimateTime(int ultimateTime) {
		this.ultimateTime = ultimateTime;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setItemUltimate(ItemStack itemUltimate) {
		this.itemUltimate = itemUltimate;
	}	
	public void setInUltimate(boolean inUltimate) {
		this.inUltimate = inUltimate;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setFidelePrefix(String fidelePrefix) {
		this.fidelePrefix = fidelePrefix;
	}
	
	public void setShipFileName(String shipFileName) {
		this.shipFileName = shipFileName;
	}
	public void setUltimateSoundName(String ultimateSoundName) {
		this.ultimateSoundName = ultimateSoundName;
	}
	
	public void setAttributedSoundName(String attributedSoundName) {
		this.attributedSoundName = attributedSoundName;
	}
	
	/*
	 * ============================================================================
	 * ========================== Functions =======================================
	 * ============================================================================
	 * */
	
	public void ultimateRecharge(PlayerData pd) {}	
	public void onGiveRole() {}
	
	public void resetPrefixData() {
		prefixData.clear();
		setPrefixData();
	}
	
	public void preUltimate(PlayerData pd) {
		if(pd.canUltimate()) {
			ultimate(pd);
			return;
		}
		pd.getPlayer().sendMessage("§cL'ulti se recharge !");
	}
	
	public void ultimate(PlayerData pd) {
		if(pd.canUltimate()) {
			if(!getUltimateSoundName().equalsIgnoreCase("")) {
				for(Player p: Bukkit.getOnlinePlayers()) {
					p.playSound(pd.getPlayer().getLocation(), getUltimateSoundName(), 2, 1);
				}
			}
			inUltimate = true;
			pd.setCanUltimate(false);
			
			for(int i=0; i<=getUltimateTime();i++) {
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						String message = "";
						if(Target.hasTarget(pd.getPlayer()) && Target.getTarget(pd.getPlayer()) != null && !pd.canUltimate()) {
							message = " §8- §7Distance de votre cible: §6" + (int)(pd.getPlayer().getLocation().distance(Target.getTarget(pd.getPlayer()).getLocation())) + " §7blocks";
							PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
							((CraftPlayer) pd.getPlayer()).getHandle().playerConnection.sendPacket(packet);
						}
						PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + ultimateItem().getItemMeta().getDisplayName() + "§7 - §cActif" + message + "\"}"), (byte) 2);
						((CraftPlayer) pd.getPlayer()).getHandle().playerConnection.sendPacket(packet);						
					}
				}, i * 20);
			}
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					ultimateEnd(pd);
				}
			}, 20 * getUltimateTime());
			
		}
	}
	
	public void ultimateEnd(PlayerData pd) {
		inUltimate = false;
		for(int i = 0; i < getUltimateRecharge(); i++){
			int loopI = i;
			
			ArrayList<BukkitRunnable> list = new ArrayList<>();
			BukkitRunnable task = new BukkitRunnable() {
				
				@Override
				public void run() {
					if(pd.canUltimate()) {
						list.clear();
						return;
					}
					int tempRestant = getUltimateRecharge() - loopI;
					int min = (int) Math.floor(tempRestant / 60);
					int sec = tempRestant - (min * 60);
					
					String secAffichage = "";
					if(sec != 0) {
						secAffichage = sec + " sec";
					}
					
					String minAffichage = "";
					if(min != 0) {
						minAffichage = min + " min & ";
						if(sec == 0) {
							minAffichage = min + " min";
						}
					}
					String message = "";
					if(Target.hasTarget(pd.getPlayer()) && Target.getTarget(pd.getPlayer()) != null && !pd.canUltimate()) {
						message = " §8- §7Distance de votre cible: §6" + (int)(pd.getPlayer().getLocation().distance(Target.getTarget(pd.getPlayer()).getLocation())) + " §7blocks";
						PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
						((CraftPlayer) pd.getPlayer()).getHandle().playerConnection.sendPacket(packet);
					}
					PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + ultimateItem().getItemMeta().getDisplayName() + "§7 - §c" + minAffichage + secAffichage + message + "\"}"), (byte) 2);
					((CraftPlayer) pd.getPlayer()).getHandle().playerConnection.sendPacket(packet);
				}
			};
			
			list.add(task);
			task.runTaskLater(Main.getInstance(), i * 20);
		}
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				pd.getPlayer().sendMessage("§aVotre ulti est rechargé");
				pd.setCanUltimate(true);
				ultimateRecharge(pd);
			}
		}, getUltimateRecharge() * 20);
	}
	
	public void resetUltimate(PlayerData pd) {
		pd.setCanUltimate(false);
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				pd.getPlayer().sendMessage("§aVotre ulti est rechargé");
				pd.setCanUltimate(true);
				ultimateRecharge(pd);
			}
		}, 21);	
	}
}
