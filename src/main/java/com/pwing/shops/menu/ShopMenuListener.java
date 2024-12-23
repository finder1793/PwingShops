package com.pwing.shops.menu;

import com.pwing.shops.PwingShops;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class ShopMenuListener implements Listener {
    private final PwingShops plugin;

    public ShopMenuListener(PwingShops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof ShopMenu shopMenu)) return;

        event.setCancelled(true);

        if (event.getClickedInventory() == event.getView().getTopInventory()) {
            shopMenu.handleClick(player, event.getClick(), event.getSlot());
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ShopMenu) {
            event.setCancelled(true);
        }
    }
}
