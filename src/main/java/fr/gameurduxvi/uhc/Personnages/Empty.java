package fr.gameurduxvi.uhc.Personnages;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Storage.PrefixData;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class Empty extends Personnage{
	
	public Empty() {
		setId(0);
		setPriority(0);
		setName("Empty");
		setPrime(0);
		setAmount(0);
		setUltimateRecharge(0);
		setUltimateTime(0);
		setItemUltimate(new ItemStack(Material.AIR));
		setDescription("");
	}

	@Override
	public void setPrefixData() {
		super.prefixData.add(new PrefixData("§7", "", new int[]{-1}));
	}
	
}
