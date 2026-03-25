package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gameurduxvi.uhc.Particles.DelayedParticle;
import fr.gameurduxvi.uhc.Particles.ParticleCircle;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class CommandTestParticles implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Location poseLoc = PlayerData.getPlayerData((Player) sender).getPlayer().getLocation();
			new ParticleCircle(poseLoc, 1, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1);
			new ParticleCircle(poseLoc, 1.5, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1);
			for(int i=0; i<=21; i++) {
				double time = 2 + (0.5 * i);
				new DelayedParticle(new ParticleCircle(poseLoc, time, 0, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 1), (0.1 * i));
			}
			
			new ParticleCircle(poseLoc, 1, 15, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 40);
			for(int i=0; i<=21; i++) {
				double time = 1.5 + (0.5 * i);
				new DelayedParticle(new ParticleCircle(poseLoc, time, 15, EnumParticle.BLOCK_CRACK, Material.COAL_BLOCK, 0, 0, 0, 0, 10), (0.1 * i));
			}
		}
		else {
			sender.sendMessage("§cSeul les joueurs peuvent faire cette commande");
		}
		return false;
	}

}
