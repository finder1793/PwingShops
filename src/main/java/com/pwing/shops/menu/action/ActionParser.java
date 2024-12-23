package com.pwing.shops.menu.action;

import com.pwing.shops.PwingShops;
import com.pwing.shops.menu.ConfigurableMenu;
import net.kyori.adventure.text.minimessage.MiniMessage;
import java.util.HashMap;
import java.util.Map;
import com.pwing.shops.util.ColorUtil;
import org.bukkit.Bukkit;


public class ActionParser {
    private final PwingShops plugin;
    private static final Map<String, ActionFactory> actionFactories = new HashMap<>();

    public ActionParser(PwingShops plugin) {
        this.plugin = plugin;
    }
    
    static {
        actionFactories.put("player", (plugin, content) -> 
            player -> Bukkit.dispatchCommand(player, content));
            
        actionFactories.put("console", (plugin, content) -> 
            player -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), content));
            
        actionFactories.put("message", (plugin, content) -> 
            player -> player.sendMessage(ColorUtil.colorize(content)));
            
        actionFactories.put("broadcast", (plugin, content) -> 
            player -> Bukkit.broadcastMessage(ColorUtil.colorize(content)));
            
    actionFactories.put("minimessage", (plugin, content) -> 
        player -> player.sendMessage(ColorUtil.colorize(content)));
            
        actionFactories.put("close", (plugin, content) -> 
            player -> player.closeInventory());
        
            
        actionFactories.put("sound", (plugin, content) -> {
            String[] parts = content.split(" ");
            return player -> player.playSound(player.getLocation(), parts[0], 
                Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
        });
        
        actionFactories.put("openmenu", (plugin, content) -> player -> {
            ConfigurableMenu menu = plugin.getMenuManager().getMenu(content);
            if (menu != null) {
                menu.open(player);
            }
        });

        // Money actions
        actionFactories.put("takemoney", (plugin, content) -> player -> {
            double amount = Double.parseDouble(content);
            plugin.getEconomy().withdrawPlayer(player, amount);
        });
        
        actionFactories.put("givemoney", (plugin, content) -> player -> {
            double amount = Double.parseDouble(content);
            plugin.getEconomy().depositPlayer(player, amount);
        });
        
        // Experience actions
        actionFactories.put("takeexp", (plugin, content) -> player -> {
            if (content.endsWith("L")) {
                int levels = Integer.parseInt(content.substring(0, content.length() - 1));
                player.setLevel(player.getLevel() - levels);
            } else {
                int exp = Integer.parseInt(content);
                player.setTotalExperience(player.getTotalExperience() - exp);
            }
        });
        
        actionFactories.put("giveexp", (plugin, content) -> player -> {
            if (content.endsWith("L")) {
                int levels = Integer.parseInt(content.substring(0, content.length() - 1));
                player.setLevel(player.getLevel() + levels);
            } else {
                int exp = Integer.parseInt(content);
                player.setTotalExperience(player.getTotalExperience() + exp);
            }
        });
        
        // Permission actions
        actionFactories.put("givepermission", (plugin, content) -> player -> {
            plugin.getPermission().playerAdd(null, player, content);
        });
        
        actionFactories.put("takepermission", (plugin, content) -> player -> {
            plugin.getPermission().playerRemove(null, player, content);
        });
    }    
    public MenuAction parse(String actionLine) {
        String type = actionLine.substring(1, actionLine.indexOf("]")).toLowerCase();
        String content = actionLine.substring(actionLine.indexOf("]") + 1).trim();
        
        ActionFactory factory = actionFactories.get(type);
        return factory != null ? factory.create(plugin, content) : null;
    }
}




