package fr.gameurduxvi.uhc.scenarios.anonymous;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import fr.gameurduxvi.uhc.Main;

public class PlayerJoinListener implements Listener {
	@SuppressWarnings({ "unchecked", "resource" })
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		GameProfile gp = ((CraftPlayer)p).getProfile();
		String value = "";
		String signature = "";
		for(Property pr: gp.getProperties().values()) {
			if(pr.getName().equals("textures")) {
				value = pr.getValue();
				signature = pr.getSignature();
			}
		}
		//Main.instance.skinData.add(new SkinDatabase(p, value, signature));
		
		JSONArray ja = null;
		if(new File(Main.getInstance().pluginDir + "skins.json").exists()) {
			try {
				Object obj = new JSONParser().parse(new FileReader(Main.getInstance().pluginDir + "skins.json"));
				ja = (JSONArray) obj;
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		else {
			ja = new JSONArray();
		}
		
		if(value.equals(Main.getScenariosManager().SkinValue)) {
			return;
		}
		
		boolean exist = false;
		JSONObject existObject = null;
		Iterator<JSONObject> iterator = ja.iterator();
		while(iterator.hasNext()) {
			JSONObject jo = iterator.next();
			if(jo.get("uuid").equals(e.getPlayer().getUniqueId().toString())) {
				exist = true;
				existObject = jo;
				break;
			}
		}
		if(exist) {
			ja.remove(existObject);
		}
		
		JSONObject newJo = new JSONObject();
		newJo.put("uuid", e.getPlayer().getUniqueId().toString());
		newJo.put("value", value);
		newJo.put("signature", signature);
		
		ja.add(newJo);
		
		try {
			FileWriter file = new FileWriter(Main.getInstance().pluginDir + "skins.json");
			
			file.write(ja.toJSONString());
			file.flush();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		

		Main.getScenariosManager().changeSkinInstance.changeSkin(p);
		
		/*for(Scenario sc: fr.gameurduxvi.uhc.Main.getInstance().getScenarios()) {
			if(sc instanceof Anonymous) {
				if(sc.isActive()) {
					Personnage pers = PlayerData.getPlayerData(p).getPersonnage();
					pers.resetPrefixData();
					for(PrefixData prf: pers.getPrefixData()) {
						prf.setPrefix(prf.getPrefix() + "§k");
					}
					
				}
			}
		}*/
		//Main.getScenariosManager().refresh();
	}
}
