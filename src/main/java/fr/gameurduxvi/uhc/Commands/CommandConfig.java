package fr.gameurduxvi.uhc.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import en.DevSrSouza.JsonItemStack.JsonItemStack;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Config.ConfigManager;

public class CommandConfig implements CommandExecutor {

	@SuppressWarnings({"unchecked" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length >=1) {
				String arg1 = Main.getInstance().argument(args, 1);
				if(arg1.equalsIgnoreCase("save")) {
					if(ConfigManager.ConfigureStuffPlayerDepart.contains(p)) {
						ConfigManager.ConfigureStuffPlayerDepart.remove(p);
						ConfigManager.StuffDepart.clear();
						for(ItemStack it: p.getInventory()) {
							if(!Main.getInstance().isNull(it) && !it.getType().equals(Material.AIR)) {
								ConfigManager.StuffDepart.add(it);					
							}
						}
						p.getInventory().clear();
						p.sendMessage("§7Config §8>> §aLe stuff de départ a été mit a jour");
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						
						JSONArray ja = new JSONArray();
						for(ItemStack it: ConfigManager.StuffDepart) {
							/*JSONObject joItem = new JSONObject();
							Map<String, Object> map = it.serialize();
							Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
							while(iterator.hasNext()) {
								Entry<String, Object> entry = iterator.next();
								joItem.put(entry.getKey(), entry.getValue());
							}
							ja.add(joItem.toJSONString());*/
							try {
								JSONObject joItem = (JSONObject) new JSONParser().parse(JsonItemStack.toJson(it));
								ja.add(joItem);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						jo.put("stuffDepart", ja);
						
						ConfigManager.saveToFile(jo);
					}
					
					if(ConfigManager.ConfigureStuffPlayerKill.contains(p)) {
						ConfigManager.ConfigureStuffPlayerKill.remove(p);
						ConfigManager.StuffKill.clear();
						for(ItemStack it: p.getInventory()) {
							if(!Main.getInstance().isNull(it) && !it.getType().equals(Material.AIR)) {
								ConfigManager.StuffKill.add(it);
							}
						}
						p.getInventory().clear();
						p.sendMessage("§7Config §8>> §aLe stuff de kill a été mit a jour");
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						
						JSONArray ja = new JSONArray();
						for(ItemStack it: ConfigManager.StuffKill) {
							/*JSONObject joItem = new JSONObject();
							Map<String, Object> map = it.serialize();
							Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
							while(iterator.hasNext()) {
								Entry<String, Object> entry = iterator.next();
								joItem.put(entry.getKey(), entry.getValue());
							}
							ja.add(joItem.toJSONString());*/
							try {
								JSONObject joItem = (JSONObject) new JSONParser().parse(JsonItemStack.toJson(it));
								ja.add(joItem);
							} catch (Exception e) {
								e.printStackTrace();
							}							
						}
						jo.put("stuffKill", ja);
						
						ConfigManager.saveToFile(jo);
					}
				}
				else {
					p.sendMessage("§7Config §8>> §e'" + arg1 + "'§c n'est pas reconnu");
				}
			}			
			ConfigManager.MenuMain((Player) sender);
		}
		else {
			sender.sendMessage("§cSeul les joueurs peuvent faire cette commande");
		}
		return false;
	}
	
	
}
