package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class BlockPlaceListener implements Listener {
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(Main.GAMESTATE.equals(GameState.Paused)) {
			e.setCancelled(true);
			return;
		}
		
		if(Main.GAMESTATE.equals(GameState.Ended)) {
			e.setCancelled(true);
			return;
		}
		
		if(Main.getInstance().isNull(e.getItemInHand())) return;
		if(Main.getInstance().isNull(e.getItemInHand().getItemMeta())) return;
		if(Main.getInstance().isNull(e.getItemInHand().getItemMeta().getDisplayName())) return;
		if(e.getItemInHand().getItemMeta().getDisplayName().equals("Hana Hana No Mi")) {
			e.setCancelled(true);
		}
		else if(e.getItemInHand().getItemMeta().getDisplayName().equals("Horu Horu No Mi")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace2(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		PlayerData pd = PlayerData.getPlayerData(p);
		if(Main.getInstance().isNull(e.getItemInHand())) return;
		if(e.getItemInHand().getType().equals(pd.getPersonnage().ultimateItem().getType())){
			if(!Main.getInstance().isNull(e.getItemInHand().getItemMeta().getDisplayName())) {
				if(e.getItemInHand().getItemMeta().getDisplayName().contains(pd.getPersonnage().ultimateItem().getItemMeta().getDisplayName())) {
					pd.getPlayer().sendMessage("§6" + pd.getPersonnage().getDescription());
					e.setCancelled(true);
				}
			}
		}
		if(e.getBlock().getWorld().getName().equals(Main.getInstance().WanoWorldName)) {
			if(Main.getInstance().getWanoUnbrakebableBlocks().contains(e.getBlock().getType())) {
				Main.getInstance().getWanoPosedLocations().add(e.getBlock().getLocation());
			}
		}
	}
}
