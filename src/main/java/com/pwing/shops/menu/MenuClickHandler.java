package com.pwing.shops.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface MenuClickHandler {
    void onClick(InventoryClickEvent event);
}
