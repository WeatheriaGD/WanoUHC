package fr.gameurduxvi.uhc.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import fr.gameurduxvi.uhc.Tasks.GameTask;
import fr.gameurduxvi.uhc.scenarios.target.Target;

public class PlayerDeathListener implements Listener {
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(e.getEntity() instanceof Player) {
			e.setDeathMessage(null);

			Player p = e.getEntity();
			Location loc = p.getLocation();

			if(!PlayerData.hasPlayerData(p)) {
				return;
			}

			PlayerData pd = PlayerData.getPlayerData(p);

			ArrayList<ItemStack> itemsToRemove = new ArrayList<>();
			
			/*if(pd.getPersonnage() instanceof Sabo) {
				Sabo sabo = (Sabo) pd.getPersonnage();
				for(ItemStack it: e.getDrops()) {
					Iterator<Entry<ItemStack, Integer>> iteretor = sabo.fireEnchantChange.entrySet().iterator();
					
					while(iteretor.hasNext()) {
						Entry<ItemStack, Integer> entry = iteretor.next();
						
						if(it.equals(entry.getKey())) {
							//Bukkit.broadcastMessage("item meme chose");
							//if(entry.getValue() != 0) {
							//	it.addEnchantment(Enchantment.FIRE_ASPECT, entry.getValue());
							//}
							//else {
								it.removeEnchantment(Enchantment.FIRE_ASPECT);
							//}
						}
					}
				}
			}*/
			for(ItemStack it: e.getDrops()) {
				if(it.hasItemMeta() && it.getItemMeta().getEnchants().containsKey(Enchantment.FIRE_ASPECT)) {
					it.removeEnchantment(Enchantment.FIRE_ASPECT);
				}
			}

			for(ItemStack it: ConfigManager.StuffKill) {
				e.getDrops().add(it);
			}

			for(ItemStack item: e.getDrops()) {
				if(item.getType().equals(Material.COMPASS))
					itemsToRemove.add(item);

				if(item.getItemMeta() == null) continue;
				if(item.getItemMeta().getDisplayName() == null) continue;
				if(pd.getPersonnage().ultimateItem() == null) continue;
				if(pd.getPersonnage().ultimateItem().getItemMeta() == null) continue;
				if(pd.getPersonnage().ultimateItem().getItemMeta().getDisplayName() == null) continue;
				if(item.getItemMeta().getDisplayName().contains(pd.getPersonnage().ultimateItem().getItemMeta().getDisplayName())) {
					//e.getDrops().remove(item);
					itemsToRemove.add(item);
				}
			}

			for(ItemStack it: itemsToRemove) {
				e.getDrops().remove(it);
			}

			// Death messages


			if(!Main.getInstance().isNull(pd.getLastDamager())) {
				if(pd.getLastDamager() instanceof Player) {
					Main.getInstance().showPrimes = true;
					Player attacker = (Player) pd.getLastDamager();

					if(!PlayerData.hasPlayerData(attacker)) return;
					PlayerData pd2 = PlayerData.getPlayerData(attacker);
					PlayerData.givePrime(pd, pd2);

					switch (p.getLastDamageCause().getCause()) {
						case BLOCK_EXPLOSION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser en fuyant §c" + attacker.getName() + " §7! ");
							break;
						case DROWNING:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a voulu goûter à l'eau en fuyant §c" + attacker.getName() + " §7! ");
							break;
						case FALL:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a fait le saut de l'ange en fuyant §c" + attacker.getName() + " §7! ");
							break;
						case FALLING_BLOCK:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est écrasé par terre en fuyant §c" + attacker.getName() + " §7! ");
							break;
						case FIRE:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait immoler par le feu en fuyant §c" + attacker.getName() + " §7! ");
							break;
						case LIGHTNING:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait foudroyer en fuyant §c" + attacker.getName() + " §7! ");
							break;
						case MAGIC:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait ensorceler en fuyant §c" + attacker.getName() + " §7! ");
							break;
						case STARVATION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort le ventre vide en fuyant §c" + attacker.getName() + " §7!");
							break;
						case SUFFOCATION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a suffoqué dans un mur en fuyant §c" + attacker.getName() + " §7!");
							break;
						case FIRE_TICK:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait brulé par §c" + attacker.getName() + " §7!");
							break;
						case POISON:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort par le poison en fuyant §c" + attacker.getName() + " §7!");
							break;
						case LAVA:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a essayé de nager dans la lave en fuyant §c" + attacker.getName() + " §7!");
							break;
						case ENTITY_ATTACK:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait terrasser par §c" + attacker.getName() + " §7!");
							break;
						case PROJECTILE:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait terrasser par §c" + attacker.getName() + " §7!");
							break;
						case THORNS:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait piquer trop fort par l'armure de §c" + attacker.getName() + " §7!");
							break;
						case ENTITY_EXPLOSION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser en fuyant §c" + attacker.getName() + " §7!");
							break;
						case SUICIDE:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a décidé de mettre fin à sa vie en fuyant §c" + attacker.getName() + " §7!");
							break;
						default:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort en fuyant §c" + attacker.getName() + " §7!");
							break;
					}

					if(Target.hasTarget(attacker))
						Target.targetKilled(attacker, p);
				}
				else {
					Target.teminateTarget(p);
					switch (p.getLastDamageCause().getCause()) {
						case BLOCK_EXPLOSION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser en fuyant §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case DROWNING:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a voulu goûter à l'eau en fuyant §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case FALL:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a fait le saut de l'ange en fuyant §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case FALLING_BLOCK:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est écrasé par terre en fuyant §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case FIRE:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait immoler par le feu en fuyant §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case LIGHTNING:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait foudroyer en fuyant §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case MAGIC:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait ensorceler en fuyant §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case STARVATION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort le ventre vide en fuyant §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case SUFFOCATION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a suffoqué dans un mur en fuyant §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case FIRE_TICK:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait brulée à cause d'un §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case POISON:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort par le poison en fuyant §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case LAVA:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a essayé de nager dans la lave en fuyant §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case ENTITY_ATTACK:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait tuer par un §c" + pd.getLastDamager().getName() + " §7! ");
							break;
						case PROJECTILE:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait tuer par un §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case THORNS:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait tuer par un §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case ENTITY_EXPLOSION:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser en fuyant §c" + pd.getLastDamager().getName() + " §7!");
							break;
						case SUICIDE:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7a décidé de mettre fin à sa vie en fuyant §c" + pd.getLastDamager().getName() + " §7!");
							break;
						default:
							Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort en fuyant §c" + pd.getLastDamager().getName() + " §7!");
							break;
					}
				}
			}
			else {
				Target.teminateTarget(p);
				switch (p.getLastDamageCause().getCause()) {
					case BLOCK_EXPLOSION:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser ! ");
						break;
					case DROWNING:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7a voulu goûter à l'eau ! ");
						break;
					case FALL:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7a fait le saut de l'ange ! ");
						break;
					case FALLING_BLOCK:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est écrasé par terre ! ");
						break;
					case FIRE:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait immoler par le feu ! ");
						break;
					case LIGHTNING:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait foudroyer ! ");
						break;
					case MAGIC:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait ensorceler ! ");
						break;
					case STARVATION:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort le ventre vide !");
						break;
					case SUFFOCATION:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7a suffoqué dans un mur !");
						break;
					case FIRE_TICK:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une toute petite brulure !");
						break;
					case POISON:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort par le poison !");
						break;
					case LAVA:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7a essayé de nager dans la lave !");
						break;
					case ENTITY_ATTACK:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une façon inconnue ! ");
						break;
					case PROJECTILE:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une façon inconnue !");
						break;
					case THORNS:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une façon inconnue !");
						break;
					case ENTITY_EXPLOSION:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser !");
						break;
					case SUICIDE:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7a décidé de mettre fin à sa vie !");
						break;
					default:
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort !");
						break;
				}
			}


			/*switch (p.getLastDamageCause().getCause()) {
				case BLOCK_EXPLOSION:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser ! ");
					break;
				case DROWNING:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7a voulu go§ter § l'eau ! ");
					break;
				case FALL:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7a fait le saut de l'ange ! ");
					break;
				case FALLING_BLOCK:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est §cras§ par terre ! ");
					break;
				case FIRE:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait immoler par le feu ! ");
					break;
				case LIGHTNING:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait foudroyer ! ");
					break;
				case MAGIC:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait ensorceler ! ");
					break;
				case STARVATION:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort le ventre vide !");
					break;
				case SUFFOCATION:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7a suffoqu§ dans un mur !");
					break;
				case FIRE_TICK:
					if(!Main.getInstance().isNull(pd.getLastDamager())) {
						if(pd.getLastDamager() instanceof Player) {
							Main.getInstance().showPrimes = true;
							Player attacker = (Player) pd.getLastDamager();

							if(!PlayerData.hasPlayerData(attacker)) return;
							PlayerData pd2 = PlayerData.getPlayerData(attacker);
							PlayerData.givePrime(pd, pd2);

							if(Target.hasTarget(p))
								Target.teminateTarget(p, attacker);

							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait brul§ par §c" + attacker.getName() + " §7!");
						}
						else {
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait brul§ § cause d'un §c" + pd.getLastDamager().getName() + " §7!");
						}
					}
					else {
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une toute petite brulure !");
					}
					break;
				case POISON:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort par le poison !");
					break;
				case LAVA:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7a essay§ de nager dans la lave !");
					break;
				case ENTITY_ATTACK:
					if(!Main.getInstance().isNull(pd.getLastDamager())) {
						if(pd.getLastDamager() instanceof Player) {
							Main.getInstance().showPrimes = true;
							Player attacker = (Player) pd.getLastDamager();

							if(!PlayerData.hasPlayerData(attacker)) return;
							PlayerData pd2 = PlayerData.getPlayerData(attacker);
							PlayerData.givePrime(pd, pd2);

							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait terrasser par §c" + attacker.getName() + " §7!");
						}
						else {
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait tuer par un §c" + pd.getLastDamager().getName() + " §7! ");
						}
					}
					else {
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une fa§on inconnue ! ");
					}
					break;
				case PROJECTILE:
					if(!Main.getInstance().isNull(pd.getLastDamager())) {
						if(pd.getLastDamager() instanceof Player) {
							Main.getInstance().showPrimes = true;
							Player attacker = (Player) pd.getLastDamager();

							if(!PlayerData.hasPlayerData(attacker)) return;
							PlayerData pd2 = PlayerData.getPlayerData(attacker);
							PlayerData.givePrime(pd, pd2);

							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait terrasser par §c" + attacker.getName() + " §7!");
						}
						else {
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait tuer par un §c" + pd.getLastDamager().getName() + " §7!");
						}
					}
					else {
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une fa§on inconnue !");
					}
					break;
				case THORNS:
					if(!Main.getInstance().isNull(pd.getLastDamager())) {
						if(pd.getLastDamager() instanceof Player) {
							Main.getInstance().showPrimes = true;
							Player attacker = (Player) pd.getLastDamager();

							if(!PlayerData.hasPlayerData(attacker)) return;
							PlayerData pd2 = PlayerData.getPlayerData(attacker);
							PlayerData.givePrime(pd, pd2);

							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait piquer trop fort par l'armure de §c" + attacker.getName() + " §7!");
						}
						else {
							Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait tuer par un §c" + pd.getLastDamager().getName() + " §7!");
						}
					}
					else {
						Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort d'une fa§on inconnue !");
					}
					break;
				case ENTITY_EXPLOSION:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7s'est fait exploser !");
					break;
				case SUICIDE:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7a d§cid§ de mettre fin § sa vie !");
					break;
				default:
					Bukkit.broadcastMessage("§c" + p.getName() + " §7est mort !");
					break;
			}*/

			for(Player lp: Bukkit.getOnlinePlayers()) lp.playSound(lp.getLocation(), "wano.death", 10, 1);


			p.spigot().respawn();
			PlayerData.toggleDeath(p, true);
			p.teleport(loc);

			pd.deathLocation = loc;

			GameTask.checkEndFight();



			// Emperor die and has fid§le

			if(pd.getPersonnage().getType() == 1) {
				for(PlayerData pd1: PlayerData.getAlivePlayers()) {
					if(pd1.getPersonnage().getId() == pd.getPersonnage().getId() + 100) {
						pd1.setMaxHealth(pd1.getMaxHealth() - 4);
						Personnage pers = null;
						try {
							pers = pd1.getPersonnage().getClass().newInstance();
							pd1.getPersonnage().setType(pers.getType());
							pd1.getPersonnage().setId(pers.getId());
							pd1.getPersonnage().getPrefixData().clear();
							for(PrefixData prfd : pers.getPrefixData()) {
								pd1.getPersonnage().getPrefixData().add(prfd);
							}
						} catch (InstantiationException | IllegalAccessException e1) {
							e1.printStackTrace();
						}
						pd1.getPlayer().sendMessage("§7Votre empereur est mort");
						pd1.getPlayer().sendMessage("§7Vous êtes désormais un pirate");
					}
				}
			}
		}
	}
}
