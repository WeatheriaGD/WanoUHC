package fr.gameurduxvi.uhc.Tasks;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class HeaderAndFooterTask extends BukkitRunnable {
	public static List<Entry<PlayerData, Integer>> topList;
	
	@Override
	public void run() {
		double tps = (double) Math.round(MinecraftServer.getServer().recentTps[0] * 100) / 100;
		if(tps > 20) tps = 20.0;
		
		for(PlayerData pd: Main.getInstance().getPlayersData()) {
			if(!Main.GAMESTATE.equals(GameState.Idle)) {
				/*******************************************************
				 * Primes Toplist
				 *******************************************************/			    
			    StringBuilder topPrimesM = new StringBuilder("");
			    if(Main.getInstance().showPrimes && !Main.GAMESTATE.equals(GameState.Ended)) {
			    	topPrimesM.append("\n§bTop Primes\n§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌\n");
			    	topList = topPrimes();
			    	int i = 0;
				    for(Entry<PlayerData, Integer> entry: topList) {
				    	if(i >= 3) break;
				    	int prime = entry.getValue();
				    	Double prime2 = (double) (prime / 1000);
					    String primeMessage = prime2 + "";;
					    if(prime != 0) {
					    	primeMessage = prime2 + "k";
					    }
				    	topPrimesM.append("§a" + entry.getKey().getPlayer().getName() + " §e" + primeMessage + " $");
				    	topPrimesM.append("§6\n ");
				    	i++;
				    }
				    topPrimesM.append("§8﹌﹌﹌﹌﹌﹌﹌﹌﹌﹌\n");
			    }
				/*******************************************************
				 * Header & Footer
				 *******************************************************/				
				Object entityPlayer = null;
				try {
					entityPlayer = pd.getPlayer().getClass().getMethod("getHandle").invoke(pd.getPlayer());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				int ping = 0;
				try {
					ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
				Main.getInstance().setHeaderFooter(pd.getPlayer(), ("§c☠ &b&l" + Main.getInstance().pluginName + " §c☠").replace("&", "§"), topPrimesM + "§7Ping: §b" + ping + " §7TPS: §b" + tps);
			}
			else {
				/*******************************************************
				 * Header & Footer
				 *******************************************************/				
				Object entityPlayer = null;
				try {
					entityPlayer = pd.getPlayer().getClass().getMethod("getHandle").invoke(pd.getPlayer());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				int ping = 0;
				try {
					ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
				//Runtime r = Runtime.getRuntime();
			    //Main.getInstance().setHeaderFooter(pd.getPlayer(), ("§c☠ &b&l"+ Main.getInstance().pluginName + " §c☠").replace("&", "§"), "§7Ping: §b" + ping + " §7TPS: §b" + tps + "\n§7RAM: §b" + ((r.totalMemory()-r.freeMemory()) / 1048576) + " / " + (r.totalMemory() / 1048576));
				Main.getInstance().setHeaderFooter(pd.getPlayer(), ("§c☠ &b&l"+ Main.getInstance().pluginName + " §c☠").replace("&", "§"), "§7Ping: §b" + ping + " §7TPS: §b" + tps);
			}
		}
	}
	
	/*******************************************************
	 * Sort top primes list
	 *******************************************************/
	public static List<Map.Entry<PlayerData, Integer>> topPrimes() {
		HashMap<PlayerData, Integer> hm = new HashMap<>();
		for(PlayerData pd: PlayerData.getAlivePlayers()) {
			if(pd.getPersonnage().getType() != 1) {
				hm.put(pd, pd.getPrime()); //+ (pd.getKills() * Main.getInstance().primeParKill));
			}
		}
			
		List<Map.Entry<PlayerData, Integer>> list = new LinkedList<Map.Entry<PlayerData, Integer>>(hm.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<PlayerData, Integer>>() {
			public int compare(Map.Entry<PlayerData, Integer> o2,
								Map.Entry<PlayerData, Integer> o1) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
        return list;
    }
	
	public int AvailableMemory() {
		return (int)((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1048576L);
	} 
}
