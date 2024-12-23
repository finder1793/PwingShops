package com.pwing.shops.integration;

import com.pwing.shops.PwingShops;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.inventory.ItemStack;

public class NexoIntegration {
    private boolean enabled = false;

    public NexoIntegration(PwingShops plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Nexo") != null) {
            this.enabled = true;
        }
    }

    public ItemStack getItem(String itemId) {
        if (!enabled) return null;
        return NexoItems.itemFromId(itemId).build();
    }

    public boolean isEnabled() {
        return enabled;
    }
}
