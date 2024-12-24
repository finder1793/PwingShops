package com.pwing.shops.menu;

import com.pwing.shops.PwingShops;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MenuManager {
    private final PwingShops plugin;
    private final Map<String, ConfigurableMenu> menus = new HashMap<>();
    
    public MenuManager(PwingShops plugin) {
        this.plugin = plugin;
        loadMenus();
    }
    
    private void loadMenus() {
        File menuFolder = new File(plugin.getDataFolder(), "menus");
        if (!menuFolder.exists()) {
            menuFolder.mkdirs();
        }
        
        for (File file : menuFolder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String menuId = file.getName().replace(".yml", "");
            menus.put(menuId, new ConfigurableMenu(plugin, config));
        }
    }
    
    public Optional<ConfigurableMenu> getMenu(String id) {
        return Optional.ofNullable(menus.get(id));
    }

    public void reload() {
        menus.clear();
        loadMenus();
    }

}


