package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir.Barbe_Noir1;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

public class InventoryDragListener implements Listener {

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if(PlayerData.hasPlayerData((Player) e.getWhoClicked())) {
			PlayerData pd = PlayerData.getPlayerData((Player) e.getWhoClicked());
			if(pd.getPersonnage() instanceof Barbe_Noir1) {
				Barbe_Noir1 pers = (Barbe_Noir1) pd.getPersonnage();
				if(pers.hidden) {
					PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(pd.getPlayer().getEntityId(), 0, null);
					 for(Player lp: Bukkit.getOnlinePlayers()) {
				            CraftPlayer craftPlayer = (CraftPlayer) lp;
				            craftPlayer.getHandle().playerConnection.sendPacket(handPacket);
					 }
				}
			}
		}
	}
}
