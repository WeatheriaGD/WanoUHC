package fr.gameurduxvi.uhc.Listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;

public class EnchantmentListener implements Listener {
	
	
	@EventHandler
	public void onAnvil(InventoryClickEvent e) {
		if(!ConfigManager.CONTROL_ENCHANTS) return;
		if(e.getInventory() == null) return;
		if(!e.getInventory().getType().equals(InventoryType.ANVIL)) return;
		
		if(e.getSlot() != 2) return;
		
		ItemStack it = e.getCurrentItem();
		if(it == null) return;
		
		if(it.getEnchantments().size() == 0) {
			if(it.getType().equals(Material.ENCHANTED_BOOK)){
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta)it.getItemMeta();
				e.setCurrentItem(correctEnchant(it, meta.getStoredEnchants()));
			}
		}
		else {
			e.setCurrentItem(correctEnchant(it, it.getEnchantments()));
		}
	}
	
	@EventHandler
	public void onPreAnvil(InventoryClickEvent e) {		
		if(!ConfigManager.CONTROL_ENCHANTS) return;
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(e.getInventory() == null) return;
				if(!e.getInventory().getType().equals(InventoryType.ANVIL)) return;
				
				if(e.getSlot() == 2) return;
				
				ItemStack it1 = e.getInventory().getItem(0);
				ItemStack it2 = e.getInventory().getItem(1);
				if(it1 == null || it2 == null) return;
				
				ItemStack result = e.getInventory().getItem(2);
				if(result == null) return;
				
				if(result.getEnchantments().size() == 0) {
					if(result.getType().equals(Material.ENCHANTED_BOOK)){
						EnchantmentStorageMeta meta = (EnchantmentStorageMeta)result.getItemMeta();
						e.getInventory().setItem(2, (correctEnchant(result, meta.getStoredEnchants())));
					}
				}
				else {
					e.getInventory().setItem(2, (correctEnchant(result, result.getEnchantments())));
				}
			}
		}, 1);
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		if(!ConfigManager.CONTROL_ENCHANTS) return;
		ItemStack it = correctEnchant(e.getItem(), e.getEnchantsToAdd());
		e.getInventory().setItem(0, it);
		if(it.getType() == Material.BOOK) {
			e.getInventory().setItem(1, new ItemStack(Material.INK_SACK, e.getInventory().getItem(1).getAmount(), (short) 4));
			e.getEnchanter().setLevel(e.getEnchanter().getLevel() + e.whichButton() + 1);
			e.getEnchanter().sendMessage("§cCe livre contient des enchantements interdits.");
		}
		else {
			if(it.getType() != Material.ENCHANTED_BOOK && it.getEnchantments().size() == 0) {
				e.getInventory().setItem(1, new ItemStack(Material.INK_SACK, e.getInventory().getItem(1).getAmount(), (short) 4));
				e.getEnchanter().setLevel(e.getEnchanter().getLevel() + e.whichButton() + 1);
				e.getEnchanter().sendMessage("§cCet item contient des enchantements interdits.");
			}
		}
		
		
		if(ConfigManager.LAPIS) {
			e.getInventory().setItem(1, new ItemStack(Material.INK_SACK, 64, (short) 4));
		}
	}
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		if(e.getInventory().getType().equals(InventoryType.ENCHANTING)) {
			if(ConfigManager.LAPIS) {
				e.getInventory().setItem(1, new ItemStack(Material.INK_SACK, 64, (short) 4));
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getType().equals(InventoryType.ENCHANTING)) {
			if(ConfigManager.LAPIS) {
				e.getInventory().setItem(1, null);
			}
		}
	}
	
	@EventHandler
	public void onClickEnchantingTable(InventoryClickEvent e) {
		if(e.getInventory() == null) return;
		if(!e.getInventory().getType().equals(InventoryType.ENCHANTING)) return;
		
		if(e.getSlot() != 1) return;
		
		if(ConfigManager.LAPIS) {
			e.setCancelled(true);			
		}
	}
	
	private ItemStack correctEnchant(ItemStack it, Map<Enchantment, Integer> enchants) {
		Map<Enchantment, Integer> resultEnchants = new HashMap<Enchantment, Integer>();
		Iterator<Entry<Enchantment, Integer>> iterator = enchants.entrySet().iterator();
		
		ItemStack result = new ItemStack(it);
				
		while(iterator.hasNext()) {
			Entry<Enchantment, Integer> entry = iterator.next();
			Enchantment ench = entry.getKey();
			int level = entry.getValue();
			
			result.removeEnchantment(ench);
			
			if(ench.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {
				if(it.getType().equals(Material.DIAMOND_CHESTPLATE)) level = level > ConfigManager.DIAMOND_PROTECTION ? ConfigManager.DIAMOND_PROTECTION : level;
				if(it.getType().equals(Material.IRON_CHESTPLATE)) level = level > ConfigManager.IRON_PROTECTION ? ConfigManager.IRON_PROTECTION : level;
			}
			else if(ench.equals(Enchantment.DAMAGE_ALL)) {
				if(it.getType().equals(Material.DIAMOND_SWORD)) level = level > ConfigManager.DIAMOND_SHARPNESS ? ConfigManager.DIAMOND_SHARPNESS : level;
				if(it.getType().equals(Material.IRON_SWORD)) level = level > ConfigManager.IRON_SHARPNESS ? ConfigManager.IRON_SHARPNESS : level;
			}
			else if(ench.equals(Enchantment.ARROW_DAMAGE)) {
				level = level > ConfigManager.POWER ? ConfigManager.POWER : level;
			}
			else if(ench.equals(Enchantment.ARROW_KNOCKBACK)) {
				level = level > ConfigManager.PUNCH_AND_KNOCKBACK ? ConfigManager.PUNCH_AND_KNOCKBACK : level;
			}
			else if(ench.equals(Enchantment.KNOCKBACK)) {
				level = level > ConfigManager.PUNCH_AND_KNOCKBACK ? ConfigManager.PUNCH_AND_KNOCKBACK : level;
			}
			else if(ench.equals(Enchantment.FIRE_ASPECT)) {
				level = 0;
			}
			else if(ench.equals(Enchantment.ARROW_FIRE)) {
				level = 0;
			}
			
			resultEnchants.put(ench, level);
		}
		
		Iterator<Entry<Enchantment, Integer>> resultIterator = resultEnchants.entrySet().iterator();
		if(result.getType() == Material.BOOK) {
			result = new ItemStack(Material.ENCHANTED_BOOK);
			EnchantmentStorageMeta m = (EnchantmentStorageMeta) result.getItemMeta();
			while(resultIterator.hasNext()) {
				Entry<Enchantment, Integer> entry = resultIterator.next();
				if(entry.getValue() != 0)
					m.addStoredEnchant(entry.getKey(), entry.getValue(), true);			
			}
			result.setItemMeta(m);
			if(m.getStoredEnchants().size() == 0) {
				return new ItemStack(Material.BOOK);
			}			
		}
		else {
			ItemMeta m = result.getItemMeta();
			while(resultIterator.hasNext()) {
				Entry<Enchantment, Integer> entry = resultIterator.next();
				if(entry.getKey().canEnchantItem(result)) {
					if(entry.getValue() != 0)
						m.addEnchant(entry.getKey(), entry.getValue(), true);
				}
				else {
					Bukkit.getConsoleSender().sendMessage("§c[EnchantmentListener] Can't set " + entry.getKey().getName() + " level " + entry.getValue() + " on " + result.getType());
				}
			}
			result.setItemMeta(m);
		}
		return result;
	}
}
