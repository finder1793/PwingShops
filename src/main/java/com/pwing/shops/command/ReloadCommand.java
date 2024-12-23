package com.pwing.shops.command;

import com.pwing.shops.PwingShops;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.pwing.shops.util.ColorUtil;

public class ReloadCommand implements CommandExecutor, TabCompleter {
    private final PwingShops plugin;
    
    public ReloadCommand(PwingShops plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pwingshops.reload")) {
            sender.sendMessage(ColorUtil.colorize("&cYou don't have permission to use this command."));
            return true;
        }
        
        if (args.length > 1) {
            String type = args[1].toLowerCase();
            switch (type) {
                case "menus":
                    plugin.getMenuManager().reload();
                    sender.sendMessage(ColorUtil.colorize("&aSuccessfully reloaded all menus!"));
                    break;
                case "shops":
                    plugin.getShopManager().reload();
                    sender.sendMessage(ColorUtil.colorize("&aSuccessfully reloaded all shops!"));
                    break;
                default:
                    reloadAll(sender);
            }
        } else {
            reloadAll(sender);
        }
        return true;
    }
    
    private void reloadAll(CommandSender sender) {
        plugin.getMenuManager().reload();
        plugin.getShopManager().reload();
        sender.sendMessage(ColorUtil.colorize("&aSuccessfully reloaded all menus and shops!"));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("menus", "shops").stream()
                .filter(s -> s.startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        return null;
    }
}


