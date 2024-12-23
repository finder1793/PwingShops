package com.pwing.shops.converter;

import com.pwing.shops.PwingShops;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ShopGUIPlusConverter {
    private final PwingShops plugin;
    
    public ShopGUIPlusConverter(PwingShops plugin) {
        this.plugin = plugin;
    }
    
    public boolean convert() {
        File shopGuiPlusFolder = new File(plugin.getServer().getPluginManager().getPlugin("ShopGUIPlus").getDataFolder(), "shops");
        if (!shopGuiPlusFolder.exists()) {
            return false;
        }
        
        for (File shopFile : shopGuiPlusFolder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            convertShop(shopFile);
        }
        return true;
    }
    
    private void convertShop(File shopFile) {
        YamlConfiguration oldConfig = YamlConfiguration.loadConfiguration(shopFile);
        YamlConfiguration newConfig = new YamlConfiguration();
        
        String shopId = shopFile.getName().replace(".yml", "");
        
        // Convert basic shop info
        newConfig.set("display-name", oldConfig.getString("shop-name", shopId));
        newConfig.set("command", "shop " + shopId);
        
        // Convert items
        for (String key : oldConfig.getConfigurationSection("items").getKeys(false)) {
            String oldPath = "items." + key;
            String newPath = "items." + key;
            
            newConfig.set(newPath + ".slot", oldConfig.getInt(oldPath + ".slot"));
            newConfig.set(newPath + ".item", oldConfig.getItemStack(oldPath + ".item"));
            newConfig.set(newPath + ".buy-price", oldConfig.getDouble(oldPath + ".price.buy"));
            newConfig.set(newPath + ".sell-price", oldConfig.getDouble(oldPath + ".price.sell"));
            
            // Convert stock settings if present
            if (oldConfig.contains(oldPath + ".stock")) {
                newConfig.set(newPath + ".stock", oldConfig.getInt(oldPath + ".stock.current"));
                newConfig.set(newPath + ".max-stock", oldConfig.getInt(oldPath + ".stock.max"));
            }
        }
        
        // Save converted shop
        File outputFile = new File(plugin.getDataFolder(), "shops/" + shopId + ".yml");
        try {
            newConfig.save(outputFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save converted shop: " + shopId, e);
        }
    }
}