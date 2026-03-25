package fr.gameurduxvi.uhc.GameEvents;

import org.bukkit.Bukkit;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;

public class AntiDamageGameEvent extends GameEvent {
	public AntiDamageGameEvent() {
		setName("Temp de d'invicibilité");
		setDescription("A partir de ce moment, \nles joueurs reprendront les dégâts normaux");
		setMinute(1);
		setSecond(0);
		setInScoreboard(true);
		setScoreboardText("§7Dégâts : §f%m%§7:§f%s%");
	}
	public void run() {
		Bukkit.broadcastMessage("§bInfo §7>> §fLes dégâts sont activés");
		Main.getInstance().anyDamage = true;
	}
}
