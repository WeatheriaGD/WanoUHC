package fr.gameurduxvi.uhc.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.scenarios.target.Target;

public class PlayerInteractEntityListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(e.getRightClicked().getWorld().equals(Bukkit.getWorld(Main.getInstance().WanoWorldName)) && e.getRightClicked() instanceof Villager && e.getRightClicked().getCustomName().equals(Target.VILLAGER_NAME)) {
			e.setCancelled(true);
			
			if(Main.GAMESTATE.equals(GameState.In_Progress)) {
				boolean hasTarget = Target.hasTarget(p);
				
				if(!hasTarget) {
					PlayerData pd = PlayerData.getPlayerData(p);
					ArrayList<PlayerData> possibleTargets = new ArrayList<>();
					for(PlayerData pd2: PlayerData.getAlivePlayers()) {
						if(!pd.getTeam().equals(pd2.getTeam()) && pd.getPlayer().getWorld().equals(pd2.getPlayer().getWorld())/* && pd2.getPersonnage().getType() != 1*/) {
							if(pd.getPersonnage().getType() == 1 && PlayerData.hasFidèle(pd) && Objects.equals(PlayerData.getFidèle(pd), pd2)) {
								
							}
							else if(pd.getPersonnage().getType() == 2 && PlayerData.hasFidèle(pd2) && Objects.equals(PlayerData.getFidèle(pd2), pd)) {
								
							}
							else {
								possibleTargets.add(pd2);
							}
						}
					}
					if(possibleTargets.size() > 0) {
						Collections.shuffle(possibleTargets);
						Target.targets.put(p, possibleTargets.get(0).getPlayer());
						possibleTargets.get(0).getPlayer().sendTitle("§a", "§c§ka§r Attention ! Vous avez été pris pour cible ! §c§ka");
						possibleTargets.get(0).getPlayer().sendMessage("§c§ka§r Attention ! Vous avez été pris pour cible ! §c§ka");
						p.sendMessage("§7[§8Marin Dodousse§7] §aHéé psst ! Ouais toi ! C'est à toi que je parle !");
						p.sendMessage("§7[§8Marin Dodousse§7] §aCa te dirais un moyen d'allonger ta vie ? Et bah j'ai exactement ce qu'il te faut !");
						p.sendMessage("§7[§8Marin Dodousse§7] §aPrend cette §4§lboussole §aet élimine le Pirate pointé par celle-ci. Reviens me voir quand tu auras fini.");
						
						ItemStack it = new ItemStack(Material.COMPASS);
						ItemMeta itM = it.getItemMeta();
						itM.setDisplayName("§6Palet de traque");
						it.setItemMeta(itM);
						
						if(pd.getPlayer().getInventory().firstEmpty() < 0) {
							pd.getPlayer().getWorld().dropItem(pd.getPlayer().getLocation(), pd.getPlayer().getInventory().getItem(7));
						}
						else {
							pd.getPlayer().getInventory().setItem(pd.getPlayer().getInventory().firstEmpty(), pd.getPlayer().getInventory().getItem(7));
						}					
						pd.getPlayer().getInventory().setItem(7, it);
					}
					else {
						p.sendMessage("§7[§8Marin Dodousse§7] §aJe n'ai aucune cible pour toi. Reviens plus tard.");
					}
				}
				else {
					if(Target.getTarget(p) == null) {
						Target.targets.remove(p);
						PlayerData.getPlayerData(p).setMaxHealth(PlayerData.getPlayerData(p).getMaxHealth() + 2);
						p.sendMessage("§7[§8Marin Dodousse§7] §aVoici ta récompense !");
					}
					else {
						p.sendMessage("§7[§8Marin Dodousse§7] §aJe t'ai déjà donné un contrat ! Reviens me voir quand tu auras fini !");
					}				
				}
			}
			else {
				p.sendMessage("§7[§8Marin Dodousse§7] §aNn mais TA GUEULE!");
			}
		}
	}
}
