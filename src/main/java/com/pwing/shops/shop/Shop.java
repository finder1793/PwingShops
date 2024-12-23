package com.pwing.shops.shop;

import com.pwing.shops.PwingShops;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;


public class Shop {
    private final String id;
    private String displayName;
    private String command;
    private final Map<Integer, ShopItem> items;
    private final File file;
    private final PwingShops plugin;
    private final Map<String, OpeningHours> weeklySchedule = new HashMap<>();

    public Shop(String id, File file, PwingShops plugin) {
        this.id = id;
        this.file = file;
        this.plugin = plugin;
        this.items = new HashMap<>();
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadConfig(config);
    }

    private void loadConfig(YamlConfiguration config) {
        // Load basic shop info
        this.displayName = config.getString("display-name", id);
        this.command = config.getString("command", "shop " + id);
        // Load schedule
        if (config.isConfigurationSection("schedule")) {
            for (String day : config.getConfigurationSection("schedule").getKeys(false)) {
                String openTime = config.getString("schedule." + day + ".open");
                String closeTime = config.getString("schedule." + day + ".close");
                
                OpeningHours hours = new OpeningHours(
                    LocalTime.parse(openTime),
                    LocalTime.parse(closeTime)
                );
                weeklySchedule.put(day, hours);
            }
        }
        
        // Load items
        loadItems(config);
    }

    public boolean isOpen() {
        LocalDateTime now = LocalDateTime.now();
        String dayOfWeek = now.getDayOfWeek().toString();
        
        OpeningHours hours = weeklySchedule.get(dayOfWeek);
        if (hours == null) return true; // Open if no restrictions
        
        return hours.isOpen(now.toLocalTime());
    }
    private void loadItems(YamlConfiguration config) {
        if (!config.isConfigurationSection("items")) return;
        
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            String path = "items." + key;
            int slot = config.getInt(path + ".slot");
            ItemStack item = config.getItemStack(path + ".item");
            double buyPrice = config.getDouble(path + ".buy-price");
            double sellPrice = config.getDouble(path + ".sell-price");
            
            ShopItem shopItem = new ShopItem(key, item, buyPrice, sellPrice);
            
            // Load bulk discounts
            if (config.isConfigurationSection(path + ".bulk-discounts")) {
                for (String amount : config.getConfigurationSection(path + ".bulk-discounts").getKeys(false)) {
                    int bulkAmount = Integer.parseInt(amount);
                    double discount = config.getDouble(path + ".bulk-discounts." + amount);
                    shopItem.addBulkDiscount(bulkAmount, discount);
                }
            }
            
            // Load sales configuration
            if (config.isConfigurationSection(path + ".sale")) {
                double saleDiscount = config.getDouble(path + ".sale.discount");
                String startTime = config.getString(path + ".sale.start");
                String endTime = config.getString(path + ".sale.end");
                
                LocalDateTime saleStart = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime saleEnd = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                
                shopItem.setSaleDetails(saleDiscount, saleStart, saleEnd);
            }
            
            // Load stock settings
            shopItem.setStock(config.getInt(path + ".stock", -1));
            shopItem.setMaxStock(config.getInt(path + ".max-stock", -1));
            
            // Load regeneration settings
            if (config.isConfigurationSection(path + ".stock-regen")) {
                int regenAmount = config.getInt(path + ".stock-regen.amount", 1);
                int regenInterval = config.getInt(path + ".stock-regen.interval", 300);
                shopItem.setRegenAmount(regenAmount);
                shopItem.setRegenInterval(regenInterval);
                shopItem.startRegeneration(plugin);
            }
            
            items.put(slot, shopItem);
        }
    }    public void save() {
        // Save implementation
    }

    // Getters
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Map<Integer, ShopItem> getItems() { return items; }
    public String getCommand() { return command; }
    
    public Map<String, OpeningHours> getWeeklySchedule() {
        return weeklySchedule;
    }
}
