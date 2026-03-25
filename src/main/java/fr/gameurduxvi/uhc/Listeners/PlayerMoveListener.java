package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Personnages.Pirates.Nami;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class PlayerMoveListener implements Listener {
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if(Main.GAMESTATE.equals(GameState.Paused)) {
			if(PlayerData.getAlivePlayers().contains(pd)) {
				e.setTo(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ(), e.getTo().getYaw(), e.getTo().getPitch()));
				return;
			}
		}
		if(!Main.getInstance().playersCanMove) {
			if(PlayerData.getAlivePlayers().contains(pd)) {
				if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
					e.getPlayer().teleport(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ(), e.getTo().getYaw(), e.getTo().getPitch()));
				}
			}
		}
		
		if(Main.GAMESTATE.equals(GameState.In_Progress)) {
			if(pd.getPersonnage() instanceof Nami) {
				Nami pers = (Nami) pd.getPersonnage();
				Location loc = e.getFrom();
				Location locBlock = e.getFrom();
				for(int i = 0; i <= 10; i++) {
					locBlock.setY(loc.getY() - i);
					if(!locBlock.getBlock().getType().equals(Material.AIR)) {
						loc.setY(locBlock.getBlockY() + 1);
						break;
					}
				}
				if(pers.particles && pers.lastParticleLocation.distance(loc) >= 0.5) {
					pers.lastParticleLocation = loc;
					loc.setY(loc.getY() + 0.1);
					pd.getPlayer().getWorld().playEffect(loc, Effect.FOOTSTEP, 1);
				}
			}
		}
	}
}
