package fr.gameurduxvi.uhc.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Commands.CommandTrilog;
import fr.gameurduxvi.uhc.Config.ConfigManager;
import fr.gameurduxvi.uhc.Config.ConfigPanels;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir.Barbe_Noir1;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir.Barbe_Noir2;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Barbe_Noir.Barbe_NoirP;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom.Big_Mom1;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom.Big_Mom2;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Big_Mom.Big_MomP;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Kaido.Kaido1;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Kaido.Kaido2;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Kaido.KaidoP;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Shanks.Shanks1;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Shanks.Shanks2;
import fr.gameurduxvi.uhc.Personnages.Empereurs.Shanks.ShanksP;
import fr.gameurduxvi.uhc.Storage.PlayerData;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onClickTrilogCraft(InventoryClickEvent e) {
		if(!Main.getInstance().isNull(e.getClickedInventory()) && !Main.getInstance().isNull(e.getClickedInventory().getName()) && e.getClickedInventory().getName().equals(CommandTrilog.INVENTORY_NAME)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClickEmepreursInv(InventoryClickEvent e) {
		if(!Main.getInstance().isNull(e.getClickedInventory())) {
			Player p = (Player) e.getWhoClicked();
			if(!PlayerData.hasPlayerData(p)) return;
			PlayerData pd = PlayerData.getPlayerData(p);
			if(e.getClickedInventory().getName().contains(pd.getPersonnage().getName())) {
				e.setCancelled(true);
				ItemStack ultiItem = pd.getPersonnage().ultimateItem();
				boolean accept = false;
				
				if(pd.getPersonnage() instanceof Barbe_NoirP) {
					if(e.getSlot() == 12) {
						pd.setPersonnage(new Barbe_Noir1());
						accept = true;
					}
					else if(e.getSlot() == 14) {
						pd.setPersonnage(new Barbe_Noir2());
						accept = true;
					}
				}
				
				if(pd.getPersonnage() instanceof ShanksP) {
					if(e.getSlot() == 12) {
						pd.setPersonnage(new Shanks1());
						accept = true;
					}
					else if(e.getSlot() == 14) {
						pd.setPersonnage(new Shanks2());
						accept = true;
					}
				}
				
				if(pd.getPersonnage() instanceof KaidoP) {
					if(e.getSlot() == 12) {
						pd.setPersonnage(new Kaido1());
						accept = true;
					}
					else if(e.getSlot() == 14) {
						pd.setPersonnage(new Kaido2());
						accept = true;
					}
				}
				
				if(pd.getPersonnage() instanceof Big_MomP) {
					if(e.getSlot() == 12) {
						pd.setPersonnage(new Big_Mom1());
						accept = true;
					}
					else if(e.getSlot() == 14) {
						pd.setPersonnage(new Big_Mom2());
						accept = true;
					}
				}
				
				if(accept) {
					for(ItemStack it:pd.getPlayer().getInventory()) {
						if(Main.getInstance().isNull(it)) continue;
						if(Main.getInstance().isNull(it.getItemMeta())) continue;
						if(Main.getInstance().isNull(it.getItemMeta().getDisplayName())) continue;
						if(it.getItemMeta().getDisplayName().contains(ultiItem.getItemMeta().getDisplayName())) {
							pd.getPlayer().getInventory().remove(it);
						}
					}
					p.closeInventory();
					p.getInventory().addItem(pd.getPersonnage().ultimateItem());
					p.sendMessage("§7Vous avez choisis " + e.getClickedInventory().getItem(e.getSlot()).getItemMeta().getDisplayName());
					pd.setCanUltimate(true);
				}
			}
		}
	}
	
	
	/*	░█████╗░░█████╗░███╗░░██╗███████╗██╗░██████╗░  ███╗░░░███╗███████╗███╗░░██╗██╗░░░██╗
		██╔══██╗██╔══██╗████╗░██║██╔════╝██║██╔════╝░  ████╗░████║██╔════╝████╗░██║██║░░░██║
		██║░░╚═╝██║░░██║██╔██╗██║█████╗░░██║██║░░██╗░  ██╔████╔██║█████╗░░██╔██╗██║██║░░░██║
		██║░░██╗██║░░██║██║╚████║██╔══╝░░██║██║░░╚██╗  ██║╚██╔╝██║██╔══╝░░██║╚████║██║░░░██║
		╚█████╔╝╚█████╔╝██║░╚███║██║░░░░░██║╚██████╔╝  ██║░╚═╝░██║███████╗██║░╚███║╚██████╔╝
		░╚════╝░░╚════╝░╚═╝░░╚══╝╚═╝░░░░░╚═╝░╚═════╝░  ╚═╝░░░░░╚═╝╚══════╝╚═╝░░╚══╝░╚═════╝░*/
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onClickConfig(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getClickedInventory();
		ItemStack it = e.getCurrentItem();
		if(Main.getInstance().isNull(it)) return;
		ItemMeta itM = it.getItemMeta();
		if(Main.getInstance().isNull(itM)) return;
		
		if(inv.getName().contains(ConfigPanels.MAIN)) {
			e.setCancelled(true);
			if(itM.getDisplayName().contains(ConfigManager.MAIN_ROLES)) {
				ConfigManager.MenuRole(p);
			}
			else if(itM.getDisplayName().contains(ConfigManager.MAIN_TIMERS)) {
				ConfigManager.MenuGameEvents(p);
			}
			else if(itM.getDisplayName().contains(ConfigManager.MAIN_SCENARIO)) {
				ConfigManager.MenuScenario(p, 1);
			}
			else if(itM.getDisplayName().contains(ConfigManager.MAIN_STUFF)) {
				ConfigManager.MenuStuff(p);
			}
			else if(itM.getDisplayName().contains(ConfigManager.MAIN_ADVANCED_CONFIG)) {
				ConfigManager.MenuAdvanced(p);
			}
			else if(itM.getDisplayName().contains(ConfigManager.MAIN_ENCHANTS)) {
				ConfigManager.MenuEnchant(p);
			}
			else if(itM.getDisplayName().contains(ConfigManager.MAIN_TEAMS)) {
				ConfigManager.MenuTeams(p, 1);
			}
		}
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.ROLES)) {
			e.setCancelled(true);

			if(itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuMain(p);
			}
			
			if(!Main.GAMESTATE.equals(GameState.Idle)) {
				return;
			}
			
			for(Personnage pers: Main.getInstance().getPersonages()) {
				if(itM.getDisplayName().contains(pers.getName())) {
					if(e.getClick().equals(ClickType.LEFT)) {
						pers.setAmount(pers.getAmount() + 1);
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						JSONObject joRoles = ConfigManager.validJSONObject((JSONObject) jo.get("roles"));
						joRoles.remove("" + pers.getId());
						joRoles.put("" + pers.getId(), pers.getAmount());
						jo.put("roles", joRoles);
						
						ConfigManager.saveToFile(jo);
					}
					else if(e.getClick().equals(ClickType.RIGHT)) {
						if(pers.getAmount() > 0) {
							pers.setAmount(pers.getAmount() - 1);
							
							JSONObject jo = ConfigManager.getFileJSONObject();
							JSONObject joRoles = ConfigManager.validJSONObject((JSONObject) jo.get("roles"));
							joRoles.remove("" + pers.getId());
							joRoles.put("" + pers.getId(), pers.getAmount());
							jo.put("roles", joRoles);
							
							ConfigManager.saveToFile(jo);
						}
					}
					else if(e.getClick().equals(ClickType.DROP)) {
						try {
							pers.setAmount(pers.getClass().newInstance().getAmount());
						} catch (InstantiationException | IllegalAccessException e1) {
							e1.printStackTrace();
						}
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						JSONObject joRoles = ConfigManager.validJSONObject((JSONObject) jo.get("roles"));
						joRoles.remove("" + pers.getId());
						joRoles.put("" + pers.getId(), pers.getAmount());
						jo.put("roles", joRoles);
						
						ConfigManager.saveToFile(jo);
					}
					if(pers.getType() == 1) {
						TeamData.checkEmperors();
					}
					ConfigManager.MenuRole(p);
					break;
				}
			}
		}
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.SCENARIO)) {
			e.setCancelled(true);
			if(itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuMain(p);
			}
			int page = Integer.parseInt(inv.getName().replace(ConfigPanels.SCENARIO, ""));
			for(Scenario sc: Main.getInstance().getScenarios()) {
				if(itM.getDisplayName().equals("§b" + sc.getScencarioName())) {
					sc.setActive(!sc.isActive());
					JSONObject jo = ConfigManager.getFileJSONObject();
					JSONObject joScenario = ConfigManager.validJSONObject((JSONObject) jo.get("scenario"));
					JSONObject joSigleScenario = ConfigManager.validJSONObject((JSONObject) joScenario.get(sc.getClass().getName()));
					joSigleScenario.put("state", sc.isActive());
					joScenario.put(sc.getClass().getName(), joSigleScenario);
					jo.put("scenario", joScenario);
					
					ConfigManager.saveToFile(jo);
					
					ConfigManager.MenuScenario(p, page);
					break;
				}
			}
			if(e.getSlot() >= 20 && e.getSlot() <= 24) {
				if(!e.getClickedInventory().getItem(e.getSlot()).getType().equals(Material.REDSTONE_BLOCK) && !e.getClickedInventory().getItem(e.getSlot()).getType().equals(Material.STONE)) {
					return;
				}
				for(Scenario sc: Main.getInstance().getScenarios()) {
					if(!e.getClickedInventory().getItem(e.getSlot() - 9).getItemMeta().equals(null)) {
						if(e.getClickedInventory().getItem(e.getSlot() - 9).getItemMeta().getDisplayName().contains(sc.getScencarioName())) {
							p.openInventory(sc.getConfigInv());
						}
					}
				}
			}
		}
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.GAME_EVENTS)) {
			e.setCancelled(true);
			
			if(itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuMain(p);
			}

			if(!Main.GAMESTATE.equals(GameState.Idle)) {
				return;
			}
			
			for(GameEvent ge: Main.getInstance().getGameEvents()) {
				if(itM.getDisplayName().equals("§b" + ge.getName())) {
					if(e.getClick().equals(ClickType.LEFT)) {
						ge.setSecond(ge.getSecond() + 1);
						
						if(ge.getSecond() >= 60) {
							ge.setMinute(ge.getMinute() + 1);
							ge.setSecond(0);
						}
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						JSONObject joGameEvent = ConfigManager.validJSONObject((JSONObject) jo.get("gameEvents"));
						JSONObject joGameEventSecond = ConfigManager.validJSONObject((JSONObject) jo.get(ge.getClass().getName()));
						joGameEventSecond.remove("second");
						joGameEventSecond.remove("minute");
						joGameEventSecond.put("second", ge.getSecond());
						joGameEventSecond.put("minute", ge.getMinute());
						joGameEvent.put(ge.getClass().getName(), joGameEventSecond);
						jo.put("gameEvents", joGameEvent);
						
						ConfigManager.saveToFile(jo);
					}
					else if(e.getClick().equals(ClickType.RIGHT)) {
						ge.setSecond(ge.getSecond() - 1);
						
						if(ge.getSecond() < 0) {
							if(ge.getMinute() > 0) {
								ge.setSecond(59);
								ge.setMinute(ge.getMinute() - 1);
							}
							else {
								ge.setSecond(0);
							}
						}
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						JSONObject joGameEvent = ConfigManager.validJSONObject((JSONObject) jo.get("gameEvents"));
						JSONObject joGameEventSecond = ConfigManager.validJSONObject((JSONObject) jo.get(ge.getClass().getName()));
						joGameEventSecond.remove("second");
						joGameEventSecond.remove("minute");
						joGameEventSecond.put("second", ge.getSecond());
						joGameEventSecond.put("minute", ge.getMinute());
						joGameEvent.put(ge.getClass().getName(), joGameEventSecond);
						jo.put("gameEvents", joGameEvent);
						
						ConfigManager.saveToFile(jo);
					}
					else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
						ge.setMinute(ge.getMinute() + 1);
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						JSONObject joGameEvent = ConfigManager.validJSONObject((JSONObject) jo.get("gameEvents"));
						JSONObject joGameEventSecond = ConfigManager.validJSONObject((JSONObject) jo.get(ge.getClass().getName()));
						joGameEventSecond.remove("second");
						joGameEventSecond.remove("minute");
						joGameEventSecond.put("second", ge.getSecond());
						joGameEventSecond.put("minute", ge.getMinute());
						joGameEvent.put(ge.getClass().getName(), joGameEventSecond);
						jo.put("gameEvents", joGameEvent);
						
						ConfigManager.saveToFile(jo);
					}
					else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
						ge.setMinute(ge.getMinute() - 1);
						
						if(ge.getMinute() < 0) {
							ge.setMinute(0);
						}
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						JSONObject joGameEvent = ConfigManager.validJSONObject((JSONObject) jo.get("gameEvents"));
						JSONObject joGameEventSecond = ConfigManager.validJSONObject((JSONObject) jo.get(ge.getClass().getName()));
						joGameEventSecond.remove("second");
						joGameEventSecond.remove("minute");
						joGameEventSecond.put("second", ge.getSecond());
						joGameEventSecond.put("minute", ge.getMinute());
						joGameEvent.put(ge.getClass().getName(), joGameEventSecond);
						jo.put("gameEvents", joGameEvent);
						
						ConfigManager.saveToFile(jo);
					}
					else if(e.getClick().equals(ClickType.DROP)) {
						try {
							GameEvent copyGe = ge.getClass().newInstance();
							ge.setMinute(copyGe.getMinute());
							ge.setSecond(copyGe.getSecond());
						} catch (InstantiationException | IllegalAccessException e1) {
							e1.printStackTrace();
						}
						
						JSONObject jo = ConfigManager.getFileJSONObject();
						JSONObject joGameEvent = ConfigManager.validJSONObject((JSONObject) jo.get("gameEvents"));
						JSONObject joGameEventSecond = ConfigManager.validJSONObject((JSONObject) jo.get(ge.getClass().getName()));
						joGameEventSecond.remove("second");
						joGameEventSecond.remove("minute");
						joGameEventSecond.put("second", ge.getSecond());
						joGameEventSecond.put("minute", ge.getMinute());
						joGameEvent.put(ge.getClass().getName(), joGameEventSecond);
						jo.put("gameEvents", joGameEvent);
						
						ConfigManager.saveToFile(jo);
					}
					ConfigManager.MenuGameEvents(p);
					break;
				}				
			}
		}
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.STUFF)) {
			e.setCancelled(true);
			if(itM.getDisplayName().contains(ConfigManager.STUFF_DEPART)) {
				ConfigManager.MenuStuffDepart(p);
			}
			else if(itM.getDisplayName().contains(ConfigManager.STUFF_KILL)) {
				ConfigManager.MenuStuffKill(p);
			}
			else if(itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuMain(p);
			}
		}
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.STUFF_DEPART)) {
			e.setCancelled(true);
			if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.STUFF_MODIFY)) {
				p.getInventory().clear();
				for(ItemStack item: ConfigManager.StuffDepart) {
					p.getInventory().addItem(item);
				}
				ConfigManager.ConfigureStuffPlayerDepart.add(p);
				p.sendMessage("§7Config §8>> §bFaites §e/config save §bpour sauvegarder les modifications faites pour le stuff");
				p.closeInventory();
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuStuff(p);
			}
		}
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.STUFF_KILL)) {
			e.setCancelled(true);
			if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.STUFF_MODIFY)) {
				p.getInventory().clear();
				for(ItemStack item: ConfigManager.StuffKill) {
					p.getInventory().addItem(item);
				}
				ConfigManager.ConfigureStuffPlayerKill.add(p);
				p.sendMessage("§7Config §8>> §bFaites §e/config save §bpour sauvegarder les modifications faites pour le stuff");
				p.closeInventory();
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuStuff(p);
			}
		}
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.AVANCE)) {
			e.setCancelled(true);
			if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.AVANCE_APPLE)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					ConfigManager.APPLE_DROP += 1;
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.APPLE_DROP > 0) {
						ConfigManager.APPLE_DROP -= 1;
					}
					else {
						ConfigManager.APPLE_DROP = 0;
					}
				}
				else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
					ConfigManager.APPLE_DROP += 10;
				}
				else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
					if(ConfigManager.APPLE_DROP > 0) {
						ConfigManager.APPLE_DROP -= 10;
						if(ConfigManager.APPLE_DROP < 0) {
							ConfigManager.APPLE_DROP = 0;
						}
					}
					else {
						ConfigManager.APPLE_DROP = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.APPLE_DROP = ConfigManager.DEFAULT_APPLE_DROP;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("advanced"));
				joAdvanced.put("appleDrop", ConfigManager.APPLE_DROP);
				jo.put("advanced", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuAdvanced(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.AVANCE_FLINT)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					ConfigManager.FLINT_DROP += 1;
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.FLINT_DROP > 0) {
						ConfigManager.FLINT_DROP -= 1;
					}
					else {
						ConfigManager.FLINT_DROP = 0;
					}
				}
				else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
					ConfigManager.FLINT_DROP += 10;
				}
				else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
					if(ConfigManager.FLINT_DROP > 0) {
						ConfigManager.FLINT_DROP -= 10;
						if(ConfigManager.FLINT_DROP < 0) {
							ConfigManager.FLINT_DROP = 0;
						}
					}
					else {
						ConfigManager.FLINT_DROP = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.FLINT_DROP = ConfigManager.DEFAULT_FLINT_DROP;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("advanced"));
				joAdvanced.put("flintDrop", ConfigManager.FLINT_DROP);
				jo.put("advanced", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuAdvanced(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.AVANCE_PEARL)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					ConfigManager.PEARL_DROP += 1;
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.PEARL_DROP > 0) {
						ConfigManager.PEARL_DROP -= 1;
					}
					else {
						ConfigManager.PEARL_DROP = 0;
					}
				}
				else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
					ConfigManager.PEARL_DROP += 10;
				}
				else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
					if(ConfigManager.PEARL_DROP > 0) {
						ConfigManager.PEARL_DROP -= 10;
						if(ConfigManager.PEARL_DROP < 0) {
							ConfigManager.PEARL_DROP = 0;
						}
					}
					else {
						ConfigManager.PEARL_DROP = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.PEARL_DROP = ConfigManager.DEFAULT_PEARL_DROP;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("advanced"));
				joAdvanced.put("pearlDrop", ConfigManager.PEARL_DROP);
				jo.put("advanced", joAdvanced);
				
				ConfigManager.saveToFile(jo);

				ConfigManager.MenuAdvanced(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.AVANCE_STRENGHT)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					ConfigManager.DAMAGE_AMPLIFIER += 1;
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.DAMAGE_AMPLIFIER > 1) {
						ConfigManager.DAMAGE_AMPLIFIER -= 1;
					}
					else {
						ConfigManager.DAMAGE_AMPLIFIER = 1;
					}
				}
				else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
					ConfigManager.DAMAGE_AMPLIFIER += 10;
				}
				else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
					if(ConfigManager.DAMAGE_AMPLIFIER > 1) {
						ConfigManager.DAMAGE_AMPLIFIER -= 10;
						if(ConfigManager.DAMAGE_AMPLIFIER < 1) {
							ConfigManager.DAMAGE_AMPLIFIER = 1;
						}
					}
					else {
						ConfigManager.DAMAGE_AMPLIFIER = 1;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.DAMAGE_AMPLIFIER = ConfigManager.DEFAULT_DAMAGE_AMPLIFIER;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("advanced"));
				joAdvanced.put("damageAmplifier", ConfigManager.DAMAGE_AMPLIFIER);
				jo.put("advanced", joAdvanced);
				
				ConfigManager.saveToFile(jo);

				ConfigManager.MenuAdvanced(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.AVANCE_XP)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					ConfigManager.EXPERIENCE_AMPLIFIER += 1;
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.EXPERIENCE_AMPLIFIER > 0) {
						ConfigManager.EXPERIENCE_AMPLIFIER -= 1;
						if(ConfigManager.EXPERIENCE_AMPLIFIER < 0) {
							ConfigManager.EXPERIENCE_AMPLIFIER = 0;
						}
					}
					else {
						ConfigManager.EXPERIENCE_AMPLIFIER = 0;
					}
				}
				else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
					ConfigManager.EXPERIENCE_AMPLIFIER += 10;				
				}
				else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
					if(ConfigManager.EXPERIENCE_AMPLIFIER > 0) {
						ConfigManager.EXPERIENCE_AMPLIFIER -= 10;
						if(ConfigManager.EXPERIENCE_AMPLIFIER < 0) {
							ConfigManager.EXPERIENCE_AMPLIFIER = 0;
						}
					}
					else {
						ConfigManager.EXPERIENCE_AMPLIFIER = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.EXPERIENCE_AMPLIFIER = ConfigManager.DEFAULT_EXPERIENCE_AMPLIFIER;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("advanced"));
				joAdvanced.put("experienceAmplifier", ConfigManager.EXPERIENCE_AMPLIFIER);
				jo.put("advanced", joAdvanced);
				
				ConfigManager.saveToFile(jo);

				ConfigManager.MenuAdvanced(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.AVANCE_LAPIS)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					ConfigManager.LAPIS = !ConfigManager.LAPIS;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("advanced"));
				joAdvanced.put("lapis", ConfigManager.LAPIS);
				jo.put("advanced", joAdvanced);
				
				ConfigManager.saveToFile(jo);

				ConfigManager.MenuAdvanced(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuMain(p);
			}
		}
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.ENCHANTS)) {
			e.setCancelled(true);
			
			
			if(it.hasItemMeta() && itM.getDisplayName().contains("§aControle des enchantements: ")) {
				
				ConfigManager.CONTROL_ENCHANTS = !ConfigManager.CONTROL_ENCHANTS;
				
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("enchants"));
				joAdvanced.put("controlEnchants", ConfigManager.CONTROL_ENCHANTS);
				jo.put("enchants", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuEnchant(p);
			}
			
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.ENCHANT_DIAMOND_PROTECTION)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					if(ConfigManager.DIAMOND_PROTECTION < ConfigManager.DEFAULT_DIAMOND_PROTECTION) {
						ConfigManager.DIAMOND_PROTECTION += 1;
					}
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.DIAMOND_PROTECTION > 0) {
						ConfigManager.DIAMOND_PROTECTION -= 1;
					}
					else {
						ConfigManager.DIAMOND_PROTECTION = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.DIAMOND_PROTECTION = ConfigManager.DEFAULT_DIAMOND_PROTECTION;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("enchants"));
				joAdvanced.put("diamondProtection", ConfigManager.DIAMOND_PROTECTION);
				jo.put("enchants", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuEnchant(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.ENCHANT_DIAMOND_SHARPNESS)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					if(ConfigManager.DIAMOND_SHARPNESS < ConfigManager.DEFAULT_DIAMOND_SHARPNESS) {
						ConfigManager.DIAMOND_SHARPNESS += 1;
					}
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.DIAMOND_SHARPNESS > 0) {
						ConfigManager.DIAMOND_SHARPNESS -= 1;
					}
					else {
						ConfigManager.DIAMOND_SHARPNESS = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.DIAMOND_SHARPNESS = ConfigManager.DEFAULT_DIAMOND_SHARPNESS;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("enchants"));
				joAdvanced.put("diamondSharpness", ConfigManager.DIAMOND_SHARPNESS);
				jo.put("enchants", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuEnchant(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.ENCHANT_POWER)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					if(ConfigManager.POWER < ConfigManager.DEFAULT_POWER) {
						ConfigManager.POWER += 1;
					}
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.POWER > 0) {
						ConfigManager.POWER -= 1;
					}
					else {
						ConfigManager.POWER = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.POWER = ConfigManager.DEFAULT_POWER;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("enchants"));
				joAdvanced.put("power", ConfigManager.POWER);
				jo.put("enchants", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuEnchant(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.ENCHANT_IRON_PROTECTION)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					if(ConfigManager.IRON_PROTECTION < ConfigManager.DEFAULT_IRON_PROTECTION) {
						ConfigManager.IRON_PROTECTION += 1;
					}
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.IRON_PROTECTION > 0) {
						ConfigManager.IRON_PROTECTION -= 1;
					}
					else {
						ConfigManager.IRON_PROTECTION = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.IRON_PROTECTION = ConfigManager.DEFAULT_IRON_PROTECTION;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("enchants"));
				joAdvanced.put("ironProtection", ConfigManager.IRON_PROTECTION);
				jo.put("enchants", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuEnchant(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.ENCHANT_IRON_SHARPNESS)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					if(ConfigManager.IRON_SHARPNESS < ConfigManager.DEFAULT_IRON_SHARPNESS) {
					ConfigManager.IRON_SHARPNESS += 1;
					}
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.IRON_SHARPNESS > 0) {
						ConfigManager.IRON_SHARPNESS -= 1;
					}
					else {
						ConfigManager.IRON_SHARPNESS = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.IRON_SHARPNESS = ConfigManager.DEFAULT_IRON_SHARPNESS;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("enchants"));
				joAdvanced.put("ironSharpness", ConfigManager.IRON_SHARPNESS);
				jo.put("enchants", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuEnchant(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.ENCHANT_PUNCH_AND_KNOCKBACK)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					if(ConfigManager.PUNCH_AND_KNOCKBACK < ConfigManager.DEFAULT_PUNCH_AND_KNOCKBACK) {
						ConfigManager.PUNCH_AND_KNOCKBACK += 1;						
					}
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(ConfigManager.PUNCH_AND_KNOCKBACK > 0) {
						ConfigManager.PUNCH_AND_KNOCKBACK -= 1;
					}
					else {
						ConfigManager.PUNCH_AND_KNOCKBACK = 0;
					}
				}
				else if(e.getClick().equals(ClickType.DROP)) {
					ConfigManager.PUNCH_AND_KNOCKBACK = ConfigManager.DEFAULT_PUNCH_AND_KNOCKBACK;
				}
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONObject joAdvanced = ConfigManager.validJSONObject((JSONObject) jo.get("enchants"));
				joAdvanced.put("punchAndKnockback", ConfigManager.PUNCH_AND_KNOCKBACK);
				jo.put("enchants", joAdvanced);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuEnchant(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuMain(p);
			}
		}
		
		
		
		
		
		
		
		else if(inv.getName().contains(ConfigPanels.TEAMS)) {
			e.setCancelled(true);
			int page = Integer.parseInt(inv.getName().replace(ConfigPanels.TEAMS, ""));
			
			if(it.hasItemMeta() && itM.getDisplayName().contains("Retour")) {
				ConfigManager.MenuMain(p);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains("Précedent")) {
				ConfigManager.MenuTeams(p, page - 1);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains("Prochain")) {
				ConfigManager.MenuTeams(p, page + 1);
			}
			
			if(!Main.GAMESTATE.equals(GameState.Idle)) {
				return;
			}
			
			if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.TEAMS_AUTO)) {
				if(e.getClick().equals(ClickType.LEFT)) {
					TeamData.maxPerTeam += 1;
				}
				else if(e.getClick().equals(ClickType.RIGHT)) {
					if(TeamData.maxPerTeam > 1) {
						TeamData.maxPerTeam -= 1;
					}
				}
				else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
					TeamData.autoGenerateTeams();
				}
				
				JSONObject jo = ConfigManager.getFileJSONObject();

				jo.put("maxPerTeam", TeamData.maxPerTeam);
				
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuTeams(p, page);
			}
			else if(it.hasItemMeta() && itM.getDisplayName().contains(ConfigManager.TEAMS_ADD)) {
				Main.getInstance().getTeamData().add(new TeamData(TeamData.maxPerTeam));
				
				JSONObject jo = ConfigManager.getFileJSONObject();
				JSONArray jaTeams = new JSONArray();
				
				for(TeamData td: Main.getInstance().getTeamData()) {
					JSONObject joTeam = new JSONObject();
					
					joTeam.put("max", td.getMax());
					joTeam.put("linkedId", td.getLinkedId());
					
					jaTeams.add(joTeam);
				}
				
				jo.put("teams", jaTeams);
				ConfigManager.saveToFile(jo);
				
				ConfigManager.MenuTeams(p, page);
			}
			else if(it.hasItemMeta() && it.getType().equals(Material.PAPER) && itM.getDisplayName().length() > 6) {
				int id = 0;
				try {
					id = Integer.parseInt(itM.getDisplayName().substring(0, 6).replace("§", ""));
					
					TeamData tdata = Main.getInstance().getTeamData().get(id);
					
					if(e.getClick().equals(ClickType.LEFT)) {
						tdata.setMax(tdata.getMax() + 1);
						
					}
					else if(e.getClick().equals(ClickType.RIGHT)) {
						if(tdata.getMax() > 1) {
							tdata.setMax(tdata.getMax() - 1);							
						}						
					}
					else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
						Main.getInstance().getTeamData().remove(tdata);				
					}
					
					JSONObject jo = ConfigManager.getFileJSONObject();
					JSONArray jaTeams = new JSONArray();
					
					for(TeamData td: Main.getInstance().getTeamData()) {
						JSONObject joTeam = new JSONObject();
						
						joTeam.put("max", td.getMax());
						joTeam.put("linkedId", td.getLinkedId());
						
						jaTeams.add(joTeam);
					}
					
					jo.put("teams", jaTeams);
					ConfigManager.saveToFile(jo);
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				ConfigManager.MenuTeams(p, page);
			}
		}
	}
}
