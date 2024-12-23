package com.pwing.shops.integration;

import com.pwing.shops.PwingShops;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderIntegration {
    private boolean enabled = false;

    public ItemsAdderIntegration(PwingShops plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("ItemsAdder") != null) {
            this.enabled = true;
        }
    }

    public ItemStack getItem(String itemId) {
        if (!enabled) return null;
        CustomStack customStack = CustomStack.getInstance(itemId);
        return customStack != null ? customStack.getItemStack() : null;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
