package com.pwing.shops.menu;

import com.pwing.shops.menu.action.MenuAction;
import com.pwing.shops.menu.action.ActionParser;
import com.pwing.shops.PwingShops;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.pwing.shops.util.ColorUtil;

import java.util.stream.Collectors;
import java.util.List;


public class ConfigurableMenu extends Menu {
    private final ActionParser actionParser;

    public ConfigurableMenu(PwingShops plugin, YamlConfiguration config) {
        super(plugin, 
              ColorUtil.colorize(config.getString("title", "Menu")), 
              config.getInt("size", 54));
        this.actionParser = new ActionParser(plugin);
        loadItems(config);
    }
    
    private void loadItems(YamlConfiguration config) {
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection == null) return;
        
        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            int slot = itemSection.getInt("slot");
            String itemString = itemSection.getString("item");
            ItemStack item;
            
            // Handle different item types
            if (itemString.startsWith("itemsadder:")) {
                item = plugin.getItemsAdderIntegration().getItem(itemString.substring(11));
            } else if (itemString.startsWith("oraxen:")) {
                item = plugin.getOraxenIntegration().getItem(itemString.substring(7));
            } else if (itemString.startsWith("nexo:")) {
                item = plugin.getNexoIntegration().getItem(itemString.substring(7));
            } else {

                item = itemSection.getItemStack("item");
            }
            
            if (item == null) continue;
            
            // Load actions
            List<String> actionStrings = itemSection.getStringList("actions");
            List<MenuAction> actions = actionStrings.stream()
                .map(actionLine -> actionParser.parse(actionLine))
                .filter(action -> action != null)
                .collect(Collectors.toList());
                
            MenuItem menuItem = new MenuItem(item, event -> {
                if (event.getWhoClicked() instanceof Player) {
                    Player player = (Player) event.getWhoClicked();
                    actions.forEach(action -> action.execute(player));
                }
                event.setCancelled(true);
            });
            
            items.put(slot, menuItem);
        }
    }

    private ItemStack processItem(ItemStack item, Player player) {
        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();
        
        if (meta.hasDisplayName()) {
            meta.setDisplayName(ColorUtil.colorize(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName())));
        }
        
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            lore.replaceAll(line -> ColorUtil.colorize(PlaceholderAPI.setPlaceholders(player, line)));
            meta.setLore(lore);
        }
        
        clone.setItemMeta(meta);
        return clone;
    }
}


