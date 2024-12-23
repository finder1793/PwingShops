package com.pwing.shops.shop;

import com.pwing.shops.PwingShops;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShopManager {
    private final PwingShops plugin;
    private final Map<String, Shop> shops = new HashMap<>();
    private final File shopsDirectory;

    public ShopManager(PwingShops plugin) {
        this.plugin = plugin;
        this.shopsDirectory = new File(plugin.getDataFolder(), "shops");
        loadShops();
    }

    private void loadShops() {
        if (!shopsDirectory.exists()) {
            shopsDirectory.mkdirs();
            plugin.saveResource("shops/example.yml", false);
        }

        for (File file : shopsDirectory.listFiles((dir, name) -> name.endsWith(".yml"))) {
            String shopId = file.getName().replace(".yml", "");
            Shop shop = new Shop(shopId, file, plugin);
            shops.put(shopId, shop);
            plugin.getCommandHandler().registerShopCommand(shop);
        }
    }

    public Shop getShop(String id) {
        return shops.get(id);
    }

    public Map<String, Shop> getShops() {
        return shops;
    }
}