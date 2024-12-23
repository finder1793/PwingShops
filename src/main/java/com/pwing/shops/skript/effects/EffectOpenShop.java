package com.pwing.shops.skript.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.pwing.shops.api.PwingShopsAPI;
import com.pwing.shops.menu.ShopMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EffectOpenShop extends Effect {
    private Expression<String> shopId;
    private Expression<Player> player;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        shopId = (Expression<String>) exprs[0];
        player = (Expression<Player>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String shop = shopId.getSingle(e);
        Player p = player.getSingle(e);
        
        if (shop != null && p != null) {
            ShopMenu menu = new ShopMenu(PwingShopsAPI.getPlugin(), PwingShopsAPI.getShop(shop));
            menu.open(p);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "open shop " + shopId.toString(e, debug) + " to " + player.toString(e, debug);
    }
}
