package com.pwing.shops.command;

import com.pwing.shops.PwingShops;
import com.pwing.shops.converter.DeluxeMenusConverter;
import com.pwing.shops.converter.ShopGUIPlusConverter;
import com.pwing.shops.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConvertCommand implements CommandExecutor, TabCompleter {
    private final PwingShops plugin;
    
    public ConvertCommand(PwingShops plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pwingshops.convert")) {
            sender.sendMessage(ColorUtil.colorize("&cYou don't have permission to use this command."));
            return true;
        }
        
        if (args.length > 1) {
            String type = args[1].toLowerCase();
            switch(type) {
                case "deluxemenus":
                    DeluxeMenusConverter menuConverter = new DeluxeMenusConverter(plugin);
                    menuConverter.convert();
                    sender.sendMessage(ColorUtil.colorize("&aSuccessfully converted DeluxeMenus configurations!"));
                    break;
                case "shopguiplus":
                    ShopGUIPlusConverter shopConverter = new ShopGUIPlusConverter(plugin);
                    shopConverter.convert();
                    sender.sendMessage(ColorUtil.colorize("&aSuccessfully converted ShopGUI+ configurations!"));
                    break;
                default:
                    sender.sendMessage(ColorUtil.colorize("&cUnknown converter type. Available: deluxemenus, shopguiplus"));
            }
        } else {
            sender.sendMessage(ColorUtil.colorize("&cUsage: /pwingshops convert <type>"));
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("deluxemenus", "shopguiplus").stream()
                .filter(s -> s.startsWith(args[1].toLowerCase()))
                .collect(Collectors.toList());
        }
        return null;
    }
}