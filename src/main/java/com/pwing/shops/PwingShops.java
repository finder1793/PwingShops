package com.pwing.shops;

import com.pwing.shops.command.ConvertCommand;
import com.pwing.shops.command.ShopCommandHandler;
import com.pwing.shops.shop.ShopManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import com.pwing.shops.integration.CitizensIntegration;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.shops.command.CommandManager;
import com.pwing.shops.integration.NexoIntegration;
import com.pwing.shops.integration.OraxenIntegration;
import com.pwing.shops.integration.ItemsAdderIntegration;
import com.pwing.shops.integration.PwingEcoIntegration;
import com.pwing.shops.menu.MenuManager;
import com.pwing.shops.command.ReloadCommand;

public class PwingShops extends JavaPlugin {
    private Economy economy;
    private ShopCommandHandler commandHandler;
    private ShopManager shopManager;
    private ItemsAdderIntegration itemsAdderIntegration;
    private OraxenIntegration oraxenIntegration;
    private NexoIntegration nexoIntegration;
    private PwingEcoIntegration pwingEcoIntegration;
    private MenuManager menuManager;
    private Permission permission;
    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        this.itemsAdderIntegration = new ItemsAdderIntegration(this);
        this.oraxenIntegration = new OraxenIntegration(this);
        this.nexoIntegration = new NexoIntegration(this);
        this.pwingEcoIntegration = new PwingEcoIntegration(this);
        this.shopManager = new ShopManager(this);
        this.commandHandler = new ShopCommandHandler(this);
        this.menuManager = new MenuManager(this);
        setupPermissions();
        
        getCommand("shop").setExecutor(commandHandler);
        getCommand("shop").setTabCompleter(commandHandler);
        
        // Register convert command
        commandHandler.registerSubCommand("convert", new ConvertCommand(this));
        
        // Register commands through command manager
        CommandManager commandManager = new CommandManager(this);
        getCommand("pwingshops").setExecutor(commandManager);
        getCommand("pwingshops").setTabCompleter(commandManager);
        
        // Register Citizens trait
        if (getServer().getPluginManager().getPlugin("Citizens") != null) {
            net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(
                net.citizensnpcs.api.trait.TraitInfo.create(CitizensIntegration.class)
            );
        }
        
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

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            permission = rsp.getProvider();
        }
        return permission != null;
    }

    public Economy getEconomy() { return economy; }
    public ShopManager getShopManager() { return shopManager; }
    public ShopCommandHandler getCommandHandler() { return commandHandler; }
    public ItemsAdderIntegration getItemsAdderIntegration() { return itemsAdderIntegration; }
    public OraxenIntegration getOraxenIntegration() { return oraxenIntegration; }
    public NexoIntegration getNexoIntegration() { return nexoIntegration; }
    public PwingEcoIntegration getPwingEcoIntegration() { return pwingEcoIntegration; }
    public MenuManager getMenuManager() { return menuManager; }
    public Permission getPermission() { return permission; }
}



