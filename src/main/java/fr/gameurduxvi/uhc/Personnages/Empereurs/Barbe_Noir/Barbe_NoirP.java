package fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class Barbe_NoirP extends Personnage {
	public Barbe_NoirP() {
		setId(11);
		setType(1);
		setPriority(3);
		setName("Barbe Noire");
		setPrime(Main.getInstance().primeEmpereur);
		setAmount(1);
		setUltimateRecharge(1);
		setUltimateTime(1);
		setDescription("Choix de ton ulti");
		
		ItemStack it = new ItemStack(Material.RED_ROSE);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName("§aChoix de ton ulti");
		it.setItemMeta(itM);
		setItemUltimate(it);
		
		setShipFileName("barbe_noir");
		
		setFidelePrefix("§8Fidèle ");
		
		
		setAttributedSoundName("wano.roles.attribution.empereurs.barbe_noir");
	}
	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§8Barbe Noire ", "", new int[]{-1}));
	}
	
	@Override
	public void ultimate(PlayerData pd) {
		Inventory inv = Bukkit.createInventory(null, 27, getName());
		
		ItemStack bg = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta bgM = bg.getItemMeta();
		bgM.setDisplayName("§a");
		bg.setItemMeta(bgM);
		
		for(int i = 0; i <= 26; i++) {
			inv.setItem(i, bg);
		}
		
		ItemStack it1 = new ItemStack(Material.BEDROCK, 1);
		ItemMeta it1M = it1.getItemMeta();
		it1M.setDisplayName("§aBlack Hole");
		it1.setItemMeta(it1M);
		inv.setItem(12, it1);
		
		ItemStack it2 = new ItemStack(Material.PACKED_ICE, 1);
		ItemMeta it2M = it2.getItemMeta();
		it2M.setDisplayName("§aShima Yurashi");
		it2.setItemMeta(it2M);
		inv.setItem(14, it2);
		
		pd.getPlayer().openInventory(inv);
	}
}
