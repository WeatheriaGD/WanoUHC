package fr.gameurduxvi.uhc.SuperClasses;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Main;

public abstract class Scenario {
	private String scencarioName;
	private String scenarioDescription;
	private boolean active = false;
	private ItemStack scenarioItem;
	private Inventory configInv = null;
	
	public Scenario(String sencarioName, String senarioDescription) {
		this.scencarioName = sencarioName;
		this.scenarioDescription = senarioDescription;
		scenarioItem = new ItemStack(Material.NAME_TAG);
	}
	
	public Scenario(String sencarioName, String senarioDescription, ItemStack senarioItem) {
		this.scencarioName = sencarioName;
		this.scenarioDescription = senarioDescription;
		this.scenarioItem = senarioItem;
	}
	
	/*******************************************************
	 * Get Functions
	 *******************************************************/
	public String getScencarioName() {
		return scencarioName;
	}
	
	public String getScenarioDescription() {
		return scenarioDescription;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public ItemStack getScenarioItem() {
		return scenarioItem;
	}
	
	public Inventory getConfigInv() {
		return configInv;
	}
	
	/*******************************************************
	 * Set Functions
	 *******************************************************/
	
	public void setScencarioName(String scencarioName) {
		this.scencarioName = scencarioName;
	}
	
	public void setScenarioDescription(String scenarioDescription) {
		this.scenarioDescription = scenarioDescription;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		stateChange();
	}
	
	public void setScenarioItem(ItemStack scenarioItem) {
		this.scenarioItem = scenarioItem;
	}
	
	public void setConfigInv(Inventory configInv) {
		this.configInv = configInv;
	}
	
	/*******************************************************
	 * Other Functions
	 *******************************************************/
	
	public abstract void stateChange();	
	public abstract void onStart();
	
	/*public static Inventory getScenarioPanel(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 45, "Sc�narios Page " + page);
		
		ItemStack bg = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta bgM = bg.getItemMeta();
		bgM.setDisplayName("�a");
		bg.setItemMeta(bgM);
		
		for(int i = 0; i <= 44; i++) {
			inv.setItem(i, bg);
		}
		
		if(Main.getInstance().getScenarios().size() == 0) {
			ItemStack it = new ItemStack(Material.BARRIER);
			ItemMeta itM = it.getItemMeta();
			itM.setDisplayName("�cAucun sc�nario n'� �t� charg� !");
			it.setItemMeta(itM);
			inv.setItem(22, it);
		}
		
		int i = 0;
		int slot = 0;
		for(Scenario sc: Main.getInstance().getScenarios()) {
			i++;
			if(i > (7 * (page - 1)) && i <= (7 * page)) {
				slot++;
				ItemStack it = sc.getScenarioItem();
				ItemMeta itM = it.getItemMeta();
				itM.setDisplayName("�b" + sc.getScencarioName());
				
				String[] split = sc.getScenarioDescription().split("\n");
				itM.setLore(Arrays.asList(split));
				ArrayList<String> lore = new ArrayList<>();
				for(String line: itM.getLore()) {
					lore.add("�7" + line);
				}
				itM.setLore(lore);
				
				it.setItemMeta(itM);
				inv.setItem(9 + slot, it);
				
				ItemStack itA = null;
				ItemMeta itAM = null;
				if(sc.isActive()) {
					itA = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
					itAM = itA.getItemMeta();
					itAM.setDisplayName("�aActif");
				}
				else {
					itA = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
					itAM = itA.getItemMeta();
					itAM.setDisplayName("�cNon actif");
				}
				
				
				itA.setItemMeta(itAM);
				inv.setItem(18 + slot, itA);
				
				try {
					if(!sc.getConfigInv().equals(null)) {
						ItemStack itC = null;
						ItemMeta itCM = null;
						if(sc.isActive()) {
							itC = new ItemStack(Material.REDSTONE_BLOCK);
						}
						else {
							itC = new ItemStack(Material.STONE);
						}
						itCM = itC.getItemMeta();
						itCM.setDisplayName("�7Menu de configuration");
						itC.setItemMeta(itCM);
						inv.setItem(27 + slot, itC);
					}
				} catch (Exception e) {
					e.getStackTrace();
				}
				
			}
		}
		
		if(page > 1) {
			ItemStack previous = new ItemStack(Material.ARROW);
			ItemMeta previousM = previous.getItemMeta();
			previousM.setDisplayName("�7Retour");
			previous.setItemMeta(previousM);
			
			inv.setItem(39, previous);
		}		
		
		if(Main.getInstance().getScenarios().size() > (7 * page)) {
			ItemStack back = new ItemStack(Material.ARROW);
			ItemMeta backM = back.getItemMeta();
			backM.setDisplayName("�7Prochain");
			back.setItemMeta(backM);
			
			inv.setItem(41, back);
		}
		return inv;
	}*/
	
	public static boolean isActive(String scenarioName) {
		for(Scenario sc: Main.getInstance().getScenarios()) {
			if(sc.getScencarioName().equals(scenarioName)) {
				return sc.isActive();
			}
		}
		System.out.println(Main.getInstance().pluginPrefix + " §cScenario " + scenarioName + " non trouvé !");
		return false;
	}
}
