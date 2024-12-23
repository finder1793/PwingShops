package com.pwing.shops.shop;

import com.pwing.shops.util.ColorUtil;
import com.pwing.shops.PwingShops;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.List;

public class Shop implements InventoryHolder {
    private final String id;
    private String displayName;
    private String command;
    private final Map<Integer, ShopItem> items;
    private final File file;
    private final PwingShops plugin;
    private final Map<String, OpeningHours> weeklySchedule = new HashMap<>();
    private final Map<String, CurrencyPrice> prices = new HashMap<>();
    private final int rows = 6;
    private Inventory inventory;

    public Shop(String id, File file, PwingShops plugin) {
        this.id = id;
        this.file = file;
        this.plugin = plugin;
        this.items = new HashMap<>();
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        loadConfig(config);
    }

    private void loadConfig(YamlConfiguration config) {
        this.displayName = config.getString("display-name", id);
        this.command = config.getString("command", "shop " + id);
        
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
        
        loadItems(config);
    }

    public boolean isOpen() {
        LocalDateTime now = LocalDateTime.now();
        String dayOfWeek = now.getDayOfWeek().toString();
        
        OpeningHours hours = weeklySchedule.get(dayOfWeek);
        if (hours == null) return true;
        
        return hours.isOpen(now.toLocalTime());
    }

    private void loadItems(YamlConfiguration config) {
        if (!config.isConfigurationSection("items")) return;
        
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            String path = "items." + key;
            int slot = config.getInt(path + ".slot");

            String itemString = config.getString(path + ".item");
            ItemStack item;
            
            if (itemString.startsWith("itemsadder:")) {
                item = plugin.getItemsAdderIntegration().getItem(itemString.substring(11));
            } else if (itemString.startsWith("oraxen:")) {
                item = plugin.getOraxenIntegration().getItem(itemString.substring(7));
            } else if (itemString.startsWith("nexo:")) {
                item = plugin.getNexoIntegration().getItem(itemString.substring(7));
            } else {
                item = config.getItemStack(path + ".item");
            }
            
            if (item == null) continue;
            
            double buyPrice = config.getDouble(path + ".buy-price");
            double sellPrice = config.getDouble(path + ".sell-price");
            
            ShopItem shopItem = new ShopItem(key, item, buyPrice, sellPrice);
            
            if (config.isConfigurationSection(path + ".bulk-discounts")) {
                for (String amount : config.getConfigurationSection(path + ".bulk-discounts").getKeys(false)) {
                    int bulkAmount = Integer.parseInt(amount);
                    double discount = config.getDouble(path + ".bulk-discounts." + amount);
                    shopItem.addBulkDiscount(bulkAmount, discount);
                }
            }
            
            if (config.isConfigurationSection(path + ".sale")) {
                double saleDiscount = config.getDouble(path + ".sale.discount");
                String startTime = config.getString(path + ".sale.start");
                String endTime = config.getString(path + ".sale.end");
                
                LocalDateTime saleStart = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime saleEnd = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                
                shopItem.setSaleDetails(saleDiscount, saleStart, saleEnd);
            }
            
            shopItem.setStock(config.getInt(path + ".stock", -1));
            shopItem.setMaxStock(config.getInt(path + ".max-stock", -1));
            
            if (config.isConfigurationSection(path + ".stock-regen")) {
                int regenAmount = config.getInt(path + ".stock-regen.amount", 1);
                int regenInterval = config.getInt(path + ".stock-regen.interval", 300);
                shopItem.setRegenAmount(regenAmount);
                shopItem.setRegenInterval(regenInterval);
                shopItem.startRegeneration(plugin);
            }
            
            items.put(slot, shopItem);
        }
    }

    public void save() {
        // Save implementation
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Map<Integer, ShopItem> getItems() { return items; }
    public String getCommand() { return command; }
    
    public Map<String, OpeningHours> getWeeklySchedule() {
        return weeklySchedule;
    }

    public static class CurrencyPrice {
        private final double buyPrice;
        private final double sellPrice;
        
        public CurrencyPrice(double buyPrice, double sellPrice) {
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
        }
        
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
    }
    
    public void addCurrencyPrice(String currency, double buyPrice, double sellPrice) {
        prices.put(currency, new CurrencyPrice(buyPrice, sellPrice));
    }
    
    public Optional<CurrencyPrice> getPriceForCurrency(String currency) {
        return Optional.ofNullable(prices.get(currency));
    }
    
    public Set<String> getAcceptedCurrencies() {
        return prices.keySet();
    }

    public void openMenu(Player player) {
        String title = ColorUtil.colorize(getDisplayName());
        inventory = Bukkit.createInventory(this, getSize(), title);
        
        for (Map.Entry<Integer, ShopItem> entry : items.entrySet()) {
            ShopItem item = entry.getValue();
            ItemStack displayItem = item.getDisplayItem();
            ItemMeta meta = displayItem.getItemMeta();
            
            if (meta.hasDisplayName()) {
                meta.setDisplayName(ColorUtil.colorize(meta.getDisplayName()));
            }
            
            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                lore.replaceAll(ColorUtil::colorize);
                meta.setLore(lore);
            }
            
            displayItem.setItemMeta(meta);
            inventory.setItem(entry.getKey(), displayItem);
        }
        
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public int getSize() {
        return rows * 9;
    }
}
