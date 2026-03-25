package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandTrilog implements CommandExecutor {
	public static String INVENTORY_NAME = "Craft du Trilog Pose";
	
	@SuppressWarnings({ })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, INVENTORY_NAME);
			
			ItemStack lapis = new ItemStack(Material.LAPIS_BLOCK);
			ItemStack redstone = new ItemStack(Material.REDSTONE_BLOCK);
			ItemStack watch = new ItemStack(Material.WATCH);
			ItemMeta watchM = watch.getItemMeta();
			watchM.setDisplayName("Boussole Endommagée");
			watch.setItemMeta(watchM);
			
			inv.setItem(0, redstone);
			inv.setItem(2, lapis);
			inv.setItem(4, watch);
			inv.setItem(6, lapis);
			inv.setItem(8, redstone);
			
			p.openInventory(inv);
		}
		else {
			sender.sendMessage("§cSeul les joueurs peuvent faire cette commande");
		}
		return false;
	}
	
	
}
