package com.pwing.shops.api;

import com.pwing.shops.PwingShops;
import com.pwing.shops.shop.Shop;
import com.pwing.shops.shop.ShopItem;
import org.bukkit.entity.Player;

public class PwingShopsAPI {
    private static PwingShops plugin;
    
    public static void init(PwingShops instance) {
        plugin = instance;
    }
    
    public static Shop getShop(String id) {
        return plugin.getShopManager().getShop(id);
    }
    
    public static void createShop(String id, String displayName) {
        // Shop creation implementation
    }
    
    public static void addItemToShop(String shopId, ShopItem item, int slot) {
        Shop shop = getShop(shopId);
        if (shop != null) {
            shop.getItems().put(slot, item);
        }
    }
    
    public static boolean purchaseItem(Player player, String shopId, String itemId, int amount) {
        Shop shop = getShop(shopId);
        if (shop != null) {
            ShopItem item = shop.getItems().values().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElse(null);
                
            if (item != null) {
                // Implement purchase logic
                return true;
            }
        }
        return false;
    }


    public static PwingShops getPlugin() {
        return plugin;
    }
}