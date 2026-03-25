package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.uhc.Storage.PlayerData;

public class PlayerItemConsumeListener implements Listener {
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		if(PlayerData.hasPlayerData(p)) {
			PlayerData pd = PlayerData.getPlayerData(p);
			if(pd.getPersonnage().getType() == 1 && e.getItem().getType().equals(Material.GOLDEN_APPLE))
				pd.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 1, true, false));
		}
	}
}
