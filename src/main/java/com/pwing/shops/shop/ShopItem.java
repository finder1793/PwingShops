package com.pwing.shops.shop;

import com.pwing.shops.PwingShops;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ShopItem {
    private final String id;
    private final ItemStack displayItem;
    private final double buyPrice;
    private final double sellPrice;
    private final Map<Integer, Double> bulkDiscounts = new HashMap<>();
    private int stock;
    private int maxStock;
    private double saleDiscount;
    private LocalDateTime saleStart;
    private LocalDateTime saleEnd;
    private int regenAmount;
    private int regenInterval; // in seconds

    public ShopItem(String id, ItemStack displayItem, double buyPrice, double sellPrice) {
        this.id = id;
        this.displayItem = displayItem;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getId() { return id; }
    public ItemStack getDisplayItem() { return displayItem.clone(); }
    public double getBuyPrice() { return buyPrice; }
    public double getSellPrice() { return sellPrice; }

    public void addBulkDiscount(int amount, double discountPercent) {
        bulkDiscounts.put(amount, discountPercent);
    }

    public double calculatePrice(int amount) {
        double basePrice = buyPrice * amount;
        
        // Apply bulk discount
        double bulkDiscountRate = 0.0;
        for (Map.Entry<Integer, Double> entry : bulkDiscounts.entrySet()) {
            if (amount >= entry.getKey() && entry.getValue() > bulkDiscountRate) {
                bulkDiscountRate = entry.getValue();
            }
        }
        
        // Apply sale discount if active
        double finalPrice = basePrice * (1 - bulkDiscountRate);
        if (isOnSale()) {
            finalPrice *= (1 - saleDiscount);
        }
        
        return finalPrice;
    }

    public Map<Integer, Double> getBulkDiscounts() {
        return bulkDiscounts;
    }

    public double getCurrentPrice() {
        return isOnSale() ? buyPrice * (1 - saleDiscount) : buyPrice;
    }

    public boolean isOnSale() {
        LocalDateTime now = LocalDateTime.now();
        return saleStart != null && saleEnd != null 
            && now.isAfter(saleStart) 
            && now.isBefore(saleEnd);
    }

    public boolean hasStock(int amount) {
        return maxStock == -1 || stock >= amount;
    }

    public void removeStock(int amount) {
        if (maxStock != -1) {
            stock -= amount;
        }
    }

    public void addStock(int amount) {
        if (maxStock != -1) {
            stock = Math.min(stock + amount, maxStock);
        }
    }

    public int getStock() { 
        return stock; 
    }
    
    public int getMaxStock() { 
        return maxStock; 
    }
    
    public double getSaleDiscount() { 
        return saleDiscount; 
    }

    public void startRegeneration(PwingShops plugin) {
        if (regenAmount > 0 && regenInterval > 0) {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if (stock < maxStock) {
                    addStock(regenAmount);
                }
            }, regenInterval * 20L, regenInterval * 20L);
        }
    }

    // Add setters
    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public void setRegenAmount(int regenAmount) {
        this.regenAmount = regenAmount;
    }

    public void setRegenInterval(int regenInterval) {
        this.regenInterval = regenInterval;
    }
    public void setSaleDetails(double saleDiscount, LocalDateTime saleStart, LocalDateTime saleEnd) {
        this.saleDiscount = saleDiscount;
        this.saleStart = saleStart;
        this.saleEnd = saleEnd;
    }
}

