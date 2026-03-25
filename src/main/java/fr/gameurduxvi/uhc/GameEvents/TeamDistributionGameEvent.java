package fr.gameurduxvi.uhc.GameEvents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.Tasks.ShipsTask;

public class TeamDistributionGameEvent extends GameEvent {
	
	public static Map<PlayerData, TeamData> preTeamList = new HashMap<PlayerData, TeamData>();
	
	public TeamDistributionGameEvent() {
		setName("Distribution des Teams");
		setDescription("Les bateaux sont atribué a ce moment");
		setMinute(25);
		setSecond(1);
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public void run() {
		
		/*for(TeamData td: Main.getInstance().getTeamData()) {
			if(td.getLinkedId() != 0) {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					if(td.getLinkedId() == pd.getPersonnage().getId()) {
						pd.setTeam(td);
						td.getPlayers().add(pd.getPlayer());
					}
				}
			}
		}
		for(TeamData td: Main.getInstance().getTeamData()) {
			if(td.getLinkedId() == 0) {
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					if(pd.getPersonnage().getType() == 2 && Main.getInstance().isNull(pd.getTeam())) {
						pd.setTeam(td);
						td.getPlayers().add(pd.getPlayer());
						break;
					}					
				}
				for(PlayerData pd: PlayerData.getAlivePlayers()) {
					if(Main.getInstance().isNull(pd.getTeam()) && pd.getPersonnage().getType() != 2 && td.getPlayers().size() < td.getMax()) {
						pd.setTeam(td);
						td.getPlayers().add(pd.getPlayer());
					}
				}
			}
		}*/
		
		// Emperors
		/*for(PlayerData pd: PlayerData.getAlivePlayers()) {
			attributeLateTeam(pd, false);
		}*/
		
		String[] suffixes = {"a", "b", "c", "d", "e", "1", "2", "3", "4", "5", "6", "7", "8", "9", "f"};
		
		List<String> intList = Arrays.asList(suffixes);

		Collections.shuffle(intList);

		intList.toArray(suffixes);
		int i = 0;
		for(TeamData td: Main.getInstance().getTeamData()) {
			if(td.getLinkedId() == 0) {
				td.setTeamSuffix("§" + suffixes[i] + "⬛");				
			}
			i++;
		}
		
		ArrayList<PlayerData> players = new ArrayList<>();
		
		for(PlayerData pd: PlayerData.getAlivePlayers()) players.add(pd);
		for(PlayerData pd: Main.getInstance().getOfflinePlayersData()) players.add(pd);
		
		// Emperors
		for(PlayerData pd: players) {
			if(pd.getPersonnage().getType() == 1) {
				for(TeamData td: Main.getInstance().getTeamData()) {
					if(td.getLinkedId() != 0 && td.getLinkedId() == pd.getPersonnage().getId()) {
						pd.setTeam(td);
					}
				}
			}
		}
		
		// Fideles
		for(PlayerData pd: players) {
			if(pd.getPersonnage().getType() == 2) {
				for(TeamData td: Main.getInstance().getTeamData()) {
					if(!td.hasFidele() && td.getLinkedId() == 0) {
						pd.setTeam(td);
					}
				}
			}
		}
		
		// Pirates without double times roles
		for(PlayerData pd: players) {
			if(pd.getPersonnage().getType() != 1 && pd.getPersonnage().getType() != 2) {
				for(TeamData td: Main.getInstance().getTeamData()) {
					boolean isRoleInTeam = false;
					
					for(Player lp: td.getPlayers()) {
						PlayerData pd2 = PlayerData.getPlayerData(lp);
						if(pd2.getPersonnage().getClass().equals(pd.getPersonnage().getClass())) {
							isRoleInTeam = true;
						}
					}
					
					if(td.getLinkedId() == 0 && td.getPlayers().size() < td.getMax() && !isRoleInTeam) {
						pd.setTeam(td);
					}
				}
			}
		}
		
		// Remaining Pirates
		for(PlayerData pd: players) {
			if(pd.getTeam() == null && pd.getPersonnage().getType() != 1 && pd.getPersonnage().getType() != 2) {
				for(TeamData td: Main.getInstance().getTeamData()) {
					if(td.getLinkedId() == 0 && td.getPlayers().size() < td.getMax()) {
						pd.setTeam(td);
					}
				}
			}
		}
		
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new ShipsTask(), 20, 20);
	}

	//public static void attributeLateTeam(PlayerData pd) {
		/*
		// Emperors
		for(TeamData td: Main.getInstance().getTeamData()) {
			if(td.getLinkedId() != 0) {
				if(td.getLinkedId() == pd.getPersonnage().getId()) {
					pd.setTeam(td);
					return;
				}
			}
		}
		
		// Pirates & Fidèles
		for(TeamData td: Main.getInstance().getTeamData()) {
			if(td.getLinkedId() == 0) {
				boolean isFidele = pd.getPersonnage().getType() == 2;
				boolean hasFidele = false;
				for(Player lp: td.getPlayers()) {
					if(PlayerData.hasPlayerData(lp) && PlayerData.getPlayerData(lp).getPersonnage().getType() == 2) {
						hasFidele = true;
					}
				}
				if(Main.getInstance().isNull(pd.getTeam()) && td.getPlayers().size() < td.getMax()) {
					if(!hasFidele && isFidele) {
						pd.setTeam(td);
						return;
					}
					else if(hasFidele && !isFidele) {
						pd.setTeam(td);
						return;
					}
					else {
						continue;
					}
				}
			}
		}*/
	//}
}
