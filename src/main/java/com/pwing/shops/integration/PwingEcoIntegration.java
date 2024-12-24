package com.pwing.shops.integration;

import com.pwing.shops.PwingShops;
import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.api.ShopIntegrationAPI;
import com.pwing.pwingeco.currency.Currency;
import com.pwing.pwingeco.manager.CurrencyManager;
import org.bukkit.entity.Player;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PwingEcoIntegration {
    private final PwingShops plugin;
    private final PwingEco pwingEco;
    private ShopIntegrationAPI shopAPI;
    private boolean enabled = false;

    public PwingEcoIntegration(PwingShops plugin) {
        this.plugin = plugin;
        this.enabled = false;
        this.pwingEco = null;
        this.shopAPI = null;
        
        try {
            if (plugin.getServer().getPluginManager().getPlugin("PwingEco") != null) {
                this.pwingEco = (PwingEco) plugin.getServer().getPluginManager().getPlugin("PwingEco");
                this.shopAPI = new ShopIntegrationAPI(this.pwingEco);
                this.enabled = true;
            }
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().info("PwingEco integration not enabled - API not found");
        }
    }
    public boolean processPurchase(Player player, String currency, double amount) {
        if (!enabled) {
            return plugin.getEconomy().withdrawPlayer(player, amount).transactionSuccess();
        }
        return shopAPI.processPurchase(player, currency, amount);
    }

    public boolean processSale(Player player, String currency, double amount) {
        if (!enabled) {
            return plugin.getEconomy().depositPlayer(player, amount).transactionSuccess();
        }
        return shopAPI.processSale(player, currency, amount);
    }

    public double getBalance(Player player, String currency) {
        if (!enabled) {
            return plugin.getEconomy().getBalance(player);
        }
        return shopAPI.getCurrencyBalance(player.getUniqueId(), currency);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Set<String> getAvailableCurrencies() {
        if (!enabled) {
            Set<String> defaultCurrency = new HashSet<>();
            defaultCurrency.add("default");
            return defaultCurrency;
        }
        return pwingEco.getCurrencyManager().getAllCurrencies().stream()
                .map(Currency::getName)
                .collect(Collectors.toSet());
    }

    public String getPrimaryCurrency() {
        if (!enabled) {
            return "default";
        }
        return pwingEco.getCurrencyManager().getPrimaryCurrency().getName();
    }
}