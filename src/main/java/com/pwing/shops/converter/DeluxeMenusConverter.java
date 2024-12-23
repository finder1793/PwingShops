package com.pwing.shops.converter;

import com.pwing.shops.PwingShops;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeluxeMenusConverter {
    private final PwingShops plugin;
    
    public DeluxeMenusConverter(PwingShops plugin) {
        this.plugin = plugin;
    }
    
    public void convert() {
        File deluxeMenusFolder = new File(plugin.getServer().getPluginManager().getPlugin("DeluxeMenus").getDataFolder(), "menus");
        if (!deluxeMenusFolder.exists()) return;
        
        File outputFolder = new File(plugin.getDataFolder(), "converted_menus");
        outputFolder.mkdirs();
        
        for (File menuFile : deluxeMenusFolder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            convertMenu(menuFile, new File(outputFolder, menuFile.getName()));
        }
    }
    
    private void convertMenu(File input, File output) {
        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(input);
        YamlConfiguration newConfig = new YamlConfiguration();
        
        // Convert basic settings
        newConfig.set("title", oldConfig.getString("menu_title"));
        newConfig.set("size", oldConfig.getInt("size", 54));
        
        // Convert items
        Map<String, Object> items = new HashMap<>();
        for (String key : oldConfig.getConfigurationSection("items").getKeys(false)) {
            convertItem(oldConfig, newConfig, key, items);
        }
        newConfig.createSection("items", items);
        
        try {
            newConfig.save(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void convertItem(YamlConfiguration oldConfig, YamlConfiguration newConfig, String key, Map<String, Object> items) {
        String basePath = "items." + key;
        Map<String, Object> itemData = new HashMap<>();
        
        itemData.put("slot", oldConfig.getInt(basePath + ".slot"));
        itemData.put("item", oldConfig.getString(basePath + ".material"));
        itemData.put("name", oldConfig.getString(basePath + ".display_name"));
        itemData.put("lore", oldConfig.getStringList(basePath + ".lore"));
        itemData.put("actions", oldConfig.getStringList(basePath + ".actions")); // Direct copy
        
        items.put(key, itemData);
    }
    
}
