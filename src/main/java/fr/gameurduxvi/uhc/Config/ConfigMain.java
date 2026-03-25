package fr.gameurduxvi.uhc.Config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.gameurduxvi.uhc.Main;

public class ConfigMain {
	private static File file;
    private static FileConfiguration fileConfig;
    
    private Map<String, String> config = new HashMap<String, String>();
    
    public ConfigMain() {
    	config.put("WanoWorldName", "Wano");
		load();		
	}
    
    public void load() {
    	file = new File(Main.getInstance().pluginDir + "config.yml");
    	Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §6Loading §e\"" + file.getPath() + "\" §6(" + this.getClass().getName() + ")");
        if(!file.exists()) {
            try {
                file.createNewFile();
                fileConfig = YamlConfiguration.loadConfiguration(file);

                Iterator<Entry<String, String>> it = config.entrySet().iterator();
                while (it.hasNext()) {
                	Entry<String, String> entry = it.next();
                	fileConfig.set(entry.getKey(), entry.getValue());
                }
                
                saveConfig();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        else {
        	fileConfig = YamlConfiguration.loadConfiguration(file);
        	
        	Iterator<Entry<String, String>> it = config.entrySet().iterator();
            while (it.hasNext()) {
            	Entry<String, String> entry = it.next();
            	needed(entry.getKey(), entry.getValue());
            }
            
            saveConfig();
        }
        
    	Bukkit.getConsoleSender().sendMessage("§b" + Main.getInstance().pluginName +  " §8>> §aLoaded  §e\"" + file.getPath() + "\" §a(" + this.getClass().getName() + ")");
	}
    
    public static File getFile() {
		return file;
	}
    
    public static FileConfiguration getFileConfig() {
		return fileConfig;
	}
    
    public static void saveConfig() {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void needed(String needed, String neededText) {
    	if(Main.getInstance().isNull(fileConfig.get(needed)) || fileConfig.getString(needed).equals("") || fileConfig.getString(needed).length() == 0) {
    		fileConfig.set(needed, neededText);
    		return;
    	}
    }
}
