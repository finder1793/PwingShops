package com.pwing.shops.util;

import org.bukkit.ChatColor;
import java.util.regex.Pattern;

public class ColorUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
    
    public static String colorize(String text) {
        if (text == null) return null;
        
        // Parse hex colors
        var matcher = HEX_PATTERN.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color).toString());
        }
        
        // Parse legacy colors
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
