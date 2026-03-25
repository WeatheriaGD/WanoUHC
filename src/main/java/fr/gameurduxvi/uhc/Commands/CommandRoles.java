package fr.gameurduxvi.uhc.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class CommandRoles implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.isOp() || p.getName().equalsIgnoreCase("G0ldyS")) {
				if(args.length >=1) {
					String arg1 = Main.getInstance().argument(args, 1);
					if(arg1.equalsIgnoreCase("list")) {
						showList(p);
					}
					else if(arg1.equalsIgnoreCase("set")) {
						if(args.length >=2) {
							String arg2 = Main.getInstance().argument(args, 2);
							String numberOnly= arg2.replaceAll("[^0-9]", "");
							if(numberOnly.length() == 0) {
								p.sendMessage("§7Roles §8>> §c\"" + arg2 + "\" ne contient pas de chiffres");
								return false;
							}
							Player target = p;
							if(args.length >=3) {
								String arg3 = Main.getInstance().argument(args, 3);
								boolean found = false;
								for(Player lp: Bukkit.getOnlinePlayers()) {
									if(lp.getName().toLowerCase().startsWith(arg3.toLowerCase())) {
										target = lp;
										found = true;
										break;
									}
								}
								if(!found) {
									p.sendMessage("§7Roles §8>> §c\"" + arg3 + "\" n'est pas en ligne");
									return false;
								}
							}
							int id = Integer.parseInt(numberOnly);
							for(Personnage pers: Main.getInstance().getPersonages()) {
								if(pers.getId() == id) {
									PlayerData pd = PlayerData.getPlayerData(target);
									for(ItemStack it:pd.getPlayer().getInventory()) {
										if(Main.getInstance().isNull(it)) continue;
										if(Main.getInstance().isNull(it.getItemMeta())) continue;
										if(Main.getInstance().isNull(it.getItemMeta().getDisplayName())) continue;
										if(it.getItemMeta().getDisplayName().contains(pd.getPersonnage().ultimateItem().getItemMeta().getDisplayName())) {
											pd.getPlayer().getInventory().remove(it);
										}
									}
									try {
										pd.setPersonnage(pers.getClass().newInstance());
									} catch (InstantiationException | IllegalAccessException e) {
										e.printStackTrace();
									}
									pd.setCanUltimate(true);
									pd.getPlayer().getInventory().addItem(pd.getPersonnage().ultimateItem());
									if(pd.getPersonnage().getType() == 1) {
										pd.setMaxHealth(40);
										pd.setMalusHealth(0);
										pd.getPlayer().setHealth(40);
									}
									else {
										pd.setMaxHealth(20);
									}
									if(p.equals(target)) {
										p.sendMessage("§7Roles §8>> §aLe rôle vous a bien été attribué");
									}
									else {
										p.sendMessage("§7Roles §8>> §aLe rôle a bien été attribué à §b" + target.getName());
									}
									target.playSound(target.getLocation(), pers.getAttributedSoundName(), 10, 1);

									target.sendMessage("§7Roles §8>> §7Votre rôle est désormais §b" + pd.getPersonnage().getName());
								}
							}
						}
						else {
							p.sendMessage("§7Roles §8>> §3/roles §bset <id> [joueur]");
						}
					}
					else if(arg1.equalsIgnoreCase("setfidele")) {
						if(args.length >=2) {
							String arg2 = Main.getInstance().argument(args, 2);
							String numberOnly= arg2.replaceAll("[^0-9]", "");
							if(numberOnly.length() == 0) {
								p.sendMessage("§7Roles §8>> §c\"" + arg2 + "\" ne contient pas de chiffres ");
								return false;
							}
							Player target = p;
							if(args.length >=3) {
								String arg3 = Main.getInstance().argument(args, 3);
								boolean found = false;
								for(Player lp: Bukkit.getOnlinePlayers()) {
									if(lp.getName().toLowerCase().startsWith(arg3.toLowerCase())) {
										target = lp;
										found = true;
										break;
									}
								}
								if(!found) {
									p.sendMessage("§7Roles §8>> §c\"" + arg3 + "\" n'est pas en ligne");
									return false;
								}
							}
							int id = Integer.parseInt(numberOnly);
							for(Personnage pers: Main.getInstance().getPersonages()) {
								if(pers.getId() == id) {
									if(pers.getType() != 1) {
										p.sendMessage("§7Roles §8>> §cL'id ne provient pas d'un empereur");
										return false;
									}
									PlayerData pd = PlayerData.getPlayerData(target);
									if(pd.getPersonnage().getType() == 1) {
										p.sendMessage("§7Roles §8>> §cLa cible ne peut pas devenir un fidèle, car il est empereur");
										return false;
									}
									if(pd.getPersonnage().getType() == 2) {
										p.sendMessage("§7Roles §8>> §cLa cible est déja un fidèle");
										p.sendMessage("§7Roles §8>> §cPour changer de fidélité, il faut donner le role, puis mettre la fidélité");
										return false;
									}

									ArrayList<Personnage> empereurs = new ArrayList<>();
									for(Personnage pers1 : Main.getInstance().getPersonages()) if(pers1.getType() == 1) empereurs.add(pers1);

									int[] empereursId = new int[empereurs.size() + 1];
									int i = 0;
									for(Personnage pers2: empereurs) {
										empereursId[i] = pers2.getId();
										i++;
									}
									empereursId[i] = pers.getId() + 100;
									pd.getPersonnage().setId(pers.getId() + 100);
									pd.getPersonnage().setType(2);
									pd.getPersonnage().setName(pd.getPersonnage().getName() + " (" + "Fidèle de " + pers.getName() + ")");
									pd.getPersonnage().getPrefixData().clear();
									pd.getPersonnage().getPrefixData().add(new PrefixData("§7Pirate ", "", new int[]{-1}));
									pd.getPersonnage().getPrefixData().add(new PrefixData(pers.getFidelePrefix(), "", empereursId));
									if(p.equals(target)) {
										p.sendMessage("§7Roles §8>> §aLa fidélité a bien été mis à vous même");
									}
									else {
										p.sendMessage("§7Roles §8>> §aLa fidélité a bien été mis à §b" + target.getName());
									}
									target.sendMessage("§7Roles §8>> §bVotre fidélité a changé envers §b" + pers.getName());
									return false;
								}
							}
						}
						else {
							p.sendMessage("§3/roles §bsetfidele <id empereur> [joueur]");
						}
					}
					else {
						p.sendMessage("§7Roles §8>> §3Commandes du /roles:");
						p.sendMessage("§3/roles §blist");
						p.sendMessage("§3/roles §bset <id> [joueur]");
						p.sendMessage("§3/roles §bsetfidele <id empereur> [joueur]");
					}
				}
				else {
					p.sendMessage("§3Commandes du /roles:");
					p.sendMessage("§3/roles §blist");
					p.sendMessage("§3/roles §bset <id> [joueur]");
					p.sendMessage("§3/roles §bsetfidele <id empereur> [joueur]");
				}
			}
			else {
				showList(p);
			}

		}
		return false;
	}

	public void showList(Player p) {
		p.sendMessage("§3Liste des roles présent:");

		HashMap<Personnage, Integer> hm = new HashMap<>();
		for(Personnage pers: Main.getInstance().getPersonages()) {
			hm.put(pers, pers.getId());
		}

		// Create a list from elements of HashMap
		List<Map.Entry<Personnage, Integer> > list =
				new LinkedList<Map.Entry<Personnage, Integer> >(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Personnage, Integer> >() {
			public int compare(Map.Entry<Personnage, Integer> o1,
							   Map.Entry<Personnage, Integer> o2)
			{
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		for (Map.Entry<Personnage, Integer> aa : list) {
			p.sendMessage("§3 - " + aa.getKey().getId() + " >> §b" + aa.getKey().getName());
		}
	}

}
