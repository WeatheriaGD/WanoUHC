package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;

public class CommandRevive implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			Player target = player;
			if(args.length >=1) {
				String arg1 = Main.getInstance().argument(args, 1);
				
				boolean found = false;
				for(Player p: Bukkit.getOnlinePlayers()) {
					if(p.getName().equalsIgnoreCase(arg1)) {
						found = true;
						target = p;
					}
				}
				
				if(!found) {
					player.sendMessage("§cCe joueur est introuvable !");
					return false;
				}
			}
			
			if(Main.GAMESTATE.equals(GameState.Idle)) {
				player.sendMessage("§cLa partie n'a pas encore commencé !");
			}
				
			
			if(Main.getInstance().secondes <= 20 && Main.getInstance().minutes == 0) {
				player.sendMessage("§cOpération impossible, la partie vient de commencer.");
				player.sendMessage("§cVeuillez recommencer dans 30 secondes.");
				return false;
			}
			
			PlayerData targetData = PlayerData.getPlayerData(target);
			
			PlayerData.toggleDeath(targetData.getPlayer(), !targetData.isDeath);
			
			if(targetData.deathLocation != null) {
				targetData.getPlayer().teleport(targetData.deathLocation);
				targetData.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 30, 0, true, false), true);
				targetData.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 30, 255, true, false), true);
			}
			
			if(targetData.isDeath) {
				targetData.getPlayer().sendMessage("§7Revive §8>> §bVous êtes désormais un spectateur");
			}
			else {
				targetData.getPlayer().sendMessage("§7Revive §8>> §bVous n'êtes plus un spectateur");
			}
			
			if(targetData.isSpec) {
				targetData.getPlayer().sendMessage("§6Warning §eVous êtes toujours en mode spectateur !");
				targetData.getPlayer().sendMessage("§eSeul les spectateurs peuvent vous voir");
			}
		}
		return false;
	}

}
