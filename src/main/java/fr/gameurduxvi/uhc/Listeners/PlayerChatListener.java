package fr.gameurduxvi.uhc.Listeners;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;


@SuppressWarnings("deprecation")
public class PlayerChatListener implements Listener {
	
	public static boolean chat = true;
	
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		e.setCancelled(true);
		String message = stripcolor(e.getMessage());
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		if(message.length() >= 1) {
			if(Main.GAMESTATE.equals(GameState.In_Progress)) {
				
				/***********************
				 * Global Chat
				 ***********************/
				
				if(message.toLowerCase().startsWith("!")) {
					if(chat) {
						for(PlayerData pdata: Main.getInstance().getPlayersData()) {
							pdata.getPlayer().sendMessage("§3Global §7>> " + PlayerData.getPrefix(pdata.getPlayer(), e.getPlayer()) + e.getPlayer().getName() + PlayerData.getSuffix(pdata.getPlayer(), e.getPlayer()) + " §r§8>> §f" + stripcolor(e.getMessage().substring(1)));
						}
						Bukkit.getConsoleSender().sendMessage("§3Global §7>> " + e.getPlayer().getName() + " §8>> §f" + stripcolor(e.getMessage().substring(1)));
					}
					else
						pd.getPlayer().sendMessage("§7{§c!§7} §cLe chat global est desactiv§");
				}
				
					
				
				
				/***********************
				 * Emperor and Fidèle Chat
				 ***********************/
				else if(message.toLowerCase().startsWith("%")) {
					boolean found = false;
					
					/***********************
					 * Chat of Emperors
					 ***********************/
					if(pd.getPersonnage().getType() == 1) {
						for(PlayerData pdata: PlayerData.getAlivePlayers()) {
							if(pdata.getPersonnage().getId() == pd.getPersonnage().getId() + 100) {
								pdata.getPlayer().sendMessage(PlayerData.getPrefix(pdata.getPlayer(), e.getPlayer()) + e.getPlayer().getName() + PlayerData.getSuffix(pdata.getPlayer(), e.getPlayer()) + " §r§6-> §3Vous §8>> §f" + stripcolor(e.getMessage().substring(1)));
								found = true;
							}
						}
						if(found) {
							pd.getPlayer().sendMessage(PlayerData.getPrefix(pd.getPlayer(), e.getPlayer()) + e.getPlayer().getName() + PlayerData.getSuffix(pd.getPlayer(), e.getPlayer()) + " §r§6-> §3Fidèle(s) §8>> §f" + stripcolor(e.getMessage().substring(1)));
							Bukkit.getConsoleSender().sendMessage("§3Fidèle §6 -> " + e.getPlayer().getName() + " §8>> §f" + stripcolor(e.getMessage().substring(1)));
						}
					}
					
					/***********************
					 * Chat of fidèles
					 ***********************/
					else if(pd.getPersonnage().getType() == 2) {
						for(PlayerData pdata: PlayerData.getAlivePlayers()) {
							if(pdata.getPersonnage().getId() + 100 == pd.getPersonnage().getId()) {
								pdata.getPlayer().sendMessage(PlayerData.getPrefix(pdata.getPlayer(), e.getPlayer()) + e.getPlayer().getName() + PlayerData.getSuffix(pdata.getPlayer(), e.getPlayer()) + " §6-> §3Vous §8>> §f" + stripcolor(e.getMessage().substring(1)));
								found = true;
							}
						}
						if(found) {
							pd.getPlayer().sendMessage(PlayerData.getPrefix(pd.getPlayer(), e.getPlayer()) + e.getPlayer().getName() + PlayerData.getSuffix(pd.getPlayer(), e.getPlayer()) + " §r§6-> §3Empereur §8>> §f" + stripcolor(e.getMessage().substring(1)));
							Bukkit.getConsoleSender().sendMessage("§3Empereur §6-> " + e.getPlayer().getName() + " §8>> §f" + stripcolor(e.getMessage().substring(1)));
						}
					}
					if(!found) {
						pd.getPlayer().sendMessage("§cVous n'êtes pas lié à quelqu'un");
					}
				}
				
				/***********************
				 * Pirate Team Chat
				 ***********************/
				else {
					if(!Main.getInstance().isNull(pd.getTeam())) {
						for(Player lp: pd.getTeam().getPlayers()) {
							lp.getPlayer().sendMessage("§3Team §7>> " + PlayerData.getPrefix(lp.getPlayer(), e.getPlayer()) + e.getPlayer().getName() + PlayerData.getSuffix(lp.getPlayer(), e.getPlayer()) + " §r§8>> §f" + stripcolor(e.getMessage()));
						}
						Bukkit.getConsoleSender().sendMessage("§3Team §7>> " + e.getPlayer().getName() + " §8>> §f" + stripcolor(e.getMessage()));
					}
					else {
						pd.getPlayer().sendMessage("§cVous n'êtes pas encore dans une équipe");
					}
				}						
			}
			
			/***********************
			 * Lobby Chat
			 ***********************/
			else {
				if(chat) 
					Bukkit.broadcastMessage("§7" + e.getPlayer().getName() + " §8>> §f" + stripcolor(e.getMessage()));
				else
					pd.getPlayer().sendMessage("§7{§c!§7} §cLe chat global est desactivé");
			}
		}
		else {
			e.getPlayer().sendMessage("§cvous devez au moins mettre 1 caractère !");
		}
	}
	
	private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");
    public String stripcolor(String input) {
        return input == null?null:STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
