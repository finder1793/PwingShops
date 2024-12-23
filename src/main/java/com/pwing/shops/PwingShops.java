package com.pwing.shops;

import com.pwing.shops.commands.ShopCommandHandler;
import com.pwing.shops.shop.ShopManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PwingShops extends JavaPlugin {
    private Economy economy;
    private ShopManager shopManager;
    private ShopCommandHandler commandHandler;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        this.shopManager = new ShopManager(this);
        this.commandHandler = new ShopCommandHandler(this);
        getLogger().info("PwingShops has been enabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() { return economy; }
    public ShopManager getShopManager() { return shopManager; }
    public ShopCommandHandler getCommandHandler() { return commandHandler; }
}