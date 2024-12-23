package com.pwing.shops.menu;

import com.pwing.shops.PwingShops;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements InventoryHolder {
    protected PwingShops plugin;
    protected Inventory inventory;
    protected Map<Integer, MenuItem> items = new HashMap<>();
    
    public Menu(PwingShops plugin, String title, int size) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, size, title);
    }
    
    public void open(Player player) {
        player.openInventory(inventory);
    }
    
    public void handleClick(InventoryClickEvent event) {
        MenuItem item = items.get(event.getSlot());
        if (item != null) {
            item.onClick(event);
        }
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
