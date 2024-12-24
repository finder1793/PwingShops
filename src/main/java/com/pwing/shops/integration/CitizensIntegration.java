package com.pwing.shops.integration;

import com.pwing.shops.PwingShops;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.event.EventHandler;

@TraitName("pwingshop")
public class CitizensIntegration extends Trait {
    private PwingShops plugin;
    private String shopId;
    private String menuId;
    
    public CitizensIntegration() {
        super("pwingshop");
    }
    
    public CitizensIntegration(PwingShops plugin) {
        super("pwingshop");
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        if (!event.getNPC().equals(getNPC())) return;
        
        if (shopId != null) {
            plugin.getShopManager().getShop(shopId).ifPresent(shop -> 
                shop.openMenu(event.getClicker()));
        }
        
        if (menuId != null) {
            plugin.getMenuManager().getMenu(menuId).ifPresent(menu -> 
                menu.open(event.getClicker()));
        }
    }
    
    public void setShop(String shopId) {
        this.shopId = shopId;
        this.menuId = null;
    }
    
    public void setMenu(String menuId) {
        this.menuId = menuId;
        this.shopId = null;
    }
}