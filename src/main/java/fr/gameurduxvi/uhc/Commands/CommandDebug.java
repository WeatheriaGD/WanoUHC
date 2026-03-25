package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.scenarios.target.Target;

public class CommandDebug implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		CommandSender p = sender;
		if(p.getName().equalsIgnoreCase("gameurduxvi") || p.getName().equalsIgnoreCase("suuuucre") || p.getName().equalsIgnoreCase("G0ldyS") || p.getName().equalsIgnoreCase("Ora_Kun") || p.getName().equalsIgnoreCase("console")) {
			if(args.length >=1) {
				String arg1 = Main.getInstance().argument(args, 1);
				if(arg1.equalsIgnoreCase("list")) {
					if(Main.getInstance().getPlayersData().size() > 0) {
						p.sendMessage("§7Debug §8>> §3Liste des joueurs:");
						for(PlayerData pd: Main.getInstance().getPlayersData()) {
							p.sendMessage("            §3--> §b" + pd.getPlayer().getName() + " §3(§b" + pd.getPersonnage().getName() + "§3)");
						}
					}
					else {
						if(Bukkit.getOnlinePlayers().size() > 0) {
							p.sendMessage("§7Debug §8>> §cUne erreur est survenu quel que part");
						}
						else {
							p.sendMessage("§7Debug §8>> §6Aucun joueur n'est en ligne");
						}
					}
				}
				else if(arg1.equalsIgnoreCase("offline")) {
					if(Main.getInstance().getOfflinePlayersData().size() > 0) {
						p.sendMessage("§7Debug §8>> §3Liste des joueurs enregistré dans la cache:");
						for(PlayerData pd: Main.getInstance().getOfflinePlayersData()) {
							p.sendMessage("§3--> §b" + pd.getPlayer().getName());
						}
					}
					else {
						p.sendMessage("§7Debug §8>> §6Aucun joueur n'est enregistré dans la cache");
					}
				}
				else if(arg1.equalsIgnoreCase("teams")) {
					if(Main.getInstance().getTeamData().size() > 0) {
						p.sendMessage("§7Debug §8>> §3Liste des batteaux enregistré dans la cache:");
						int i = 0;
						for(TeamData sd: Main.getInstance().getTeamData()) {
							i++;
							p.sendMessage("§3--> " + i + "§b(" + sd.getTeamSuffix() + "§b)");
							if(sd.getShipLocation() != null) p.sendMessage("§3   > Ship Location: §b" + sd.getShipLocation().getX() + " " + sd.getShipLocation().getY() + " " + sd.getShipLocation().getZ());
							p.sendMessage("§3   > Linked id: §b" + sd.getLinkedId());
							p.sendMessage("§3   > Max: §b" + sd.getMax());
							p.sendMessage("§3   > Players: §b");
							if(sd.getPlayers().size() > 0) {
								for(Player lp: sd.getPlayers()) {
									p.sendMessage("§3   > §b" + lp.getName());
								}
							}
						}
					}
					else {
						p.sendMessage("§7Debug §8>> §6Aucun batteau n'est enregistré dans la cache");
					}
				}
				else {
					boolean found = false;
					for(Player lp: Bukkit.getOnlinePlayers()) {
						if(lp.getName().toLowerCase().startsWith(arg1.toLowerCase())) {
							found = true;

							PlayerData pd = PlayerData.getPlayerData(lp);
							if(args.length >=2) {
								String arg2 = Main.getInstance().argument(args, 2);
								if(arg2.equalsIgnoreCase("info")) {
									p.sendMessage("§3========================");
									p.sendMessage("§3Joueur: §b" + pd.getPlayer().getName());
									p.sendMessage("§3Personnage: §b" + pd.getPersonnage().getName());
									p.sendMessage("§3Kills: §b" + pd.getKills());
									p.sendMessage("§3Prime: §b" + pd.getPrime());
									p.sendMessage("§3Max Health: §b" + pd.getMaxHealth());
									p.sendMessage("§3Malus Health: §b" + pd.getMalusHealth());
									p.sendMessage("§3Bonus Health: §b" + pd.getTopBonusHealth());
									p.sendMessage("§3Latescatter: §b" + pd.lateScatter);
									p.sendMessage("§3Spawn Location: §b" + pd.spawnLocation.getBlockX() + " " + pd.spawnLocation.getBlockY() + " " + pd.spawnLocation.getBlockZ());
									p.sendMessage("§3Is death: §b" + pd.isDeath);
									p.sendMessage("§3Is in spectator: §b" + pd.isSpec);
									p.sendMessage("§3Can ultimate: §b" + pd.canUltimate());
									p.sendMessage("§3Has team: §b" + !Main.getInstance().isNull(pd.getTeam()));
									p.sendMessage("§3========================");
									return false;
								}
								else if(arg2.equalsIgnoreCase("pers") || arg2.equalsIgnoreCase("personnage")) {
									p.sendMessage("§3========================");
									p.sendMessage("§3Joueur: §b" + pd.getPlayer().getName());
									p.sendMessage("§3Id: §b" + pd.getPersonnage().getId());
									p.sendMessage("§3Personnage: §b" + pd.getPersonnage().getName());
									p.sendMessage("§3Prefix de fid§le: §b" + pd.getPersonnage().getFidelePrefix());
									p.sendMessage("§3Prime: §b" + pd.getPersonnage().getPrime());
									p.sendMessage("§3Priority: §b" + pd.getPersonnage().getPriority());
									p.sendMessage("§3Amount: §b" + pd.getPersonnage().getAmount());
									p.sendMessage("§3Type: §b" + pd.getPersonnage().getType());
									p.sendMessage("§3Description: §b" + pd.getPersonnage().getDescription());
									p.sendMessage("§3========================");
									return false;
								}
								else if(arg2.equalsIgnoreCase("team")) {
									if(!Main.getInstance().isNull(pd.getTeam())) {
										p.sendMessage("§3========================");
										p.sendMessage("§3Joueur: §b" + pd.getPlayer().getName());
										p.sendMessage("§3Linekd id: §b" + pd.getTeam().getLinkedId());
										p.sendMessage("§3Max: §b" + pd.getTeam().getMax());
										p.sendMessage("§3Location: §b" + pd.getTeam().getShipLocation().getX() + " " + pd.getTeam().getShipLocation().getY() + " " + pd.getTeam().getShipLocation().getZ());
										p.sendMessage("§3Players in ship: §b");
										for(Player lp2: pd.getTeam().getPlayers()) {
											p.sendMessage(" §3>§b " + lp2.getName());
										}
										p.sendMessage("§3========================");
									}
									else {
										p.sendMessage("§7Debug §8>> §cCe joueur n'est pas attribué à une team");
									}
									return false;
								}
								else if(arg2.equalsIgnoreCase("target")) {
									if(args.length >= 3) {
										String arg3 = Main.getInstance().argument(args, 3);
										if(arg3.equalsIgnoreCase("clear")) {
											Target.targets.remove(lp);
										}
										else {
											p.sendMessage("§7Debug §8>> §3/debug target §bclear");
										}
									}
									else {
										if(Target.hasTarget(lp)) {
											p.sendMessage("§3========================");
											p.sendMessage("§3Target: " + Target.getTarget(lp).getName());
											p.sendMessage("§3========================");
										}
										else {
											p.sendMessage("§3========================");
											p.sendMessage("§3Target: Aucune");
											p.sendMessage("§3========================");
										}
									}
									return false;
								}
							}
							p.sendMessage("§3Commandes du /debug:");
							p.sendMessage("§3/debug " + arg1 + " §bpersonnage");
							p.sendMessage("§3/debug " + arg1 + " §binfo");
							p.sendMessage("§3/debug " + arg1 + " §bteam");
							p.sendMessage("§3/debug " + arg1 + " §btarget");
						}
					}
					if(!found) {
						p.sendMessage("§7Debug §8>> §c\"" + arg1 + "\" n'est pas en ligne");
					}
				}

			}
			else {
				p.sendMessage("§7Debug §8>> §3/debug §blist");
				p.sendMessage("§7Debug §8>> §3/debug §boffline");
				p.sendMessage("§7Debug §8>> §3/debug §bteams");
				p.sendMessage("§7Debug §8>> §3/debug §b<player>");
			}
		}
		else {
			p.sendMessage("§7Debug §8>> §cVous n'avez pas la permission");
		}
		return false;
	}

}
