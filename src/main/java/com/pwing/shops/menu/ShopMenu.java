package com.pwing.shops.menu;

import com.pwing.shops.PwingShops;
import com.pwing.shops.shop.Shop;
import com.pwing.shops.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.pwing.shops.integration.PwingEcoIntegration;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopMenu implements InventoryHolder {
    private final PwingShops plugin;
    private final Shop shop;
    private final Inventory inventory;
    private final Map<Integer, Long> lastClick = new HashMap<>();
    private static final long CLICK_COOLDOWN = 100; //100 ms cooldown
    private final PwingEcoIntegration ecoIntegration;
    
    public ShopMenu(PwingShops plugin, Shop shop) {
        this.plugin = plugin;
        this.shop = shop;
        this.inventory = Bukkit.createInventory(this, 54, shop.getDisplayName());
        this.ecoIntegration = new PwingEcoIntegration(plugin);
        setupItems();
    }

    private void setupItems() {
        // Add clock item to show shop hours
        ItemStack clockItem = new ItemStack(Material.CLOCK);
        ItemMeta clockMeta = clockItem.getItemMeta();
        clockMeta.setDisplayName("§6Shop Hours");
    
        List<String> hourLore = new ArrayList<>();
        hourLore.add("§7Current Status: " + (shop.isOpen() ? "§aOpen" : "§cClosed"));
        hourLore.add("");
        hourLore.add("§eSchedule:");
    
        shop.getWeeklySchedule().forEach((day, hours) -> {
            hourLore.add(String.format("§7%s: §f%s - %s", 
                day, 
                hours.getOpenTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                hours.getCloseTime().format(DateTimeFormatter.ofPattern("HH:mm"))
            ));
        });
    
        clockMeta.setLore(hourLore);
        clockItem.setItemMeta(clockMeta);
        inventory.setItem(8, clockItem);
    
        // Setup shop items
        shop.getItems().forEach((slot, item) -> {
            ItemStack displayItem = item.getDisplayItem().clone();
            ItemMeta meta = displayItem.getItemMeta();
        
            List<String> lore = new ArrayList<>();
        
            // Price display with sale info
            if (item.isOnSale()) {
                lore.add("§cSALE! §7Buy Price: §m$" + item.getBuyPrice() + "§a $" + item.getCurrentPrice());
            } else {
                lore.add("§7Buy Price: §a$" + item.getBuyPrice());
            }
            lore.add("§7Sell Price: §a$" + item.getSellPrice());
        
            // Stock information
            if (item.getMaxStock() != -1) {
                lore.add("§7Stock: §e" + item.getStock() + "/" + item.getMaxStock());
            }
        
            // Bulk discounts
            if (!item.getBulkDiscounts().isEmpty()) {
                lore.add("");
                lore.add("§6Bulk Discounts:");
                item.getBulkDiscounts().forEach((amount, discount) -> 
                    lore.add(String.format("§e%d+ items: §a%.0f%% off", amount, discount * 100)));
            }
        
            lore.add("");
            lore.add("§eLeft-click to buy one");
            lore.add("§eShift-left-click to buy a stack");
            lore.add("§eRight-click to sell one");
            lore.add("§eShift-right-click to sell all");
        
            meta.setLore(lore);
            displayItem.setItemMeta(meta);
            inventory.setItem(slot, displayItem);
        });
    }
    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void handleClick(Player player, ClickType clickType, int slot) {
        if (isOnCooldown(player)) return;
        
        ShopItem item = shop.getItems().get(slot);
        if (item == null) return;

        switch (clickType) {
            case LEFT -> buyItem(player, item, 1);
            case SHIFT_LEFT -> buyItem(player, item, item.getDisplayItem().getMaxStackSize());
            case RIGHT -> sellItem(player, item, 1);
            case SHIFT_RIGHT -> sellAllItems(player, item);
        }
        
        setClickCooldown(player);
    }

    private boolean isOnCooldown(Player player) {
        return lastClick.containsKey(player.getUniqueId().hashCode()) && 
               System.currentTimeMillis() - lastClick.get(player.getUniqueId().hashCode()) < CLICK_COOLDOWN;
    }

    private void setClickCooldown(Player player) {
        lastClick.put(player.getUniqueId().hashCode(), System.currentTimeMillis());
    }

    private void buyItem(Player player, ShopItem item, int amount) {
        if (!shop.isOpen()) {
            player.sendMessage("§cThis shop is currently closed!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        if (!item.hasStock(amount)) {
            player.sendMessage("§cNot enough stock available!");
            return;
        }

        double totalCost = item.calculatePrice(amount);
        if (!plugin.getEconomy().has(player, totalCost)) {
            player.sendMessage("§cYou don't have enough money to buy this!");
            return;
        }

        ItemStack buyStack = item.getDisplayItem().clone();
        buyStack.setAmount(amount);
        
        if (!player.getInventory().addItem(buyStack).isEmpty()) {
            player.sendMessage("§cYou don't have enough inventory space!");
            return;
        }

        plugin.getEconomy().withdrawPlayer(player, totalCost);
        item.removeStock(amount);
        
        String saleMessage = item.isOnSale() ? " §c(SALE!)" : "";
        String discountMessage = totalCost < (item.getCurrentPrice() * amount) ? " §a(Bulk discount applied!)" : "";
            
        player.sendMessage(String.format("§aBought %dx %s for $%.2f%s%s", 
            amount, 
            item.getDisplayItem().getType(), 
            totalCost,
            saleMessage,
            discountMessage));
    }
    private void sellItem(Player player, ShopItem item, int amount) {
        ItemStack sellStack = item.getDisplayItem().clone();
        if (!player.getInventory().containsAtLeast(sellStack, amount)) {
            player.sendMessage("§cYou don't have enough items to sell!");
            return;
        }

        sellStack.setAmount(amount);
        player.getInventory().removeItem(sellStack);
        
        double totalPrice = item.getSellPrice() * amount;
        plugin.getEconomy().depositPlayer(player, totalPrice);
        player.sendMessage(String.format("§aSold %dx %s for $%.2f", amount, item.getDisplayItem().getType(), totalPrice));
    }

    private void sellAllItems(Player player, ShopItem item) {
        ItemStack checkStack = item.getDisplayItem().clone();
        int amount = 0;
        
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack != null && stack.isSimilar(checkStack)) {
                amount += stack.getAmount();
            }
        }

        if (amount > 0) {
            sellItem(player, item, amount);
        }
    }


    public boolean handlePurchase(Player player, double cost) {
        String currency = ecoIntegration.getPrimaryCurrency();
        if (ecoIntegration.getBalance(player, currency) >= cost) {
            return ecoIntegration.processPurchase(player, currency, cost);
        }
        return false;
    }
}


