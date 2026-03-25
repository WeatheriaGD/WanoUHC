package fr.gameurduxvi.uhc.GameEvents;

import java.util.ArrayList;
import java.util.Collections;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Commands.CommandInfo;
import fr.gameurduxvi.uhc.Personnages.Empty;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class RoleDistributionGameEvent extends GameEvent {
	
	public RoleDistributionGameEvent() {
		setName("Distribution des Roles");
		setDescription("");
		setMinute(20);
		setSecond(0);
		setInScoreboard(true);
		setScoreboardText("§7Roles : §f%m%§7:§f%s%");
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void run() {
		CommandInfo.broadcast("§fLes roles ont été attribués à chaque joueur");
		
		/*******************************************************
		 * Roles Distribution
		 *******************************************************/		
		ArrayList<PlayerData> players = new ArrayList<>();
		
		for(PlayerData pd: PlayerData.getAlivePlayers()) players.add(pd);
		for(PlayerData pd: Main.getInstance().getOfflinePlayersData()) players.add(pd);
		
		Collections.shuffle(players); 

		for(PlayerData pd: players) {
			ArrayList<Personnage> persList = (ArrayList<Personnage>) Main.getInstance().getPersonages().clone();
			Collections.shuffle(persList); 
			for(Personnage pers : persList) {
				int max = pers.getAmount();
				int current = 0;
				for(PlayerData pd2: players) {
					if(pd2.getPersonnage().getClass().equals(pers.getClass())) {
						current++;
					}
				}
				if(pd.getPersonnage() instanceof Empty) {
					if(current >= max) continue;
					try {
						pd.setPersonnage(pers.getClass().newInstance());
						current++;
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
			pd.getPlayer().sendTitle("§7Vous êtes désormais", "§b" + pd.getPersonnage().getName());
			pd.getPlayer().sendMessage("§7Ton role est §b" + pd.getPersonnage().getName());
			pd.getPlayer().sendMessage("§6Jette ton item pour plus d'infos");
			
			pd.getPlayer().playSound(pd.getPlayer().getLocation(), pd.getPersonnage().getAttributedSoundName(), 10, 1);
			
			pd.getPlayer().sendMessage("§6" + pd.getPersonnage().getDescription());
			
			if(!Main.getInstance().isNull(pd.getPersonnage().ultimateItem())) {
				if(pd.getPlayer().getInventory().firstEmpty() < 0) {
					pd.getPlayer().getWorld().dropItem(pd.getPlayer().getLocation(), pd.getPlayer().getInventory().getItem(8));
				}
				else {
					pd.getPlayer().getInventory().setItem(pd.getPlayer().getInventory().firstEmpty(), pd.getPlayer().getInventory().getItem(8));
				}					
				pd.getPlayer().getInventory().setItem(8, pd.getPersonnage().ultimateItem());
			}		
			
			if(pd.getPersonnage().getType() == 1) {
				pd.setMaxHealth(40);
				if(pd.getPlayer().getHealth() <= 20) {
					pd.getPlayer().setHealth(pd.getPlayer().getHealth() + 20);				
				}
				pd.setPrime(Main.getInstance().primeEmpereur);
			}
			else {
				pd.setPrime(Main.getInstance().primePirate);
				pd.setMaxHealth(20);
			}
		}
	}
}
