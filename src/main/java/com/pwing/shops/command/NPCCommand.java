package com.pwing.shops.command;

import com.pwing.shops.PwingShops;
import com.pwing.shops.util.ColorUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.pwing.shops.integration.CitizensIntegration;

public class NPCCommand implements CommandExecutor {
    private final PwingShops plugin;
    
    public NPCCommand(PwingShops plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.colorize("&cOnly players can use this command."));
            return true;
        }
        
        Player player = (Player) sender;
        if (!player.hasPermission("pwingshops.admin")) {
            player.sendMessage(ColorUtil.colorize("&cYou don't have permission to use this command."));
            return true;
        }
        
        if (args.length < 3) {
            player.sendMessage(ColorUtil.colorize("&cUsage: /pwingshops npc <shop|menu> <id>"));
            return true;
        }
        
        NPC selected = CitizensAPI.getDefaultNPCSelector().getSelected(player);
        if (selected == null) {
            player.sendMessage(ColorUtil.colorize("&cPlease select an NPC first!"));
            return true;
        }
        
        CitizensIntegration trait = selected.getTrait(CitizensIntegration.class);
        
        if (args[1].equalsIgnoreCase("shop")) {
            trait.setShop(args[2]);
            player.sendMessage(ColorUtil.colorize("&aSuccessfully set NPC to open shop: " + args[2]));
        } else if (args[1].equalsIgnoreCase("menu")) {
            trait.setMenu(args[2]);
            player.sendMessage(ColorUtil.colorize("&aSuccessfully set NPC to open menu: " + args[2]));
        }
        
        return true;
    }
}

