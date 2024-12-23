package com.pwing.shops.integration;

import com.pwing.shops.PwingShops;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.inventory.ItemStack;

public class OraxenIntegration {
    private boolean enabled = false;

    public OraxenIntegration(PwingShops plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Oraxen") != null) {
            this.enabled = true;
        }
    }

    public ItemStack getItem(String itemId) {
        if (!enabled) return null;
        return OraxenItems.getItemById(itemId).build();
    }

    public boolean isEnabled() {
        return enabled;
    }
}
