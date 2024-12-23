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
    private PwingEco pwingEco = null;
    private ShopIntegrationAPI shopAPI = null;
    private boolean enabled = false;

    public PwingEcoIntegration(PwingShops plugin) {
        this.plugin = plugin;
        
        try {
            org.bukkit.plugin.Plugin pwingPlugin = plugin.getServer().getPluginManager().getPlugin("PwingEco");
            if (pwingPlugin != null) {
                this.pwingEco = (PwingEco) pwingPlugin;
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