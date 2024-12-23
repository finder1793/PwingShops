package com.pwing.shops.command;

import com.pwing.shops.PwingShops;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import com.pwing.shops.shop.Shop;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ShopCommandHandler implements CommandExecutor, TabCompleter {
    private final PwingShops plugin;
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public ShopCommandHandler(PwingShops plugin) {
        this.plugin = plugin;
    }

    public void registerSubCommand(String name, CommandExecutor executor) {
        subCommands.put(name.toLowerCase(), executor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            CommandExecutor subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                return subCommand.onCommand(sender, command, label, args);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream()
                .filter(cmd -> cmd.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

public void registerShopCommand(Shop shop) {
    registerSubCommand(shop.getId(), new CommandExecutor() {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                shop.openMenu((Player) sender);
                return true;
            }
            return false;
        }
    });
}
}
