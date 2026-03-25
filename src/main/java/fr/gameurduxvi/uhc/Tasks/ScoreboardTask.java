package fr.gameurduxvi.uhc.Tasks;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Commands.CommandInfo;
import fr.gameurduxvi.uhc.Personnages.Empty;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class ScoreboardTask extends BukkitRunnable{

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(!Main.GAMESTATE.equals(GameState.Idle)) {
			/*******************************************************
			 * Time
			 *******************************************************/
			if(!Main.GAMESTATE.equals(GameState.Ended) && !Main.GAMESTATE.equals(GameState.Paused) && StartTask.canStart) {
				Main.getInstance().secondes++;
				if(Main.getInstance().secondes >= 60) {
					Main.getInstance().secondes = 0;
					Main.getInstance().minutes++;
				}
			}
			Main.getInstance().episode = (short) (1 + Math.floor(Main.getInstance().minutes / 20));
			
			if((Main.getInstance().minutes % 20) == 19 && 
					(Main.getInstance().secondes == 30 
					|| Main.getInstance().secondes == 55 
					|| Main.getInstance().secondes == 56
					|| Main.getInstance().secondes == 57
					|| Main.getInstance().secondes == 58
					|| Main.getInstance().secondes == 59)) {
				CommandInfo.broadcast("Fin de l'épisode " + Main.getInstance().episode + " dans " + (60 - Main.getInstance().secondes) + " secondes");
			}
			else if(Main.getInstance().episode != 1 && (Main.getInstance().minutes % 20) == 0 && Main.getInstance().secondes == 0) {
				CommandInfo.broadcast("Fin de l'épisode " + (Main.getInstance().episode - 1));
			}
			else if(Main.getInstance().episode != 1 && (Main.getInstance().minutes % 20) == 0 && Main.getInstance().secondes == 1) {
				CommandInfo.broadcast("Début de l'épisode " + Main.getInstance().episode);
			}

			
			
			
			/*******************************************************
			 * Game Events
			 *******************************************************/
			if(!Main.GAMESTATE.equals(GameState.Ended) && StartTask.canStart) {
				for(GameEvent ge: Main.getInstance().getGameEvents()) {
					Main.getInstance().nextEvent = "";
					if(ge.canBeInScoreboard()) {
						Main.getInstance().episode = (short) (1 + Math.floor(Main.getInstance().minutes / 20));
						int currentTime = (Main.getInstance().minutes * 60) + Main.getInstance().secondes;
						int time = (ge.getMinute() * 60) + ge.getSecond();
						
						if(currentTime > time) continue;
						time = time - currentTime;
						
						if(time == 0) {
							continue;
						}
						
						int diffMinute = (int) Math.floor(time / 60);
						int diffSecond = time - (diffMinute * 60);
						
						Main.getInstance().nextEvent = ge.getScoreboardText();
						if(diffMinute < 10) {
							Main.getInstance().nextEvent = Main.getInstance().nextEvent.replace("%m%", "0" + diffMinute);
						}
						else {
							Main.getInstance().nextEvent = Main.getInstance().nextEvent.replace("%m%", "" + diffMinute);
						}
						
						if(diffSecond < 10) {
							Main.getInstance().nextEvent = Main.getInstance().nextEvent.replace("%s%", "0" + diffSecond);
						}
						else {
							Main.getInstance().nextEvent = Main.getInstance().nextEvent.replace("%s%", "" + diffSecond);
						}
						if(Main.getInstance().nextEvent.length() > 36) {
							Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName + " §7Game Event Manager §8>> §cLe message est plus long de 40 charactères: \"" + Main.getInstance().nextEvent + "\"");
							Main.getInstance().nextEvent = Main.getInstance().nextEvent.substring(0, 36);
						}
						break;
					}
				}
				for(GameEvent ge: Main.getInstance().getGameEvents()) {
					if(Main.getInstance().minutes == ge.getMinute() && Main.getInstance().secondes == ge.getSecond()) {
						ge.run();
					}
				}
			}
			if(Main.getInstance().minutes == 0 && Main.getInstance().secondes < 21) {
				Main.getInstance().nextEvent = "§7Evènements...";
			}
			
			
			
			/*******************************************************
			 * Scoreboard
			 *******************************************************/
			String secAffichage = "" + Main.getInstance().secondes;
		    if(Main.getInstance().secondes < 10) secAffichage = "0" + Main.getInstance().secondes;
		    
		    String minAffichage = "" + Main.getInstance().minutes;
		    if(Main.getInstance().minutes < 10) minAffichage = "0" + Main.getInstance().minutes;
		    
		    for(PlayerData pd: Main.getInstance().getPlayersData()) {
				if(pd.getPlayer().getScoreboard().equals(Main.getInstance().manager.getMainScoreboard())) pd.getPlayer().setScoreboard(Main.getInstance().manager.getNewScoreboard());
				
				Scoreboard board = pd.getPlayer().getScoreboard();
				boolean notExist = board.getObjective(pd.getPlayer().getName()) == null;
			    Objective objective = board.getObjective(pd.getPlayer().getName()) == null ? board.registerNewObjective(pd.getPlayer().getName(), "dummy") : board.getObjective(pd.getPlayer().getName());
			    
			    if(notExist) objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			    
			    try {
			    	refreshTablistTeams(pd, board);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    			    
			    objective.setDisplayName(("&b&l" + Main.getInstance().pluginName + " §a" + minAffichage + "&8:&a" + secAffichage).replace("&", "§"));
			    
			    int border = (int) (pd.getPlayer().getWorld().getWorldBorder().getSize()/2);
			    double prime = pd.getPrime() / 1000;
			    String primeMessage = prime + "";;
			    if(prime > 1000) {
			    	primeMessage = prime + "k";
			    }
			    
			    Map<Integer, String> list = new HashMap<Integer, String>();
				list.put(10, "&8> &fSaison 1 - &bEp&a" + Main.getInstance().episode);
				list.put(9, "&4");
				if(!Main.GAMESTATE.equals(GameState.Ended) && Main.getInstance().nextEvent.length() != 0) {
					list.put(8, "&8> " + Main.getInstance().nextEvent);
					list.put(7, "&3");					
				}
				else {
					if(hasScoreTaken(objective, 7))	board.resetScores(getEntryFromScore(objective, 7));
					if(hasScoreTaken(objective, 8))	board.resetScores(getEntryFromScore(objective, 8));
				}
				
				list.put(6, "&8> &7Joueurs &f: &b" + PlayerData.getAlivePlayers().size());
				list.put(5, "&8> &7Kills &f: &b" + pd.getKills());
				
				list.put(4, "&8> &7Prime &f: &e" + primeMessage + " $");
				
				list.put(3, "&2");
				if(!Main.GAMESTATE.equals(GameState.Ended) && Main.getInstance().nextEvent.length() != 0) {
					list.put(2, "&8> &7Border &f: &c" + border + "/" + border);
					list.put(1, "&1");
				}
				else {
					if(hasScoreTaken(objective, 2))	board.resetScores(getEntryFromScore(objective, 2));
					if(hasScoreTaken(objective, 1))	board.resetScores(getEntryFromScore(objective, 1));
				}
				list.put(0, "&8> &rCréer par &aWeatheria&f/&cPandora");
				
				for(Map.Entry<Integer, String> entry : list.entrySet()) {
					replaceScore(objective, entry.getKey(), entry.getValue().replace("&", "§"));
				}
				pd.getPlayer().setScoreboard(board);

				
				
				
			}
		}
		else {
			for(PlayerData pd: Main.getInstance().getPlayersData()) {
				if(pd.getPlayer().getScoreboard().equals(Main.getInstance().manager.getMainScoreboard())) pd.getPlayer().setScoreboard(Main.getInstance().manager.getNewScoreboard());
				
				Scoreboard board = pd.getPlayer().getScoreboard();
				boolean notExist = board.getObjective(pd.getPlayer().getName()) == null;
			    Objective objective = board.getObjective(pd.getPlayer().getName()) == null ? board.registerNewObjective(pd.getPlayer().getName(), "dummy") : board.getObjective(pd.getPlayer().getName());
			    
			    if(notExist) objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			    for(PlayerData data2: Main.getInstance().getPlayersData()) {
			    	Team global = null;
				    if(board.getTeam(data2.getPlayer().getName()) == null) {
				    	global = board.registerNewTeam(data2.getPlayer().getName());
				    }
				    else {
				    	global = board.getTeam(data2.getPlayer().getName());
				    }			    
				    global.setPrefix("§7");
			    	if(!global.hasPlayer(data2.getPlayer())) {
			    		global.addPlayer(data2.getPlayer());
			    	}
			    }
			    
			    objective.setDisplayName(("&b&l" + Main.getInstance().pluginName).replace("&", "§"));
			    
			    Map<Integer, String> list = new HashMap<Integer, String>();
				list.put(4, "&8> &fSaison 1");
				list.put(3, "&8> &7Joueurs &f: &b" + PlayerData.getAlivePlayers().size());
				list.put(2, "&1");
				list.put(1, "&8> &rCréer par &aWeatheria&f/&cPandora");
				
				for(Map.Entry<Integer, String> entry : list.entrySet()) {
					replaceScore(objective, entry.getKey(), entry.getValue().replace("&", "§"));
				}
				pd.getPlayer().setScoreboard(board);
			}
		}
	}
	
	/*******************************************************
	 * Scoreboard Function
	 *******************************************************/
	public String getEntryFromScore(Objective o, int score) {
	    if(o == null) return null;
	    if(!hasScoreTaken(o, score)) return null;
	    for (String s : o.getScoreboard().getEntries()) {
	        if(o.getScore(s).getScore() == score) return o.getScore(s).getEntry();
	    }
	    return null;
	}

	public boolean hasScoreTaken(Objective o, int score) {
	    for (String s : o.getScoreboard().getEntries()) {
	        if(o.getScore(s).getScore() == score) return true;
	    }
	    return false;
	}

	public void replaceScore(Objective o, int score, String name) {
	    if(hasScoreTaken(o, score)) {
	        if(getEntryFromScore(o, score).equalsIgnoreCase(name)) return;
	        if(!(getEntryFromScore(o, score).equalsIgnoreCase(name))) o.getScoreboard().resetScores(getEntryFromScore(o, score));
	    }
	    o.getScore(name).setScore(score);
	}
	
	/*******************************************************
	 * Tablist Teams
	 *******************************************************/
	@SuppressWarnings("deprecation")
	public void refreshTablistTeams(PlayerData pdata, Scoreboard board) {
		for(PlayerData data: Main.getInstance().getPlayersData()) {
			for(PrefixData prefixData: data.getPersonnage().getPrefixData()) {
				for(int type: prefixData.getWhoCanSee()) {
					if(type == -1 || type == pdata.getPersonnage().getId()) {
						// pdata - Player who see in tab
						// data  - player where calculate view
						
						/*******************************************************
						 * §k if anonymous
						 *******************************************************/
						String name = data.getPlayer().getName();
						if(data.getPlayer().getName().length() > 9) {
							name = data.getPlayer().getName().substring(0, 9);
						}
						
						String teamSymbol = "";
						if(!Main.getInstance().isNull(data.getTeam())) {
							teamSymbol = data.getTeam().getTeamSuffix();
						}
						
						
						String teamname = teamSymbol + data.getPersonnage().getPriority() + "_" + name;
						for(Team lt: board.getTeams()) {
							if(lt.hasPlayer(data.getPlayer())) {
								String anonymousPrefix = "";
								String anonymousSuffix = "";
								if(Scenario.isActive(Main.getScenariosManager().ANONYMOUS_NAME) && Main.GAMESTATE != GameState.Ended) {
									anonymousPrefix = "§k";
									for(int i = data.getPlayer().getName().length(); i<12; i++) {
										anonymousSuffix = anonymousSuffix + "a";
									}
									if((pdata == data) ||
											!(pdata.getPersonnage() instanceof Empty) && pdata.getPersonnage().getType() == 1 && PlayerData.getFidèle(pdata) == data) {
										anonymousPrefix = "";
										anonymousSuffix = "";								
									}
									else if(!(pdata.getPersonnage() instanceof Empty) && pdata.getPersonnage().getType() == 2) {
										for(PlayerData pd3: Main.getInstance().getPlayersData()) {
											if(pd3 == data && pd3.getPersonnage().getType() == 1 && PlayerData.getFidèle(pd3) == pdata) {
												anonymousPrefix = "";
												anonymousSuffix = "";
											}
										}
									}						
								}
								String teamSuffix = "";
								if(!Main.getInstance().isNull(data.getTeam())) {
									teamSuffix = data.getTeam().getTeamSuffix();								
								}
								
								if((prefixData.getPrefix() + anonymousPrefix).length() > 16) {
							    	Bukkit.getConsoleSender().sendMessage(Main.getInstance().pluginPrefix + " §7>> §cLenght of prefix > 16 \"" + (prefixData.getPrefix() + anonymousPrefix).replace("§", "&") + "§r\" with lenght of " + (prefixData.getPrefix() + anonymousPrefix).length());
							    }
							    else {
							    	lt.setPrefix(prefixData.getPrefix() + anonymousPrefix);
							    }
								
								if((anonymousSuffix + teamSuffix).length() > 16) {
							    	Bukkit.getConsoleSender().sendMessage(Main.getInstance().pluginPrefix + " §7>> §cLenght of suffix > 16 \"" + anonymousSuffix + teamSuffix + "§r\" with lenght of " + (anonymousSuffix + teamSuffix).length());
							    }
								else {
									lt.setSuffix(anonymousSuffix + teamSuffix);
								}
								
								if(!lt.getName().equals(teamname)) {
									lt.unregister();
								}
							}
						}
						
						/*******************************************************
						 * Teams
						 *******************************************************/
						Team team = null;
						if(teamname.length() > 16) {
					    	Bukkit.getConsoleSender().sendMessage(Main.getInstance().pluginPrefix + " §7>> §cLenght of teamname > 16 \"" + teamname + "§r\" with lenght of " + teamname.length());
					    }
						else {
							if(board.getTeam(teamname) == null) {
						    	team = board.registerNewTeam(teamname);
						    }
						    else {
						    	team = board.getTeam(teamname);
						    }
						}
					    
					    String anonymousPrefix = "";
						String anonymousSuffix = "";
						if(Scenario.isActive(Main.getScenariosManager().ANONYMOUS_NAME) && Main.GAMESTATE != GameState.Ended) {
							anonymousPrefix = "§k";
							for(int i = data.getPlayer().getName().length(); i<12; i++) {
								anonymousSuffix = anonymousSuffix + "a";
							}
							if((pdata == data) ||
									!(pdata.getPersonnage() instanceof Empty) && pdata.getPersonnage().getType() == 1 && PlayerData.getFidèle(pdata) == data) {
								anonymousPrefix = "";
								anonymousSuffix = "";								
							}
							else if(!(pdata.getPersonnage() instanceof Empty) && pdata.getPersonnage().getType() == 2) {
								for(PlayerData pd3: Main.getInstance().getPlayersData()) {
									if(pd3 == data && pd3.getPersonnage().getType() == 1 && PlayerData.getFidèle(pd3) == pdata) {
										anonymousPrefix = "";
										anonymousSuffix = "";
									}
								}
							}
						}
						
						String teamSuffix = "";
						if(!Main.getInstance().isNull(data.getTeam())) {
							teamSuffix = data.getTeam().getTeamSuffix();
						}

					    if((prefixData.getPrefix() + anonymousPrefix).length() > 16) {
					    	Bukkit.getConsoleSender().sendMessage(Main.getInstance().pluginPrefix + " §7>> §cLenght of prefix > 16 \"" + (prefixData.getPrefix() + anonymousPrefix).replace("§", "&") + "§r\" with lenght of " + (prefixData.getPrefix() + anonymousPrefix).length());
					    }
					    else {
					    	team.setPrefix(prefixData.getPrefix() + anonymousPrefix);
					    }
						
						if((anonymousSuffix + teamSuffix).length() > 16) {
					    	Bukkit.getConsoleSender().sendMessage(Main.getInstance().pluginPrefix + " §7>> §cLenght of suffix > 16 \"" + anonymousSuffix + teamSuffix + "§r\" with lenght of " + (anonymousSuffix + teamSuffix).length());
					    }
						else {
							team.setSuffix(anonymousSuffix + teamSuffix);
						}
					    
					    team.addPlayer(data.getPlayer());
					}
				}
			}
		}
		
	}
}
