package fr.gameurduxvi.uhc.GameEvents;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Commands.CommandInfo;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class FideleDistributionGameEvent extends GameEvent {
	
	public FideleDistributionGameEvent() {
		setName("Distribution des Fidèles");
		setDescription("");
		setMinute(25);
		setSecond(0);
		setInScoreboard(true);
		setScoreboardText("§7Fidèles : §f%m%§7:§f%s%");
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void run() {
		CommandInfo.broadcast("§fLes empereurs ont désormais leur fidèles");
		
		/*ArrayList<Personnage> empereurs = new ArrayList<>();				
		ArrayList<Personnage> persList = (ArrayList<Personnage>) Main.getInstance().getPersonages().clone();
		Collections.shuffle(persList); 
		for(Personnage pers : persList) {
			if(pers.getType() == 1 && pers.getAmount() > 0) {
				empereurs.add(pers);
			}
		}*/
		
		/*******************************************************
		 * Creating 1 "Fid�le" for each Emperor
		 *******************************************************/	
		/*ArrayList<PlayerData> randomPlayers = (ArrayList<PlayerData>) PlayerData.getAlivePlayers().clone();
		Collections.shuffle(randomPlayers);
		for(Personnage pers: empereurs) {
			for(PlayerData pd: randomPlayers) {
				if(pd.getPersonnage().getType() == 0) {
					int[] empereursId = new int[empereurs.size() + 1];
					int i = 0;
					for(Personnage pers2: empereurs) {
						empereursId[i] = pers2.getId();
						i++;
					}
					empereursId[i] = pers.getId() + 100;
					pd.getPersonnage().setId(pers.getId() + 100);
					pd.getPersonnage().setType(2);
					pd.getPersonnage().setName(pd.getPersonnage().getName() + " (" + "Fid�le de " + pers.getName() + ")");
					pd.getPersonnage().getPrefixData().clear();
					pd.getPersonnage().getPrefixData().add(new PrefixData("�7Pirate ", "", new int[]{-1}));
					pd.getPersonnage().getPrefixData().add(new PrefixData(pers.getFidelePrefix(), "", empereursId));
					break;
				}
			}
		}
		for(PlayerData pd: PlayerData.getAlivePlayers()) {			
			if(pd.getPersonnage().getType() == 1) {
				for(PlayerData pd1: PlayerData.getAlivePlayers()) {
					if(pd1.getPersonnage().getId() == pd.getPersonnage().getId() + 100) {
						pd.getPlayer().sendTitle("�b" + pd1.getPlayer().getName(), "�7est votre fid�le");
						pd.getPlayer().sendMessage("�7Votre fid�le est �b" + pd1.getPlayer().getName());
						pd1.getPlayer().sendTitle("�b" + pd.getPlayer().getName(), "�7est votre empereur");
						pd1.getPlayer().sendMessage("�7Votre empereur est �b" + pd.getPlayer().getName());
					}
				}
			}
		}*/
		

		ArrayList<PlayerData> players = new ArrayList<>();
		
		for(PlayerData pd: PlayerData.getAlivePlayers()) players.add(pd);
		for(PlayerData pd: Main.getInstance().getOfflinePlayersData()) players.add(pd);
		
		// Emperors
		for(PlayerData pd: players) {
			ArrayList<Personnage> empereurs = new ArrayList<>();
			ArrayList<Personnage> persList = (ArrayList<Personnage>) Main.getInstance().getPersonages().clone();
			Collections.shuffle(persList); 
			for(Personnage pers : persList) {
				if(pers.getType() == 1 && pers.getAmount() > 0) {
					empereurs.add(pers);
				}
			}
			
			ArrayList<PlayerData> randomPlayers = (ArrayList<PlayerData>) players.clone();
			Collections.shuffle(randomPlayers); 
			for(PlayerData pd2: randomPlayers) {
				if(pd2.getPersonnage().getType() == 1) {
					if(!PlayerData.hasFidèle(pd2) && pd.getPersonnage().getType() == 0) {
						int[] empereursId = new int[empereurs.size() + 1];
						int i = 0;
						for(Personnage pers2: empereurs) {
							empereursId[i] = pers2.getId();
							i++;
						}
						Personnage pers = pd2.getPersonnage();
						empereursId[i] = pers.getId() + 100;
						pd.getPersonnage().setId(pers.getId() + 100);
						pd.getPersonnage().setType(2);
						pd.getPersonnage().setName(pd.getPersonnage().getName() + " (" + "Fidèle de " + pers.getName() + ")");
						pd.getPersonnage().getPrefixData().clear();
						pd.getPersonnage().getPrefixData().add(new PrefixData("§7Pirate ", "", new int[]{-1}));
						pd.getPersonnage().getPrefixData().add(new PrefixData(pers.getFidelePrefix(), "", empereursId));
						pd.setPrime(Main.getInstance().primeFidele);
						Scoreboard board = pd.getPlayer().getScoreboard();
						for(Team team: board.getTeams()) {
							team.unregister();
						}
						pd.getPlayer().setScoreboard(board);
						
						if(pd.getPersonnage().getId() == pd2.getPersonnage().getId() + 100) {
							pd2.getPlayer().sendTitle("§b" + pd.getPlayer().getName(), "§7est votre fidèle");
							pd2.getPlayer().sendMessage("§7Votre fidèle est §b" + pd.getPlayer().getName());
							pd.getPlayer().sendTitle("§b" + pd2.getPlayer().getName(), "§7est votre empereur");
							pd.getPlayer().sendMessage("§7Votre empereur est §b" + pd2.getPlayer().getName());
						}
						break;
					}
				}
			}
		}
		
		//GameTask.checkEndFight();
	}

	/*@SuppressWarnings({ "unchecked", "deprecation" })
	public static void attributeLateFid�le(PlayerData pd) {
		ArrayList<Personnage> empereurs = new ArrayList<>();
		ArrayList<Personnage> persList = (ArrayList<Personnage>) Main.getInstance().getPersonages().clone();
		Collections.shuffle(persList); 
		for(Personnage pers : persList) {
			if(pers.getType() == 1 && pers.getAmount() > 0) {
				empereurs.add(pers);
			}
		}
		
		for(PlayerData pd2: PlayerData.getAlivePlayers()) {
			if(pd2.getPersonnage().getType() == 1) {
				if(!PlayerData.hasFid�le(pd2.getPlayer()) && pd.getPersonnage().getType() == 0) {
					int[] empereursId = new int[empereurs.size() + 1];
					int i = 0;
					for(Personnage pers2: empereurs) {
						empereursId[i] = pers2.getId();
						i++;
					}
					Personnage pers = pd2.getPersonnage();
					empereursId[i] = pers.getId() + 100;
					pd.getPersonnage().setId(pers.getId() + 100);
					pd.getPersonnage().setType(2);
					pd.getPersonnage().setName(pd.getPersonnage().getName() + " (" + "Fid�le de " + pers.getName() + ")");
					pd.getPersonnage().getPrefixData().clear();
					pd.getPersonnage().getPrefixData().add(new PrefixData("�7Pirate ", "", new int[]{-1}));
					pd.getPersonnage().getPrefixData().add(new PrefixData(pers.getFidelePrefix(), "", empereursId));
					Scoreboard board = pd.getPlayer().getScoreboard();
					for(Team team: board.getTeams()) {
						team.unregister();
					}
					pd.getPlayer().setScoreboard(board);
					
					if(pd.getPersonnage().getId() == pd2.getPersonnage().getId() + 100) {
						pd2.getPlayer().sendTitle("�b" + pd.getPlayer().getName(), "�7est votre fid�le");
						pd2.getPlayer().sendMessage("�7Votre fid�le est �b" + pd.getPlayer().getName());
						pd.getPlayer().sendTitle("�b" + pd2.getPlayer().getName(), "�7est votre empereur");
						pd.getPlayer().sendMessage("�7Votre empereur est �b" + pd2.getPlayer().getName());
					}
				}
			}
		}		
	}*/
}
