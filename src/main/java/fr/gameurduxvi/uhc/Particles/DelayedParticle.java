package fr.gameurduxvi.uhc.Particles;

import org.bukkit.Bukkit;

import fr.gameurduxvi.uhc.Main;

public class DelayedParticle {
	public DelayedParticle(DefaultParticle dp, double seconds) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), dp, (long) (20 * seconds));
	}
	
	public DelayedParticle(DefaultParticle dp, int ticks) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), dp, (long) ticks);
	}
}
