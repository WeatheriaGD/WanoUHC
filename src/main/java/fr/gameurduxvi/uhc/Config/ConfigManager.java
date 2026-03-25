package fr.gameurduxvi.uhc.Config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import en.DevSrSouza.JsonItemStack.JsonItemStack;
import fr.gameurduxvi.uhc.GameState;
import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.Storage.TeamData;
import fr.gameurduxvi.uhc.SuperClasses.GameEvent;
import fr.gameurduxvi.uhc.SuperClasses.Personnage;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class ConfigManager {
	// Main Panel
	public static String MAIN_ROLES = "§7Config Roles";
	public static String MAIN_STUFF = "§7Config Stuff";
	public static String MAIN_ENCHANTS = "§7Config Enchants";
	public static String MAIN_SCENARIO = "§7Config Scénario";
	public static String MAIN_TEAMS = "§7Config Teams";
	public static String MAIN_TIMERS = "§7Config Timers";
	public static String MAIN_ADVANCED_CONFIG = "§7Config Avancée";

	// Advanced Pannel
	public static String AVANCE_APPLE = "§7Drop de pommes:";
	public static String AVANCE_FLINT = "§7Drop de silex:";
	public static String AVANCE_PEARL = "§7Drop d'ender pearl:";
	public static String AVANCE_STRENGHT = "§7Strenght:";
	public static String AVANCE_XP = "§7XP:";
	public static String AVANCE_LAPIS = "§7Lapis in enchanting table:";

	public static int APPLE_DROP;
	public static int FLINT_DROP;
	public static int PEARL_DROP;
	public static int DAMAGE_AMPLIFIER;
	public static int EXPERIENCE_AMPLIFIER;
	public static boolean LAPIS;

	public static int DEFAULT_APPLE_DROP = 10;
	public static int DEFAULT_FLINT_DROP = 10;
	public static int DEFAULT_PEARL_DROP = 10;
	public static int DEFAULT_DAMAGE_AMPLIFIER = 100;
	public static int DEFAULT_EXPERIENCE_AMPLIFIER = 100;
	public static boolean DEFAULT_LAPIS = false;

	// Enchant Pannel
	public static boolean CONTROL_ENCHANTS = true;

	public static String ENCHANT_DIAMOND_PROTECTION = "§1§rPotection:";
	public static String ENCHANT_IRON_PROTECTION = "§2§rPotection:";
	public static String ENCHANT_DIAMOND_SHARPNESS = "§1§rSharpness:";
	public static String ENCHANT_IRON_SHARPNESS = "§2§rSharpness:";
	public static String ENCHANT_POWER = "§7Power:";
	public static String ENCHANT_PUNCH_AND_KNOCKBACK = "§7Punch & Knockback:";

	public static int DIAMOND_PROTECTION;
	public static int IRON_PROTECTION;
	public static int DIAMOND_SHARPNESS;
	public static int IRON_SHARPNESS;
	public static int POWER;
	public static int PUNCH_AND_KNOCKBACK;

	public static int DEFAULT_DIAMOND_PROTECTION = 4;
	public static int DEFAULT_IRON_PROTECTION = 4;
	public static int DEFAULT_DIAMOND_SHARPNESS = 5;
	public static int DEFAULT_IRON_SHARPNESS = 5;
	public static int DEFAULT_POWER = 5;
	public static int DEFAULT_PUNCH_AND_KNOCKBACK = 2;

	// Stuff Pannel
	public static String STUFF_DEPART = "§7Stuff de Départ";
	public static String STUFF_KILL = "§7Stuff de Kill";
	public static String STUFF_MODIFY = "§7Modifier le stuff";

	public static ArrayList<ItemStack> StuffDepart = new ArrayList<>();
	public static ArrayList<ItemStack> StuffKill = new ArrayList<>();

	public static ArrayList<Player> ConfigureStuffPlayerDepart = new ArrayList<>();
	public static ArrayList<Player> ConfigureStuffPlayerKill = new ArrayList<>();

	// Teams Pannel
	public static String TEAMS_AUTO = "§bConfiguration Automatique";
	public static String TEAMS_ADD = "§aAjouter une équipe";

	@SuppressWarnings("unchecked")
	public ConfigManager() {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				JSONObject jo = getFileJSONObject();







				// Roles
				JSONObject joRoles = validJSONObject((JSONObject) jo.get("roles"));
				for(Personnage pers: Main.getInstance().getPersonages()) {
					if(Main.getInstance().isNull(joRoles.get("" + pers.getId()))) {
						joRoles.put("" + pers.getId(), pers.getAmount());
					}
					else {
						pers.setAmount(Integer.parseInt("" + joRoles.get("" + pers.getId())));
					}
				}
				jo.put("roles", joRoles);









				// Game Events
				JSONObject joGameEvents = validJSONObject((JSONObject) jo.get("gameEvents"));
				for(GameEvent ge: Main.getInstance().getGameEvents()) {
					JSONObject joGe = validJSONObject((JSONObject) joGameEvents.get(ge.getClass().getName()));
					if(Main.getInstance().isNull(joGe.get("minute"))) {
						joGe.put("minute", ge.getMinute());
					}
					else {
						ge.setMinute(Integer.parseInt("" + joGe.get("minute")));
					}
					if(Main.getInstance().isNull(joGe.get("second"))) {
						joGe.put("second", ge.getSecond());
					}
					else {
						ge.setSecond(Integer.parseInt("" + joGe.get("second")));
					}
					joGameEvents.put(ge.getClass().getName(), joGe);
				}
				jo.put("gameEvents", joGameEvents);








				// Enchants
				JSONObject joEnchants = validJSONObject((JSONObject) jo.get("enchants"));

				if(Main.getInstance().isNull(joEnchants.get("controlEnchants"))) {
					joEnchants.put("controlEnchants", CONTROL_ENCHANTS);
					CONTROL_ENCHANTS = true;
				}
				else {
					CONTROL_ENCHANTS = (boolean) joEnchants.get("controlEnchants");
				}

				if(Main.getInstance().isNull(joEnchants.get("diamondProtection"))) {
					joEnchants.put("diamondProtection", DEFAULT_DIAMOND_PROTECTION);
					DIAMOND_PROTECTION = DEFAULT_DIAMOND_PROTECTION;
				}
				else {
					DIAMOND_PROTECTION = Integer.parseInt("" + joEnchants.get("diamondProtection"));
				}

				if(Main.getInstance().isNull(joEnchants.get("ironProtection"))) {
					joEnchants.put("ironProtection", DEFAULT_IRON_PROTECTION);
					IRON_PROTECTION = DEFAULT_IRON_PROTECTION;
				}
				else {
					IRON_PROTECTION = Integer.parseInt("" + joEnchants.get("ironProtection"));
				}

				if(Main.getInstance().isNull(joEnchants.get("diamondSharpness"))) {
					joEnchants.put("diamondSharpness", DEFAULT_DIAMOND_SHARPNESS);
					DIAMOND_SHARPNESS = DEFAULT_DIAMOND_SHARPNESS;
				}
				else {
					DIAMOND_SHARPNESS = Integer.parseInt("" + joEnchants.get("diamondSharpness"));
				}

				if(Main.getInstance().isNull(joEnchants.get("ironSharpness"))) {
					joEnchants.put("ironSharpness", DEFAULT_IRON_SHARPNESS);
					IRON_SHARPNESS = DEFAULT_IRON_SHARPNESS;
				}
				else {
					IRON_SHARPNESS = Integer.parseInt("" + joEnchants.get("ironSharpness"));
				}

				if(Main.getInstance().isNull(joEnchants.get("power"))) {
					joEnchants.put("power", DEFAULT_POWER);
					POWER = DEFAULT_POWER;
				}
				else {
					POWER = Integer.parseInt("" + joEnchants.get("power"));
				}

				if(Main.getInstance().isNull(joEnchants.get("punchAndKnockback"))) {
					joEnchants.put("punchAndKnockback", DEFAULT_PUNCH_AND_KNOCKBACK);
					PUNCH_AND_KNOCKBACK = DEFAULT_PUNCH_AND_KNOCKBACK;
				}
				else {
					PUNCH_AND_KNOCKBACK = Integer.parseInt("" + joEnchants.get("punchAndKnockback"));
				}








				// Avancé
				JSONObject joAdvanced = validJSONObject((JSONObject) jo.get("advanced"));

				if(Main.getInstance().isNull(joAdvanced.get("appleDrop"))) {
					joAdvanced.put("appleDrop", DEFAULT_APPLE_DROP);
					APPLE_DROP = DEFAULT_APPLE_DROP;
				}
				else {
					APPLE_DROP = Integer.parseInt("" + joAdvanced.get("appleDrop"));
				}

				if(Main.getInstance().isNull(joAdvanced.get("flintDrop"))) {
					joAdvanced.put("flintDrop", DEFAULT_FLINT_DROP);
					FLINT_DROP = DEFAULT_FLINT_DROP;
				}
				else {
					FLINT_DROP = Integer.parseInt("" + joAdvanced.get("flintDrop"));
				}

				if(Main.getInstance().isNull(joAdvanced.get("pearlDrop"))) {
					joAdvanced.put("pearlDrop", DEFAULT_PEARL_DROP);
					PEARL_DROP = DEFAULT_PEARL_DROP;
				}
				else {
					PEARL_DROP = Integer.parseInt("" + joAdvanced.get("pearlDrop"));
				}

				if(Main.getInstance().isNull(joAdvanced.get("damageAmplifier"))) {
					joAdvanced.put("damageAmplifier", DEFAULT_DAMAGE_AMPLIFIER);
					DAMAGE_AMPLIFIER = DEFAULT_DAMAGE_AMPLIFIER;
				}
				else {
					DAMAGE_AMPLIFIER = Integer.parseInt("" + joAdvanced.get("damageAmplifier"));
				}

				if(Main.getInstance().isNull(joAdvanced.get("experienceAmplifier"))) {
					joAdvanced.put("experienceAmplifier", DEFAULT_EXPERIENCE_AMPLIFIER);
					EXPERIENCE_AMPLIFIER = DEFAULT_EXPERIENCE_AMPLIFIER;
				}
				else {
					EXPERIENCE_AMPLIFIER = Integer.parseInt("" + joAdvanced.get("experienceAmplifier"));
				}

				if(Main.getInstance().isNull(joAdvanced.get("lapis"))) {
					joAdvanced.put("lapis", DEFAULT_LAPIS);
					LAPIS = DEFAULT_LAPIS;
				}
				else {
					LAPIS = (boolean) joAdvanced.get("lapis");
				}

				jo.put("advanced", joAdvanced);







				// Scénarios
				JSONObject joScenario = validJSONObject((JSONObject) jo.get("scenario"));
				for(Scenario sc: Main.getInstance().getScenarios()) {
					if(Main.getInstance().isNull(joScenario.get(sc.getClass().getName()))) {
						JSONObject joSigleScenario = validJSONObject((JSONObject) joScenario.get(sc.getClass().getName()));
						joSigleScenario.put("state", sc.isActive());
						joScenario.put(sc.getClass().getName(), joSigleScenario);
					}
					else {
						JSONObject joSigleScenario = validJSONObject((JSONObject) joScenario.get(sc.getClass().getName()));
						if(Main.getInstance().isNull(joSigleScenario.get("state"))) {
							joSigleScenario.put("state", sc.isActive());
							joScenario.put(sc.getClass().getName(), joSigleScenario);
						}
						else {
							Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

								@Override
								public void run() {
									sc.setActive((boolean) joSigleScenario.get("state"));
								}
							}, 10);
						}
					}
				}

				jo.put("scenario", joScenario);







				// Stuff Départ
				try {
					JSONArray jaStuffDepart = validJSONArray((JSONArray) jo.get("stuffDepart"));
					for(Object obj: jaStuffDepart) {
						try {
							StuffDepart.add(JsonItemStack.fromJson(((JSONObject) obj).toJSONString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					jo.put("stuffDepart", jaStuffDepart);
				} catch (Exception e) {
					e.printStackTrace();
				}





				// Stuff Kill
				try {
					JSONArray jaStuffKill = validJSONArray((JSONArray) jo.get("stuffKill"));
					for(Object obj: jaStuffKill) {
						try {
							StuffKill.add(JsonItemStack.fromJson(((JSONObject) obj).toJSONString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					jo.put("stuffKill", jaStuffKill);
				} catch (Exception e) {
					e.printStackTrace();
				}






				// Teams Max
				if(Main.getInstance().isNull(jo.get("maxPerTeam"))) {
					jo.put("maxPerTeam", 4);
				}
				else {
					TeamData.maxPerTeam = Integer.parseInt(jo.get("maxPerTeam") + "");
				}






				//Teams
				Main.getInstance().getTeamData().clear();

				JSONArray jaTeams = ConfigManager.validJSONArray((JSONArray) jo.get("teams"));

				for(Object obj: jaTeams) {
					JSONObject joTeam = (JSONObject) obj;
					int max = Integer.parseInt(joTeam.get("max") + "");
					int linkedId = Integer.parseInt(joTeam.get("linkedId") + "");
					Main.getInstance().getTeamData().add(new TeamData(max).setLinkedId(linkedId));
				}

				if(Main.getInstance().getTeamData().size() == 0) {
					TeamData.autoGenerateTeams();
				}

				saveToFile(jo);
			}
		}, 20);
	}

	public static JSONObject validJSONObject(JSONObject jsonObject) {
		JSONObject jo = jsonObject;
		if(Main.getInstance().isNull(jo)) {
			jo = new JSONObject();
		}
		return jo;
	}

	public static JSONArray validJSONArray(JSONArray jsonArray) {
		JSONArray ja = jsonArray;
		if(Main.getInstance().isNull(ja)) {
			ja = new JSONArray();
		}
		return ja;
	}

	@SuppressWarnings("unchecked")
	public static void set(String path, Object value) {
		String[] split = path.split("\\|");
		ArrayList<JSONObject> jos = new ArrayList<>();

		jos.add(getFileJSONObject());

		for(int i = 0; i < split.length - 1; i++) {
			jos.add(validJSONObject((JSONObject)(jos.get(i).get(split[i]))));
		}

		jos.get(split.length - 1).put(split[split.length - 1], value);

		for(int i = split.length - 2; i >= 0 ; i--) {
			jos.get(i).put(split[i], jos.get(i + 1));
		}

		saveToFile(jos.get(0));
	}

	public static Object get(String path) {
		String[] split = path.split("\\|");
		ArrayList<JSONObject> jos = new ArrayList<>();

		jos.add(getFileJSONObject());

		for(int i = 0; i < split.length - 1; i++) {
			jos.add(validJSONObject((JSONObject)(jos.get(i).get(split[i]))));
		}
		return jos.get(jos.size() - 1).get(split[split.length - 1]);
	}

	public static JSONObject getFileJSONObject() {
		JSONObject jo = null;
		try {
			File file = new File(Main.getInstance().pluginDir + "data.json");
			if(!file.exists()) {
				Bukkit.getConsoleSender().sendMessage("§eConfigManager >> §cMissing \"data.json\"");
				Bukkit.getConsoleSender().sendMessage("§eConfigManager >> §cCreating a new one");
				file.createNewFile();
				try (FileWriter fw = new FileWriter(Main.getInstance().pluginDir + "data.json")) {
					fw.write("{}");
					fw.flush();
					jo = new JSONObject();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				Object obj = new JSONParser().parse(new FileReader(Main.getInstance().pluginDir + "data.json"));
				jo = (JSONObject) obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}

	public static void saveToFile(JSONObject jo) {
		try (FileWriter fw = new FileWriter(Main.getInstance().pluginDir + "data.json")) {
			fw.write(jo.toJSONString());
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void MenuMain(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.MAIN);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		for(int i = 0; i < 36; i++) {
			inv.setItem(i, pane);
		}

		pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
		paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] bluePane = {10,12,14,16,19,21,23,25};
		for(int i: bluePane) {
			inv.setItem(i, pane);
		}

		ItemStack stuff = new ItemStack(Material.CHEST);
		ItemMeta stuffM = stuff.getItemMeta();
		stuffM.setDisplayName(MAIN_STUFF);
		stuff.setItemMeta(stuffM);
		inv.setItem(11, stuff);

		ItemStack enchants = new ItemStack(Material.ENCHANTMENT_TABLE);
		ItemMeta enchantsM = enchants.getItemMeta();
		enchantsM.setDisplayName(MAIN_ENCHANTS);
		enchants.setItemMeta(enchantsM);
		inv.setItem(13, enchants);

		ItemStack scenario = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta scenarioM = scenario.getItemMeta();
		scenarioM.setDisplayName(MAIN_SCENARIO);
		scenario.setItemMeta(scenarioM);
		inv.setItem(15, scenario);

		ItemStack roles = new ItemStack(Material.NAME_TAG);
		ItemMeta rolesM = roles.getItemMeta();
		rolesM.setDisplayName(MAIN_ROLES);
		roles.setItemMeta(rolesM);
		inv.setItem(20, roles);

		ItemStack teamsConfig = new ItemStack(Material.BANNER);
		BannerMeta teamsConfigM = (BannerMeta) teamsConfig.getItemMeta();
		teamsConfigM.setDisplayName(MAIN_TEAMS);
		teamsConfigM.setBaseColor(DyeColor.BLUE);
		teamsConfigM.addPattern(new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_LEFT));
		teamsConfigM.addPattern(new Pattern(DyeColor.LIGHT_BLUE, PatternType.STRIPE_RIGHT));
		teamsConfigM.addPattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP));
		for(ItemFlag itf: ItemFlag.values()) teamsConfigM.addItemFlags(itf);
		teamsConfig.setItemMeta(teamsConfigM);
		inv.setItem(22, teamsConfig);

		ItemStack timers = new ItemStack(Material.WATCH);
		ItemMeta timersM = timers.getItemMeta();
		timersM.setDisplayName(MAIN_TIMERS);
		timers.setItemMeta(timersM);
		inv.setItem(24, timers);

		ItemStack advancedConfig = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta advancedConfigM = advancedConfig.getItemMeta();
		advancedConfigM.setDisplayName(MAIN_ADVANCED_CONFIG);
		advancedConfig.setItemMeta(advancedConfigM);
		inv.setItem(31, advancedConfig);

		ItemStack dev = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta devM = (SkullMeta) dev.getItemMeta();
		devM.setOwner("GameurDuXVI");
		devM.setDisplayName("§7Dev by §bGameurDuXVI");
		List<String> lore = new ArrayList<>();
		lore.add("§7Fix par §6G0ldyS");
		lore.add("§7Merci pour ces années de plaisir ! §8§l- §f§lEquipe Weatheria");
		devM.setLore(lore);
		dev.setItemMeta(devM);
		inv.setItem(35, dev);

		p.openInventory(inv);
	}

	public static void MenuRole(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.ROLES);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,1,2,3,5,6,7,8,9,17,18,26,27,28,29,30,32,33,34,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		int i = 9;
		for(Personnage pers: Main.getInstance().getPersonages()) {
			i++;
			if(i == 17) i+=2;
			ItemStack it = new ItemStack(Material.NAME_TAG);
			ItemMeta itM = it.getItemMeta();
			itM.setDisplayName("§b" + pers.getName());
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§7> Amount: " + pers.getAmount());
			if(Main.GAMESTATE.equals(GameState.Idle)) {
				lore.add("§aClick gauche ajouter");
				lore.add("§aClick Droit enlever");
				lore.add("§aDrop pour réinitialiser");
			}
			else {
				lore.add("§cVous ne pouvez pas modifier pendent la partie");
			}
			itM.setLore(lore);
			it.setItemMeta(itM);
			inv.setItem(i, it);
		}

		int max = 0;
		for(Personnage pers: Main.getInstance().getPersonages()) {
			max += pers.getAmount();
		}

		ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta itM = (SkullMeta) it.getItemMeta();
		itM.setDisplayName("§7Roles prévu pour " + max + " joueurs");
		it.setItemMeta(itM);
		inv.setItem(4, it);

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}

	public static void MenuGameEvents(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.GAME_EVENTS);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,8,9,17,18,26,27,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
		paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] bluePane = {1,2,3,4,5,6,7,10,16,19,25,28,29,30,32,33,34,35};
		for(int i: bluePane) {
			inv.setItem(i, pane);
		}

		int i = 10;
		for(GameEvent ge: Main.getInstance().getGameEvents()) {
			i++;
			if(i == 16) i+=4;
			ItemStack it = new ItemStack(Material.NAME_TAG);
			ItemMeta itM = it.getItemMeta();
			itM.setDisplayName("§b" + ge.getName());
			ArrayList<String> lore = new ArrayList<>();
			String[] description = ge.getDescription().split("\n");
			for(String ligne: description) {
				lore.add("§7> " + ligne);
			}
			lore.add("§7> " + ge.getMinute() + "min " + ge.getSecond());
			if(Main.GAMESTATE.equals(GameState.Idle)) {
				lore.add("§a+§b1 Seconde §7Click gauche");
				lore.add("§c-§b1 Seconde §7Click Droit");
				lore.add("§a+§b1 Minute §7Shift Click Gauche");
				lore.add("§c-§b1 Minute §7Shift Click Droit");
				lore.add("§bReset §7Drop");
			}
			else {
				lore.add("§cVous ne pouvre pas modifier pendent la partie");
			}
			itM.setLore(lore);
			it.setItemMeta(itM);
			inv.setItem(i, it);
		}

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}

	public static void MenuScenario(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.SCENARIO + page);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,8,9,17,18,26,27,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
		paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] bluePane = {1,2,3,4,5,6,7,10,16,19,25,28,29,30,32,33,34};
		for(int i: bluePane) {
			inv.setItem(i, pane);
		}

		if(Main.getInstance().getScenarios().size() == 0) {
			ItemStack it = new ItemStack(Material.BARRIER);
			ItemMeta itM = it.getItemMeta();
			itM.setDisplayName("§cAucun scénario n'à été chargé !");
			it.setItemMeta(itM);
			inv.setItem(22, it);
		}

		int i = 0;
		int slot = 0;
		for(Scenario sc: Main.getInstance().getScenarios()) {
			i++;
			if(i > (5 * (page - 1)) && i <= (5 * page)) {
				slot++;
				ItemStack it = sc.getScenarioItem();
				ItemMeta itM = it.getItemMeta();
				itM.setDisplayName("§b" + sc.getScencarioName());
				if(sc.isActive()) {
					itM.addEnchant(Enchantment.DURABILITY, 1, true);
					itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				else {
					itM.removeEnchant(Enchantment.DURABILITY);
				}
				String[] split = sc.getScenarioDescription().split("\n");
				itM.setLore(Arrays.asList(split));
				ArrayList<String> lore = new ArrayList<>();
				for(String line: itM.getLore()) {
					lore.add("§7" + line);
				}
				itM.setLore(lore);

				it.setItemMeta(itM);
				inv.setItem(10 + slot, it);

				try {
					if(sc.getConfigInv() != null) {
						ItemStack itC = null;
						ItemMeta itCM = null;
						if(sc.isActive()) {
							itC = new ItemStack(Material.REDSTONE_BLOCK);
						}
						else {
							itC = new ItemStack(Material.STONE);
						}
						itCM = itC.getItemMeta();
						itCM.setDisplayName("§7Menu de configuration");
						itC.setItemMeta(itCM);
						inv.setItem(19 + slot, itC);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		if(page > 1) {
			ItemStack previous = new ItemStack(Material.ARROW);
			ItemMeta previousM = previous.getItemMeta();
			previousM.setDisplayName("§7Retour");
			previous.setItemMeta(previousM);

			inv.setItem(30, previous);
		}

		if(Main.getInstance().getScenarios().size() > (7 * page)) {
			ItemStack back = new ItemStack(Material.ARROW);
			ItemMeta backM = back.getItemMeta();
			backM.setDisplayName("§7Prochain");
			back.setItemMeta(backM);

			inv.setItem(32, back);
		}

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}

	public static void MenuStuff(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.STUFF);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,28,29,30,31,32,33,34,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
		paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] bluePane = {10,11,13,15,16,19,20,21,22,23,24,25};
		for(int i: bluePane) {
			inv.setItem(i, pane);
		}

		ItemStack depart = new ItemStack(Material.BED);
		ItemMeta departM = depart.getItemMeta();
		departM.setDisplayName(STUFF_DEPART);
		depart.setItemMeta(departM);
		inv.setItem(12, depart);

		ItemStack kill = new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
		ItemMeta killM = kill.getItemMeta();
		killM.setDisplayName(STUFF_KILL);
		kill.setItemMeta(killM);
		inv.setItem(14, kill);

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}

	public static void MenuStuffDepart(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.STUFF_DEPART);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,28,29,30,31,32,33,34,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		int i = 9;
		for(ItemStack it: StuffDepart) {
			i++;
			if(i == 17) i+=2;
			if(i > 25) break;
			inv.setItem(i, it);
		}

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		ItemStack modify = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta modifyM = modify.getItemMeta();
		modifyM.setDisplayName(STUFF_MODIFY);
		modify.setItemMeta(modifyM);
		inv.setItem(30, modify);

		p.openInventory(inv);
	}

	public static void MenuStuffKill(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.STUFF_KILL);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,28,29,30,31,32,33,34,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		int i = 9;
		for(ItemStack it: StuffKill) {
			i++;
			if(i == 17) i+=2;
			if(i > 25) break;
			inv.setItem(i, it);
		}

		ItemStack modify = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta modifyM = modify.getItemMeta();
		modifyM.setDisplayName(STUFF_MODIFY);
		modify.setItemMeta(modifyM);
		inv.setItem(30, modify);

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}

	public static void MenuAdvanced(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.AVANCE);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,28,29,30,31,32,33,34,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
		paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] bluePane = {10,12,14,16,19,21,23,25};
		for(int i: bluePane) {
			inv.setItem(i, pane);
		}

		ItemStack apple = new ItemStack(Material.APPLE);
		ItemMeta appleM = apple.getItemMeta();
		appleM.setDisplayName(AVANCE_APPLE + " §a" + APPLE_DROP + "%");
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§a+§b1% §7Click gauche");
		lore.add("§c-§b1% §7Click Droit");
		lore.add("§a+§b10% §7Shift Click Gauche");
		lore.add("§c-§b10% §7Shift Click Droit");
		lore.add("§bReset §7Drop");
		appleM.setLore(lore);
		apple.setItemMeta(appleM);
		inv.setItem(11, apple);

		ItemStack silex = new ItemStack(Material.FLINT);
		ItemMeta silexM = silex.getItemMeta();
		silexM.setDisplayName(AVANCE_FLINT + " §a" + FLINT_DROP + "%");
		lore = new ArrayList<>();
		lore.add("§a+§b1% §7Click gauche");
		lore.add("§c-§b1% §7Click Droit");
		lore.add("§a+§b10% §7Shift Click Gauche");
		lore.add("§c-§b10% §7Shift Click Droit");
		lore.add("§bReset §7Drop");
		silexM.setLore(lore);
		silex.setItemMeta(silexM);
		inv.setItem(13, silex);

		ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
		ItemMeta pearlM = pearl.getItemMeta();
		pearlM.setDisplayName(AVANCE_PEARL + " §a" + PEARL_DROP + "%");
		lore = new ArrayList<>();
		lore.add("§a+§b1% §7Click gauche");
		lore.add("§c-§b1% §7Click Droit");
		lore.add("§a+§b10% §7Shift Click Gauche");
		lore.add("§c-§b10% §7Shift Click Droit");
		lore.add("§bReset §7Drop");
		pearlM.setLore(lore);
		pearl.setItemMeta(pearlM);
		inv.setItem(15, pearl);

		ItemStack strenght = new ItemStack(Material.POTION);
		ItemMeta strenghtM = strenght.getItemMeta();
		strenghtM.setDisplayName(AVANCE_STRENGHT + " §a" + DAMAGE_AMPLIFIER + "%");
		lore = new ArrayList<>();
		lore.add("§a+§b1% §7Click gauche");
		lore.add("§c-§b1% §7Click Droit");
		lore.add("§a+§b10% §7Shift Click Gauche");
		lore.add("§c-§b10% §7Shift Click Droit");
		lore.add("§bReset §7Drop");
		strenghtM.setLore(lore);
		strenght.setItemMeta(strenghtM);
		inv.setItem(20, strenght);

		ItemStack xp = new ItemStack(Material.EXP_BOTTLE);
		ItemMeta xpM = xp.getItemMeta();
		xpM.setDisplayName(AVANCE_XP + " §a" + EXPERIENCE_AMPLIFIER + "%");
		lore = new ArrayList<>();
		lore.add("§a+§b1% §7Click gauche");
		lore.add("§c-§b1% §7Click Droit");
		lore.add("§a+§b10% §7Shift Click Gauche");
		lore.add("§c-§b10% §7Shift Click Droit");
		lore.add("§bReset §7Drop");
		xpM.setLore(lore);
		xp.setItemMeta(xpM);
		inv.setItem(22, xp);

		ItemStack lapis = new ItemStack(Material.INK_SACK, 1, (short) 4);
		ItemMeta lapisM = lapis.getItemMeta();
		String stateLapis = LAPIS ? "oui" : "non";
		lapisM.setDisplayName(AVANCE_LAPIS + " §a" + stateLapis);
		lore = new ArrayList<>();
		lore.add("§bSwitch §7Click gauche");
		lore.add("§bReset §7Drop");
		lapisM.setLore(lore);
		lapis.setItemMeta(lapisM);
		inv.setItem(24, lapis);

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}

	public static void MenuEnchant(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.ENCHANTS);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,28,29,30,31,32,33,34,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
		paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] bluePane = {10,12,14,16,19,21,23,25};
		for(int i: bluePane) {
			inv.setItem(i, pane);
		}

		pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) (CONTROL_ENCHANTS? 5: 14));
		paneM = pane.getItemMeta();
		paneM.setDisplayName("§aContrôle des enchantements: " + (CONTROL_ENCHANTS? "Actif": "§cInnactif"));
		pane.setItemMeta(paneM);
		inv.setItem(4, pane);

		ItemStack dChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
		ItemMeta dChestplateM = dChestplate.getItemMeta();
		dChestplateM.setDisplayName(ENCHANT_DIAMOND_PROTECTION + " §a" + DIAMOND_PROTECTION);
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§a+§b1 §7Click gauche");
		lore.add("§c-§b1 §7Click Droit");
		lore.add("§bReset §7Drop");
		dChestplateM.setLore(lore);
		dChestplate.setItemMeta(dChestplateM);
		inv.setItem(11, dChestplate);

		ItemStack dSword = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta dSwordM = dSword.getItemMeta();
		dSwordM.setDisplayName(ENCHANT_DIAMOND_SHARPNESS + " §a" + DIAMOND_SHARPNESS);
		lore = new ArrayList<>();
		lore.add("§a+§b1 §7Click gauche");
		lore.add("§c-§b1 §7Click Droit");
		lore.add("§bReset §7Drop");
		dSwordM.setLore(lore);
		dSword.setItemMeta(dSwordM);
		inv.setItem(13, dSword);

		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowM = bow.getItemMeta();
		bowM.setDisplayName(ENCHANT_POWER + " §a" + POWER);
		lore = new ArrayList<>();
		lore.add("§a+§b1 §7Click gauche");
		lore.add("§c-§b1 §7Click Droit");
		lore.add("§bReset §7Drop");
		bowM.setLore(lore);
		bow.setItemMeta(bowM);
		inv.setItem(15, bow);

		ItemStack iChestplate = new ItemStack(Material.IRON_CHESTPLATE);
		ItemMeta iChestplateM = iChestplate.getItemMeta();
		iChestplateM.setDisplayName(ENCHANT_IRON_PROTECTION + " §a" + IRON_PROTECTION);
		lore = new ArrayList<>();
		lore.add("§a+§b1 §7Click gauche");
		lore.add("§c-§b1 §7Click Droit");
		lore.add("§bReset §7Drop");
		iChestplateM.setLore(lore);
		iChestplate.setItemMeta(iChestplateM);
		inv.setItem(20, iChestplate);

		ItemStack iSword = new ItemStack(Material.IRON_SWORD);
		ItemMeta iSwordM = iSword.getItemMeta();
		iSwordM.setDisplayName(ENCHANT_IRON_SHARPNESS + " §a" + IRON_SHARPNESS);
		lore = new ArrayList<>();
		lore.add("§a+§b1 §7Click gauche");
		lore.add("§c-§b1 §7Click Droit");
		lore.add("§bReset §7Drop");
		iSwordM.setLore(lore);
		iSword.setItemMeta(iSwordM);
		inv.setItem(22, iSword);

		ItemStack slime = new ItemStack(Material.SLIME_BALL);
		ItemMeta slimeM = slime.getItemMeta();
		slimeM.setDisplayName(ENCHANT_PUNCH_AND_KNOCKBACK + " §a" + PUNCH_AND_KNOCKBACK);
		lore = new ArrayList<>();
		lore.add("§a+§b1 §7Click gauche");
		lore.add("§c-§b1 §7Click Droit");
		lore.add("§bReset §7Drop");
		slimeM.setLore(lore);
		slime.setItemMeta(slimeM);
		inv.setItem(24, slime);

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}

	public static void MenuTeams(Player p, int page) {
		Inventory inv = Bukkit.createInventory(null, 36, ConfigPanels.TEAMS + page);

		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta paneM = pane.getItemMeta();
		paneM.setDisplayName("§a");
		pane.setItemMeta(paneM);
		int[] grayPane = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,28,29,30,31,32,33,34,35};
		for(int i: grayPane) {
			inv.setItem(i, pane);
		}

		int num = 0;
		int i = 9;
		for(TeamData td: Main.getInstance().getTeamData()) {
			if(num >= (14 * (page - 1)) && num <= (14 * page)) {
				i++;
				if(i == 17) i+=2;
				if(i > 25) break;
				ItemStack it;
				if(td.getLinkedId() != 0) {
					it = new ItemStack(Material.EMPTY_MAP);
				}
				else {
					it = new ItemStack(Material.PAPER);
				}
				ItemMeta itM = it.getItemMeta();
				ArrayList<String> lore = new ArrayList<>();
				lore.add("§7" + td.getMax() + " emplacements disponible");

				String numString = "";
				String[] numSplited = ("" + num).split("");
				for(String txt: numSplited) {
					numString += "§" + txt;
				}
				for(int place = numSplited.length; place < 3; place++) {
					numString = "§0" + numString;
				}



				if(Main.GAMESTATE.equals(GameState.Idle)) {
					if(td.getLinkedId() != 0) {
						itM.setDisplayName(numString + "§bTeam d'empereur");
						lore.add("§cVous ne pouvez pas modifier cette team");
					}
					else {
						itM.setDisplayName(numString +"§bTeam de pirate");
						lore.add("§a+§b1 §7Click Gauche");
						lore.add("§c-§b1 §7Click Droit");
						lore.add("§bSupprimer §7Shift Click Droit");
					}
				}
				else {
					if(td.getLinkedId() != 0) {
						itM.setDisplayName(numString + "§bTeam d'empereur");
					}
					else {
						itM.setDisplayName(numString +"§bTeam de pirate");
					}
					lore.add("§cVous ne pouvez pas modifier pendent la partie");
				}

				itM.setLore(lore);
				it.setItemMeta(itM);
				inv.setItem(i, it);
			}
			num++;
		}

		ItemStack auto = new ItemStack(Material.BANNER);
		BannerMeta autoM = (BannerMeta) auto.getItemMeta();
		autoM.setDisplayName(TEAMS_AUTO);
		autoM.setBaseColor(DyeColor.LIGHT_BLUE);
		autoM.addPattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_RIGHT));
		autoM.addPattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_LEFT));
		autoM.addPattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_MIDDLE));
		autoM.addPattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP));
		autoM.addPattern(new Pattern(DyeColor.LIGHT_BLUE, PatternType.BORDER));
		for(ItemFlag itf: ItemFlag.values()) autoM.addItemFlags(itf);
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§7Pirates par équipe: §b" + TeamData.maxPerTeam);
		lore.add("§a+§b1 §7Click Gauche");
		lore.add("§c-§b1 §7Click Droit");
		lore.add("§aAppliquer §7Shift Click Gauche");
		autoM.setLore(lore);
		auto.setItemMeta(autoM);
		inv.setItem(3, auto);

		int max = 0;
		for(TeamData td: Main.getInstance().getTeamData()) {
			max += td.getMax();
		}

		ItemStack j = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta jM = (SkullMeta) j.getItemMeta();
		jM.setDisplayName("§7Equipes prévues pour " + max + " joueurs");
		j.setItemMeta(jM);
		inv.setItem(4, j);

		ItemStack add = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta addM = add.getItemMeta();
		addM.setDisplayName(TEAMS_ADD);
		add.setItemMeta(addM);
		inv.setItem(5, add);

		if(page > 1) {
			ItemStack prec = new ItemStack(Material.ARROW);
			ItemMeta precM = prec.getItemMeta();
			precM.setDisplayName("§7Précedent");
			prec.setItemMeta(precM);
			inv.setItem(30, prec);
		}

		if(Main.getInstance().getTeamData().size() > (page * 14)) {
			ItemStack next = new ItemStack(Material.ARROW);
			ItemMeta nextM = next.getItemMeta();
			nextM.setDisplayName("§7Prochain");
			next.setItemMeta(nextM);
			inv.setItem(32, next);
		}

		ItemStack prev = new ItemStack(Material.ARROW);
		ItemMeta prevM = prev.getItemMeta();
		prevM.setDisplayName("§7Retour");
		prev.setItemMeta(prevM);
		inv.setItem(31, prev);

		p.openInventory(inv);
	}
}