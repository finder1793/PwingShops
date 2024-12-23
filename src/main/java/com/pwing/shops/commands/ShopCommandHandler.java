package com.pwing.shops.commands;

import com.pwing.shops.PwingShops;
import com.pwing.shops.shop.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class ShopCommandHandler {
    private final PwingShops plugin;

    public ShopCommandHandler(PwingShops plugin) {
        this.plugin = plugin;
    }

    public void registerShopCommand(Shop shop) {
        PluginCommand command = plugin.getCommand(shop.getCommand());
        if (command != null) {
            command.setExecutor((sender, cmd, label, args) -> {
                // Open shop menu implementation
                return true;
            });
        }
    }
}