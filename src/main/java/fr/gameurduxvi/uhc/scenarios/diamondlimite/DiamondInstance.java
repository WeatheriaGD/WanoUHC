package fr.gameurduxvi.uhc.scenarios.diamondlimite;

import java.util.Date;

import org.bukkit.entity.Player;

public class DiamondInstance {
	public Player player;
	public int amountDiamond = 0;
	public Date date = new Date();
	
	public DiamondInstance(Player player) {
		this.player = player;
	}
}
