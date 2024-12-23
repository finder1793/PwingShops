package com.pwing.shops.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuItem {
    private ItemStack item;
    private MenuClickHandler clickHandler;
    
    public MenuItem(ItemStack item, MenuClickHandler clickHandler) {
        this.item = item;
        this.clickHandler = clickHandler;
    }
    
    public void onClick(InventoryClickEvent event) {
        if (clickHandler != null) {
            clickHandler.onClick(event);
        }
    }
    
    public ItemStack getItem() {
        return item;
    }
}
