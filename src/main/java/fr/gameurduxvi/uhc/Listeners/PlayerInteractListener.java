package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(Main.GAMESTATE.equals(GameState.Ended)) {
			e.setCancelled(true);
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			PlayerData pd = PlayerData.getPlayerData(p);
			if(Main.getInstance().isNull(e.getItem())) return;
			if(Main.getInstance().isNull(e.getItem().getItemMeta())) return;
			if(Main.getInstance().isNull(e.getItem().getItemMeta().getDisplayName())) return;
			if(e.getItem().getType().equals(pd.getPersonnage().ultimateItem().getType())){
				if(e.getItem().getItemMeta().getDisplayName().contains(pd.getPersonnage().ultimateItem().getItemMeta().getDisplayName())) {
					pd.getPersonnage().preUltimate(pd);
					e.setCancelled(true);
				}
			}
			else if(e.getItem().getItemMeta().getDisplayName().equals("Hana Hana No Mi")) {
				if(e.getPlayer().getItemInHand().getAmount() == 1) {
					e.getPlayer().setItemInHand(null);
				}
				else {
					ItemStack it = e.getPlayer().getItemInHand();
					it.setAmount(it.getAmount() - 1);
					e.getPlayer().setItemInHand(it);
				}
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 0));
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2 * 60 * 20, 1));
				if(e.getPlayer().getFoodLevel() + 4 > 20) {
					e.getPlayer().setFoodLevel(20);
				}
				else {
					e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 4);
				}
				pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.EAT, 1, 1);
			}
			else if(e.getItem().getItemMeta().getDisplayName().equals("Horu Horu No Mi")) {
				if(e.getPlayer().getItemInHand().getAmount() == 1) {
					e.getPlayer().setItemInHand(null);
				}
				else {
					ItemStack it = e.getPlayer().getItemInHand();
					it.setAmount(it.getAmount() - 1);
					e.getPlayer().setItemInHand(it);
				}
				pd.getPlayer().playSound(pd.getPlayer().getLocation(), Sound.EAT, 1, 1);
				pd.getPersonnage().resetUltimate(pd);
				for(PotionEffect pe: pd.getPlayer().getActivePotionEffects()) pd.getPlayer().removePotionEffect(pe.getType());
				pd.setMalusHealth(0);
			}
		}
	}
}
