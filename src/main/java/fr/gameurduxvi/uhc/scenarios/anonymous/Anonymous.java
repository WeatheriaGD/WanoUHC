package fr.gameurduxvi.uhc.scenarios.anonymous;

import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class Anonymous extends Scenario {
	Thread thread = new Thread();

	public Anonymous(String sencarioName, String senarioDescription) {
		super(sencarioName, senarioDescription);
	}
	
	public Anonymous(String sencarioName, String senarioDescription, ItemStack senarioItem) {
		super(sencarioName, senarioDescription, senarioItem);
	}
	
	
	public void stateChange() {
		if(super.getScencarioName().equals(Main.getScenariosManager().ANONYMOUS_NAME)) {
			if(super.isActive()) {
				for(PlayerData pd: fr.gameurduxvi.uhc.Main.getInstance().getPlayersData()) {
					Main.getScenariosManager().changeSkinInstance.changeSkin(pd.getPlayer());
				}
				//thread = new Thread(new thread(this));
				//thread.start();
			}
			else {
				/*try {
					if(thread.isAlive()) {
						thread.stop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				
				/*for(PlayerData pd: fr.gameurduxvi.uhc.Main.getInstance().getPlayersData()) {
					pd.getPersonnage().resetPrefixData();
				}*/
				for(PlayerData pd: fr.gameurduxvi.uhc.Main.getInstance().getPlayersData()) {
					Main.getScenariosManager().changeSkinInstance.changeSkin(pd.getPlayer());
				}
			}
		}
	}
	
	@Override
	public void onStart() {
		stateChange();
	}
}
