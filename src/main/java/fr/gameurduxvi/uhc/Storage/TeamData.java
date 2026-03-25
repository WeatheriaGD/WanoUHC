package fr.gameurduxvi.uhc.Storage;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;

public class TeamData {
	
	public static int maxPerTeam = 4;
	
	private boolean teleport = true;
	private Location shipLocation = null;
	private int linkedId = 0;
	private ArrayList<Player> playersInZone = new ArrayList<>();
	private int max;
	private int shipTimer = 30;
	private String teamSuffix = "";
	private String schematicName = "ship";
	
	
	
	public TeamData(Location loc, int max) {
		this.shipLocation = loc;
		this.max = max;
	}
	
	public TeamData(int max) {
		this.max = max;
	}
	
	
	
	/*******************************************************
	 * Get Functions
	 *******************************************************/
	public boolean canTeleport() {
		return teleport;
	}
	
	public Location getShipLocation() {
		return shipLocation;
	}
	
	public int getLinkedId() {
		return linkedId;
	}
	
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> list = new ArrayList<>();
		for(PlayerData pd: Main.getInstance().getPlayersData()) {
			if(!Main.getInstance().isNull(pd.getTeam()) && pd.getTeam().equals(this)) {
				list.add(pd.getPlayer());
			}
		}
		return list;
	}
	
	public ArrayList<Player> getPlayersInZone() {
		return playersInZone;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getShipTimer() {
		return shipTimer;
	}
	
	public String getTeamSuffix() {
		return teamSuffix;
	}
	
	public String getSchematicName() {
		return schematicName;
	}
	
	
	
	/*******************************************************
	 * Set Functions
	 *******************************************************/
	public TeamData setTeleport(boolean enabled) {
		this.teleport = enabled;
		return this;
	}
	
	public TeamData setLocation(Location shipLocation) {
		this.shipLocation = shipLocation;
		return this;
	}
	
	public TeamData setLinkedId(int linkedId) {
		this.linkedId = linkedId;
		return this;
	}
	
	public TeamData setMax(int max) {
		this.max = max;
		return this;
	}
	
	public TeamData setTimer(int shipTimer) {
		this.shipTimer = shipTimer;
		return this;
	}
	
	public TeamData setTeamSuffix(String teamSuffix) {
		this.teamSuffix = teamSuffix;
		return this;
	}
	
	public TeamData setSchematicName(String schematicName) {
		this.schematicName = schematicName;
		return this;
	}
	
	
	/*******************************************************
	 * Other Functions
	 *******************************************************/
	@SuppressWarnings("unchecked")
	public static void autoGenerateTeams() {
		Main.getInstance().getTeamData().clear();
		
		ArrayList<Personnage> emperors = new ArrayList<>();
		for(Personnage pers: Main.getInstance().getPersonages()) {
			if(pers.getType() == 1 && pers.getAmount() > 0) {
				for(int time = 0; time < pers.getAmount(); time++) {
					emperors.add(pers);					
				}
			}
		}
		
		double pirates = 0;
		for(Personnage pers: Main.getInstance().getPersonages()) {
			if(pers.getType() != 1) {
				pirates += pers.getAmount();
			}
		}
		
		int amountTeams = (int) (emperors.size() + Math.ceil(pirates / TeamData.maxPerTeam));
		
		JSONObject jo = ConfigManager.getFileJSONObject();
		JSONArray jaTeams = new JSONArray();
		
		for(int i = 0; i < amountTeams; i++) {
			TeamData td = null;
			if(i < emperors.size()) {
				td = new TeamData(1);
				td.setLinkedId(emperors.get(i).getId());
				td.setSchematicName(emperors.get(i).getShipFileName());
			}
			else {
				int maxTeams = 0;
				for(TeamData td2: Main.getInstance().getTeamData()) {
					maxTeams += td2.getMax();
				}
				int maxPers = 0;
				for(Personnage pers: Main.getInstance().getPersonages()) {
					maxPers += pers.getAmount();
				}
				int free = maxPers - maxTeams;
				if(free > maxPerTeam) {
					td = new TeamData(maxPerTeam);					
				}
				else {
					td = new TeamData(free);
				}
			}
			
			JSONObject joTeam = new JSONObject();
			
			joTeam.put("max", td.getMax());
			joTeam.put("linkedId", td.getLinkedId());
			
			jaTeams.add(joTeam);
			
			Main.getInstance().getTeamData().add(td);
		}
		jo.put("teams", jaTeams);
		ConfigManager.saveToFile(jo);
	}
	
	@SuppressWarnings("unchecked")
	public static void checkEmperors() {
		ArrayList<TeamData> bcp = new ArrayList<>();
		
		// Backuping pirate teams
		for(TeamData td: Main.getInstance().getTeamData()) {
			if(td.getLinkedId() == 0) {
				bcp.add(td);
			}
		}
		
		// clearing whole database
		Main.getInstance().getTeamData().clear();
		
		JSONObject jo = ConfigManager.getFileJSONObject();
		JSONArray jaTeams = new JSONArray();
		
		// adding emperors
		for(Personnage pers: Main.getInstance().getPersonages()) {
			
			if(pers.getType() == 1 && pers.getAmount() > 0) {
				for(int time = 0; time < pers.getAmount(); time++) {
					TeamData td = new TeamData(1);
					td.setLinkedId(pers.getId());
					td.setSchematicName(pers.getShipFileName());
					
					JSONObject joTeam = new JSONObject();
					
					joTeam.put("max", td.getMax());
					joTeam.put("linkedId", td.getLinkedId());
					
					jaTeams.add(joTeam);
					
					Main.getInstance().getTeamData().add(td);
				}
			}
			
		}
		
		// adding back pirates teams
		for(TeamData td: bcp) Main.getInstance().getTeamData().add(td);
		
		jo.put("teams", jaTeams);
		ConfigManager.saveToFile(jo);
	}
	
	public boolean hasFidele() {
		for(Player lp: getPlayers()) {
			if(PlayerData.hasPlayerData(lp) && PlayerData.getPlayerData(lp).getPersonnage().getType() == 2) {
				return true;
			}
		}
		return false;
	}
}
