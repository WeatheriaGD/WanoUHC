package fr.gameurduxvi.uhc.scenarios.anonymous;

public class thread {
	
	Anonymous anonymous;
	
	public thread(Anonymous anonymous) {
		this.anonymous = anonymous;
	}

	/*@Override
	public void run() {
		while(anonymous.isActive()) {
			try {
				Thread.sleep(10);
				for(PlayerData pd: fr.gameurduxvi.uhc.Main.getInstance().getPlayersData()) {
					Personnage pers = pd.getPersonnage();
					pers.resetPrefixData();
					for(PrefixData prf: pers.getPrefixData()) {
						prf.setPrefix(prf.getPrefix() + "§k");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}*/
}
